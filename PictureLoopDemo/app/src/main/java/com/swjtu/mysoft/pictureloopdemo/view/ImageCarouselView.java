package com.swjtu.mysoft.pictureloopdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swjtu.mysoft.pictureloopdemo.R;
import com.swjtu.mysoft.pictureloopdemo.bean.LoopImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片轮播view
 * Created by yhp5210 on 2016/9/21.
 */
public class ImageCarouselView extends RelativeLayout implements Runnable {

    private final String TAG = "ImageCarouselView";
    private Context context;
    private List<View> views;
    private ViewPager mViewPager;
    private List<ImageView> points;
    private boolean isReady = false;
    private List<LoopImage> mLoopImages;
    private TextView mTitleTextView;
    private boolean mAutoScroll;
    private LoopImageListenter mListener;
    private int mCurrentPos; // 记录当前图片的下标
    private long mSleepTime = 2000; //停顿时间默认一秒
    private TypedArray mTypedArray;
    private final int MSG_UPDATE_IMAGES = 0;
    private Thread mAutoLoopThread;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_UPDATE_IMAGES: {

                    if (null != views && views.size() > 1 && mAutoScroll) {
                        updateView((++mCurrentPos) % views.size());
                        sendEmptyMessageDelayed(MSG_UPDATE_IMAGES, mSleepTime);
                    }

                }
                break;

            }

        }
    };


    public ImageCarouselView(Context context) {
        super(context);
        this.context = context;
    }

    public ImageCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageCarouselView);
    }

    public ImageCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageCarouselView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        if (!isReady) {


            //得到相应的参数
            mAutoScroll = mTypedArray.getBoolean(R.styleable.ImageCarouselView_autoScroll, true);
            mSleepTime = mTypedArray.getInt(R.styleable.ImageCarouselView_loopSleepTime, 1500);
            mViewPager = new ViewPager(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mViewPager, params);
            points = new ArrayList<ImageView>();

            //底下包含标题以及point的RelativeLayout
            RelativeLayout tailContent = new RelativeLayout(context);
            RelativeLayout.LayoutParams tailParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            tailParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            int color = context.getResources().getColor(R.color.pointbackground);
            tailContent.setBackgroundColor(color);

            //包含下面point的LinearLayout
            LinearLayout pointsContainer = new LinearLayout(context);
            RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            pointParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            pointParams.addRule(RelativeLayout.CENTER_VERTICAL);

            int tailMarginLeftAndRight = (int) context.getResources().getDimension(R.dimen.tail_leftAndRight_Margin);
            int marginRight = (int) context.getResources().getDimension(R.dimen.point_distance);
            int marginTop = (int) context.getResources().getDimension(R.dimen.point_topMarign);
            int marginBottom = (int) context.getResources().getDimension(R.dimen.point_BottomMarign);

            pointParams.setMarginEnd(tailMarginLeftAndRight);
            for (int i = 0; i < views.size(); i++) {
                ImageView point = new ImageView(context);
                points.add(point);
            }
            LinearLayout.LayoutParams singlePointParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);

            singlePointParams.setMargins(0, marginTop, marginRight, marginBottom);
            for (int i = 0; i < points.size(); i++) {
                ImageView point = points.get(i);
                if (i == 0) {
                    point.setBackgroundResource(R.drawable.whitepoint);
                } else {
                    point.setBackgroundResource(R.drawable.greypoint);
                }
                pointsContainer.addView(point, singlePointParams);
            }

            //添加标题栏内容
            mTitleTextView = new TextView(context);
            mTitleTextView.setTextColor(context.getResources().getColor(R.color.titlefontColor));
            mTitleTextView.setText(mLoopImages.get(0).getTitle());
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
            titleParams.setMargins(tailMarginLeftAndRight, marginTop, 0, marginBottom);
            tailContent.addView(mTitleTextView, titleParams);
            tailContent.addView(pointsContainer, pointParams);
            addView(tailContent, tailParams);
            mViewPager.setAdapter(new ImagePageAdapter(views));
            mViewPager.setOnPageChangeListener(new ImageOnPageChangeListener(views));
            if (mAutoScroll && views.size() > 1) {
                mAutoLoopThread = new Thread(this);
                mAutoLoopThread.start();

            }

        }
        isReady = true;
        super.onLayout(changed, l, t, r, b);


    }


    public int getSize(int measureSpec) {

        int mode = MeasureSpec.getMode(measureSpec);
        int size = 200;
        switch (mode) {

            case MeasureSpec.EXACTLY: {

                size = MeasureSpec.getSize(measureSpec);

            }
            break;
            case MeasureSpec.AT_MOST: {

                size = Math.min(size, MeasureSpec.getSize(measureSpec));

            }
            break;
            default: {
                size = 200;
            }
        }
        return size;
    }

    /**
     * 必须要调用，初始化view
     *
     * @param loopImages
     */
    public void bindData(List<LoopImage> loopImages, LoopImageListenter listener) {


        mLoopImages = loopImages;
        mListener = listener;
        views = new ArrayList<View>();
        for (int i = 0; i < mLoopImages.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.top_image, null);
            ImageView iv1 = (ImageView) view.findViewById(R.id.image);
            Bitmap bitmap = mLoopImages.get(i).getmBitmap();
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            iv1.setBackground(bd);
            views.add(view);
        }



    }

    public void setAutoScroll(boolean isAutoScroll) {
        mAutoScroll = isAutoScroll;
    }


    public void updateView(int position) {

        for (int i = 0; i < points.size(); i++) {
            ImageView point = points.get(i);
            if (i == position) {
                point.setBackgroundResource(R.drawable.whitepoint);
            } else {
                point.setBackgroundResource(R.drawable.greypoint);
            }
        }
        mTitleTextView.setText(mLoopImages.get(position).getTitle());
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void run() {

        if (mAutoScroll) {

            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGES, mSleepTime);

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (MotionEvent.ACTION_DOWN == ev.getAction()) {

            mAutoScroll = false;

        } else if (MotionEvent.ACTION_UP == ev.getAction()) {
            mAutoScroll = true;
            mHandler.removeMessages(MSG_UPDATE_IMAGES);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGES, mSleepTime);
        }
        return super.onInterceptTouchEvent(ev);

    }


    public void stopImageLoop() {

        mAutoScroll = false;
        mAutoLoopThread = null;
    }


    class ImagePageAdapter extends PagerAdapter {

        private List<View> src;


        public ImagePageAdapter(List<View> src) {

            this.src = src;

        }

        @Override
        public int getCount() {
            return src.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(src.get(position % src.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = src.get(position % src.size());
            container.addView(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.imageOnClickListenter(position);
                }
            });
            return view;
        }
    }


    class ImageOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public List<View> views;

        public ImageOnPageChangeListener(List<View> views) {

            this.views = views;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            int size = views.size();
            mCurrentPos = position;
            updateView(mCurrentPos % size);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public interface LoopImageListenter {

        public void imageOnClickListenter(int position);
    }

}
