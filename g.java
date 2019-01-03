package org.shr.sockettesting;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class g extends Application {
    private static final String URL = "http://192.168.10.138:2010";

    @Override
    public void onCreate() {
        super.onCreate();
    /*    try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }*/
    }


}