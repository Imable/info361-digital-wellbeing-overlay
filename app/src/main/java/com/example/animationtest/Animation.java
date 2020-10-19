package com.example.animationtest;

import androidx.databinding.BaseObservable;

public class Animation extends BaseObservable {
    private String fname;
    private Integer cols;

    public Animation(String fname, Integer cols) {
        this.fname = fname;
        this.cols = cols;
    }
}
