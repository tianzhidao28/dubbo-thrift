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
package cn.jpush.dubbo.thrift;

import java.util.Map;

import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.protocol.AbstractExporter;


public class ThriftRpcExporter<T> extends AbstractExporter<T> {

    private final String                        key;

    private final Map<String, Exporter<?>> exporterMap;

    public ThriftRpcExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap){
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
    }

	@Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(key);
    }
}