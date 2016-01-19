package cn.jpush.dubbo.thritf093;

import org.apache.thrift.TException;

/**
 * Created by rocyuan on 16/1/18.
 */
public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public User getUser(int id, String name, int age) throws Xception, TException {
        System.out.println("execute  getUser ");
        return null;
    }

    @Override
    public String getString(String str) throws TException {
        System.out.println("execute  getString ");
        return "HelloServiceImpl : " + str;
    }

    @Override
    public void sayHello(String str) throws TException {
        System.out.println("execute  sayHello ");
    }
}
