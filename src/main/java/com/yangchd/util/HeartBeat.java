package com.yangchd.util;

import org.csource.fastdfs.TrackerServer;

import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 连接池定时器设置
 * @author yangchd
 * @date 2017年11月15日
 */
class HeartBeat {
    /** fastdfs连接池 */
    private ConnectionPool pool = null;
    /** 等待时间 */
    private static int waitTimes = 200;

    HeartBeat(ConnectionPool pool) {
        this.pool = pool;
    }

    /**
     * 定时执行任务，检测当前的空闲连接是否可用，如果不可用将从连接池中移除
     */
    void beat() {
        FastDFSLogger.info("[心跳任务方法（beat）初始化]");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String logId = UUID.randomUUID().toString();
                LinkedBlockingQueue<TrackerServer> idleConnectionPool = pool
                        .getIdleConnectionPool();
                FastDFSLogger.info("[心跳任务方法（beat）]["
                        + logId
                        + "][Description:对idleConnectionPool中的TrackerServer进行监测,当前数量"+idleConnectionPool.size()+"]");
                TrackerServer trackerServer = null;
                for (int i = 0; i < idleConnectionPool.size(); i++) {
                    try {
                        trackerServer = idleConnectionPool.poll(waitTimes,
                                TimeUnit.SECONDS);
                        if (trackerServer != null) {
                            org.csource.fastdfs.ProtoCommon.activeTest(trackerServer
                                    .getSocket());
                            idleConnectionPool.add(trackerServer);
                        } else {
                            /* 代表已经没有空闲长连接 */
                            break;
                        }
                    } catch (Exception e) {
                        /* 发生异常,要删除，进行重建 */
                        FastDFSLogger.error("[心跳任务方法（beat）][" + logId
                                + "][异常：当前连接已不可用将进行重新获取连接]");
                        pool.drop(trackerServer, logId);
                    }
                }
            }
        };
//        Timer timer = new Timer();
//        这里使用ScheduledExecutorService替代Timer
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
        newScheduledThreadPool.scheduleWithFixedDelay(task, 60, 3600, TimeUnit.SECONDS);
    }
}