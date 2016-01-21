package cn.jpush.dubbo.thritf093;

import java.util.concurrent.TimeUnit;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


/**
 * TEST 0.9.3 通过
 */
public class SimpleClient {
	public static void main(String[] args) throws Exception {
		
		TTransport transport = new TSocket("localhost", 28093);//28088

		TProtocol protocol = new TBinaryProtocol(new TFramedTransport((transport)));
		
		transport.open();
		
		HelloService.Client client = new HelloService.Client(protocol);
		for (int i = 0; i <10; i++) {
			try {
				String str = client.getString("hello" + i);

				System.out.println(str);
				TimeUnit.SECONDS.sleep(1);
			} catch(Exception e) {
//				System.out.println("--------------------" + i);
//				System.out.println();
				e.printStackTrace();
			}
		}
//		client.sayHello("hello");

		transport.close();
		
		System.out.println("end");
	}
}
