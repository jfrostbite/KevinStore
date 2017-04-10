package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 * 可以调用Home页的getView的操作，应为都是一样的展示形式
 */
public class AppHolder extends BaseHolder<String> {

    private TextView tvHomeItem;

    //Home页面 ListView项目填充的布局
    @Override
    public View initView() {
        View appItem = UiUtils.inflateView(R.layout.layout_home_item);
        //tvHomeItem = (TextView) appItem.findViewById(R.id.tv_home_item_title);
        return appItem;
    }

    //自己实现类的getView中特有的操作。数据的传递就是构造函数传给父类。公用的数据
    @Override
    public void initData() {
        //tvHomeItem.setText(data);
    }
}
