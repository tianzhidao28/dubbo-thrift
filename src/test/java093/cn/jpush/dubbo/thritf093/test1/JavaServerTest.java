package cn.jpush.dubbo.thritf093.test1;

import cn.jpush.dubbo.thritf093.HelloService;
import cn.jpush.dubbo.thritf093.HelloServiceImpl;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.*;

/**
 * Created by rocyuan on 16/1/21.
 */
public class JavaServerTest {
    public static void main(String[] args) throws TTransportException, InterruptedException {




        createHsHaServer();

        System.out.println("start");

        client();


    }


    public static void createHsHaServer() {


        try {
            System.out.println("HelloWorld THsHaServer start ....");

            TProcessor tprocessor = new HelloService.Processor<HelloService.Iface>(
                    new HelloServiceImpl());

            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(28093);
            THsHaServer.Args thhsArgs = new THsHaServer.Args(tnbSocketTransport);
            thhsArgs.processor(tprocessor);
            thhsArgs.transportFactory(new TFramedTransport.Factory());
            thhsArgs.protocolFactory(new TBinaryProtocol.Factory());

            //半同步半异步的服务模型
            TServer server = new THsHaServer(thhsArgs);
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }


    public static void createNonServer() throws TTransportException {
        TProcessor processor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(28093);
        TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
        TProcessorFactory processorFactory = new TProcessorFactory(processor);
        tArgs.processorFactory(processorFactory);
        tArgs.transportFactory(new TFramedTransport.Factory());
        tArgs.protocolFactory( new TBinaryProtocol.Factory());
        TServer server = new TThreadedSelectorServer(tArgs);
        server.serve();
    }


    public static void client() {

        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket("127.0.0.1",
                    28093));
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            HelloService.Client client = new HelloService.Client(
                    protocol);
            transport.open();
            for (int i = 0; i < 100 ; i++) {

                String result = client.getString("12121");
                System.out.println("Thrify client result =: " + result);
            }
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }
}
