package com.example.ja010.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permi();

    }
    public String getExternalPath(){ // sd 카드 경로 설정
        String sdpath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)){
            sdpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        }
        else
            sdpath = getFilesDir() +"";
    return sdpath;
    }
    private void permi(){ // 권한 설정
        int pinfo = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(pinfo == PackageManager.PERMISSION_GRANTED){
        }
        else {
            String a [] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, a,100);
            }
            else {
            }
        }
    }


}
