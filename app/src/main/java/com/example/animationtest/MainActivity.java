package com.example.animationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

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

import com.codyzen.spriterunner.SpriteView;
import com.example.animationtest.databinding.ActivityMainBinding;
import com.example.animationtest.databinding.BubbleAvatarBinding;

public class MainActivity extends AppCompatActivity {
    public Animation animation;
    public final static int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BubbleAvatarBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.bubble_avatar, null, false);
        setContentView(binding.getRoot());

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

        animation = new Animation(this, "avatar_climb_4", 4,8);
        binding.setAnimation(animation);

        // Temporary cod for changing animation
//        new android.os.Handler().postDelayed(
//            new Runnable() {
//                public void run() {
//                    animation.updateAnimation("avatar_idle_4");
//                    binding.setAnimation(animation);
//                }
//            },
//            2000);
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