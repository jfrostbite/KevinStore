package appstore.keivn.jf.kevinstore.ui.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.*;
import android.view.View;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.global.MyApplication;

/**
 * Created by Kevin on 2016/3/2.
 *
 * UI界面操作的同一工具类，也是为后面添加自定义控件PagerTab，其中调用的一些方法做铺垫。
 */
public class UiUtils {
    //获取全局变量
    public static Context getContext(){
        return MyApplication.getContext();
    }
    public static Handler getHandler(){
        return MyApplication.getHandler();
    }
    public static int getMainThreadId(){
        return MyApplication.getMainThreadId();
    }
    
    //获取资源文件：字符串数组
    public static String[] getStringArray(int resId){
        return getContext().getResources().getStringArray(resId);
    }
        //获取资源文件：ColorId
    public static int getColor(int resId){
        return getContext().getResources().getColor(resId);
    }
        //获取资源文件：Dimens
    public static int getDimens(int resId){
        return getContext().getResources().getDimensionPixelSize(resId);
    }
    //获取资源文件：颜色状态选择器
    public static ColorStateList getColorStateList(int resId) {
        return getContext().getResources().getColorStateList(resId);
    }
    //获取资源文件：Drawable
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);
    }
    
        //判断当前线程是否是主线程：原理，就是谁调用Process.myTid就返回谁的线程ID
    public static boolean isRunMainThread(){
        //获取谁调用就是谁的线程ID
        int currentThreadId = android.os.Process.myTid();
        //和主线程Id比较当前线程Id，并返回
        return currentThreadId==getMainThreadId();
    }

    //dp到px的转换：原理：dp*密度=px
    public static int dip2px(int dp){
        //获取当前密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dp*density+0.5f);
    }
    //px到dp的转换：原理：dp*密度=px
    public static int px2dip(int px){
        //获取当前密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(px/density+0.5f);
    }

    //抽取需要在主线程中运行的方法：原理传入线程对象，判断是否是主线程，如果是就直接运行，否则利用消息机制丢到主线程中去执行
    public static void runOnUiThread(final Runnable r){
        //此处直接执行获取线程id的方法，因为是需要在主线程中执行的方法 调用的该方法，所以返回的是该方法的线程id
        if(isRunMainThread()){
            r.run();
        }else{
            getHandler().post(r);
        }
    }

    //布局填充抽取
    public static View inflateView(int resId){
        return View.inflate(UiUtils.getContext(), resId,null);
    }

    //圆角矩形的创建，和xml中的selector一样
    public static GradientDrawable getGradientDrawable(float radius, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);//设置圆角半径
        drawable.setGradientType(GradientDrawable.RECTANGLE);//设置类型为矩形
        drawable.setColor(color);//设置颜色
        return drawable;
    }

    //状态选择器
    public static StateListDrawable getSelector(Drawable pressedDrawable,
                                                Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(new int[] { android.R.attr.state_pressed },
                pressedDrawable);//增加点击状态的图片
        stateListDrawable.addState(new int[] {}, normalDrawable);//增加默认状态的图片

        return stateListDrawable;
    }

}
