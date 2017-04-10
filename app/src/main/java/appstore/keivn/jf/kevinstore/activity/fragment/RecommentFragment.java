package appstore.keivn.jf.kevinstore.activity.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.db.annotation.Unique;

import java.util.Random;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.http.httputils.RecommentHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;
import appstore.keivn.jf.kevinstore.ui.widget.randomLayout.ShakeListener;
import appstore.keivn.jf.kevinstore.ui.widget.randomLayout.StellarMap;

/**
 * Created by Kevin on 2016/3/3.
 * 推荐
 * 需求：实现星际 效果，效果中展示应用的名字
 * 利用StellarMap 类实现。
 */
public class RecommentFragment extends BaseFragment<String> implements ShakeListener.OnShakeListener {


    private MyAdapter adapter;
    private StellarMap stellarMap;

    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        RecommentHttpRequest recomHttp = new RecommentHttpRequest();
        listData = recomHttp.getData();
        return getStateCode(listData);
    }

    /**
     * StellarMap需要封装随机控件，类似ListView 需要添加有规则的条目。可以设置适配器来完成
     *
     * @return
     */
    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("推荐页数据");*/
        //创建StellarMap控件
        stellarMap = new StellarMap(UiUtils.getContext());
        int padding = UiUtils.dip2px(10);
        stellarMap.setInnerPadding(padding, padding, padding, padding);
        stellarMap.setRegularity(9, 9);
        //给StellarMap设置适配器。
        adapter = new MyAdapter();
        stellarMap.setAdapter(adapter);
        //设置当前默认展示页面，true：动画效果
        stellarMap.setGroup(0, true);
        //摇一摇监听器
        ShakeListener listener = new ShakeListener(UiUtils.getContext());
        listener.setOnShakeListener(this);
        return stellarMap;
    }

    /**
     * 摇动手机监听器
     * 需求：摇动手机，切换推荐页面，就是实现StellarMap的操作（设置组为当前组的下一组）
     */
    @Override
    public void onShake() {
        //获取当前组
        int currentGroup = stellarMap.getCurrentGroup();
        //设置下一组
        currentGroup++;
        if(currentGroup>adapter.getGroupCount()-1)
            currentGroup=0;
        stellarMap.setGroup(currentGroup,true);
    }

    class MyAdapter implements StellarMap.Adapter {

        /**
         * 滑动该控件时，会展示不同的数据群，比如三组数据，滑动式就会在这三组数据中来回切换显示。
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            //return listData.size()/getCount(0)+(listData.size()%getCount(0))>0?1:0;
            return 4;
        }

        @Override
        public int getCount(int group) {
            /*//返回指定组的子控件个数。最后一页 的 子 个数应该是整除剩下的个数。
            if(group == listData.size()-1){
                return listData.size()%getGroupCount();
            }g
            return 10;*/
            int count = listData.size() / getGroupCount();
            //如果是最后一组，考虑除不尽的情况
            if (group == getGroupCount() - 1) {
                count += listData.size() % getGroupCount();
            }
            return count;
        }

        /**
         * 返回StellarMap 添加的子控件
         *
         * @param group
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getView(int group, int position, View convertView) {
            if(convertView == null){
                convertView = new TextView(UiUtils.getContext());
            }
            TextView tv = (TextView)convertView;
            //设置TextView的背景色
            Random random = new Random();
            int red = 30 + random.nextInt(191);
            int green = 30 + random.nextInt(191);
            int blue = 30 + random.nextInt(191);
            int color = Color.rgb(red, green, blue);
            tv.setTextColor(color);

            // 大小随机 16~25
            int textSize = 16 + random.nextInt(10);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);

            int currentPosition = 0;
            for (int i = 0; i < group; i++) {
                currentPosition += getCount(i);
            }
            currentPosition += position;
            tv.setText(listData.get(currentPosition));
            //设置该TextView的点击事件
            //要给TextView的监听中展示TextView的文本，将该文本绑定到tag，更具tag获取Tv，在获取tv的文本
            tv.setTag("title");
            tv.setOnClickListener(listener);
            return tv;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            //return isZoomIn?(++group>getGroupCount()-1?0:group):(--group<0?0:getGroupCount()-1);
            if (isZoomIn) {
                group++;
                if (group == getGroupCount()) {
                    group = 0;
                }
            } else {
                group--;
                if (group < 0) {
                    group = getGroupCount() - 1;
                }
            }
            return group;
        }
    }

    /**
     * 每个TextView的点击事件，实现点击应用名字进行相应操作
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v.findViewWithTag("title");
            CharSequence text = tv.getText();
            Toast.makeText(UiUtils.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    };
}
