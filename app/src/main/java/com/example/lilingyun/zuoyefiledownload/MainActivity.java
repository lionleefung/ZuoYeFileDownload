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
    private String[] names = {"Oicq","Pink Gum","Pc Zone","Hezisi","Yamadie","Hehehe","Guabeisi","Ma Jia Jia","Qu Mingzi","Gezidan"};
    private String[] url = {"http://gdown.baidu.com/data/wisegame/f170a8c78bcf9aac/QQ_818.apk",
            "http://gdown.baidu.com/data/wisegame/89eb17d6287ae627/weixin_1300.apk",
            "https://dl.guangzhuiyuan.com/as/config/i_2g7hnsaatezon4s.da",
            "http://gdown.baidu.com/data/wisegame/7a6e27989f0ea852/tiebajisuban_100991744.apk",
            " http://gdown.baidu.com/data/wisegame/09b36392389cd2af/longguangkehuduan_13.apk",
            "http://gdown.baidu.com/data/wisegame/89fce26b620d8d43/QQkongjian_109.apk",
            "http://gdown.baidu.com/data/wisegame/f12d5a939c1f914a/jinritoutiao_531.apk",
            "https://dl.guangzhuiyuan.com/as/config/i_2g7hnsaatezon4s.da",
            "http://gdown.baidu.com/data/wisegame/7a6e27989f0ea852/tiebajisuban_100991744.apk",
            " http://gdown.baidu.com/data/wisegame/09b36392389cd2af/longguangkehuduan_13.apk"};

    private MyProgressbar mQQpb;
    private MyProgressbar mWeChantPb;
    private MyProgressbar mQzonePb;
    private MyProgressbar mHezisi;
    private MyProgressbar mYamadie;
    private MyProgressbar mHehehe;
    private MyProgressbar mGuabeisi;
    private MyProgressbar mMajiajia;
    private MyProgressbar mQumingzi;
    private MyProgressbar mGezidan;



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

        mGezidan = findViewById(R.id.gezidan);
        mGezidan.setTag(STATUS_DOWNLOADING);
        mGezidan.setOnClickListener(this);

        mGuabeisi = findViewById(R.id.Guabeisi);
        mGuabeisi.setOnClickListener(this);
        mGuabeisi.setTag(STATUS_DOWNLOADING);

        mHehehe = findViewById(R.id.hehehe);
        mHehehe.setOnClickListener(this);
        mHehehe.setTag(STATUS_DOWNLOADING);

        mHezisi = findViewById(R.id.hezisi);
        mHezisi.setTag(STATUS_DOWNLOADING);
        mHezisi.setOnClickListener(this);

        mMajiajia = findViewById(R.id.majiajia);
        mMajiajia.setOnClickListener(this);
        mMajiajia.setTag(STATUS_DOWNLOADING);

        mQumingzi = findViewById(R.id.qumingzi);
        mQumingzi.setTag(STATUS_DOWNLOADING);
        mQumingzi.setOnClickListener(this);

        mYamadie = findViewById(R.id.yamadie);
        mYamadie.setOnClickListener(this);
        mYamadie.setTag(STATUS_DOWNLOADING);
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
                                    //Log.d(TAG,"下载进度"+totalProgress+"  "+currentLength);
                                    mQQpb.setCurrentProgress(Utils.keepTwoBit((float) totalProgress /currentLength));
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
                                    mWeChantPb.setCurrentProgress(Utils.keepTwoBit((float) totalProgress /currentLength));
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

                }
                break;

            case R.id.hezisi:
                if (mHezisi.getTag().equals(STATUS_DOWNLOADING)) {
                    mHezisi.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[3], url[3], new DownloadCallback() {
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
                                    mHezisi.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[3] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mHezisi.getTag().equals(STATUS_STOP)) {
                    mHezisi.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[3]);

                }
                break;

            case R.id.yamadie:
                if (mYamadie.getTag().equals(STATUS_DOWNLOADING)) {
                    mYamadie.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[4], url[4], new DownloadCallback() {
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
                                    mYamadie.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[4] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mYamadie.getTag().equals(STATUS_STOP)) {
                    mYamadie.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[4]);

                }
                break;

            case R.id.hehehe:
                if (mHehehe.getTag().equals(STATUS_DOWNLOADING)) {
                    mHehehe.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[5], url[5], new DownloadCallback() {
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
                                    mHehehe.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[5] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mHehehe.getTag().equals(STATUS_STOP)) {
                    mHehehe.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[5]);

                }
                break;

            case R.id.Guabeisi:
                if (mGuabeisi.getTag().equals(STATUS_DOWNLOADING)) {
                    mGuabeisi.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[6], url[6], new DownloadCallback() {
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
                                    mGuabeisi.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[6] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mGuabeisi.getTag().equals(STATUS_STOP)) {
                    mGuabeisi.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[6]);

                }
                break;

            case R.id.majiajia:
                if (mMajiajia.getTag().equals(STATUS_DOWNLOADING)) {
                    mMajiajia.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[7], url[7], new DownloadCallback() {
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
                                    mMajiajia.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[7] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mMajiajia.getTag().equals(STATUS_STOP)) {
                    mMajiajia.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[7]);

                }
                break;

            case R.id.qumingzi:
                if (mQumingzi.getTag().equals(STATUS_DOWNLOADING)) {
                    mQumingzi.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[8], url[8], new DownloadCallback() {
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
                                    mQumingzi.setCurrentProgress(Utils.keepTwoBit((float) totalProgress /  currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[8] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mQumingzi.getTag().equals(STATUS_STOP)) {
                    mQumingzi.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[8]);

                }
                break;

            case R.id.gezidan:
                if (mGezidan.getTag().equals(STATUS_DOWNLOADING)) {
                    mGezidan.setTag(STATUS_STOP);
                    DownloadDispatcher.getInstance().startDownload(names[9], url[9], new DownloadCallback() {
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
                                    mGezidan.setCurrentProgress(Utils.keepTwoBit((float) totalProgress / currentLength));
                                }
                            });
                        }

                        @Override
                        public void onPause(long progress, long currentLength) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, names[9] + "暂停下载", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else if (mGezidan.getTag().equals(STATUS_STOP)) {
                    mGezidan.setTag(STATUS_DOWNLOADING);
                    DownloadDispatcher.getInstance().stopDownload(url[9]);

                }
                break;
        }
    }
}

