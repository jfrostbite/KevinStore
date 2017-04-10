package appstore.keivn.jf.kevinstore.activity.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import appstore.keivn.jf.kevinstore.http.httputils.HotHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.FlowLayout;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;

/**
 * Created by Kevin on 2016/3/3.
 * 排行
 * 利用自定义控件，加载文本控件。
 */
public class HotFragment extends BaseFragment<String> {


    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        HotHttpRequest hotHttp = new HotHttpRequest();
        listData = hotHttp.getData();
        return getStateCode(listData);
    }

    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("排行榜页面数据");*/
        ScrollView sv = new ScrollView(UiUtils.getContext());
        FlowLayout fl = new FlowLayout(UiUtils.getContext());
        int padding = UiUtils.dip2px(10);
        sv.setPadding(padding,padding,padding,padding);


        for (int i = 0; i < listData.size(); i++) {
            TextView tv = new TextView(UiUtils.getContext());
            // 颜色的随机 30~220
            Random random = new Random();
            int red = 30 + random.nextInt(191);
            int green = 30 + random.nextInt(191);
            int blue = 30 + random.nextInt(191);
            int color = Color.rgb(red, green, blue);
            // tv.setBackgroundColor(color);

            GradientDrawable bg = UiUtils.getGradientDrawable(
                    UiUtils.dip2px(5), color);
            GradientDrawable pressedDrawable = UiUtils.getGradientDrawable(
                    UiUtils.dip2px(5), Color.rgb(200, 200, 200));

            StateListDrawable selector = UiUtils.getSelector(pressedDrawable, bg);

            tv.setBackgroundDrawable(selector);
            tv.setTextColor(Color.WHITE);
            tv.setText(listData.get(i));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTag(listData.get(i));
            tv.setOnClickListener(mClickListener);
            fl.addView(tv);
        }
        sv.addView(fl);
        return sv;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String keyword = (String) v.getTag();
            Toast.makeText(UiUtils.getContext(), keyword, Toast.LENGTH_SHORT).show();
        }
    };
}
