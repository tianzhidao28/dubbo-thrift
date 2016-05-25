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
 * todo 可以参照 RMI的实现;增加自动重连
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
