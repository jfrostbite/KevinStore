package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 *
 */
public class DetailSimpleHolder extends BaseHolder<AppList.AppInfo> {

    private ImageView appicon;
    private TextView appname;
    private RatingBar appstar;
    private TextView appdownload;
    private TextView appversion;
    private TextView apptime;
    private TextView appsize;
    @Override
    public View initView() {
        View simpleView = UiUtils.inflateView(R.layout.layout_detail_simple);
        appicon = (ImageView) simpleView.findViewById(R.id.appicon);
        appname = (TextView) simpleView.findViewById(R.id.appname);
        appstar = (RatingBar) simpleView.findViewById(R.id.appstar);
        appdownload = (TextView) simpleView.findViewById(R.id.appdownload);
        appversion = (TextView) simpleView.findViewById(R.id.appversion);
        apptime = (TextView) simpleView.findViewById(R.id.apptime);
        appsize = (TextView) simpleView.findViewById(R.id.appsize);
        return simpleView;
    }

    //自己实现类的getView中特有的操作。数据的传递就是构造函数传给父类。公用的数据
    @Override
    public void initData() {
        BitmapSingle.getInstance().display(appicon, HttpHelper.URL+"image?name="+data.iconUrl);
        appname.setText(data.name);
        appdownload.setText(data.downloadNum);
        appversion.setText(data.version);
        apptime.setText(data.date);
        appsize.setText(data.size);
        appstar.setRating(Float.valueOf(data.stars));
    }
}
