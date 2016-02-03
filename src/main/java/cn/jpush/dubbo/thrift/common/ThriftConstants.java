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
package cn.jpush.dubbo.thrift.common;

public class ThriftConstants {

    // 添加 0.9.1之后 新增的一些协议; json binary compact ....


    public static final String THRIFT_PROTOCOL_KEY = "thrift.protocol";
    /**
     * add protacol when thrift version > 0.8
     */
    // 暂时只支持这一种
    public static final String BINARY_THRIFT_PROTOCOL = "binary";

    /**
     * compact json tuple 这几张传输协议现在都不支持
     */
    public static final String COMPACT_THRIFT_PROTOCOL = "compact";
    public static final String JSON_THRIFT_PROTOCOL = "json";

    public static final String CLASS_NAME_GENERATOR_KEY = "class.name.generator";
    public static final String DEFAULT_PROTOCOL = BINARY_THRIFT_PROTOCOL;



    private ThriftConstants() {


    }




}
