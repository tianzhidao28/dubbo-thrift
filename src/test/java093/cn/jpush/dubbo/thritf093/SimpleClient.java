package cn.jpush.dubbo.thritf093;

import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


/**
 * TEST 0.9.3 通过
 */
public class SimpleClient {
	public static void main(String[] args) throws Exception {


		for (int i=0; i< 1000000; ++i) {
			//String kk = thredClient("test"+i);
			hshaClient();
			if(i % 1000 == 0) {
				System.out.println(i);
			}
		}

	}


	public static String thredClient(String ipStr) throws TException {

		TTransport transport = null;
		HelloService.Client client = null;

		transport = new TSocket("127.0.0.1", 28093);
		TProtocol protocol = new TBinaryProtocol(transport);

		transport.open();
		client = new HelloService.Client(protocol);
		String xx = client.getString("test iiii"+ipStr);
		transport.close();
		return xx;


	}

	public static void hshaClient() throws TTransportException {


		TTransport transport = new TSocket("localhost", 28093);//28088

		TProtocol protocol = new TBinaryProtocol(new TFramedTransport((transport)));

		transport.open();

		HelloService.Client client = new HelloService.Client(protocol);

			try {
				String str = client.getString("hello");
				//TimeUnit.SECONDS.sleep(1);
			} catch(Exception e) {
//				System.out.println("--------------------" + i);
//				System.out.println();
				e.printStackTrace();
			}

//		client.sayHello("hello");

		transport.close();

	}
}
