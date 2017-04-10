package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.ImageView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 *
 * 技巧：界面中需要初始化的组件重复性很高，可以用数组来保存该组件的变量名字，然后再初始化时，遍历数组即可方便的
 *      初始化组件，前提：命名
 */
public class DetailScreenHolder extends BaseHolder<AppList.AppInfo> {

    private ImageView[] ivs;
    //包裹整个安全信息的载体控件
    private View ll_footer;
    //显隐安全信息的标记
    private boolean hideSafe;
    @Override
    public View initView() {
        View screenView = UiUtils.inflateView(R.layout.layout_detail_screen);
        ivs = new ImageView[5];
        ivs[0] = (ImageView) screenView.findViewById(R.id.image1);
        ivs[1] = (ImageView) screenView.findViewById(R.id.image2);
        ivs[2] = (ImageView) screenView.findViewById(R.id.image3);
        ivs[3] = (ImageView) screenView.findViewById(R.id.image4);
        ivs[4] = (ImageView) screenView.findViewById(R.id.image5);

        return screenView;
    }

    //自己实现类的getView中特有的操作。数据的传递就是构造函数传给父类。公用的数据
    @Override
    public void initData() {

        for (int i = 0; i < data.screen.size(); i++) {
            //添加安全信息中的安全图标
            BitmapSingle.getInstance().display(ivs[i], HttpHelper.URL + "image?name=" + data.screen.get(i));
        }
    }
}
