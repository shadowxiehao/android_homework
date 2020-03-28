package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;

public class Work3Activity extends AppCompatActivity {

    ImageView iv;
    Bitmap srcBitmap;
    Bitmap copyBimap;
    Paint paint;
    Canvas canvas;

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

        // [3.2]创建一个画布类 相当于把白纸铺到了画布上

        canvas = new Canvas(copyBimap);

        // [3.3]开始作画 当32行代码执行完毕后 白纸上就有内容了

        canvas.drawBitmap(srcBitmap, new Matrix(), paint);

        // [3.4]画一条线 线由2个点确定一条线

        // canvas.drawLine(20, 30, 40, 70, paint);

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

        paint.setColor(Color.RED);

    }


    // 点击按钮让画笔加粗

    public void btn_bold(View v) {

        paint.setStrokeWidth(19);

    }


    //点击按钮保存 大作

    public void btn_save(View v) {

        try {

            File file = new File(Environment.getExternalStorageDirectory().getPath(), "dazuo.png");

            FileOutputStream fos = new FileOutputStream(file);

            //参1:保存图片的格式   参数2:quality 质量

            copyBimap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

            Toast.makeText(getApplicationContext(), "sucess", 1).show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
