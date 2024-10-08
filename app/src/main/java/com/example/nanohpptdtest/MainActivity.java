package com.example.nanohpptdtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_READ_STORAGE = 1001;
    private static final int REQUEST_CODE_PICK_VIDEO = 1;
    //NanoHTTPD test
    //FileServer mHttpServer;
    private TextView textViewHello;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*mHttpServer = new FileServer(FileServer.HTTP_IP);
        try {
            mHttpServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //test(); //RxJava test
        textViewHello = findViewById(R.id.tv_hello);
        textViewHello.setOnClickListener(view -> {
            test2();
        });
        mVideoView = findViewById(R.id.video_view);
    }

    private void test2() {
        Log.i(TAG, "test2() = 前");
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，需要申请
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            // 已经拥有权限，可以执行读取操作
            openGalleryForVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，可以执行读取操作
                openGalleryForVideo();
            } else {
                // 用户拒绝了权限，提示用户需要权限才能继续操作
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();
            // 处理所选视频的 URI
            Log.i(TAG, "selectedVideoUri = " + selectedVideoUri);
            playSelectedVideo(selectedVideoUri);
        }
    }

    private void playSelectedVideo(Uri sUri) {
        if (sUri != null) {
            mVideoView.setVideoURI(sUri);

            // 添加媒体控制器
            MediaController mediaController = new MediaController(this);
            mVideoView.setMediaController(mediaController);
            mediaController.setAnchorView(mVideoView);

            mVideoView.start();  // 开始播放
        }
    }

    private void openGalleryForVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");  // 只显示视频
        startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }

    /**
     * RxJava test
     */
    private void test() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "订阅时刻thread = " + Thread.currentThread().getName()
                        + " id = " + Thread.currentThread().getId());
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i(TAG, "观察者thread = " + Thread.currentThread().getName()
                        + " id = " + Thread.currentThread().getId());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable<String> observable = Observable.create(emitter -> {
            Log.i(TAG, "执行任务thread = " + Thread.currentThread().getName()
                    + " id = " + Thread.currentThread().getId());
            emitter.onNext(" hello");
            emitter.onComplete();
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(observer);
    }

    protected void onDestroy() {
        super.onDestroy();
        //mHttpServer.stop();//停止
    }

}