package com.example.bubbleavatar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
    public final static int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check permission (allow if this application is allowed to draw other applications)
        //Default for API < 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)) {
            //Grant permission if the draw over permission is not available
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        } else {
            showBubbleAvatar();
        }
    }

    //Activate the bubble avatar if the permission is allowed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                showBubbleAvatar();
            }
        }
    }

    //Display the bubble avatar
    public void showBubbleAvatar() {
        startService(new Intent(MainActivity.this, BubbleAvatarService.class));
    }

}