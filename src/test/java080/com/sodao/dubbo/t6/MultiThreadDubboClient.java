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
package com.sodao.dubbo.t6;

import org.apache.thrift.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MultiThreadDubboClient {
	static class Task extends Thread {
		 final HelloService.Iface service;
		final HelloService2.Iface service2;
		
		public Task(HelloService.Iface service, HelloService2.Iface service2) {
			this.service = service;
			this.service2 = service2;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					System.out.println(service.getString("hello" + i));
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		String path = 
				DubboConsumer.class.getPackage().getName().replace(".", "/")
				+ "/demo-consumer080.xml";
		
		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext(path);
		
		final HelloService.Iface service = (HelloService.Iface) ctx.getBean("helloService");
		final HelloService2.Iface service2 = (HelloService2.Iface) ctx.getBean("helloService2");
		
		for (int t = 0; t < 3; t++) {
			new Task(service, service2).start();
		}
	}
}
