package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Util.CommonUtil;

public class Work4Activity extends AppCompatActivity implements View.OnClickListener{
    String login_state=null;
    Button btn_login;
    Button btn_logout;
    Button btn_signup;

    EditText email;
    EditText pwd;
    CheckBox remember;
    CheckBox auto_login;
    
    Map<String, String> map = new HashMap<String, String>(); //本地保存数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work4);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Work4Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //权限动态申请
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 10001);



        //找到控件
        email=findViewById(R.id.email);
        pwd=findViewById(R.id.password);
        
        remember=findViewById(R.id.remember);
        auto_login=findViewById(R.id.autologin);
        remember.setOnClickListener(this);
        auto_login.setOnClickListener(this);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);

        //自动填写信息
        String auto_email = CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "uemail");
        String auto_pwd = CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "upwd");
        String remember_state = CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "remember");
        String autologin_state = CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "auto_login");
        //将勾选框状态补回去
        if(remember_state!=null&&remember_state.equals("true")){
            remember.setChecked(true);
        }
        if(autologin_state!=null&&autologin_state.equals("true")){
            auto_login.setChecked(true);
        }
        //自动填写账号
        Log.d("auto_email","---"+email+"---");
        if(auto_email!=null) {
            email.setText(auto_email);
        }
        if(remember.isChecked()&&auto_pwd!=null){
            pwd.setText(auto_pwd);
        }

        //读取登录状态
        login_state= CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "state");
        Log.d("4_state","---"+login_state+"---");
        if(login_state==null){
            Toast.makeText(Work4Activity.this,"尚未登录",Toast.LENGTH_SHORT).show();
        } else if(login_state.equals("true")){
            Toast.makeText(Work4Activity.this,"您已登录",Toast.LENGTH_SHORT).show();
        }else{
            if(auto_login.isChecked()){
                Toast.makeText(Work4Activity.this, "自动登录中", Toast.LENGTH_SHORT).show();
                Intent intenti = new Intent(this,MainActivity.class);
                intenti.putExtra("state", "login");
                map.put("state", "true");
                CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                startActivity(intenti);
                finish();
            }else {
                Toast.makeText(Work4Activity.this, "尚未登录", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //设置按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String email_save= CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "uemail");
                String pwd_save= CommonUtil.getSettingNote(Work4Activity.this, "userinfo", "upwd");
                try {
                    Log.d("email_save", email_save);
                    Log.d("pwd_save", pwd_save);
                }catch (Exception e){
                    Log.d("保存结果", "无法获取");
                }
                if(email_save!=null&&pwd_save!=null&&email_save.equals(email.getText().toString())&&pwd_save.equals(pwd.getText().toString())) {
                    Intent intenti = new Intent(this,MainActivity.class);
                    intenti.putExtra("state", "login");
                    map.put("state", "true");
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                    startActivity(intenti);
                    finish();
                }else{
                    Toast.makeText(Work4Activity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_logout:
                map.put("state", "flase");
                CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                Intent intento = new Intent(this,MainActivity.class);
                intento.putExtra("state", "logout");
                startActivity(intento);
                finish();
                break;
            case R.id.btn_signup:
                //保存账号密码
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email.getText().toString());
                boolean flag = matcher.matches();
                if(!flag){
                    //邮箱格式错误
                    Toast.makeText(Work4Activity.this,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                }else {
                    //开始本地注册
                    map.put("uemail", email.getText().toString());
                    Log.d("emailn", email.getText().toString());
                    map.put("upwd", pwd.getText().toString());
                    Log.d("pswn", pwd.getText().toString());
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                    Toast.makeText(Work4Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remember:
                if(remember.isChecked()) {
                    map.put("remember", "true");
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                }else {
                    map.put("remember", "false");
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                }
                break;
            case R.id.autologin:
                if(auto_login.isChecked()) {
                    map.put("auto_login", "true");
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                }else {
                    map.put("auto_login", "false");
                    CommonUtil.saveSettingNote(Work4Activity.this, "userinfo", map);//参数（上下文，userinfo为文件名，需要保存的数据）
                }
                break;
        }
    }
    //返回时
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击返回键
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(Work4Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || PackageManager.PERMISSION_GRANTED != grantResults[0]) {
            Toast.makeText(this,"拒绝权限将导致app使用异常",Toast.LENGTH_LONG).show();
        } else {
        //在这执行你创建文件的代码
        }
    }
}
