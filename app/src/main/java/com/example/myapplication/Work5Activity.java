package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import java.util.ArrayList;

import Data.Staff_info;
import Util.MySqliteHelper5;

public class Work5Activity extends AppCompatActivity {
    private MySqliteHelper5 msh;
    private SQLiteDatabase sd;
    private ArrayList<Staff_info> StaffList;
    private ListView lv;
    private BaseAdapter baseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work5);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Work5Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //权限动态申请
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 10001);

        //初始化数据库类
        msh = new MySqliteHelper5(this, "student.db", null, 2);

        //获取数据库数据存入arraylist
        StaffList = getALData();

        lv = findViewById(R.id.staff_lv);
        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return StaffList.size() + 1;
            }

            //ListView的每一个条目都是一个view对象
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                //对ListView的优化，convertView为空时，创建一个新视图；convertView不为空时，代表它是滚出
                //屏幕，放入Recycler中的视图,若需要用到其他layout，则用inflate(),同一视图，用fiindViewBy()
                if (convertView == null) {
                    view = View.inflate(getBaseContext(), R.layout.work5_staff, null);
                } else {
                    view = convertView;
                }
                Log.d("position", position + "");
                if (StaffList.size() > 0 && position < StaffList.size()) {
                    //从studentlist中取出一行数据，position相当于数组下标,可以实现逐行取数据
                    Staff_info st = StaffList.get(position);
                    final TextView t_id = (TextView) view.findViewById(R.id.st_id);
                    EditText t_name = (EditText) view.findViewById(R.id.st_name);
                    EditText t_salary = (EditText) view.findViewById(R.id.st_salary);
                    EditText t_depart = (EditText) view.findViewById(R.id.st_depart);
                    Button btn_op = (Button) view.findViewById(R.id.btn_operate);

                    t_id.setText(st.getId() + "");
                    t_id.setBackgroundColor(0x00FFFFFF);

                    t_name.setText(st.getName());
                    t_name.setBackgroundColor(0x00FFFFFF);
                    t_name.setEnabled(false);

                    t_salary.setText(st.getSalary());
                    t_salary.setBackgroundColor(0x00FFFFFF);
                    t_salary.setEnabled(false);

                    t_depart.setText(st.getDepart());
                    t_depart.setBackgroundColor(0x00FFFFFF);
                    t_depart.setEnabled(false);

                    btn_op.setText("删除");
                    btn_op.setBackgroundColor(0xCEE61246);
                    btn_op.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("操作按钮", "删除按钮响应");
                            if (msh.delete(t_id.getText().toString()) == -1) {
                                Toast.makeText(Work5Activity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Work5Activity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                            StaffList = getALData();
                            notifyDataSetChanged();
                        }
                    });
                    return view;
                } else {
                    TextView t_id = (TextView) view.findViewById(R.id.st_id);
                    final EditText t_name = (EditText) view.findViewById(R.id.st_name);
                    final EditText t_salary = (EditText) view.findViewById(R.id.st_salary);
                    final EditText t_depart = (EditText) view.findViewById(R.id.st_depart);
                    Button btn_op = (Button) view.findViewById(R.id.btn_operate);
                    t_name.setEnabled(true);
                    t_name.setText("新名字");
                    t_salary.setEnabled(true);
                    t_salary.setText("新工资");
                    t_depart.setEnabled(true);
                    t_depart.setText("新部门");

                    t_id.setText("new");
                    //按钮功能改成插入
                    btn_op.setText("插入");
                    btn_op.setBackgroundColor(Color.rgb(255, 127, 80));
                    btn_op.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("操作按钮", "插入按钮响应");
                            //插入新数据
                            if (msh.add(t_name.getText().toString(), t_salary.getText().toString(), t_depart.getText().toString()) == -1) {
                                Toast.makeText(Work5Activity.this, "插入失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Work5Activity.this, "插入成功", Toast.LENGTH_SHORT).show();
                            }
                            StaffList = getALData();
                            notifyDataSetChanged();
                        }
                    });
                    return view;
                }
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

        final EditText et_search = (EditText)findViewById(R.id.et_search);
        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) { //使用匿名的Button实例
                String arg=et_search.getText().toString().trim();
                StaffList = getALData(arg);
                Toast.makeText(Work5Activity.this,"搜索成功",Toast.LENGTH_SHORT).show();
                baseAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @return 包含了员工数据库数据的arraylist
     * @author xie
     */
    private ArrayList<Staff_info> getALData() {
        StaffList = new ArrayList<>();

        //扫描数据库,将数据库信息放入studentlist
        Cursor cursor = msh.searchall();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String salary = cursor.getString(cursor.getColumnIndex("salary"));
            String depart = cursor.getString(cursor.getColumnIndex("depart"));
            Staff_info st = new Staff_info(id, name, salary, depart);    //student_info存一个条目的数据
            StaffList.add(st);//把数据库的每一行加入数组中
        }
        return StaffList;
    }
    /**
     * @return 包含了员工数据库数据的arraylist
     * @author xie
     */
    private ArrayList<Staff_info> getALData(String arg) {
        StaffList = new ArrayList<>();

        //扫描数据库,将数据库信息放入studentlist
        Cursor cursor = msh.search(arg);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String salary = cursor.getString(cursor.getColumnIndex("salary"));
            String depart = cursor.getString(cursor.getColumnIndex("depart"));
            Staff_info st = new Staff_info(id, name, salary, depart);    //student_info存一个条目的数据
            StaffList.add(st);//把数据库的每一行加入数组中
        }
        return StaffList;
    }
}

