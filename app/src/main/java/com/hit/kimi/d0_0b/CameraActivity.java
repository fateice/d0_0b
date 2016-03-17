package com.hit.kimi.d0_0b;

/**
 * Created by Kimi on 2015/12/6.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity {
    private CameraView view;
    public int share;
    Button back;
    public Bitmap bm;
    public String path;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置横屏模式以及全屏模式
        */

        view = new CameraView(this);// 通过一个surfaceview的view来实现拍照
        view.setId(1);
        view = new CameraView(this, this);
        setContentView(R.layout.camera_layout);

        RelativeLayout relative = (RelativeLayout) this.findViewById(R.id.ly);
        RelativeLayout.LayoutParams Layout = new RelativeLayout.LayoutParams(3, 3);// 设置surfaceview使其满足需求无法观看预览
        Layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        Layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);

        Bundle bundle = this.getIntent().getExtras();
        share = bundle.getInt("share");

        back = (Button) findViewById(R.id.button4);

        //为按钮添加监听器
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intents = new Intent();
                intents.setClass(CameraActivity.this, MainActivity.class);
                startActivity(intents);
                CameraActivity.this.finish();
            }
        });

        relative.addView(view, Layout);


    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intents = new Intent();
            intents.setClass(CameraActivity.this, MainActivity.class);
            startActivity(intents);
            CameraActivity.this.finish();
        }
        return false;
    }

    public void shareToTimeLine(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);

        //intent.setAction(Intent.ACTION_SEND);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.putExtra(Intent.EXTRA_TEXT,"来自Fateice的线控耳机一键拍照分享应用d0_0b");
// intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
// ArrayList<Uri> uris = new ArrayList<Uri>();
// for (int i = 0; i < images.size(); i++) {
// Uri data = Uri.fromFile(new File(thumbPaths.get(i)));
// uris.add(data);
// }
// intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(intent);
    }


    public class CameraView extends SurfaceView implements SurfaceHolder.Callback,
            Camera.PictureCallback {

        private SurfaceHolder holder;
        private Camera camera;
        private Camera.Parameters parameters;
        private Activity act;
        private Handler handler = new Handler();
        private Context context;
        private SurfaceView surfaceView;
        private AudioManager audio;
        private int current;

        //初始化superclass，创建一个采用context对象的构造函数，同时初始化回调函数
        public CameraView(Context context) {
            super(context);

            surfaceView = this;
            audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //final int current = audio.getRingerMode();
            //audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            this.context = context;
            holder = getHolder();// 生成Surface Holder
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 指定Push Buffer

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (camera == null) {
                        handler.postDelayed(this, 1 * 1000);// 由于启动camera需要时间，在此让其等两秒再进行聚焦知道camera不为空
                    } else {
                        camera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                    camera.takePicture(new Camera.ShutterCallback() {// 如果聚焦成功则进行拍照
                                        @Override
                                        public void onShutter() {
                                        }
                                    }, null, CameraView.this);
                                } else {
                                }
                            }
                        });
                    }
                }
                //}, 2 * 1000);
            }, 1000);
        }

        public CameraView(Context context, Activity act) {// 在此定义一个构造方法用于拍照过后把CameraActivity给finish掉
            this(context);
            this.act = act;
        }

        //实现回调函数
        @Override
        public void surfaceCreated(final SurfaceHolder holder) {
            // TODO Auto-generated method stub

            camera = Camera.open();// 摄像头的初始化
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (holder != null) {
                        try {
                            camera.setPreviewDisplay(holder);
                            Camera.Parameters params = camera.getParameters();
                            params.setJpegQuality(100);
                            params.setPictureSize(2000, 1500);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        handler.postDelayed(this, 1 * 1000);
                    }
                }
                //}, 2 * 1000);
            },2000);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

            parameters = camera.getParameters();
            camera.setParameters(parameters);// 设置参数
            camera.startPreview();// 开始预览

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }


        public void onPictureTaken(byte[] data, Camera camera) {// 拍摄完成后旋转并保存照片

            try {

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = format.format(date);

                //在SD卡上创建文件夹
                File file = new File(Environment.getExternalStorageDirectory()
                        + "/d0_0b/pic");
                if (!file.exists()) {

                    file.mkdirs();
                }

                String path = Environment.getExternalStorageDirectory()
                        + "/d0_0b/pic/" + time + ".jpg";

                /*
                file=new File(path);
                if(file.exists()){
                    file.delete();
                }
                Uri u=Uri.fromFile(file);
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, 0);
*/



                Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix m = new Matrix();
                m.setRotate(90,(float) bm0.getWidth() / 2, (float) bm0.getHeight() / 2);
                bm = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);

                data2file(data, path);
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
                holder.removeCallback(CameraView.this);
                //audio.setRingerMode(current);
                //act.finish();
                file = new File(path);
                if(share == 1){
                    shareToTimeLine(file);
                }
                //Toast.makeText(getApplicationContext(), "按键", Toast.LENGTH_SHORT).show();
                handler.post(new Runnable(){
                    public void run(){

                        Toast.makeText(context, "拍照完成", Toast.LENGTH_LONG).show();
                    }
                });
                if(share != 1){
                Intent intents = new Intent();
                intents.setClass(CameraActivity.this, MainActivity.class);
                startActivity(intents);
                CameraActivity.this.finish();
                }
                //act.finish();
                //uploadFile(path);

            } catch (Exception e) {

            }

        }


        /*
        //@Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode==RESULT_OK){
                File file=new File(path);
                try {
                    Uri uri = Uri.fromFile(file);
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=true;
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                    options.inSampleSize=4;
                    options.inJustDecodeBounds=false;
                    Bitmap map=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                    android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), map, null, null);

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));

                    //file.setImageBitmap(map);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
*/
        private void data2file(byte[] w, String fileName) throws Exception {// 将二进制数据转换为文件的函数
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(fileName);
                bm.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.write(w);
                out.close();
            } catch (Exception e) {
                if (out != null)
                    out.close();
                throw e;
            }
        }

    }


}







