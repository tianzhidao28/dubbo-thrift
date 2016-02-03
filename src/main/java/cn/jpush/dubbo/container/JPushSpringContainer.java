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
package cn.jpush.dubbo.container;

import cn.jpush.dubbo.ServiceRunInfoHelper;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.container.Container;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * jpush_spring 扩展名
 * Created by rocyuan on 16/2/2.
 */
public class JPushSpringContainer implements Container {

    private static final Logger logger = LoggerFactory.getLogger(JPushSpringContainer.class);

    /**
     * 用此文件配置 启动的spring主配置文件的名字 eg: classpath*:spring-root.xml
     */
    public static final String SPRING_CONFIG = "dubbo.spring.config";

    /**
     * 默认启动 加载全部以spring开头的文件,不过最好还是配置一个 主入口文件的spring配置文件; spring-root.xml spring-application.xml等
     */
    public static final String DEFAULT_SPRING_CONFIG = "classpath*:spring-root.xml";

    static ClassPathXmlApplicationContext context;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }

    public void start() {
        String configPath = ConfigUtils.getProperty(SPRING_CONFIG);
        if (configPath == null || configPath.length() == 0) {
            logger.info("未配置 dubbo.spring.config ,已使用默认主配置文件: classpath*:spring-root.xml");
            configPath = DEFAULT_SPRING_CONFIG;
        }
        logger.info("正在加载 spring配置文件:"+configPath+",容器正在启动.");
        context = new ClassPathXmlApplicationContext(configPath.split("[,\\s]+"));
        context.start();
        ServiceRunInfoHelper.info(logger,getServiceInfo() + "spring容器启动完毕");
    }

    public void stop() {
        try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
                ServiceRunInfoHelper.info(logger,getServiceInfo() + " 此服务停止了;");
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            logger.info("spring 容器关闭异常");
        }
    }


    /**
     *    dubbo.container=log4j,spring
     *    dubbo.application.name=data-service-server
     *    dubbo.application.owner=rocyuan
     *    dubbo.registry.address=zookeeper://demo.thering.cn:2181
     * @return
     */
    private String getServiceInfo() {
        String configNeededs[] = {"dubbo.container","dubbo.application.name","dubbo.application.owner","dubbo.registry.address"};

        List<String> noConfigProperties = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<configNeededs.length; ++i) {
            String value = ConfigUtils.getProperty(configNeededs[i]);
            if (StringUtils.isBlank(value)){
                noConfigProperties.add(configNeededs[i]);
            } else {
                sb.append(configNeededs[i]).append(":").append(value).append(";");
            }
        }
        return sb.toString() + (noConfigProperties.size()>0 ? ("未配置:"+StringUtils.join(noConfigProperties,",")) : "");




    }

}
