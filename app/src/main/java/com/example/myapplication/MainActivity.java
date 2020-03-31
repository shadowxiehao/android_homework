package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import Util.CommonUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //申请读写权限
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 10001);


        //找到按钮
        button_1 = findViewById(R.id.bt1);
        button_1.setOnClickListener(this);
        button_2 = findViewById(R.id.bt2);
        button_2.setOnClickListener(this);
        button_3 = findViewById(R.id.bt3);
        button_3.setOnClickListener(this);
        button_4 = findViewById(R.id.bt4);
        button_4.setOnClickListener(this);

        //当第四个登录信息传来时显示
        try {
            //接收数据时需新建intent对象，获得intent
            Intent intent = getIntent();
            //通过intent.getExtras()返回bundle对象
            Bundle bundle = intent.getExtras();
            //通过bundle对象和key值获得传递进来的数据 
            String state = bundle.getString("state");
            Log.d("state",state);
            if (state.equals("login")) {
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            } else if (state.equals("logout")) {
                Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                Intent intent1=new Intent(MainActivity.this,Work1Activity.class);
                startActivity(intent1);
                break;
            case R.id.bt2:
                Intent intent2=new Intent(MainActivity.this,Work2Activity.class);
                startActivity(intent2);
                break;
            case R.id.bt3:
                Intent intent3=new Intent(MainActivity.this,Work3Activity.class);
                startActivity(intent3);
                break;
            case R.id.bt4:
                Intent intent4=new Intent(MainActivity.this,Work4Activity.class);
                startActivity(intent4);
                finish();
                break;
        }
    }
    //返回时
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击返回键
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            //声明弹出对象并初始化
            AlertDialog.Builder dialog_out=new AlertDialog.Builder(this);
            dialog_out.setTitle("提示：");
            dialog_out.setMessage("是否退出?");
            //设置确定按钮
            dialog_out.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            dialog_out.setPositiveButton("取消",null);
            dialog_out.setCancelable(false);
            //显示弹窗
            dialog_out.show();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


}
