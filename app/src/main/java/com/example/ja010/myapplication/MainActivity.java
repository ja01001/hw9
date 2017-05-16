package com.example.ja010.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listv;
    ArrayList<String> memos = new ArrayList<String>();
    ArrayAdapter<String >adapter;
    DatePicker dp;
    LinearLayout l1,l2; // l1 = list , l2 = datepicker
    Button save,cancel;
    EditText e1;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permi();
        dp = (DatePicker)findViewById(R.id.dp);
        l1 = (LinearLayout)findViewById(R.id.linear1);
        l2 = (LinearLayout)findViewById(R.id.linear2);
        save = (Button)findViewById(R.id.btnsave);
        cancel = (Button)findViewById(R.id.btncancel);
        e1 = (EditText)findViewById(R.id.et);
        listv = (ListView)findViewById(R.id.lv);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memos);
        listv.setAdapter(adapter);
        mkdi();
        memos.add("메모");
        adapter.notifyDataSetChanged();
        Toast.makeText(this,memos.get(0),Toast.LENGTH_SHORT).show();
        // long item click event (not)
        listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dig = new AlertDialog.Builder(MainActivity.this);
                dig.setTitle("삭제?").setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        memos.remove(i);

                    }
                }).setPositiveButton("아니요",null).show();

                return true;
            }
        });
        // item click event ( not)
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE); // data set;
                save.setText("수정");
                read(getExternalPath()+"diary/"+memos.get(i));
            }
        });


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
    } //clear
    private void permi(){ // 권한 설정
        int pinfo = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(pinfo == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"권한이 있네요",Toast.LENGTH_SHORT).show();
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,"권한설정좀...",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }
    public void mkdi(){ //clear
        String path = getExternalPath();
        File file = new File(path +"diary");
        file.mkdir();
        String msg = "make";
        if(file.isDirectory() ==false){
            msg = "error";
        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,""+path,Toast.LENGTH_SHORT).show();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
                break;
            case R.id.btnsave:
                if(save.getText()=="수정"){
                    // 수정시 넣을 내용s
                }
                else{
                    datepick();
                    title tt = new title(data,e1.getText().toString());
                    memos.add(tt.getTt());
                    adapter.notifyDataSetChanged();
                    write(getExternalPath()+"diary/"+tt.getTt(),tt.getContext());
                    Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btncancel:
                Toast.makeText(this,"취소",Toast.LENGTH_SHORT).show();
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);

        }
    }
    public void write(String path,String title){// file 쓰기
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path,true));
            bw.write(title);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }
    public void read(String path){ // file 읽기
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String readstr = "";
            String str;
            while ((str = br.readLine())!=null){
                readstr += str+"";
            }
            br.close();
            e1.setText(readstr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void datepick(){
        // date picker
        // init
        String a =String.valueOf(dp.getYear()).substring(2);
        String b = String.valueOf(dp.getMonth()+1);
        String c;
        String e;String d = String.valueOf(dp.getDayOfMonth());
        if(b.length()==1){
            c = "0"+b;}
        else{c = b;}
        if(d.length()==1){e = "0"+d;}
        else{e=d;}
        data = a+"-"+c+"-"+e+".memo";
        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                String a = String.valueOf(i).substring(2);
                String b = String.valueOf(i1+1);
                String c;
                String e;String d = String.valueOf(i2);
                if(b.length()==1){
                    c = "0"+b;}
                else{c = b;}
                if(d.length()==1){e = "0"+d;}
                else{e=d;}
                data = a+"-"+(c)+"-"+e+".memo";
            }
        });
    }


}
