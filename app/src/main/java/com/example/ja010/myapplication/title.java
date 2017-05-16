package com.example.ja010.myapplication;

import android.content.Context;

/**
 * Created by ja010 on 17-05-16.
 */

public class title {
    String tt;
    Context c;
    public title(String tt,Context c){
        this.tt = tt;
        this.c = c;
    }

    public String getTt() {
        return tt;
    }
    public Context getContext(){
        return c;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }
    public void setC(Context c) {
        this.c = c;
    }
}
