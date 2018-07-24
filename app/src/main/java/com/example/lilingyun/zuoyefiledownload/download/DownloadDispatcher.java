package com.example.lilingyun.zuoyefiledownload.download;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lilingyun.zuoyefiledownload.okhttp.OkHttpManager;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadDispatcher {
    private static final String TAG = "DownloadDispatcher";
    private static volatile DownloadDispatcher sDownloadDispatcher;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors(); //JVM可用处理器个数
    private static final int THREAD_SIZE = Math.max(3,Math.min(CPU_COUNT - 1,5));
    //内核线程数
    private static final int CORE_POOL_SIZE = THREAD_SIZE;
    //线程池
    private ExecutorService mExecutorService;
    private final Deque<DownloadTask> runningTasks = new ArrayDeque<>();


    private DownloadDispatcher(){

    }

    public static  DownloadDispatcher getInstance(){   //如果有新任务则
        if(sDownloadDispatcher == null){
            synchronized (DownloadDispatcher.class){
                if(sDownloadDispatcher == null){
                    sDownloadDispatcher = new DownloadDispatcher();
                }
            }
        }
        return sDownloadDispatcher;
    }

    /**
     * 创建线程池
     *synchronized代表这个方法加锁,相当于不管哪一个线程（例如线程A），运行到这个方法时,
     * 都要检查有没有其它线程B（或者C、 D等）正在用这个方法(或者该类的其他同步方法)，
     * 有的话要等正在使用synchronized方法的线程B（或者C 、D）运行完这个方法后再运行此线程A,
     * 没有的话,锁定调用者,然后直接运行。
     */
    public synchronized ExecutorService executorService(){
        if(mExecutorService == null){
            mExecutorService = new ThreadPoolExecutor(CORE_POOL_SIZE, Integer.MAX_VALUE, 60, TimeUnit.SECONDS
                    , new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return mExecutorService;
    }

    /**
     * 文件名
     * 下载地址
     * 回调接口
     *
     */
    public void startDownload(final String name,final String url,final DownloadCallback callback){
        Call call = OkHttpManager.getInstance().asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取文件大小
                long contentLength = response.body().contentLength();
                Log.i(TAG,"contentLength=" +contentLength);
                if(contentLength <= -1){
                    return ;
                }
                DownloadTask downloadTask = new DownloadTask(name,url,THREAD_SIZE,contentLength,callback);
                    downloadTask.init();
                    runningTasks.add(downloadTask);
            }
        });
    }
    /**
     * 根据url去暂停哪个
     */
    public void stopDownload(String url){
        //这个停止是不是这个正在下载的
        for(DownloadTask runningTask:runningTasks){
            if(runningTask.getUrl().equals(url)){
                runningTask.stopDownload();
            }
        }
    }

    public void recyclerTask(DownloadTask downloadTask){
        runningTasks.remove(downloadTask);
    }

}
