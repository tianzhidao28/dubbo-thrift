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
package cn.jpush.dubbo;

import cn.jpush.alarm.AlarmClient;
import com.alibaba.dubbo.common.logger.Logger;

/**
 * 服务的相关信息通知
 * Created by rocyuan on 16/2/2.
 */
public class ServiceRunInfoHelper {

    private static boolean isLocalMachine = false;

    /**
     * 粗略的判断下是不是本机
     */
    static {
        isLocalMachine = false;
        String os = System.getProperty("os.name").toLowerCase();
        isLocalMachine = os.contains("mac") || os.contains("windows");
    }

    public static void info(Logger logger,String msg) {
        logger.info(msg);
        if (!isLocalMachine) {
            AlarmClient.send(81,msg);
        }

    }

    public static void error(Logger logger,String msg) {
        logger.error(msg);
        if (!isLocalMachine) {
            AlarmClient.send(81,msg);
        }

    }




}
