package cn.jpush.dubbo.thritf093;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboProvider {
	public static void main(String[] args) throws Exception{

		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext("demo-provider093.xml");
		ctx.start();
		System.in.read();
	}
}
