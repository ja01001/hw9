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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        adapter.notifyDataSetChanged();
        // long item click event (not)
        listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dig = new AlertDialog.Builder(MainActivity.this);
                dig.setTitle("삭제?").setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String path = getExternalPath();
                        removes(path+"diary/"+memos.get(position));
                        memos.remove(position);
                        adapter.notifyDataSetChanged();
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
        String a = Environment.MEDIA_MOUNTED;
        if(ext.equals(a)){
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
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
                e1.setText("");
                save.setText("저장");
                break;
            case R.id.btnsave:
                if(save.getText()=="수정"){
                    Toast.makeText(this,"asdasd",Toast.LENGTH_SHORT).show();
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                else{
                    datepick();
                    final title tt = new title(data,e1.getText().toString());
                    if(check(memos,tt.getTt())){
                        Toast.makeText(this,"이미 파일이 존재합니다.",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder bb = new AlertDialog.Builder(MainActivity.this);
                        bb.setTitle("중복")
                                .setMessage("이미 해당 날짜의 파일이 존재합니다. 아니오를 누르시면 초기화면으로 돌아갑니다.")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        save.setText("수정");
                                        read(getExternalPath()+"diary/"+data);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        l2.setVisibility(View.GONE);
                                        l1.setVisibility(View.VISIBLE);
                                    }
                                })
                                .show();
                    }
                    else {
                        memos.add(tt.getTt());
                        sort();
                        adapter.notifyDataSetChanged();
                        write(getExternalPath() + "diary/" + tt.getTt(), tt.getContext());
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.btncancel:
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
    } //clear
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
    } //clear
    public void datepick(){
        // date picker
        // init
        data = dateset(dp.getYear(),dp.getMonth(),dp.getDayOfMonth())+".memo";
        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                data = dateset(dp.getYear(),dp.getMonth(),dp.getDayOfMonth())+".memo";
            }
        });
    } // clear
    public void sort(){
        Collections.sort(memos,comparator);
    } // clear
    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String o, String t1) {
            return o.compareTo(t1);
        }
    };
    public String dateset(int year,int month, int day){ // date format
        String a = String.valueOf(year).substring(2);
        String b = String.valueOf(month+1);
        String c = String.valueOf(day);
        if(b.length()==1){
            b = "0"+b;
        }
        if(c.length()==1){
            c = "0"+c;
        }
        String dd = String.format(a+"-"+b+"-"+c);
        return dd;
    }
    public boolean check(ArrayList<String> mm,String a){
        boolean xxx = false;
        for(int x = 0; x<mm.size(); x++) {
            if (a == mm.get(x)) {
                xxx = false;
            }
            else{
                xxx = true;
            }
        }
        return  xxx;
    }
    public void removes(String path){
        File f = new File(path);
        f.delete();
    }

}
