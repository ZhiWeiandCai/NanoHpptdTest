package com.example.nanohpptdtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.observers.BlockingObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //NanoHTTPD test
    //FileServer mHttpServer;

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
        test();
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