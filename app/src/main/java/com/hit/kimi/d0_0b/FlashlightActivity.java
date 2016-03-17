package com.hit.kimi.d0_0b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.hardware.Camera;

import java.security.Policy;

/**
 * Created by Kimi on 2015/12/21.
 */
public class FlashlightActivity extends Activity {
    public Button back;
    private Camera camera;
    private Camera.Parameters parameters;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashlight);
        back = (Button) findViewById(R.id.button);

        //为按钮添加监听器
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeLight();
                camera.release();
                Intent intents = new Intent();
                intents.setClass(FlashlightActivity.this, MainActivity.class);
                startActivity(intents);
                FlashlightActivity.this.finish();
            }
        });
        camera = Camera.open();
        parameters = camera.getParameters();

        openLight();
    }

    public void openLight(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }
    private void closeLight()
    {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        camera.setParameters(parameters);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
            closeLight();
            camera.release();
            Intent intents = new Intent();
            intents.setClass(FlashlightActivity.this, MainActivity.class);
            startActivity(intents);
            FlashlightActivity.this.finish();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            closeLight();
            camera.release();
            Intent intents = new Intent();
            intents.setClass(FlashlightActivity.this, MainActivity.class);
            startActivity(intents);
            FlashlightActivity.this.finish();
        }
        return  false;
    }

}
