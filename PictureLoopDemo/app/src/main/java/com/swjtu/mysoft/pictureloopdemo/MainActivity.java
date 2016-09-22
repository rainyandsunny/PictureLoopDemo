package com.swjtu.mysoft.pictureloopdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swjtu.mysoft.pictureloopdemo.view.ImageCarouselView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<View> images;
    private ImageCarouselView mImagecarouselView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<View>();
        Drawable bd1 = getResources().getDrawable(R.drawable.bg1);
        Drawable bd2 = getResources().getDrawable(R.drawable.bg2);
        Drawable bd3 = getResources().getDrawable(R.drawable.bg3);
        Drawable bd4 = getResources().getDrawable(R.drawable.bg4);
        View view1 = LayoutInflater.from(this).inflate(R.layout.top_image,null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.top_image,null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.top_image,null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.top_image,null);

        ImageView iv1 = (ImageView) view1.findViewById(R.id.image);
        iv1.setBackground(bd1);
        ImageView iv2 = (ImageView) view2.findViewById(R.id.image);
        iv2.setBackground(bd2);
        ImageView iv3 = (ImageView) view3.findViewById(R.id.image);
        iv3.setBackground(bd3);
        ImageView iv4 = (ImageView) view4.findViewById(R.id.image);
        iv4.setBackground(bd4);
        images.add(view1);
        images.add(view2);
        images.add(view3);
        images.add(view4);

        mImagecarouselView = (ImageCarouselView) findViewById(R.id.imagecarouse);
        mImagecarouselView.initView(images);

    }


}
