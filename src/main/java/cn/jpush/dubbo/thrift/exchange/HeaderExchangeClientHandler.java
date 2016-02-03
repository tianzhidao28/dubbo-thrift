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
package cn.jpush.dubbo.thrift.exchange;


import org.jboss.netty.buffer.ChannelBuffer;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ExchangeChannel;
import com.alibaba.dubbo.remoting.exchange.ExchangeHandler;
import com.alibaba.dubbo.remoting.transport.ChannelHandlerDelegate;
/**
 * 
 * @author yankai
 * @date 2012-8-31
 */
public class HeaderExchangeClientHandler implements ChannelHandlerDelegate {
	
	protected static final Logger logger   = LoggerFactory.getLogger(HeaderExchangeClientHandler.class);

    private final ExchangeHandler handler;

    public HeaderExchangeClientHandler(ExchangeHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.handler = handler;
    }
    
	@Override
	public void connected(Channel channel) throws RemotingException {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel2.getOrAddChannel(channel);
        try {
            handler.connected(exchangeChannel);
        } finally {
            HeaderExchangeChannel2.removeChannelIfDisconnected(channel);
        }
	}

	@Override
	public void disconnected(Channel channel) throws RemotingException {
		ExchangeChannel exchangeChannel = HeaderExchangeChannel2.getOrAddChannel(channel);
        try {
            handler.disconnected(exchangeChannel);
        } finally {
            HeaderExchangeChannel2.removeChannelIfDisconnected(channel);
        }
	}
	
	@Override
	public void sent(Channel channel, Object message) throws RemotingException {
		Throwable exception = null;
		try {
			ExchangeChannel exchangeChannel = HeaderExchangeChannel2.getOrAddChannel(channel);
			try {
				handler.sent(exchangeChannel, message);
			} finally {
				HeaderExchangeChannel2.removeChannelIfDisconnected(channel);
			}
		} catch (Throwable t) {
	            exception = t;
        }
	 	DefaultFuture2.sent(channel, message);
        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else if (exception instanceof RemotingException) {
                throw (RemotingException) exception;
            } else {
                throw new RemotingException(channel.getLocalAddress(), channel.getRemoteAddress(),
                                            exception.getMessage(), exception);
            }
        }
	}

	@Override
	public void received(Channel channel, Object message) throws RemotingException {
		if (message != null) {
			DefaultFuture2.received(channel, (ChannelBuffer)message);
		}
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws RemotingException {
		handler.caught(channel, exception);
	}

	@Override
	public ChannelHandler getHandler() {
		return handler;
	}

}
