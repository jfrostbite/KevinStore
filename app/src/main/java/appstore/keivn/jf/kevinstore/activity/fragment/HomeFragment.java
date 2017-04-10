package appstore.keivn.jf.kevinstore.activity.fragment;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.adapter.HomeAdapter;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.holder.HomeTopHolder;
import appstore.keivn.jf.kevinstore.http.httputils.HomeHttpRequest;
import appstore.keivn.jf.kevinstore.activity.AppDetailActivity;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;

/**
 * Created by Kevin on 2016/3/3.
 * 首页
 * ViewPager+ListView
 * 该页面需要进行首页相关的数据网络访问操作。
 *
 * 首页田间轮播图
 * 思路：给首页的ListView添加一个头，该头的布局文件可以看作一个Holder对象。
 */
public class HomeFragment extends BaseFragment<AppList.AppInfo> {

    private ListView lvHome;
    private HomeAdapter adapter;
    private ArrayList<String> picData;

    /**
     * 首页请求网络的方式
     *
     * @return
     */
    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        HomeHttpRequest homeHttp = new HomeHttpRequest();
        AppList appList = homeHttp.getData();
        if(appList!=null){
            listData = appList.list;
            picData = appList.picture;
        }
        //list = (ArrayList<String>) homeHttp.getData();
        /*listData = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            listData.add("测试数据" + i);
        }*/
        return getStateCode(listData);
    }
    /**
     * 首页独有的布局方式
     *
     * 没有页面特有的请求成功的页面最终由paddingPager 调用进行填充到每个Fragment，至于填不填，展示不展示
     * 具体有该页面请求数据结果决定，就是请求成功，失败，没有数据，枚举类型的那几个
     *
     * @return
     */
    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("首页数据");*/
        /*for (int i = 0; i < 20; i++) {
            list.add("测试数据" + i);
        }*/
        //填充布局文件，最终由loadingPager调用
        View view = UiUtils.inflateView(R.layout.layout_home);
        //初始化控件
        lvHome = (ListView) view.findViewById(R.id.lv_home);

        //初始化头需要的布局
        HomeTopHolder holder = new HomeTopHolder();
        holder.setData(picData);
        //给ListView添加头
        lvHome.addHeaderView(holder.convertView);

        adapter = new HomeAdapter(listData);
        //利用自定义适配器的加载更多数据的接口，给适配器请求数据加载更多数据
        adapter.setOnLoadMoreListener(new HomeAdapter.OnLoadmoreListener() {
            @Override
            public ArrayList<AppList.AppInfo> loadMore() {
                return loadMOre();
            }
        });

        lvHome.setAdapter(adapter);

        //listview 的条目点击事件，就是点击需要查看的应用的详情，然后弹出详情页面。
        lvHome.setOnItemClickListener(listener);
        return view;
    }

    /**
     * ListView条目点击事件
     */
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        //点击条目后弹出详情页面
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //创建意图
            Intent intent = new Intent();
            //给意图设置跳转页面的View对象
            intent.setClass(UiUtils.getContext(),AppDetailActivity.class);
            //传递数据
            intent.putExtra("packageName",listData.get(position-1).packageName);
            //执行意图
            startActivity(intent);
            //设置动画
            //界面切换的动画效果由 Activity界面实现。这里不是Activity，所以使用getActivity实现设置动画效果
            getActivity().overridePendingTransition(R.anim.from_right_in,R.anim.to_left_out);
        }
    };


    /**
     * 加载更多数据,上啦加载的时候请求网络数据可以卸载这里。
     */
    private ArrayList<AppList.AppInfo> loadMOre() {
        ArrayList<AppList.AppInfo> moreData = new ArrayList<AppList.AppInfo>();
        HomeHttpRequest homeHttp = new HomeHttpRequest();
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
