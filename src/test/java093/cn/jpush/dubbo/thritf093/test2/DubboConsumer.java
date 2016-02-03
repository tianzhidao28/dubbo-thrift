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
package cn.jpush.dubbo.thritf093.test2;

import cn.jpush.dubbo.thritf093.HelloService;
import org.apache.thrift.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
				new ClassPathXmlApplicationContext("classpath:test2/demo-consumer093.xml");

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
