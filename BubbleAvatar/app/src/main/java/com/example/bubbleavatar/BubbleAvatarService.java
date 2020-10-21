package com.example.bubbleavatar;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

        //Add the view to the window
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the bubble position
        params.gravity = Gravity.TOP | Gravity.LEFT; //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(bubbleavatarView, params);

        //Make the bubble movable by touch
        ImageView bubbleavatarImage = bubbleavatarView.findViewById(R.id.bubbleavatar_image);
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

                        //When tapping on the bubble, a close button should appear
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            //Tapping the button should close the bubble
                            if (lastAction == MotionEvent.ACTION_DOWN) {
                                Button button = new Button(BubbleAvatarService.this);
                                button.setText("Close");

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
