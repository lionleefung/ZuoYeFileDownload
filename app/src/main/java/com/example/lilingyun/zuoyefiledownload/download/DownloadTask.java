package com.example.lilingyun.zuoyefiledownload.download;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载apk,复用类
 */
public class DownloadTask {
    private static final String TAG = "DownloadTask";
    //下载文件的url
    private String url;
    //文件名称
    private String name;
    //文件大小
    private long mContentLength;
    //下载文件的线程个数
    private int mThreadSize;
    //线程下载成功的个数，变量修饰volatile,多线程保证变量的可见性
    private volatile int mSuccessNumber;
    //总进度 = 每个线程的进度和
    private long mTotalProgress;
    private List<DownloadRunnable> mDownloadRunnables;
    private DownloadCallback mDownloadCallback;


    public DownloadTask(String name, String url, int threadSize, long contentLength, DownloadCallback callBack) {
        this.name = name;
        this.url = url;
        this.mThreadSize = threadSize;
        this.mContentLength = contentLength;
        this.mDownloadRunnables = new ArrayList<>();
        this.mDownloadCallback = callBack;
    }

    public void init(){
        for(int i = 0;i < mThreadSize; i++){
            //初始化的时候，需要读取数据库
            //每个线程的下载大小threadSize
            long threadSize = mContentLength / mThreadSize;
            //开始下载的位置
            long start = i * threadSize;
            //结束下载的位置
            long end = start + threadSize - 1;
            if(i == mThreadSize - 1 ){
                end = mContentLength - 1;
            }
            DownloadRunnable downloadRunnable = new DownloadRunnable(name, url, mContentLength, i, start, end, new DownloadCallback() {
                @Override
                public void onSuccess(File file) {
                    mSuccessNumber = mSuccessNumber + 1;
                    if(mSuccessNumber == mThreadSize){
                        mDownloadCallback.onSuccess(file);
                        DownloadDispatcher.getInstance().recyclerTask(DownloadTask.this);
                    //如果下载完毕，清楚数据库 todo
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //有一个线程发生异常，下载失败，需要其他线程停止掉
                    mDownloadCallback.onFailure(e);
                    stopDownload();

                }

                @Override
                public void onProgress(long progress, long currentLength) {
                    //叠加下progress,实时更新进度条
                    //这里需要synchronized下
                    synchronized (DownloadTask.this){
                        mTotalProgress = mTotalProgress + progress;
                        mDownloadCallback.onProgress(mTotalProgress,currentLength);
                    }
                }

                @Override
                public void onPause(long progress, long currentLength) {
                    mDownloadCallback.onPause(progress,currentLength);

                }
            });
            DownloadDispatcher.getInstance().executorService().execute(downloadRunnable);
            mDownloadRunnables.add(downloadRunnable);
        }
    }
    public void stopDownload(){
        for(DownloadRunnable runnable : mDownloadRunnables){
            runnable.stop();
        }
    }
    public String getUrl(){
        return url;
    }
}

