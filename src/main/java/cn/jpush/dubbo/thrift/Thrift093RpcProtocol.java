package cn.jpush.dubbo.thrift;

import cn.jpush.dubbo.thrift.common.ThriftServerType;
import cn.jpush.dubbo.thrift.common.ThriftTools;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
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

    public static final String NAME = "thrift093";

    public static final int DEFAULT_PORT = 28093;

    // TODO
    public static final String SERVER_TYPE_KEY = "thrift_server_type";

    private static ConcurrentHashMap<String,ServerThread> serverThreads = new ConcurrentHashMap<>();


    @Override
    protected <T> Runnable doExport(T impl, Class<T> type, URL url) throws RpcException {

        String serverTypeString = url.getParameter(SERVER_TYPE_KEY, String.valueOf(ThriftServerType.THsHaServer));
        String ip = url.getIp();
        int port = url.getPort(DEFAULT_PORT);
        ThriftServerType serverType = ThriftServerType.valueOf(serverTypeString);

        TServer server = null ;
        String serviceName = type.getEnclosingClass().getName();

        TProcessor processor = ThriftTools.getTProcessClass(type,impl);
        server = ThriftTools.createTServer(serverType,port,processor);

        final ServerThread serverThread = new ServerThread(server, serviceName);
        serverThread.setDaemon(true);
        serverThread.start();

        serverThreads.put(serviceName, serverThread);

        System.out.println("thrift server start at " + ip + ":" + port);


        return new Runnable() {
            @Override
            public void run() {
                serverThread.stopServer();
            }
        };
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) throws RpcException {
        return null;
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