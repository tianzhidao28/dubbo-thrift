package cn.jpush.dubbo.thrift.common;

/**
 * Thrift Server的集中工作模式
 * Created by rocyuan on 16/1/21.
 */

public  enum ThriftServerType {
    TThreadPoolServer,TNonblockingServer,THsHaServer;
}
