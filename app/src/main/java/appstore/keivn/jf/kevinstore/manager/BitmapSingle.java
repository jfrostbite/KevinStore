package appstore.keivn.jf.kevinstore.manager;

import android.content.SyncStatusObserver;

import com.lidroid.xutils.BitmapUtils;

import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/4.
 * 单例设计模式
 * 懒汉式
 */
public class BitmapSingle {
    private BitmapSingle(){};
    static BitmapUtils bitmapUtils = null;
    public static synchronized BitmapUtils getInstance(){
        if(bitmapUtils == null){
            bitmapUtils = new BitmapUtils(UiUtils.getContext());
        }
        return bitmapUtils;
    }
}
