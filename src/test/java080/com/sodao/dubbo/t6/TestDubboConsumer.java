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

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDubboConsumer {
	
	public static void main(String[] args) throws Exception{
		String path = 
				DubboConsumer.class.getPackage().getName().replace(".", "/")
				+ "/demo-consumer093.xml";
		
		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext(path);
		
		final HelloService.Iface service = (HelloService.Iface) ctx.getBean("helloService");
		
		service.getUser(1, "tom", 24);
	}
}
