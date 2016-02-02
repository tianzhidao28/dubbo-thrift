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
