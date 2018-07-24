package com.example.lilingyun.zuoyefiledownload.download;

import java.io.File;

public interface DownloadCallback {
    void onSuccess(File file);  //下载成功

    void onFailure(Exception e);  //下载失败

    void onProgress(long progress,long currentLength);//下载进行

    void onPause(long progress,long currentLength);//下载停止
}
