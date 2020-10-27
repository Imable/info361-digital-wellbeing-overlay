package com.example.animationtest;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;

public class Animation extends BaseObservable {
    public final Integer cols;
    public Drawable image;
    public final Integer fps;
    private Context context;

    public Animation(Context context, String fname, Integer cols, Integer fps) {
        this.context = context;
        this.cols = cols;
        this.fps = fps;
        updateAnimation(fname);
    }

    public void updateAnimation(String fname) {
        this.image = getDrawableFromString(fname);
    }

    private Drawable getDrawableFromString(String s) {
        int id = this.context.getResources().getIdentifier(s, "drawable", this.context.getPackageName());
        Drawable drawable = this.context.getResources().getDrawable(id);
        return drawable;
    }

    public Integer getFps() {
        return this.fps;
    }

    public Integer getCols() {
        return this.cols;
    }

    public Drawable getImage() {
        return this.image;
    }
}
