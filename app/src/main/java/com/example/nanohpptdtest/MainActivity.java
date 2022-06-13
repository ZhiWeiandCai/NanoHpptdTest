package com.example.nanohpptdtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class MainActivity extends AppCompatActivity {
    FileServer mHttpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHttpServer = new FileServer(FileServer.HTTP_IP);
        try {
            mHttpServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mHttpServer.stop();//停止
    }

}