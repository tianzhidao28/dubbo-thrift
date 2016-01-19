package cn.jpush.dubbo.thritf093;

import com.alibaba.dubbo.rpc.RpcContext;

import org.apache.thrift.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Future;

public class DubboConsumer {
	static class Task extends Thread {
		HelloService.Iface service;
		public Task(HelloService.Iface service) {
			this.service = service;
		}

		@Override
		public void run() {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					String str = service.getString("hello" + i);
					long k = i + 1L;
					if (Integer.parseInt(str.substring(5)) != k) {
					throw new IllegalStateException("result is error!");
				}
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {


		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext("demo-consumer093.xml");

		final HelloService.Iface service = (HelloService.Iface) ctx.getBean("helloService");

			
//		HelloService2.Iface service2 = (HelloService2.Iface) ctx.getBean("helloService2");
//		EchoService echoService = (EchoService) service; // 强制转型为EchoService
//		String status = (String) echoService.$echo("OK"); // 回声测试可用性
//		try {
		for (int i = 0; i < 10; i++) {
			System.out.println(service.getString("xxx"+i));
			System.in.read();
		}
		
		
	}
}
