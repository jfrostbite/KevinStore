package appstore.keivn.jf.kevinstore.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.activity.fragment.BaseFragment;
import appstore.keivn.jf.kevinstore.activity.fragment.FragmentFactory;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.PagerTab;

/**
 * 项目底层界面：添加pageTab和ViewPager，在ViewPager中添加页签对应页面
 * 因为是PagerTab和ViewPage联动，此时ViewPager必须使用FragmentAdapter
 * 当ViewPager中填充简单的页面（如轮播图）一般使用PagerAdapter，但是其中填充复杂页面（Fragment）是使用FragmentAdapter
 * Fragment就是可以添加很多页面控件时使用
 *
 * ViewPager 中getItem 获取到预加载页面，OnPagerChangedListener 中页面切换完毕是获取当前页面
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        PagerTab ptTitle = (PagerTab) findViewById(R.id.pt_main_title);
        ViewPager vpPage = (ViewPager) findViewById(R.id.vp_main_page);
        //设置适配器
        vpPage.setAdapter(new MyAdapter(getSupportFragmentManager()));
        //关联PagerTab和ViewPager
        ptTitle.setViewPager(vpPage);
        //页面切换时加载数据
        ptTitle.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面切换完毕 加载数据
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.getFragment(position);
                //BaseFragment 对外提供的 LoadingPager中访问网络的方式。
                fragment.onLoad();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * viewPager适配器，需要继承FragmentAdapter，因为里面需要填充Fragment
     * 页面个数就是顶部PagerTab的标题数，该标题从字符串数组中获取（资源文件中）
     * 可以在适配器初始化时获取
     */
    class MyAdapter extends FragmentPagerAdapter{

        String[] titles = null;
        public MyAdapter(FragmentManager fm) {
            super(fm);
            //构造函数初始化时获取该tab字符串数组
            titles = UiUtils.getStringArray(R.array.tab_titles);
        }

        //相当于getView，这里因为需要填充Fragment，所以返回的是Fragment.
        //此处需要返回的就是每个页面的Fragment，因为每个页面不同，所以利用FragmentFactory，根据position获取相对应的Fragment
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.getFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        //获取ViewPager每个页面的标题，改标题用于给页签设置标题，所以需要页签和ViewPager做关联
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
