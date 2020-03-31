package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;

public class Work3Activity extends AppCompatActivity {

    ImageView iv;
    Bitmap srcBitmap;
    Bitmap copyBimap;
    Paint paint;
    Canvas canvas;
    int color_flag=0;
    int bold_flag=8;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work3);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 10001);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // [1]找到iv 用来展示我们画的内容
        iv = (ImageView) findViewById(R.id.iv_db);

        // [2]先获取bg.png的原图
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drawboard);

        copyBimap = Bitmap.createBitmap(iv.getWidth(),
                iv.getHeight(), srcBitmap.getConfig());
//        copyBimap = Bitmap.createBitmap(srcBitmap.getWidth(),
//
//                                srcBitmap.getHeight(), srcBitmap.getConfig());

        // [3.1]创建画笔类
        paint = new Paint();
        paint.setStrokeWidth(bold_flag);
        // [3.2]创建一个画布类 相当于把白纸铺到了画布上
        canvas = new Canvas(copyBimap);

        // [3.3]开始作画 当32行代码执行完毕后 白纸上就有内容了
//        canvas.drawBitmap(copyBimap, new Matrix(), paint);

        // [4]把质的内容展示到iv上
        iv.setImageBitmap(copyBimap);

        // [5]给iv设置触摸事件

        iv.setOnTouchListener(new View.OnTouchListener() {
            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // [6]具体判断一下触摸事件的类型
                int action = event.getAction();

                switch (action) {

                    case MotionEvent.ACTION_DOWN: // 按下

                        // [7]获取手指按下的坐标
                        startX = event.getX();
                        startY = event.getY();

                        Log.d("按下坐标获取", "按下:" + startX + "~~~~" + startY);

                        break;

                    case MotionEvent.ACTION_MOVE: // 移动

                        // [8]获取移动后的坐标

                        float stopX = event.getX();
                        float stopY = event.getY();

                        // [9]不停的画线

                        canvas.drawLine(startX, startY, stopX, stopY, paint);

                        System.out.println("移动:" + stopX + "~~~~" + stopY);

                        // [10]更新iv

                        iv.setImageBitmap(copyBimap);

                        // [11]更新一下开始的坐标 startX startY

                        startX = stopX;
                        startY = stopY;
                        break;

                    case MotionEvent.ACTION_UP: // 抬起
                        break;
                }
                // 如果返回值是true 当前监听器对象会消费掉
                return true;

            }
        });
    }

    // 点击按钮让画笔变红色
    public void btn_color(View v) {
        ++color_flag;
        if(color_flag%4==0) {
            paint.setColor(Color.BLACK);
        }
        if(color_flag%4==1) {
            paint.setColor(Color.BLUE);
        }
        if(color_flag%4==2) {
            paint.setColor(Color.YELLOW);
        }
        if(color_flag%4==3) {
            paint.setColor(Color.RED);
        }
    }

    // 点击按钮让画笔加粗,超过40还原
    public void btn_bold(View v) {
        if(bold_flag>40){
        bold_flag=8;
        }else{
            bold_flag += 3;
            paint.setStrokeWidth(bold_flag);
        }
    }

    //点击按钮保存

    public void btn_save(View v) {
        try {
            FileOutputStream fos;
            Log.d("Path",Environment.getExternalStorageDirectory().getPath()) ;
//            file = new File(Environment.getDataDirectory().getPath(), "painting.png");
            File folder=new File(Environment.getExternalStorageDirectory().getPath()+"/mytestapp");
            //先判断目录是否存在,不存在则创建
            if(!folder.exists())
            {
                Log.d("File Detect","nofile");
                Log.d("MakeDirReault",""+folder.mkdirs());
            }
            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/mytestapp","painting.png");

            //创建保存文件流
            Log.d("FilePath",file.getPath()+" "+file.getAbsolutePath());
            fos = new FileOutputStream(file.getPath());

            //参1:保存图片的格式   参数2:quality 质量

            copyBimap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(getApplicationContext(), "保存成功,存储位置:"+Environment.getExternalStorageDirectory().getPath()+"/mytestapp/painting.png", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || PackageManager.PERMISSION_GRANTED != grantResults[0]) {
            Toast.makeText(this,"你拒绝了权限，无法创建!",Toast.LENGTH_LONG).show();
        } else {
        //在这执行你创建文件的代码
        }
    }
}
