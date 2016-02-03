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
package cn.jpush.dubbo.thrift.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.jboss.netty.buffer.ChannelBuffer;
/**
 * 
 * @author yankai
 * @date 2012-8-31
 */
public class ThriftTransport extends TTransport {
	private ChannelBuffer inputBuffer;
	private ChannelBuffer outputBuffer;

	public ThriftTransport(ChannelBuffer input, ChannelBuffer output) {
		this.inputBuffer = input;
		this.outputBuffer = output;
	}

	@Override
	public boolean isOpen() {
		// Buffer is always open
		return true;
	}

	@Override
	public void open() throws TTransportException {
		// Buffer is always open
	}

	@Override
	public void close() {
		// Buffer is always open
	}

	@Override
	public int read(byte[] buffer, int offset, int length)
			throws TTransportException {
		int readableBytes = inputBuffer.readableBytes();
		int bytesToRead = length > readableBytes ? readableBytes : length;

		inputBuffer.readBytes(buffer, offset, bytesToRead);
		return bytesToRead;
	}

	@Override
	public void write(byte[] buffer, int offset, int length)
			throws TTransportException {
		outputBuffer.writeBytes(buffer, offset, length);
	}

	public ChannelBuffer getInputBuffer() {
		return inputBuffer;
	}

	public ChannelBuffer getOutputBuffer() {
		return outputBuffer;
	}
}
