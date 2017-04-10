package appstore.keivn.jf.kevinstore.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Kevin on 2016/3/5.
 *
 * 自定义ListView
 * 需求：屏蔽缓存状态,屏蔽分割线，屏蔽状态选择器
 */
public class MyListView extends ListView {

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //屏蔽缓存背景
        setCacheColorHint(Color.TRANSPARENT);
        //屏蔽分割线
        setDivider(null);
        //屏蔽背景点击
        setSelector(new ColorDrawable());
    }
}
