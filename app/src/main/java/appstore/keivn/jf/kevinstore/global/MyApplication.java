package appstore.keivn.jf.kevinstore.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Kevin on 2016/3/2.
 * 因为Application，是整个项目中最先执行的代码，所以可以在此进行全局变量的初始化
 *
 * 创建Application 需要在清单文件中进行配置。
 */
public class MyApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    //Application创建时执行 初始化全局变量的操作目前已知会在今后项目中频繁调用的变量：
    // （Context，Handler,主线程Id：经常需要判断是否在主线程中运行，比如只能主线程修改UI，主线程不能延时操作，如网络请求）
    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        context = getApplicationContext();
        //获取Handler
        handler = new Handler();
        //获取主线程Id,利用android.os下的Process获取
        mainThreadId = android.os.Process.myTid();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
