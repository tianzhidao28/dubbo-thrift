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
package cn.jpush.dubbo.thrift.proxy;

import com.alibaba.dubbo.rpc.RpcException;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 缓存 TServiceClient客户端
 * Created by rocyuan on 16/1/21.
 */
public class ThriftServiceClientProxyFactory implements FactoryBean,InitializingBean{

    private String host;
    private int port;
    private Object proxy;
    private Class<?> serviceInterface;
    private boolean refreshTServerClientOnConnectFailure = false;


    public Object getObject() throws RpcException {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() {



    }

    public <T> void setServiceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }


    public void setRefreshTServerClientOnConnectFailure(boolean refreshTServerClientOnConnectFailure) {
        this.refreshTServerClientOnConnectFailure = refreshTServerClientOnConnectFailure;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
