package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_im1;
    Button button_im2;
    Button button_im3;

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

        //找到按钮
        button_im1 = findViewById(R.id.im1);
        button_im1.setOnClickListener(this);
        button_im2 = findViewById(R.id.im2);
        button_im2.setOnClickListener(this);
        button_im3 = findViewById(R.id.im3);
        button_im3.setOnClickListener(this);
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
            case R.id.im1:
                Intent intent1=new Intent(MainActivity.this,Work1Activity.class);
                startActivity(intent1);
                break;
            case R.id.im2:
                Intent intent2=new Intent(MainActivity.this,Work2Activity.class);
                startActivity(intent2);
                break;
            case R.id.im3:
                Intent intent3=new Intent(MainActivity.this,Work3Activity.class);
                startActivity(intent3);
                break;
        }

    }
}
