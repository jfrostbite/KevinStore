package appstore.keivn.jf.kevinstore.activity.fragment;

import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.db.annotation.Foreign;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.adapter.CategoryAdapter;
import appstore.keivn.jf.kevinstore.bean.CategoryList;
import appstore.keivn.jf.kevinstore.http.httputils.CategoryHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;
import appstore.keivn.jf.kevinstore.ui.widget.MyListView;

/**
 * Created by Kevin on 2016/3/3.
 * 分类
 * 需求：按照应用和游戏进行分类，应用下各种分类，游戏下各种分类
 * 思路：根据服务器返回数据分析：分类项目填充到ListView即可。需要利用adapter的项目类型方法。不同数据类型，填充不
 *      同布局。
 */
public class CategoryFragment extends BaseFragment<CategoryList> {

    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("分类页面数据");*/
        MyListView lvCategory = new MyListView(UiUtils.getContext());
        lvCategory.setAdapter(new CategoryAdapter(listData));
        return lvCategory;
    }

    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        CategoryHttpRequest categoryHttp = new CategoryHttpRequest();
        listData = categoryHttp.getData();
        return getStateCode(listData);
    }
}
