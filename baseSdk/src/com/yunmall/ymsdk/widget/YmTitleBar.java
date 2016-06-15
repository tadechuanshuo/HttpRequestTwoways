package com.yunmall.ymsdk.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.yunmall.ymsdk.R;

/**
 * titlebar 控件
 * 在xml中使用的例子
 *
 *     <com.yunmall.ymsdk.widget.YmTitleBar
          xmlns:app="http://schemas.android.com/apk/res/com.yunmall.ym"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"

          app:leftBgDrawable="@drawable/btn_large_mode_selector"    //左按钮 图
          app:leftPadding="5dp"                                     //左按钮 与左侧的间距

          app:rightText="找店"                                      //右按钮 图
          app:rightTextColor="#FFFFFF"                              //右按钮文本颜色
          app:rightTextSize="19"                                    //右按钮文本字体大小，值为sp
          app:rightPadding="10dp"                                   //左按钮 与左侧的间距

          app:titleText="云茂"                                       //标题文本
          app:titleTextColor="#FF0000"                               //标题文本颜色
          app:titleTextSize="20"                                     //标题字体大小

          app:tabItem1="@string/boutique"商品                         //tab1的文本
          app:tabItem2="@string/follow"                               //Tab2的文本   暂时最多支持2个tab
          app:tabItemSpace="35dp"                                     //tabitem的间距
          app:tabTextColor="@color/titlebar_tab_text_color"           //tabitem的颜色，必须是statelistcolor
          app:tabTextSize="19"/>                                      //tabitem的文本字体大小

 */
public class YmTitleBar extends FrameLayout {

    private TextView mLeftBtn;
    private TextView mRightBtn;
    private TextView mTitleTv;

    private RadioGroup mTabHost;
    private RadioButton mTabItem1;
    private RadioButton mTabItem2;

    private OnClickListener mLeftBtnClickListener = null;
    private OnClickListener mRightBtnClickListener = null;
    private OnClickListener mTitleClickListener = null;
    private OnTitleTabChangeListener mOnTitleTabChangeListener = null;

    public YmTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public YmTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public YmTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.ymsdk_widget_titlebar, this);
        mLeftBtn = (TextView) findViewById(R.id.ymtitlebar_left_btn);
        mRightBtn = (TextView) findViewById(R.id.ymtitlebar_right_btn);
        mTitleTv = (TextView) findViewById(R.id.ymtitlebar_title_text);

        mTabHost = (RadioGroup) findViewById(R.id.ymtitlebar_tabhost);
        mTabItem1 = (RadioButton) findViewById(R.id.ymtitlebar_tabitem1);
        mTabItem2 = (RadioButton) findViewById(R.id.ymtitlebar_tabitem2);

        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.YmTitleBar);
            init(a);
            a.recycle();
        }
    }

    private void updateBtn(TextView btn, Drawable drawable, CharSequence text, int color, float size, int padding) {
        if (drawable == null && TextUtils.isEmpty(text)) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
            btn.setBackgroundDrawable(drawable);
            btn.setText(text);
            btn.setTextColor(color);
            btn.setTextSize(size);

            MarginLayoutParams params = (MarginLayoutParams) btn.getLayoutParams();
            if (params != null) {
                params.setMargins(padding, 0, padding, 0);
            }
        }
    }

    private void init(TypedArray a) {

        int defaultColor = 0xffffffff; // 默认字体的颜色 白色
        int defaultBtnTextSize = 18; // 默认左右按键的字体大小 18sp
        int defaultTitleTextSize = 19; //  默认标题字体大小 19sp
        int defaultTabItemSpace = 20; // 默认TabItem的间距为 20dp

        Drawable leftDrawable = a.getDrawable(R.styleable.YmTitleBar_leftBgDrawable);
        CharSequence leftText = a.getText(R.styleable.YmTitleBar_leftText);
        int leftTextColor = a.getColor(R.styleable.YmTitleBar_leftTextColor, defaultColor);
        float leftTextSize = a.getFloat(R.styleable.YmTitleBar_leftTextSize, defaultBtnTextSize);
        int leftPadding = a.getDimensionPixelSize(R.styleable.YmTitleBar_leftPadding, 0);

        Drawable rightDrawable = a.getDrawable(R.styleable.YmTitleBar_rightBgDrawable);
        CharSequence rightText = a.getText(R.styleable.YmTitleBar_rightText);
        int rightTextColor = a.getColor(R.styleable.YmTitleBar_rightTextColor, defaultColor);
        float rightTextSize = a.getFloat(R.styleable.YmTitleBar_rightTextSize, defaultBtnTextSize);
        int rightPadding = a.getDimensionPixelSize(R.styleable.YmTitleBar_rightPadding, 0);

        Drawable titleDrawable = a.getDrawable(R.styleable.YmTitleBar_titleBgDrawable);
        CharSequence titleText = a.getText(R.styleable.YmTitleBar_titleText);
        int titleTextColor = a.getColor(R.styleable.YmTitleBar_titleTextColor, defaultColor);
        float titleTextSize = a.getFloat(R.styleable.YmTitleBar_titleTextSize, defaultTitleTextSize);

        CharSequence tabItem1 = a.getText(R.styleable.YmTitleBar_tabItem1);
        CharSequence tabItem2 = a.getText(R.styleable.YmTitleBar_tabItem2);
        ColorStateList colorList = a.getColorStateList(R.styleable.YmTitleBar_tabTextColor);
        float tabTextSize = a.getFloat(R.styleable.YmTitleBar_tabTextSize, defaultTitleTextSize);
        int tabItemSpace = a.getDimensionPixelSize(R.styleable.YmTitleBar_tabItemSpace, defaultTabItemSpace);

        updateBtn(mLeftBtn, leftDrawable, leftText, leftTextColor, leftTextSize, leftPadding);
        updateBtn(mRightBtn, rightDrawable, rightText, rightTextColor, rightTextSize, rightPadding);
        updateBtn(mTitleTv, titleDrawable, titleText, titleTextColor, tabTextSize, 0);

        if (!TextUtils.isEmpty(tabItem1)) {
            mTabHost.setVisibility(View.VISIBLE);
            mTabItem1.setVisibility(View.VISIBLE);
            mTabItem1.setText(tabItem1);
            mTabItem1.setTextColor(colorList);
            mTabItem1.setTextSize(tabTextSize);
        }
        if (!TextUtils.isEmpty(tabItem2)) {
            mTabItem2.setVisibility(View.VISIBLE);
            mTabItem2.setText(tabItem2);
            mTabItem2.setTextColor(colorList);
            mTabItem2.setTextSize(tabTextSize);
            MarginLayoutParams params = (MarginLayoutParams) mTabItem2.getLayoutParams();
            if (params != null) {
                params.setMargins(tabItemSpace, 0, 0, 0);
            }
        }

        mLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLeftBtnClickListener != null){
                    mLeftBtnClickListener.onClick(mLeftBtn);
                }
            }
        });
        mRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRightBtnClickListener != null){
                    mRightBtnClickListener.onClick(mRightBtn);
                }
            }
        });
        this.setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTitleClickListener != null){
                    mTitleClickListener.onClick(YmTitleBar.this);
                }
            }
        });

        mTabItem1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTitleTabChangeListener != null) {
                    mOnTitleTabChangeListener.onTabChanged(0);
                }
            }
        });

        mTabItem2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTitleTabChangeListener != null) {
                    mOnTitleTabChangeListener.onTabChanged(1);
                }
            }
        });
    }

    public void setTabIndex(int index) {
        if (index == 0) {
            mTabItem1.setChecked(true);
        } else {
            mTabItem2.setChecked(true);
        }
    }

    /**
     * 设置Title中间标题文字
     * @param title 标题文字
     */
    public void setTitle(String title) {
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }
    
    /**
     * 设置Title标题颜色
     * @param resId
     */
    public void setTitleColor(int resId){
    	if(mTitleTv!=null){
    		ColorStateList csl=(ColorStateList)getResources().getColorStateList(resId);
    		if(csl!=null){
    			mTitleTv.setTextColor(csl);
    		}
    	}
    }

    /**
     * 设置Title中间标题文字
     * @param resId 标题文字
     */
    public void setTitle(int resId) {
        if (mTitleTv != null) {
            mTitleTv.setText(resId);
        }
    }

    /**
     * 设置左按钮的提示标签
     * @param badgeStr
     */
    public void setLeftBadge(String badgeStr){
        BadgeView badgeView = new BadgeView(getContext(), mLeftBtn);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setText(badgeStr);
        badgeView.setTextColor(Color.WHITE);
        badgeView.setBadgeBackgroundColor(Color.RED);
        badgeView.setTextSize(10);
        badgeView.show();
    }

    /**
     * 设置右按钮的提示标签
     * @param badgeStr
     */
    public void setRightBadge(String badgeStr){
        BadgeView badgeView = new BadgeView(getContext(), mRightBtn);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setText(badgeStr);
        badgeView.setTextColor(Color.WHITE);
        badgeView.setBadgeBackgroundColor(Color.RED);
        badgeView.setTextSize(10);
        badgeView.show();
    }

    /**
     * 设置左按键图标
     * @param drawable
     */
    public void setLeftDrawable(int drawable){
        mLeftBtn.setBackgroundResource(drawable);
    }

    /**
     * 设置左按键文本
     * @param resId
     */
    public void setLeftText(int resId){
        mLeftBtn.setText(resId);
    }

    /**
     * 设置左按键文本
     * @param str
     */
    public void setLeftText(String str){
        mLeftBtn.setText(str);
    }

    /**
     * 设置左按键可见性
     * @param visible
     */
    public void setLeftVisiable(int visible){
        mLeftBtn.setVisibility(visible);
    }

    /**
     * 设置右按键图标
     * @param drawable
     */
    public void setRightDrawable(int drawable){
        mRightBtn.setBackgroundResource(drawable);
    }

    /**
     * 设置右按键文本
     * @param resId
     */
    public void setRightText(int resId){
        mRightBtn.setText(resId);
    }

    /**
     * 设置右按键文本
     * @param str
     */
    public void setRightText(String str){
        mRightBtn.setText(str);
    }

    /**
     * 设置左按键间距
     * @param left   dip值
     * @param top  dip值
     * @param right  dip值
     * @param bottom  dip值
     */
    public void setLeftMargins(float left, float top, float right, float bottom){
        float density = getResources().getDisplayMetrics().density;
        int marginLeft = (int)(left * density);
        int marginTop = (int)(top * density);
        int marginRight = (int)(right * density);
        int marginBottom = (int)(bottom * density);
        MarginLayoutParams params = (MarginLayoutParams)mLeftBtn.getLayoutParams();
        if(params != null){
            params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }
    }

    /**
     * 设置右按键间距
     * @param left   dip值
     * @param top  dip值
     * @param right  dip值
     * @param bottom  dip值
     */
    public void setRightMargins(float left, float top, float right, float bottom){
        float density = getResources().getDisplayMetrics().density;
        int marginLeft = (int)(left * density);
        int marginTop = (int)(top * density);
        int marginRight = (int)(right * density);
        int marginBottom = (int)(bottom * density);
        MarginLayoutParams params = (MarginLayoutParams)mRightBtn.getLayoutParams();
        if(params != null){
            params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        }
    }

    /**
     * 设置右按键可见性
     * @param visible
     */
    public void setRightVisiable(int visible){
        mRightBtn.setVisibility(visible);
    }
    public TextView getRightButton() {
        return mRightBtn;
        
    }
    public TextView getLeftButton() {
        return mLeftBtn;
        
    }
    public interface SpaceOnClickListener {
        public void OnClick();
    }
    
    /**
     * 设置左按钮按键响应
     * @param mListener
     */
    public void setLeftBtnListener(OnClickListener mListener) {
        mLeftBtnClickListener = mListener;
    }

    /**
     * 设置右按钮按键响应
     * @param mListener
     */
    public void setRightBtnListener(OnClickListener mListener) {
        mRightBtnClickListener = mListener;
    }
   
    /**
     * 设置title非按钮区域按键响应
     * @param mListener
     */
    public void setTitleListener(OnClickListener mListener) {
        mTitleClickListener = mListener;
    }

    /**
     * 设置tab的响应
     * @param mListener
     */
    public void setOnTitleTabChangeListener(OnTitleTabChangeListener mListener) {
        mOnTitleTabChangeListener = mListener;
    }

    /**
     * tabitem切换监听器
     */
    public interface OnTitleTabChangeListener {
        public void onTabChanged(int index);
    }

}
