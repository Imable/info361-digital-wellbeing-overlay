package com.example.animationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.codyzen.spriterunner.SpriteView;
import com.example.animationtest.databinding.BubbleAvatarBinding;

public class MainActivity extends AppCompatActivity {
    private Animation animation;
    public final static int PERMISSION_REQUEST_CODE = 1;

    private Button summonAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        summonAvatar = (Button) findViewById(R.id.summon_avatar);
        summonAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLayoutPermissionAllowed(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, BubbleAvatarService.class);
                    startService(intent);

                } else {
                    grantOverlayPermission(MainActivity.this);
                }
            }
        });

//        final BubbleAvatarBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.bubble_avatar, null, false);

//
//
//        animation = new Animation(this, "avatar_climb_4", 4,8);
//
//        binding.setAnimation(animation);

//         Temporary cod for changing animation
//        new android.os.Handler().postDelayed(
//            new Runnable() {
//                public void run() {
//                    animation.updateAnimation("avatar_idle_4");
//                    binding.setAnimation(animation);
//                }
//            },
//            2000);
    }


    //Check permission (allow if this application is allowed to draw other applications)
    //Default for API < 23
    private boolean isLayoutPermissionAllowed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(activity)) {
            return false;
        }
        else {
            return true;
        }
    }

    private void grantOverlayPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }
    }

    @BindingAdapter("src")
    public static void setSrc(SpriteView view, Drawable image) {
        Bitmap bm = drawableToBitmap(image);
        view.setImage(bm);
        view.start();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}