package cn.jpush.dubbo.thrift.netty;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.Transporter;

/**
 * @author ding.lid
 */
public class NettyTransporter2 implements Transporter {

    public static final String NAME = "netty2";
    
    public Server bind(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyServer2(url, listener);
    }

    public Client connect(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyClient2(url, listener);
    }

}