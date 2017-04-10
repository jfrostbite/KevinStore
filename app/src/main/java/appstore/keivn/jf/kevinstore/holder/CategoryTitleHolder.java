package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.CategoryList;
import appstore.keivn.jf.kevinstore.holder.BaseHolder;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/7.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryList> {

    private TextView tv;

    @Override
    public View initView() {
        View titleView = UiUtils.inflateView(R.layout.layout_category_title);
        tv = (TextView) titleView.findViewById(R.id.tv);
        return titleView;
    }

    @Override
    public void initData() {
        tv.setText(data.title);
    }
}
