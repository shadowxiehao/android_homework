package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;

import Interface.DownloadListener;
import Util.DownloadTask;

public class Work8Activity extends AppCompatActivity {
    private DownloadTask task;
    private BaseAdapter baseAdapter;

    ArrayList<String[]> itemList = new ArrayList<String[]>();
    ArrayList<String[]> itemList1 = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work8);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Work8Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //权限动态申请
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        }, 10001);
        MultiDex.install(this);
        bindItem();
        initView();
    }

    private void initView() {
        EditText et_name = (EditText) findViewById(R.id.et_name);
        Button btn_download = (Button) findViewById(R.id.btn_download);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("操作按钮", "下载");
                String downloadUrl = et_name.getText().toString();
                try {
                    String fieName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    itemList.add(new String[]{fieName, downloadUrl});
//                    itemList=new ArrayList<String[]>();
//                    itemList.addAll(itemList1);
                    baseAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(Work8Activity.this,"下载路径错误,请输入例如:https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.3.6.4590_537064458.apk",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //绑定listview
    private void bindItem() {

        ListView lv = findViewById(R.id.item_lv);

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                Log.d("itemList.size",itemList.size()+"");
                return itemList.size();
            }
            //ListView的每一个条目都是一个view对象
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d("position", position + "");

                View view;
                //对ListView的优化，convertView为空时，创建一个新视图；convertView不为空时，代表它是滚出
                //屏幕，放入Recycler中的视图,若需要用到其他layout，则用inflate(),同一视图，用fiindViewBy()
                if (convertView == null) {
                    view = View.inflate(getBaseContext(), R.layout.work8_item, null);
                } else {
                    view = convertView;
                }

                Button start_btn = (Button) view.findViewById(R.id.start_btn);
                Button pause_btn = (Button) view.findViewById(R.id.pause_btn);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                NumberProgressBar pb = (NumberProgressBar) view.findViewById(R.id.pb);
                //获取名称与地址
                tv_name.setText(itemList.get(position)[0].substring(1));
                String downloadUrl = itemList.get(position)[1];

                start_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task = new DownloadTask(new DownloadListener() {
                            @Override
                            public void onProgress(int progress) {
                                pb.setProgress(progress);
                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(Work8Activity.this, "下载成功,存储至手机download文件夹下", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(Work8Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPause() {
                                Toast.makeText(Work8Activity.this, "下载暂停", Toast.LENGTH_SHORT).show();
                            }

                        });
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadUrl);
                        Toast.makeText(Work8Activity.this, "开始下载", Toast.LENGTH_SHORT).show();
                    }
                });
                pause_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task.pauseDoenload();
                    }
                });
                return view;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }
        };
        baseAdapter.notifyDataSetChanged();
        lv.setAdapter(baseAdapter);
        Log.d("bind","bind完毕");
    }

}

