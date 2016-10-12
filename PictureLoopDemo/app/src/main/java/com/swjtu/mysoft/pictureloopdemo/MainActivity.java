package com.swjtu.mysoft.pictureloopdemo;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.swjtu.mysoft.pictureloopdemo.bean.LoopImage;
import com.swjtu.mysoft.pictureloopdemo.view.ImageCarouselView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageCarouselView.LoopImageListenter {


    private ImageCarouselView mImagecarouselView;
    private List<LoopImage> mLoopImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Simple Code : How To Use

        //1. 找到ImageCarouselView控件
        mImagecarouselView = (ImageCarouselView) findViewById(R.id.imagecarouse);

        //2. 创建N个LoopImage对象
        mLoopImages = new ArrayList<LoopImage>();
        Drawable d1 = getResources().getDrawable(R.drawable.bg1);
        Drawable d2 = getResources().getDrawable(R.drawable.bg2);
        Drawable d3 = getResources().getDrawable(R.drawable.bg3);
        Drawable d4 = getResources().getDrawable(R.drawable.bg4);
        BitmapDrawable bd1 = (BitmapDrawable) d1;
        BitmapDrawable bd2 = (BitmapDrawable) d2;
        BitmapDrawable bd3 = (BitmapDrawable) d3;
        BitmapDrawable bd4 = (BitmapDrawable) d4;
        LoopImage loopImage1 = new LoopImage();
        loopImage1.setTitle("西南交大美如画1");
        loopImage1.setmBitmap(bd1.getBitmap());
        mLoopImages.add(loopImage1);
        LoopImage loopImage2 = new LoopImage();
        loopImage2.setTitle("西南交大美如画2");
        loopImage2.setmBitmap(bd2.getBitmap());
        mLoopImages.add(loopImage2);
        LoopImage loopImage3 = new LoopImage();
        loopImage3.setTitle("西南交大美如画3");
        loopImage3.setmBitmap(bd3.getBitmap());
        mLoopImages.add(loopImage3);
        LoopImage loopImage4 = new LoopImage();
        loopImage4.setTitle("西南交大美如画4");
        loopImage4.setmBitmap(bd4.getBitmap());
        mLoopImages.add(loopImage4);

        //3. 进行数据绑定
        mImagecarouselView.bindData(mLoopImages,this);




    }


    @Override
    public void imageOnClickListenter(int position) {

        //设置点击事件 TODO
        Toast.makeText(this,"您点击了："+position,Toast.LENGTH_SHORT).show();
    }
}
