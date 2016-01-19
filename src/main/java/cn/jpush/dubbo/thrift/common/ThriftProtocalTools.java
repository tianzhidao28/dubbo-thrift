package cn.jpush.dubbo.thrift.common;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.*;
import org.jboss.netty.buffer.ChannelBuffer;

import cn.jpush.dubbo.thrift.transport.ThriftTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftProtocalTools {

	private static final Logger logger = LoggerFactory.getLogger(ThriftProtocalTools.class);
	static final ConcurrentMap<String, Class<?>> CACHED_CLASS = new ConcurrentHashMap<String, Class<?>>();
	static final ConcurrentMap<String, Constructor<?>> CACHED_CONSTRUCTOR = new ConcurrentHashMap<String, Constructor<?>>();
	
	public static int getTMessageId(ChannelBuffer buf) {
    	int length = buf.getInt(4);
    	int id = buf.getInt(8 + length);
    	return id;
	}
	
	public static String getStringAtAbsoluteIndex(ChannelBuffer buf, int index){
        byte[] arr = new byte[buf.getInt(index)];
        buf.getBytes(index + 4, arr);
        String str = null;
		try {
			str = new String(arr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 纯粹的二进制协议传输
	 * @param input
	 * @param output
     * @return
     */
	public static TProtocol newBinaryProtocol(ChannelBuffer input, ChannelBuffer output) {
		return new TBinaryProtocol(new ThriftTransport(input, output));
	}


	/**
	 * 多路复用的二进制协议
	 * @param input
	 * @param output
	 * @param serviceName 服务暴露的名称
	 * @return
	 */
	public static TProtocol newTMultiplexedBinaryProtocol(ChannelBuffer input, ChannelBuffer output,String serviceName) {
		TProtocol protocol = newBinaryProtocol(input,output);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, serviceName);
		return new TBinaryProtocol(new ThriftTransport(input, output));
	}

	
	public static String getArgsClassName(String serviceName, String methodName, String tag) {
		return serviceName.substring(0, serviceName.lastIndexOf("$")) + "$" + methodName + tag;
	}
	
	public static Class<?> getTBaseClass(String argsServiceName) throws Exception {
		Class<?> clazz = CACHED_CLASS.get(argsServiceName);
		if (clazz == null) {
			CACHED_CLASS.putIfAbsent(argsServiceName, Class.forName(argsServiceName));
			clazz = CACHED_CLASS.get(argsServiceName);
		}
		return clazz;
	}
	
	public static Class<?> getTBaseClass(String serviceName, String methodName, String tag) 
			throws Exception {
		String argsServiceName = getArgsClassName(serviceName, methodName, tag);
		return getTBaseClass(argsServiceName);
	}
	
	public static TBase<?, ?> getTBaseObject(Class<?> clazz, Class<?>[] parameterTypes, Object[] initargs) 
			throws Exception {
		StringBuilder keyBuf = new StringBuilder(clazz.getName());
		if (parameterTypes != null) {
			 for (Class<?> t : parameterTypes) {
				 keyBuf.append(".").append(t.getName());
			 }
		} else {
			keyBuf.append(".").append(parameterTypes);
		}
		String key = keyBuf.toString();
		Constructor<?> constructor = CACHED_CONSTRUCTOR.get(key);
		if (constructor == null) {
			CACHED_CONSTRUCTOR.putIfAbsent(key, clazz.getConstructor(parameterTypes));
			constructor = CACHED_CONSTRUCTOR.get(key);
		}
		return (TBase<?, ?>) constructor.newInstance(initargs);
	}
	
	public static Object getResult(TBase<?, ?> _result) throws Exception {
		try {
			Field success = _result.getClass().getDeclaredField("success");//hard code
			success.setAccessible(true);
			Object object = success.get(_result);
			if (object != null) {
				return object;
			}
		} catch (NoSuchFieldException e) {//没有success字段说明方法是void
			return void.class;
		}
		Field[] fields = _result.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.getModifiers() == Modifier.PUBLIC 
					&& TBase.class.isAssignableFrom(f.getType())
					&& Exception.class.isAssignableFrom(f.getType())) {
				f.setAccessible(true);
				if (f.get(_result) != null) {
					throw (Exception)f.get(_result);
				}
			}
		}
		throw new TApplicationException(TApplicationException.MISSING_RESULT, "unknown result");
	}

	/**
	 * 异常时发送的TMessage
	 * @param oprot
	 * @param methodName
	 * @param id
	 * @param errMsg
     * @throws Exception
     */
	public static void createErrorTMessage(TProtocol oprot, String methodName, int id, String errMsg)
			throws Exception {
		TMessage tmessage = new TMessage(methodName, TMessageType.EXCEPTION, id);
		oprot.writeMessageBegin(tmessage);
		oprot.writeMessageEnd();
		TApplicationException ex = new TApplicationException(TApplicationException.INTERNAL_ERROR, errMsg); 
		try {
			ex.write(oprot);
		} catch (TException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 通过 接口 *****.Iface得到 className的简称作为 serviceName
	 * @param clazz 接口类 Iface
	 * @return serviceName
     */
	public static String getServiceNameByInterface(Class clazz) {
		// 理论上className一定是 XXXXX$Iface
		return clazz.getSimpleName().replaceAll("\\.Iface$","");
	}
}
