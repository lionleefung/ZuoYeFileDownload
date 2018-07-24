package com.example.lilingyun.zuoyefiledownload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lilingyun.zuoyefiledownload.download.DownloadCallback;
import com.example.lilingyun.zuoyefiledownload.download.DownloadDispatcher;
import com.example.lilingyun.zuoyefiledownload.myprogressbar.MyProgressbar;
import com.example.lilingyun.zuoyefiledownload.utils.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION_CODE = 0x088;
    private String[] names = {"QQ","weixin","QQ空间"};
    private String[] url = {"http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk",//http://gdown.baidu.com/data/wisegame/f12d5a939c1f914a/jinritoutiao_531.apk
            "http://gdown.baidu.com/data/wisegame/89eb17d6287ae627/weixin_1300.apk",  // http://gdown.baidu.com/data/wisegame/09b36392389cd2af/longguangkehuduan_13.apk
            "http://gdown.baidu.com/data/wisegame/89fce26b620d8d43/QQkongjian_109.apk"};
     private MyProgressbar mQQpb;
    private MyProgressbar mWeChantPb;
    private MyProgressbar mQzonePb;

    private static final String STATUS_DOWNLOADING = "downloading";
    private static final String STATUS_STOP = "stop";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQQpb = findViewById(R.id.qq_pb);
        mQQpb.setTag(STATUS_DOWNLOADING);
        mWeChantPb = findViewById(R.id.wechat_pb);
        mWeChantPb.setTag(STATUS_DOWNLOADING);
        mQzonePb = findViewById(R.id.qzone_pb);
        mQzonePb.setTag(STATUS_DOWNLOADING);
        mQzonePb.setOnClickListener(this);
        mWeChantPb.setOnClickListener(this);
        mQQpb.setOnClickListener(this);
        int isPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(isPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "需要存储权限", Toast.LENGTH_SHORT).show();
                //没有授权的话，直接finish掉Activity
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.qq_pb:
                if (mQQpb.getTag().equals(STATUS_DOWNLOADING)) {
                    mQQpb.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[0], url[0], new DownloadCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.i(TAG, "onFailure:下载失败");
                        }

                        @Override
                        public void onSuccess(File file) {
                            Log.i(TAG, "onSuccess:下载成功 " + file.getAbsolutePath());
                        }

                        @Override
                        public void onProgress(final long totalProgress, final long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mQQpb.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[0] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mQQpb.getTag().equals(STATUS_STOP)) {
                    mQQpb.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[0]);
                    mQQpb.setText("继续");
                }
                break;
            case R.id.wechat_pb:
                if (mWeChantPb.getTag().equals(STATUS_DOWNLOADING)) {
                    mWeChantPb.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[1], url[1], new DownloadCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.i("DownLoadActivity", "onFailure:下载失败");
                        }

                        @Override
                        public void onSuccess(File file) {
                            Log.i("DownLoadActivity", "onSuccess:下载成功 " + file.getAbsolutePath());
                        }

                        @Override
                        public void onProgress(final long totalProgress, final long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mWeChantPb.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[1] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mWeChantPb.getTag().equals(STATUS_STOP)) {
                    mWeChantPb.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[1]);
                    mWeChantPb.setText("继续");
                }
                break;
            case R.id.qzone_pb:
                if (mQzonePb.getTag().equals(STATUS_DOWNLOADING)) {
                    mQzonePb.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[2], url[2], new DownloadCallback() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.i(TAG, "onFailure:下载失败");
                        }

                        @Override
                        public void onSuccess(File file) {
                            Log.i(TAG, "onSuccess:下载成功 " + file.getAbsolutePath());
                        }

                        @Override
                        public void onProgress(final long totalProgress, final long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mQzonePb.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /**
                                     * 记录断点，todo
                                     */
                                    Toast.makeText(MainActivity.this, names[2] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }else if (mQzonePb.getTag().equals(STATUS_STOP)) {
                    mQzonePb.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[2]);
                    mQzonePb.setText("继续");
                }
                break;
        }
    }
}

