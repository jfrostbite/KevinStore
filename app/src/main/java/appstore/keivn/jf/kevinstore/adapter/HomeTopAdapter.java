package appstore.keivn.jf.kevinstore.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/7.
 * 首页顶部轮播图的ViewPager填充的适配器
 * 改适配器中的数据，可以提供接口，让调用它的类将数据传入。
 */
public class HomeTopAdapter extends PagerAdapter {

    /**
     * HomeBean 中的picture 集合中俄图片链接
     */
    private ArrayList<String> data;

    public HomeTopAdapter(ArrayList<String> data){
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = new ImageView(UiUtils.getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BitmapUtils bitmapUtils = BitmapSingle.getInstance();
        bitmapUtils.display(iv, HttpHelper.URL + "image?name=" + data.get(position));


        //记得添加控件。。。。。。。
        container.addView(iv);
        return iv;
    }
}
