package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float xmax,ymax;

    public float stickXpos, stickYpos;
    public float stickXmax_left, stickXmax_right, stickYmax_top, stickYmax_bottom;

    private Bitmap mBitmap;
    private Bitmap mWood;
    private Bitmap mWoodStick;

    private SensorManager sensorManager = null;
    // public float frameTime = 0.666f;
    public float frameTime = 0.12f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set FullScreen & portrait
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        // setContentView(R.layout.main);

        //Calculate Boundry
        Display display = getWindowManager().getDefaultDisplay();
        float displayWidth = (float)display.getWidth();
        float displayHeight = (float)display.getHeight();

        xmax = displayWidth - 200;
        ymax = displayHeight - 380;

        stickXpos = displayWidth / 2;
        stickYpos = displayHeight / 2;

        stickXmax_left = stickXpos - 150;
        stickXmax_right = stickXpos + 150;
        stickYmax_top = stickYpos - 350;
        stickYmax_bottom = stickYpos + 350;

        // setContentView(R.layout.activity_main2);

    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                //Set sensor values as acceleration
                yAcceleration = sensorEvent.values[1];
                xAcceleration = sensorEvent.values[2];
                updateBall();
            }
        }
    }

    private void updateBall() {


        //Calculate new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xmax) {
            xPosition = xmax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > ymax) {
            yPosition = ymax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }

        if ( (xPosition >= stickXmax_left && xPosition <= stickXmax_right) && (yPosition >= stickYmax_top && yPosition <= stickYmax_bottom) ) {
//            xPosition = 0;
//            yPosition = 0;
            Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
        }
        else {

        }

//        if (xPosition > stickXmax_left && (yPosition > stickYmax_top && yPosition < stickYmax_bottom)) {
//            xPosition = stickXmax_left;
//        }
//        else if(xPosition < stickXmax_right && (yPosition > stickYmax_top && yPosition < stickYmax_bottom)) {
//            xPosition = stickXmax_right;
//        }
//
//        else if (yPosition > stickYmax_top && (xPosition > stickXmax_left && xPosition < stickXmax_right)) {
//            yPosition = stickYmax_top;
//        }
//        else if(yPosition < stickYmax_bottom && (xPosition > stickXmax_left && xPosition < stickXmax_right)) {
//            yPosition = stickYmax_bottom;
//        }

//        if (xPosition > stickXmax_left) {
//            xPosition = stickXmax_left - 30;
//        }
//        else if(xPosition < stickXmax_right) {
//            xPosition = stickXmax_right + 30;
//        }



    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        public CustomDrawableView(Context context)
        {
            super(context);
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 200;
            final int dstHeight = 270;
            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood_bg_2);

            Bitmap stick = BitmapFactory.decodeResource(getResources(), R.drawable.wood_stick);
            mWoodStick = Bitmap.createScaledBitmap(stick, 300, 700, true);

        }

        protected void onDraw(Canvas canvas)
        {
            final Bitmap bitmap = mBitmap;
            canvas.drawBitmap(mWood, 0, 0, null);
            canvas.drawBitmap(bitmap, xPosition, yPosition, null);



            canvas.drawBitmap(mWoodStick, stickXpos, stickYpos, null);

            invalidate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}