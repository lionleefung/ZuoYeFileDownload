package com.example.lilingyun.zuoyefiledownload.download;

import android.os.Environment;
import android.util.Log;

import com.example.lilingyun.zuoyefiledownload.okhttp.OkHttpManager;
import com.example.lilingyun.zuoyefiledownload.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import okhttp3.Response;

public class DownloadRunnable implements Runnable{

    private static final String TAG = "DownloadRunnable";
    private static final int STATUS_DOWNLOADING = 1;
    private static final int STATUS_STOP = 2;
    //线程的状态
    private int mStatus = STATUS_DOWNLOADING;
    //文件下载的url
    private String url;
    //文件的名称
    private String name;

    //线程个数
    private int mThreadsize;

    //线程id
    private int threadId;
    //每个线程下载开始的位置
    private long start;
    //每个线程下载结束的位置
    private long end;
    //每个线程的下载进度
    private long mProgress;
    //文件的总大小
    private long mCurrentLength;
    private DownloadCallback downloadCallback;

    public DownloadRunnable(String name,String url,long currentLength,
                            int threadId,long start,long end,int threadsize,DownloadCallback downloadCallback){
        this.name = name;
        this.url = url;
        this.mCurrentLength = currentLength;
        this.threadId = threadId;
        this.start = start;
        this.end = end;
        this.downloadCallback =downloadCallback;
        this.mThreadsize = threadsize;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try{
            Response response = OkHttpManager.getInstance().syncResponse(url,start,end);
            Log.i(TAG,"fileName=" + name + " 每个线程负责下载的文件大小contentLength=" + (end - start + 1) +
            " 开始位置start=" + start + " 结束位置end=" + end + " threadId=" + Thread.currentThread().getId());
            inputStream = response.body().byteStream();
            long length1 = response.body().contentLength();
            Log.d(TAG,String.valueOf(length1));
            //保存文件路径
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),name);
            randomAccessFile = new RandomAccessFile(file,"rwd");
            //从哪里开始下载
            randomAccessFile.seek(start);
            int length;
            byte[] bytes = new byte[10 * 1024];
            //读取下载文件并保存，遇到文件尾结束
            while(((length = inputStream.read(bytes)) != -1) ){
               // Log.d(TAG,"读取文件长度为"+String.valueOf(mProgress)+",线程ID为："+threadId+"文件名为："+name);
                if(mStatus == STATUS_STOP){
                    downloadCallback.onPause(length,mCurrentLength);
                    break;
                }
                //写入
                randomAccessFile.write(bytes,0,length);
                //保存进度，断点待实现 todo
                mProgress = mProgress +length;
                //实时更新进度条，将每次写入的length传出去
                downloadCallback.onProgress(length,mCurrentLength * mThreadsize);
            }
            downloadCallback.onSuccess(file);
        }catch (IOException e){
            e.printStackTrace();
            downloadCallback.onFailure(e);
        }finally {
            Utils.close(inputStream);
            Utils.close(randomAccessFile);
            //保存数据库 todo
        }
    }
    public void stop(){
        mStatus = STATUS_STOP;
    }
}
