package appstore.keivn.jf.kevinstore.activity.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.adapter.AppAdapter;
import appstore.keivn.jf.kevinstore.adapter.HomeAdapter;
import appstore.keivn.jf.kevinstore.adapter.MyBaseAdapter;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.http.httputils.AppHttpRequest;
import appstore.keivn.jf.kevinstore.http.httputils.HomeHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;

/**
 * Created by Kevin on 2016/3/3.
 * 应用
 */
public class AppFragment extends BaseFragment {
    private ListView lvHome;
    private ArrayList<AppList.AppInfo> dataList;
    private AppAdapter adapter;

    /**
     * 应用请求网络的方式
     *
     * @return
     */
    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        AppHttpRequest http = new AppHttpRequest();
        AppList appList = http.getData();
        if(appList!=null) {
            dataList = appList.list;
        }
        return getStateCode(dataList);
    }

    /**
     * 应用独有的布局方式
     *
     * @return
     */
    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("首页数据");*/
        View view = UiUtils.inflateView(R.layout.layout_home);
        lvHome = (ListView) view.findViewById(R.id.lv_home);
        /*ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            temp.add("应用界面："+i);
        }*/

        //回掉需要加载更多数据的操作
        adapter = new AppAdapter(dataList);
        adapter.setOnLoadMoreListener(new MyBaseAdapter.OnLoadmoreListener<AppList.AppInfo>() {
            @Override
            public ArrayList<AppList.AppInfo> loadMore() {
                return loadMOre();
            }
        });

        lvHome.setAdapter(adapter);
        return view;
    }

    /**
     * 加载更多数据,上啦加载的时候请求网络数据可以卸载这里。
     */
    private ArrayList<AppList.AppInfo> loadMOre() {
        ArrayList<AppList.AppInfo> moreData = new ArrayList<AppList.AppInfo>();
        AppHttpRequest homeHttp = new AppHttpRequest();
        homeHttp.setIndex(adapter.getCount());
        AppList appList = homeHttp.getData();
        //moreData = appList.list;

        /*for (int i = 0; i < 20; i++) {
            temp.add("加载数据：" + i);
        }*/
        //list.addAll(temp);
        if(appList!=null){
            return appList.list;
        }else{
            return null;
        }
    }
}
