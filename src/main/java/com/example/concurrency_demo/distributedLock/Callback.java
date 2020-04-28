package com.example.concurrency_demo.distributedLock;

/**
 * Created by sunyujia@aliyun.com on 2016/2/23.
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeout() throws InterruptedException;
}
