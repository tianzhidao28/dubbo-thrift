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


public class HelloServiceImpl2 implements HelloService2.Iface {
	
	@Override
	public void sayHello(String str) throws TException {
		System.out.println("sayHello2:" + str);
//		throw new NullPointerException("test nullpointer");
	}

	@Override
	public String getString(String str) throws TException {
		String sub = str.substring(0, 5);
		int i = Integer.parseInt(str.substring(5));
		i = i + 1;
//		try {
//			Thread.sleep(10000);
//		} catch (Exception e) {
//			
//		}
		return "hello2:"+sub + i;
//		throw new NullPointerException("test nullpointer");
	}

	@Override
	public User getUser(int id, String name, int age) throws Xception,
			TException {
		return new User(id, name, age);
	}
}
