package com.swjtu.mysoft.pictureloopdemo.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.List;

/**
 * 图片轮播view
 * Created by yhp5210 on 2016/9/21.
 */
public class ImageCarouselView extends RelativeLayout{

    private Context context;
    private List<View> views;
    private ViewPager mViewPager;
    private boolean isReady = false;

    public ImageCarouselView(Context context) {
        super(context);
        this.context = context;
    }
    public ImageCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public ImageCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getSize(widthMeasureSpec),getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
        if(!isReady){
            mViewPager = new ViewPager(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mViewPager,params);
            mViewPager.setAdapter(new ImagePageAdapter(views));
            mViewPager.setOnPageChangeListener(new ImageOnPageChangeListener(views));

        }
        isReady = true;



    }



    public int getSize(int measureSpec){

        int mode = MeasureSpec.getMode(measureSpec);
        int size = 200;
        switch (mode){

            case MeasureSpec.EXACTLY:{

                size = MeasureSpec.getSize(measureSpec);

            }break;
            case MeasureSpec.AT_MOST:{

                size = Math.min(size,MeasureSpec.getSize(measureSpec));

            }break;
            default:{
                size = 200;
            }
        }
        return size;
    }


    /**
     * 必须要调用，初始化view
     * @param views
     */
    public void initView(List<View> views){

        this.views = views;


    }

    class ImagePageAdapter extends PagerAdapter {

        public List<View> src;


        public ImagePageAdapter(List<View> src){

            this.src = src;
        }
        @Override
        public int getCount() {
            return src.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(src.get(position%src.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            System.out.println("position:"+position);
            View view = src.get(position%src.size());
            container.addView(view);
            return view;
        }
    }


    class ImageOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public List<View> views;

        public ImageOnPageChangeListener( List<View> views){

            this.views = views;
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            int size = views.size();
            mViewPager.setCurrentItem(position%size,true);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}