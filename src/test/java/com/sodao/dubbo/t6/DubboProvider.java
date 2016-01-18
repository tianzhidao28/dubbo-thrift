package com.sodao.dubbo.t6;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboProvider {
	public static void main(String[] args) throws Exception{

		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext("demo-provider080.xml");
		ctx.start();
		System.in.read();
	}
}
