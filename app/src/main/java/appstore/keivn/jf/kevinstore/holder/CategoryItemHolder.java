package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.CategoryList;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/7.
 */
public class CategoryItemHolder extends BaseHolder<CategoryList> {

    private LinearLayout llItem1;
    private LinearLayout llItem2;
    private LinearLayout llItem3;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private TextView text1;
    private TextView text2;
    private TextView text3;

    @Override
    public View initView() {
        View itemView = UiUtils.inflateView(R.layout.layout_category_item);
        llItem1 = (LinearLayout) itemView.findViewById(R.id.llItem1);
        llItem2 = (LinearLayout) itemView.findViewById(R.id.llItem2);
        llItem3 = (LinearLayout) itemView.findViewById(R.id.llItem3);
        /*llItem1.setVisibility(View.GONE);
        llItem2.setVisibility(View.GONE);
        llItem3.setVisibility(View.GONE);*/

        image1 = (ImageView) itemView.findViewById(R.id.image1);
        image2 = (ImageView) itemView.findViewById(R.id.image2);
        image3 = (ImageView) itemView.findViewById(R.id.image3);

        text1 = (TextView) itemView.findViewById(R.id.text1);
        text2 = (TextView) itemView.findViewById(R.id.text2);
        text3 = (TextView) itemView.findViewById(R.id.text3);
        return itemView;
    }

    @Override
    public void initData() {
        BitmapSingle.getInstance().display(image1, HttpHelper.URL+"image?name="+data.url1);
        BitmapSingle.getInstance().display(image2, HttpHelper.URL+"image?name="+data.url2);
        BitmapSingle.getInstance().display(image3, HttpHelper.URL+"image?name="+data.url3);

        text1.setText(data.name1);
        text2.setText(data.name2);
        text3.setText(data.name3);
        for (int i = 0; i < 3; i++) {
            if(data.name1 == ""){

            }
        }
    }
}
