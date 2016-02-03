/**
 * The MIT License (MIT)
 *
 * Copyright (c) [2015] [rocyuan at jpush]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE PROCTED OFFER SOME PLUGINS OF DUBBO FOR JPUSH, WITH IT WE WILL DO SOA EASIRY.
 */
package cn.jpush.dubbo.thrift;

import cn.jpush.alarm.AlarmClient;
import cn.jpush.dubbo.thrift.common.ThriftServerType;
import cn.jpush.dubbo.thrift.common.ThriftTools;
import cn.jpush.dubbo.thrift.proxy.ThriftServiceClientProxyFactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServerEventHandler;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;



/**
 * @author rocyuan
 * @date 2016-01-20
 * @desc
 *      新增协议 thrift093 端口默认 28093
 *      生成原始的 thrift server,可以使用原生的Thrift调用
 *
 */
public class Thrift093RpcProtocol extends AbstractProxyProtocol{

    private static final Logger logger = LoggerFactory.getLogger(Thrift093RpcProtocol.class);

    public static final String NAME = "thrift093";

    public static final int DEFAULT_PORT = 28093;

    //Hsha目前已知道是最高效的
    public static final String SERVER_TYPE_KEY = "thrift_server_type";

    private static ConcurrentHashMap<String,ServerThread> serverThreads = new ConcurrentHashMap<>();



    @Override
    protected <T> Runnable doExport(T impl, Class<T> type, URL url) throws RpcException {

        String serverTypeString = url.getParameter(SERVER_TYPE_KEY, String.valueOf(ThriftServerType.THsHaServer));
        String ip = url.getIp();
        int port = url.getPort(DEFAULT_PORT);
        ThriftServerType serverType = ThriftServerType.valueOf(serverTypeString);

        TServer server = null ;
        final String serviceName = type.getEnclosingClass().getName();

        TProcessor processor = ThriftTools.getTProcessClass(type,impl);
        server = ThriftTools.createTServer(serverType,port,processor);

        // todo 此处添加 失败记录

        server.setServerEventHandler(new TServerEventHandler() {
            @Override
            public void preServe() {
                logger.debug("{} preServe",serviceName);
            }

            @Override
            public ServerContext createContext(TProtocol input, TProtocol output) {
                logger.debug("{} createContext"+input.toString(),serviceName);
                return null;
            }

            @Override
            public void deleteContext(ServerContext serverContext, TProtocol input, TProtocol output) {
                logger.debug("{} deleteContext",serviceName);
            }

            @Override
            public void processContext(ServerContext serverContext, TTransport inputTransport, TTransport outputTransport) {
                logger.debug("{} processContext ",serviceName);
            }
        });

        final ServerThread serverThread = new ServerThread(server, serviceName);
        serverThread.setDaemon(true);
        serverThread.start();

        serverThreads.put(serviceName, serverThread);

        System.out.println("thrift server start at " + ip + ":" + port);

        final TServer finalServer = server;
        Runnable stopRun = new Runnable() {
            @Override
            public void run() {
                if(finalServer != null && finalServer.isServing()) {
                    // todo 前期留在上面 停止的时候 我可以知晓
                    AlarmClient.send(81,"[请知晓]info : "+serviceName + " stop ");
                    finalServer.stop();
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread(stopRun));


        return stopRun;
    }

    // todo 未完成
    @Override
    protected <T> T doRefer(Class<T> serviceType, URL url) throws RpcException {
        String ip = url.getIp();
        int port = url.getPort();
        ThriftServiceClientProxyFactory factory = new ThriftServiceClientProxyFactory();
        factory.setServiceInterface(serviceType);
        factory.setRefreshTServerClientOnConnectFailure(true);
        factory.afterPropertiesSet();
        factory.setHost(ip);
        factory.setPort(port);
        return (T)factory.getObject();
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }


    /**
     * 阻塞的方法,只能放在线程中
     */
    class ServerThread extends Thread {
        private TServer server;
        private String serviceName;
        ServerThread(TServer server,String serviceName) {
            this.server = server;
            this.serviceName = serviceName;
        }

        @Override
        public void run(){
            try{
                //启动服务
                server.serve();
            }catch(Exception e){

            }
        }

        public void stopServer(){
            if (server!=null && server.isServing()) {
                server.stop();
                serverThreads.remove(serviceName);
            }
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        for (ServerThread thread : serverThreads.values()) {
            thread.stopServer();
        }
    }
}