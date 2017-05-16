package com.example.ja010.myapplication;

import android.content.Context;

/**
 * Created by ja010 on 17-05-16.
 */

public class title {
    String tt;
    String c;
    public title(String tt,String c){
        this.tt = tt;
        this.c = c;
    }

    public String getTt() {
        return tt;
    }
    public String getContext(){
        return c;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }
    public void setC(String c) {
        this.c = c;
    }
}
