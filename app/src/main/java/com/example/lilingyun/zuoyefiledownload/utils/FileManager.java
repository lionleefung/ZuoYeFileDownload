package com.example.lilingyun.zuoyefiledownload.utils;

import android.content.Context;

import java.io.File;

public class FileManager {
    private static final FileManager INSTANCE = new FileManager();
    private File mRootDir;
    private Context mContext;

    private FileManager(){

    }

    public static FileManager getInstance(){return  INSTANCE;}

    public void init(Context context){this.mContext = context.getApplicationContext();}

    public void rootDir(File file){
        if(! file.exists()){
            file.mkdir();
        }
        if(! file.exists() && file.isDirectory()){
            mRootDir = file;
        }
    }
    /*通过网络下载路径获取一个本地文件路径*/
    public File getFile(String url){
        String fileName = Utils.md5Url(url);
        if(mRootDir == null){
            mRootDir = mContext.getCacheDir();
        }
        return new File(mRootDir,fileName);
    }
}
