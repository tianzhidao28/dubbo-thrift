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

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;


public class SimpleServer {
	public static void main(String[] argss) throws Exception {
		
		HelloService.Iface impl = new HelloServiceImpl();
		
		TProtocolFactory protocolFactory = new  TBinaryProtocol.Factory();
		
		TServerTransport serverSocket = new TServerSocket(7911);
		
		TSimpleServer.Args args = new TSimpleServer.Args(serverSocket);
		
		args.processor(new HelloService.Processor<HelloService.Iface>(impl));
		
		args.protocolFactory(protocolFactory);
		
		TServer server = new TSimpleServer(args); 
		
		server.serve();
	}
}
