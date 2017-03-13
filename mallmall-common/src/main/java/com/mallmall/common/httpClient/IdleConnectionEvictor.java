package com.mallmall.common.httpClient;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 定期清理无线连接
 * 
 * @author Ja0ck5
 *
 */
public  class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager httpClientConnectionManager;

    private volatile boolean shutdown;

    public IdleConnectionEvictor(HttpClientConnectionManager httpClientConnectionManager) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        //启动线程
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    httpClientConnectionManager.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
