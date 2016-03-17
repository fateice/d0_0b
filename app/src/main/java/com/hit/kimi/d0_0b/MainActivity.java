package com.hit.kimi.d0_0b;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioGroup radioGroup;
    CheckBox checkBox;
    int share = 0;
    int function;

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        checkBox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked()){
                            share = 1;
                        }
                        else{
                            share = 0;
                        }
                    }
                }
        );
        checkBox.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == radioButton1.getId()) {
                    function=1;
                    checkBox.setEnabled(true);
                } else if (checkedId == radioButton2.getId()) {
                    function=2;
                    checkBox.setChecked(false);
                    checkBox.setEnabled(false);
                } else if (checkedId == radioButton3.getId()) {
                    function=3;
                    checkBox.setChecked(false);
                    checkBox.setEnabled(false);
                }
            }

        });

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
            //if (event.getRepeatCount() == 0) {  //如果长按的话，getRepeatCount值会一直变大
            if (function==1)
            {
                Toast.makeText(getApplicationContext(), "启动拍照",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CameraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("share", share);
                intent.putExtras(bundle);
                startActivity(intent);
                MainActivity.this.finish();
            }
            else if(function==2){
                Toast.makeText(getApplicationContext(), "启动录音",
                        Toast.LENGTH_SHORT).show();
                Intent intents = new Intent();
                intents.setClass(MainActivity.this, RecorderActivity.class);
                startActivity(intents);
                MainActivity.this.finish();
            }
            else if(function==3){
                Toast.makeText(getApplicationContext(), "启动手电筒",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, FlashlightActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }

                /*
                Intent intent = new Intent(); //调用照相机

                intent.setAction("android.media.action.STILL_IMAGE_CAMERA");

                startActivity(intent);
                */
                /*
                Toast.makeText(getApplicationContext(), "启动拍照",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
*/

            //}
        }
        return false;
    }

}
