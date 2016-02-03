package cn.jpush.dubbo.thrift.proxy;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 连接池,thrift-client for spring
 */
public class ThriftClientPoolFactory extends BasePoolableObjectFactory<TServiceClient> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final TServiceClientFactory<TServiceClient> clientFactory;
	private PoolOperationCallBack callback;

	private String ip;
	private int port;

	//todo  多路复用备用
	private String serviceName;

	protected ThriftClientPoolFactory(TServiceClientFactory<TServiceClient> clientFactory,String ip,int port) throws Exception {
		this.clientFactory = clientFactory;
		this.ip = ip;
		this.port = port;
	}

	protected ThriftClientPoolFactory( TServiceClientFactory<TServiceClient> clientFactory,String ip,int port,
									  PoolOperationCallBack callback) throws Exception {
		this.clientFactory = clientFactory;
		this.callback = callback;
		this.ip = ip;
		this.port = port;
	}

	static interface PoolOperationCallBack {
		// 销毁client之前执行
		void destroy(TServiceClient client);

		// 创建成功是执行
		void make(TServiceClient client);
	}

	@Override
	public void destroyObject(TServiceClient client) throws Exception {
		if (callback != null) {
			try {
				callback.destroy(client);
			} catch (Exception e) {
				logger.warn("destroyObject:{}", e);
			}
		}
		logger.info("destroyObject:{}", client);
		TTransport pin = client.getInputProtocol().getTransport();
		pin.close();
		TTransport pout = client.getOutputProtocol().getTransport();
		pout.close();
	}

	@Override
	public void activateObject(TServiceClient client) throws Exception {
	}

	@Override
	public void passivateObject(TServiceClient client) throws Exception {
	}

	@Override
	public boolean validateObject(TServiceClient client) {
		TTransport pin = client.getInputProtocol().getTransport();
		logger.info("validateObject input:{}", pin.isOpen());
		TTransport pout = client.getOutputProtocol().getTransport();
		logger.info("validateObject output:{}", pout.isOpen());
		return pin.isOpen() && pout.isOpen();
	}

	@Override
	public TServiceClient makeObject() throws Exception {

		TSocket tsocket = new TSocket(ip, port);
		TTransport transport = new TFramedTransport(tsocket);
		TProtocol protocol = new TBinaryProtocol(transport);
		TServiceClient client = this.clientFactory.getClient(protocol);
		transport.open();
		if (callback != null) {
			try {
				callback.make(client);
			} catch (Exception e) {
				logger.warn("makeObject:{}", e);
			}
		}
		return client;
	}

}
