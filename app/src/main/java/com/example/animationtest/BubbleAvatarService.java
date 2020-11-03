package com.example.animationtest;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.codyzen.spriterunner.SpriteView;


public class BubbleAvatarService extends Service {
    private WindowManager windowManager;
    private View bubbleavatarView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        //Inflate the bubble
        bubbleavatarView = LayoutInflater.from(this).inflate(R.layout.bubble_avatar, null);

        final SpriteView avatar1 = (SpriteView) bubbleavatarView.findViewById(R.id.bubbleavatar_image1);
        final SpriteView avatar2 = (SpriteView) bubbleavatarView.findViewById(R.id.bubbleavatar_image2);
        final LinearLayout text_bubble = (LinearLayout) bubbleavatarView.findViewById(R.id.text_bubble);

        //Add the view to the window
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(bubbleavatarView, params);

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        //Specify the bubble position
        params.gravity = Gravity.TOP | Gravity.LEFT; //Initially view will be added to top-left corner
        params.x = dm.widthPixels;
        params.y = 100;

        //Create a sleep thread
        runnableThread(avatar1, avatar2, text_bubble, params, 5000);

        //Make the bubble movable by touch
        SpriteView bubbleavatarImage = avatar1;

        setButtonOnAvatar(bubbleavatarImage, params);

        text_bubble.setOnTouchListener(
                new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            text_bubble.setVisibility(View.GONE);
                            avatar2.setVisibility(View.GONE);
                            avatar1.setVisibility(View.VISIBLE);

                            runnableThread(avatar1, avatar2, text_bubble, params, 2000);
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    public void runnableThread(final SpriteView avatar1, final SpriteView avatar2, final LinearLayout text_bubble, final WindowManager.LayoutParams params, int sec) {

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        avatar2.setVisibility(View.VISIBLE);
                        avatar1.setVisibility(View.GONE);

                        text_bubble.setVisibility(View.VISIBLE);

                        SpriteView bubbleavatarImage = avatar2;
                        setButtonOnAvatar(bubbleavatarImage, params);

                    }
                },
                sec);
    }

    public void setButtonOnAvatar(SpriteView bubbleavatarImage, final WindowManager.LayoutParams params) {
        bubbleavatarImage.setOnTouchListener(
                new View.OnTouchListener() {
                    private int initialX;
                    private int initialY;
                    private float touchX;
                    private float touchY;
                    private int lastAction;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        //When holding down on the bubble
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            //Remember initial position
                            initialX = params.x;
                            initialY = params.y;

                            //Get touch position
                            touchX = event.getRawX();
                            touchY = event.getRawY();

                            lastAction = event.getAction();

                            return true;
                        }
                        Button button = new Button(BubbleAvatarService.this);
                        button.setText("Close");
                        //When tapping on the bubble, a close button should appear
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Tapping the button should close the bubble

                            if (lastAction == MotionEvent.ACTION_DOWN) {


                                RelativeLayout layout = bubbleavatarView.findViewById(R.id.bubble_avatar);
                                layout.addView(button);

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        stopSelf(); //Close the service (remove bubble)
                                    }
                                });
                            }
                        }
                        
                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                            //Calculate X and Y coordinates of the view
                            params.x = initialX + (int) (event.getRawX() - touchX);
                            params.y = initialY + (int) (event.getRawY() - touchY);

                            //Update the layout with the new X and Y coordinate
                            windowManager.updateViewLayout(bubbleavatarView, params);
                            lastAction = event.getAction();
                            return true;
                        }

                        return false;
                    }
                }
        );
    }

    //Remove the bubble from view
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bubbleavatarView != null) {
            windowManager.removeView(bubbleavatarView);
        }
    }
}