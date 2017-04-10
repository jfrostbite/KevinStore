package appstore.keivn.jf.kevinstore.adapter;

/**
 * Created by Kevin on 2016/3/3.
 */

import java.util.ArrayList;
import java.util.List;

import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.holder.BaseHolder;
import appstore.keivn.jf.kevinstore.holder.HomeHolder;

/**
 * 首页ListView的适配器
 * ListView的适配器在很多地方复用性高
 * 1、抽取Adapter
 * 2、抽取父类中getView方法
 * <p/>
 * 该适配器中需要访问网络，可以利用接口监听的方式实现调用HomeFragment的网络访问方式
 */
public class HomeAdapter extends MyBaseAdapter<AppList.AppInfo>{
    public HomeAdapter(ArrayList<AppList.AppInfo> dataList) {
        super(dataList);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeHolder();
    }
}
