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
package cn.jpush.dubbo.thritf093;

import org.apache.thrift.TException;

/**
 * Created by rocyuan on 16/1/18.
 */
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public User getUser(int id, String name, int age) throws Xception, TException {
        System.out.println("execute  getUser ");
        return null;
    }

    @Override
    public String getString(String str) throws TException {
        System.out.println("execute  getString ");
        return "HelloServiceImpl : " + str;
    }

    @Override
    public void sayHello(String str) throws TException {
        System.out.println("execute  sayHello ");
    }
}
