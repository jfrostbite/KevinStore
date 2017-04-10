package appstore.keivn.jf.kevinstore.holder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.adapter.HomeTopAdapter;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.randomLayout.RandomLayout;

/**
 * Created by Kevin on 2016/3/7.
 * 首页轮播图封装的Holder对象
 * 该对象返回一个包裹ViewPager和点的布局对象 其中封装好了首页的头需要的数据和界面。
 * 该轮播图的界面利用代码实现界面
 *
 * 轮播图中校园点指示器的实现:
 * 思路：动态显示圆点，个数由ViewPager展示个数决定，所以应该在initData中进行。初始化时创建圆点的载体。LineaerLayout。
 */
public class HomeTopHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager vpHomeTop;
    private LinearLayout dots;
    private HomeTopAdapter adapter;

    @Override
    public View initView() {
        //创建载体。
        RelativeLayout fl = new RelativeLayout(UiUtils.getContext());
        //设置载体的参数，设置载体参数需要给他添加他的父控件的参数对象,该rl的父控件是ListView，ListView的载体参数为AbsListView.paragrams
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dip2px(150));
        fl.setLayoutParams(params);
        //创建ViewPager
        vpHomeTop = new ViewPager(UiUtils.getContext());

        //创建原点指示器,载体
        dots = new LinearLayout(UiUtils.getContext());
        //设置 圆点载体属性
        RelativeLayout.LayoutParams dotsParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置位置的属性在 addRule中，就是是添加一个规则
        dotsParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dotsParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //设置距离
        dotsParams.rightMargin = UiUtils.dip2px(10);
        dotsParams.bottomMargin = UiUtils.dip2px(10);
        //dots.setLayoutParams(dotsParams);
        fl.addView(vpHomeTop);
        fl.addView(dots,dotsParams);
        return fl;
    }

    @Override
    public void initData() {

        adapter = new HomeTopAdapter(data);
        vpHomeTop.setAdapter(adapter);
        //根据轮播图片的个数，确定点的个数，然后填充到dots（点的载体 LinearLayout）中
        for (int i = 0; i < data.size(); i++) {
            //设置首页默认点为白色
            ImageView dot = new ImageView(UiUtils.getContext());
            dot.setImageResource(R.mipmap.indicator_normal);
            if(i == 0){
                dot.setImageResource(R.mipmap.indicator_selected);
            }
            //设置点间距
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            dotParams.leftMargin = UiUtils.dip2px(5);
            //添加到dots中
            dots.addView(dot,dotParams);
        }

        vpHomeTop.setOnPageChangeListener(listener);
        //利用消息机制实现，图片轮播。
        UiUtils.getHandler().postDelayed(r, 2000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            //开始下一个事件前判断上一个消息是否需要销毁
            int current = vpHomeTop.getCurrentItem();
            current++;
            if(current > adapter.getCount()-1){
                current = 0;
            }
            vpHomeTop.setCurrentItem(current);
            UiUtils.getHandler().removeCallbacks(r);
            UiUtils.getHandler().postDelayed(r, 2000);
        }
    };

    /**
     * 设置页面切换时的时间，比如 页面切换时需要改变小圆点样式。到最后一张是，循环切换。
     */
    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        //页面切换完毕
        @Override
        public void onPageSelected(int position) {
            /*if(position == data.size()-1){
                vpHomeTop.setCurrentItem(0);
            }*/
            //此处不能用创建出来的点，因为创建出来的点始终未最后一个，前面的都被覆盖了。所以这里只能从父控件中获取
            int count = dots.getChildCount();
            for (int i = 0; i < count; i++) {
                ImageView dot = (ImageView) dots.getChildAt(i);
                if(i == position){
                    dot.setImageResource(R.mipmap.indicator_selected);
                }else{
                    dot.setImageResource(R.mipmap.indicator_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
