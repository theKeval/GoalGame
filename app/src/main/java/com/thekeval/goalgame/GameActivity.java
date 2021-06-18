package com.thekeval.goalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.thekeval.goalgame.Model.PlayerModel;
import com.thekeval.goalgame.database.DatabaseHandler;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float xmax,ymax;

    public float stickXpos, stickYpos;
    public int stickWidth = 300;
    public int stickHeight = 50;
    public float stickXmax_left, stickXmax_right, stickYmax_top, stickYmax_bottom;

    public float holeXpos, holeYpos;
    public int holeWidth = 130;
    public int holeHeight = 130;
    public float holeXmax_left, holeXmax_right, holeYmax_top, holeYmax_bottom;


    // public float stickOneXpos, stickOneYpos;

    private Bitmap mBitmap;
    private Bitmap mWood;
    private Bitmap mWoodStick;
    private Bitmap mHole;

    private SensorManager sensorManager = null;
    // public float frameTime = 0.666f;
    public float frameTime = 0.10f;

    public int score = 500;
    private Handler handler = new Handler();
    Timer timer = new Timer();

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new DatabaseHandler(this);

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

        xmax = displayWidth - 100;
        ymax = displayHeight - 150;

//        stickWidth = 300;
//        stickHeight = 50;

        stickXpos = (displayWidth / 2) - (stickWidth / 2);
        stickYpos = (displayHeight / 2) - (stickHeight / 2);

//        stickXpos = 300;
//        stickYpos = 500;

        stickXmax_left = stickXpos;
        stickXmax_right = stickXpos + stickWidth;
        stickYmax_top = stickYpos - 50;
        stickYmax_bottom = stickYpos + stickHeight - 50;

//        stickOneXpos = 500;
//        stickOneYpos = 700;

        holeXpos = (displayWidth / 1.3f) - (holeWidth / 2);
        holeYpos = (displayHeight / 1.3f) - (holeHeight / 2);

        holeXmax_left = holeXpos;
        holeXmax_right = holeXpos + holeWidth;
        holeYmax_top = holeYpos - 50;
        holeYmax_bottom = holeYpos + holeHeight - 50;


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        calculateScore();
                    }
                });
            }
        }, 0, 1000);

    }

    public void calculateScore() {
        score--;
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

    float previousX = 0.0f;
    float previousY = 0.0f;

    boolean isStopBall = false;

    private void updateBall() {

        if (isStopBall) {
            return;
        }

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

        if ((xPosition >= stickXmax_left && xPosition <= stickXmax_right) && (yPosition >= stickYmax_top && yPosition <= stickYmax_bottom)) {
//            xPosition = 0;
//            yPosition = 0;
            // Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
            xPosition = previousX - 10;
            yPosition = previousY - 10;

        }

        if ((xPosition >= holeXmax_left && xPosition <= holeXmax_right) && (yPosition >= holeYmax_top && yPosition <= holeYmax_bottom)) {
            xPosition = previousX - 10;
            yPosition = previousY - 10;

            isStopBall = true;

            // dbHandler.updatePlayer(new PlayerModel())

            Intent scoreIntent = new Intent(this, ScoreActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("score", score);
            scoreIntent.putExtras(extras);
            startActivity(scoreIntent);
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

        previousX = xPosition;
        previousY = yPosition;

    }

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
            final int dstWidth = 100;
            final int dstHeight = 135;
            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood_bg_2);

            Bitmap stick = BitmapFactory.decodeResource(getResources(), R.drawable.wood_stick_verticle);
            mWoodStick = Bitmap.createScaledBitmap(stick, stickWidth, stickHeight, true);

            Bitmap hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
            mHole = Bitmap.createScaledBitmap(hole, holeWidth, holeHeight, true);

        }

        protected void onDraw(Canvas canvas)
        {
            final Bitmap bitmap = mBitmap;
            canvas.drawBitmap(mWood, 0, 0, null);
            canvas.drawBitmap(bitmap, xPosition, yPosition, null);

            canvas.drawBitmap(mWoodStick, stickXpos, stickYpos, null);
            canvas.drawBitmap(mHole, holeXpos, holeYpos, null);

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