package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.MediaRouteActionProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class Work2Activity extends AppCompatActivity {
    private ListView listview1 = null;
    private int[] icons = new int[]{
            R.mipmap.icon1,
            R.mipmap.icon2,
            R.mipmap.icon3
    };
    private String[] date = {
            "2020年2月20号",
            "2020年3月10号",
            "2020年3月15号"
    };
    private String[] book_names = {"SQL 经典实例","深度学习的数学","React设计模式与最佳实践"};
    private int[] imagesIds = new int[]{
            R.drawable.sql,
            R.drawable.math,
            R.drawable.react
    };
    private String[] book_introductions = {
            "本书详细介绍了各种数据库的SQL查询技术和一些基础的SQL查询语句，并且通过实例操作的方式讲解了如何插入、更新和删除数据等相关知识。",
            "《深度学习的数学》基于丰富的图示和具体示例，通俗易懂地介绍了深度学习相关的数学知识。",
            "本书共分为12章，通过介绍React中有价值的设计模式，展示如何将设计模式和最佳实践应用于现实的新项目和已有项目中。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<Map<String,Object>> listItems =
                new ArrayList<Map<String,Object>>();
        for(int i=0;i<book_names.length;i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("icon",icons[i]);
            listItem.put("date",date[i]);
            listItem.put("name",book_names[i]);
            listItem.put("image",imagesIds[i]);
            listItem.put("introduction",book_introductions[i]);

            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                listItems,
                R.layout.book_items,
                new String[]{"icon","date","name","image","introduction"},
                new int[]{R.id.icon,R.id.date,R.id.book_name,R.id.book_image,R.id.book_introduction}
        )
        {
            //在这个重写的函数里设置 每个 item 中按钮的响应事件
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final int p=position;
                final View view=super.getView(position, convertView, parent);

                ImageButton btn_like = (ImageButton) view.findViewById(R.id.like);
                btn_like.setOnTouchListener(new View.OnTouchListener() {
                    boolean flag1=false;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            if(!flag1) {
                                //重新设置按下时的背景图片
                                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(Work2Activity.this, R.drawable.liked));
                                flag1=true;
                            }else {
                                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(Work2Activity.this, R.drawable.like));
                                flag1=false;
                            }
                        }
//                        else if(event.getAction() == MotionEvent.ACTION_UP){
//                            //再修改为抬起时的正常图片
//                            ((ImageButton)v).setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.like));
//                        }
                        return false;
                    }
                });

                ImageButton btn_collect = (ImageButton) view.findViewById(R.id.collect);
                btn_collect.setOnTouchListener(new View.OnTouchListener() {
                    boolean flag1=false;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            if(!flag1) {
                                //重新设置按下时的背景图片
                                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(Work2Activity.this, R.drawable.collected));
                                flag1=true;
                            }else {
                                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(Work2Activity.this, R.drawable.collect));
                                flag1=false;
                            }
                        }

                        return false;
                    }
                });

                ImageButton btn_icon = (ImageButton) view.findViewById(R.id.icon);
                btn_icon.setOnTouchListener(new View.OnTouchListener() {
                    boolean flag1=false;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            Toast.makeText(Work2Activity.this,"头像:"+book_names[position],Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });

                return view;
            }
        };



        listview1 = (ListView)findViewById(R.id.list1);
        listview1.setAdapter(simpleAdapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Work2Activity.this,"单击了:"+book_names[position],Toast.LENGTH_SHORT).show();
                Log.d("TAG",book_names[position]+"被单击了");
            }
        });



    }

}
