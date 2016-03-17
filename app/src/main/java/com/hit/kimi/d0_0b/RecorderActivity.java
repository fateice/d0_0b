package com.hit.kimi.d0_0b;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kimi on 2015/12/21.
 */
public class RecorderActivity extends Activity implements OnClickListener {
    public static final int UPDATE_TIME=0;//更新录音时间的消息编号

    ImageButton ibRecord;
    ImageButton ibPause;
    ImageButton ibStop;

    TextView tvTime;//时间长度显示
    Handler hd;//消息处理器

    File myFile ;//用于存放音轨的文件
    MediaRecorder myMediaRecorder;//媒体录音机
    int countSecond=0;//录制的秒数
    boolean recordFlag=false;//录制中标志
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorder);
        ibRecord=(ImageButton)this.findViewById(R.id.imageButton);
        ibStop=(ImageButton)this.findViewById(R.id.imageButton2);
        tvTime=(TextView)findViewById(R.id.textView5);
        ibRecord.setOnClickListener(this);
        ibStop.setOnClickListener(this);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        //通过附加线程修改计时器TextView中的内容，因此要在主线程中创建一个Handler
        hd=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                //调用父类处理
                super.handleMessage(msg);
                //根据消息what编号的不同，执行不同的业务逻辑
                switch(msg.what)
                {
                    //将消息中的内容提取出来显示在Toast中
                    case UPDATE_TIME:
                        //获取消息中的数据
                        Bundle b=msg.getData();
                        //获取内容字符串
                        String msgStr=b.getString("msg");
                        //设置字符串到显示录音时长的文本框中
                        tvTime.setText(msgStr);
                        break;
                }
            }
        };



        if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {//若没有插闪存卡则报错
            Toast.makeText(this, "请检测内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try
        {
            if(recordFlag==true)
            {//若已经在录音中则提示并返回
                Toast.makeText(this, "录音中，请结束本次录音再开始新录音！", Toast.LENGTH_SHORT).show();
                return;
            }

            //String tempFilePath = Environment.getExternalStorageDirectory() + "/d0_0b/record";
            //File tempDir = new File(tempFilePath);
            //初始化临时文件对象
            myFile = File.createTempFile
                    (
                            "myAudio",  //基本文件名
                            ".amr",     //后缀
                            Environment.getExternalStorageDirectory()//目录路径
                            //tempDir
                    );
            /*
            //初始化临时文件对象
            myFile = File.createTempFile
                    (
                            "myAudio",  //基本文件名
                            ".amr",     //后缀
                            Environment.getExternalStorageDirectory() //目录路径
                    );
            */
            //创建录音机对象
            myMediaRecorder = new MediaRecorder();
            //设置输入设备为麦克风
            myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置输出格式为默认的amr格式
            myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //设置音频编码器为默认的编码器
            myMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //设置输出文件的路径
            myMediaRecorder.setOutputFile(myFile.getAbsolutePath());
            //准备录音
            myMediaRecorder.prepare();
            //开始录音
            myMediaRecorder.start();
            //设置录音中标记为true
            recordFlag=true;
            //启动一个线程进行计时
            new Thread()
            {
                public void run()
                {
                    while(recordFlag)
                    {
                        //计时器加一
                        countSecond++;
                        //调用方法设置新时长
                        setTime();
                        //休息1000ms
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == ibRecord){//按下录音按钮
            if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            {//若没有插闪存卡则报错
                Toast.makeText(this, "请检测内存卡", Toast.LENGTH_SHORT).show();
                return;
            }
            try
            {
                if(recordFlag==true)
                {//若已经在录音中则提示并返回
                    Toast.makeText(this, "录音中，请结束本次录音再开始新录音！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String tempFilePath = Environment.getExternalStorageDirectory() + "/sdcard/d0_0b/record";
                //String tempFilePath = Environment.getExternalStorageState() + "/d0_0b/record";
                //String tempFilePath = "/storage/sdcard0/d0_0b/record";
                //File tempDir = new File(tempFilePath);
                //初始化临时文件对象
                myFile = File.createTempFile
                        (
                                "myAudio",  //基本文件名
                                ".amr",     //后缀
                                Environment.getExternalStorageDirectory()//目录路径
                                //tempDir
                        );
                //创建录音机对象
                myMediaRecorder = new MediaRecorder();
                //设置输入设备为麦克风
                myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //设置输出格式为默认的amr格式
                myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                //设置音频编码器为默认的编码器
                myMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                //设置输出文件的路径
                myMediaRecorder.setOutputFile(myFile.getAbsolutePath());
                //准备录音
                myMediaRecorder.prepare();
                //开始录音
                myMediaRecorder.start();
                //设置录音中标记为true
                recordFlag=true;
                //启动一个线程进行计时
                new Thread()
                {
                    public void run()
                    {
                        while(recordFlag)
                        {
                            //计时器加一
                            countSecond++;
                            //调用方法设置新时长
                            setTime();
                            //休息1000ms
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(v == ibStop){//按下停止按钮
            if(myFile != null&&myMediaRecorder!=null)
            {
                Toast.makeText(this, "录音结束", Toast.LENGTH_SHORT).show();
                //停止录音
                myMediaRecorder.stop();
                //释放录音机对象
                myMediaRecorder.release();
                //将录音机对象引用设置为null
                myMediaRecorder = null;
            }
            //设置录音中标记为false
            recordFlag=false;
            //计时器清0
            countSecond=0;
            //调用方法设置新时长
            setTime();
        }
        else if(v == button){
            Intent intents = new Intent();
            intents.setClass(RecorderActivity.this, MainActivity.class);
            startActivity(intents);
            RecorderActivity.this.finish();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intents = new Intent();
            intents.setClass(RecorderActivity.this, MainActivity.class);
            startActivity(intents);
            RecorderActivity.this.finish();
        }
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
            //if (event.getRepeatCount() == 0) {  //如果长按的话，getRepeatCount值会一直变大
            if(recordFlag == true)
            {
                if(myFile != null&&myMediaRecorder!=null)
                {
                    Toast.makeText(this, "录音结束", Toast.LENGTH_SHORT).show();
                    //停止录音
                    myMediaRecorder.stop();
                    //释放录音机对象
                    myMediaRecorder.release();
                    //将录音机对象引用设置为null
                    myMediaRecorder = null;
                }
                //设置录音中标记为false
                recordFlag=false;
                //计时器清0
                countSecond=0;
                //调用方法设置新时长
                setTime();
            }
            else if (recordFlag==false){
                if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                {//若没有插闪存卡则报错
                    Toast.makeText(this, "请检测内存卡", Toast.LENGTH_SHORT).show();
                    return false;
                }
                try
                {
                    if(recordFlag==true)
                    {//若已经在录音中则提示并返回
                        Toast.makeText(this, "录音中，请结束本次录音再开始新录音！", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    //String tempFilePath = Environment.getExternalStorageDirectory() + "/d0_0b/record";
                    //File tempDir = new File(tempFilePath);
                    //初始化临时文件对象
                    myFile = File.createTempFile
                            (
                                    "myAudio",  //基本文件名
                                    ".amr",     //后缀
                                    Environment.getExternalStorageDirectory()//目录路径
                                    //tempDir
                            );
            /*
            //初始化临时文件对象
            myFile = File.createTempFile
                    (
                            "myAudio",  //基本文件名
                            ".amr",     //后缀
                            Environment.getExternalStorageDirectory() //目录路径
                    );
            */
                    //创建录音机对象
                    myMediaRecorder = new MediaRecorder();
                    //设置输入设备为麦克风
                    myMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    //设置输出格式为默认的amr格式
                    myMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    //设置音频编码器为默认的编码器
                    myMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    //设置输出文件的路径
                    myMediaRecorder.setOutputFile(myFile.getAbsolutePath());
                    //准备录音
                    myMediaRecorder.prepare();
                    //开始录音
                    myMediaRecorder.start();
                    //设置录音中标记为true
                    recordFlag=true;
                    //启动一个线程进行计时
                    new Thread()
                    {
                        public void run()
                        {
                            while(recordFlag)
                            {
                                //计时器加一
                                countSecond++;
                                //调用方法设置新时长
                                setTime();
                                //休息1000ms
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    //设置显示时间的方法
    public void setTime()
    {
        //计算分钟和秒
        int second=countSecond%60;
        int minute=countSecond/60;
        //创建内容字符串
        String msgStr=minute+"m:"+second+"s";
        //创建消息数据Bundle
        Bundle b=new Bundle();
        //将内容字符串放进数据Bundle中
        b.putString("msg", msgStr);
        //创建消息对象
        Message msg=new Message();
        //设置数据Bundle到消息中
        msg.setData(b);
        //设置消息的what值
        msg.what=UPDATE_TIME;
        //发送消息
        hd.sendMessage(msg);
    }

}
