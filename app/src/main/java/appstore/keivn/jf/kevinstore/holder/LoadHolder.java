package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.adapter.MyBaseAdapter;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 */
public class LoadHolder extends BaseHolder<Integer> {

    private ProgressBar pbLoad;
    private TextView tvLoad;

    @Override
    public View initView() {
        View loadView = UiUtils.inflateView(R.layout.layout_listview_loadmore);
        pbLoad = (ProgressBar) loadView.findViewById(R.id.pb_listview_loadmore);
        tvLoad = (TextView) loadView.findViewById(R.id.tv_listview_loadmore);
        //loadView的点击事件，就是重新加载数据
        return loadView;
    }

    @Override
    /*public void initData() {
        switch (data) {
            case MyBaseAdapter.LOADERROR:
                tvLoad.setText("数据加载失败，请点击重试！");
                pbLoad.setVisibility(View.GONE);
                break;
            case MyBaseAdapter.LOADING:
                tvLoad.setText("正在加载...");
                pbLoad.setVisibility(View.VISIBLE);
                break;
            case MyBaseAdapter.LOADSUCCESS:
                tvLoad.setText("数据加载成功！");
                pbLoad.setVisibility(View.GONE);
                break;
            case MyBaseAdapter.LOADNODATA:
                tvLoad.setText("没有更多数据了！");
                pbLoad.setVisibility(View.GONE);
                break;

        }
    }*/

    public void initData() {
        switch (data) {
            case MyBaseAdapter.ERROR:
                tvLoad.setText("数据加载失败，请点击重试！");
                pbLoad.setVisibility(View.GONE);
                break;
            case MyBaseAdapter.HAS_MORE:
                tvLoad.setText("正在加载...");
                pbLoad.setVisibility(View.VISIBLE);
                break;
            case MyBaseAdapter.NO_MORE:
                tvLoad.setText("没有更多数据了！");
                pbLoad.setVisibility(View.GONE);
                break;
            case MyBaseAdapter.LOADHIDE:
                tvLoad.setVisibility(View.GONE);
                pbLoad.setVisibility(View.GONE);
                break;

        }
    }


}
