package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.SubjectList;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/5.
 * 给专题界面的ListView添加条目用的布局文件。
 */
public class SubjectHolder extends BaseHolder<SubjectList.SubjectInfo>{

    private ImageView ivSubItem;
    private TextView tvSubItem;

    @Override
    public View initView() {
        /*//创建条目载体
        LinearLayout linearLayout = new LinearLayout(UiUtils.getContext());
        //设置布局
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //创建ImageView夹在图片
        imageView = new ImageView(UiUtils.getContext());
        //创建文本显示区域
        textView = new TextView(UiUtils.getContext());
        linearLayout.addView(imageView);
        //linearLayout.addView(textView);*/
        View subView = UiUtils.inflateView(R.layout.layout_subject_item);
        ivSubItem = (ImageView)subView.findViewById(R.id.iv_subject_item);
        tvSubItem = (TextView)subView.findViewById(R.id.tv_subject_des);
        return subView;
    }

    @Override
    public void initData() {
        BitmapUtils bitmapUtils = BitmapSingle.getInstance();
        String imageUrl = HttpHelper.URL + "image?name=" + data.url;
        bitmapUtils.display(ivSubItem, imageUrl);
        tvSubItem.setText(data.des);
    }
}
