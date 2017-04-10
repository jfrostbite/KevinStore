package appstore.keivn.jf.kevinstore.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.holder.DetailSafeHolder;
import appstore.keivn.jf.kevinstore.holder.DetailScreenHolder;
import appstore.keivn.jf.kevinstore.holder.DetailSimpleHolder;
import appstore.keivn.jf.kevinstore.holder.DetailDesHolder;
import appstore.keivn.jf.kevinstore.holder.DownLoadHolder;
import appstore.keivn.jf.kevinstore.http.httputils.AppDetailHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;

/**
 * Created by Kevin on 2016/3/7.
 * 该详情页面也有失败页面展示，也哟成功页面展示，所以可以使用loadingPager
 *
 * 详情页面中的三个项目利用holder完成，
 *
 * 理解holder是用来获取VIew，填充View，初始化View的。
 */
public class AppDetailActivity extends BaseActivity{

    private AppList.AppInfo appInfo;
    private ScrollView svDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传递来的数据
        final String packageName = getIntent().getStringExtra("packageName");
        Loadingpage loadingPage = new Loadingpage(UiUtils.getContext()) {
            @Override
            public StateCode requestData() {

                return DetailRequestData(packageName);
            }

            @Override
            public View createSuccessPage() {
                return getSuccessPage();
            }
        };
        setContentView(loadingPage);

        loadingPage.getData();
    }

    /**
     * 返回成功页面的方法
     * 因为有个holder需要操作scollView。所有在这传给构造函数即可
     * @return
     */
    private View getSuccessPage() {
        View detailView = UiUtils.inflateView(R.layout.layout_delait);
        //初始化ScrollView
        svDetail = (ScrollView) detailView.findViewById(R.id.sv_detail);
        //基本信息
        FrameLayout flSimple = (FrameLayout)detailView.findViewById(R.id.fl_detail_simple);
        DetailSimpleHolder simpleHolder = new DetailSimpleHolder();
        simpleHolder.setData(appInfo);
        flSimple.addView(simpleHolder.convertView);
        //安全信息
        FrameLayout flSafe = (FrameLayout)detailView.findViewById(R.id.fl_detail_safe);
        DetailSafeHolder safeHolder = new DetailSafeHolder();
        safeHolder.setData(appInfo);
        flSafe.addView(safeHolder.convertView);
        //屏幕截图
        FrameLayout flScreen = (FrameLayout)detailView.findViewById(R.id.fl_detail_screen);
        DetailScreenHolder screenHolder = new DetailScreenHolder();
        screenHolder.setData(appInfo);
        HorizontalScrollView hsv = new HorizontalScrollView(UiUtils.getContext());
        hsv.addView(screenHolder.convertView);
        flScreen.addView(hsv);
        //描述信息
        FrameLayout flDes = (FrameLayout)detailView.findViewById(R.id.fl_detail_des);
        DetailDesHolder desHolder = new DetailDesHolder(svDetail);
        desHolder.setData(appInfo);
        flDes.addView(desHolder.convertView);

        //下载UI的实现
        FrameLayout flDownload = (FrameLayout)detailView.findViewById(R.id.fl_detail_bottom);
        DownLoadHolder downoadHolder = new DownLoadHolder();
        downoadHolder.setData(appInfo);
        flDownload.addView(downoadHolder.convertView);
        return detailView;
    }

    /**
     * 封装应用详情访问网络的方法
     * @return
     * @param packageName
     */
    private Loadingpage.StateCode DetailRequestData(String packageName) {
        AppDetailHttpRequest appDetailHttp = new AppDetailHttpRequest();
        appDetailHttp.setPackageName(packageName);
        appInfo = appDetailHttp.getData();
        return getStateCode(appInfo);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in,R.anim.to_right_out);
    }

    /**
     * 复写父类关于获取网络数据状态的封装方法啊，因为父类判断是集合，本子类需要判断的是appinfo
     */
    private Loadingpage.StateCode getStateCode(AppList.AppInfo app) {
        if(app!=null){
            return Loadingpage.StateCode.SUCCESS;
        }else{
            return Loadingpage.StateCode.ERROR;
        }
    }
}
