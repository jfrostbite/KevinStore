package appstore.keivn.jf.kevinstore.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 * <p/>
 * 技巧：界面中需要初始化的组件重复性很高，可以用数组来保存该组件的变量名字，然后再初始化时，遍历数组即可方便的
 * 初始化组件，前提：命名
 * <p/>
 * 标签页的展开隐藏可以利用设置高度的方式进行。，此时可以实现动画效果
 */
public class DetailSafeHolder extends BaseHolder<AppList.AppInfo> {

    private ImageView[] ivs;
    private ImageView[] ivDes;
    private TextView[] tvs;
    private LinearLayout[] lls;
    //包裹整个安全信息的载体控件
    private LinearLayout llFooter;
    //显隐安全信息的标记
    private boolean isHide = true;

    @Override
    public View initView() {
        View safeView = UiUtils.inflateView(R.layout.layout_detail_safe);

        ivs = new ImageView[4];
        ivs[0] = (ImageView) safeView.findViewById(R.id.imagesafe1);
        ivs[1] = (ImageView) safeView.findViewById(R.id.imagesafe2);
        ivs[2] = (ImageView) safeView.findViewById(R.id.imagesafe3);
        ivs[3] = (ImageView) safeView.findViewById(R.id.imagesafe4);

        ivDes = new ImageView[4];
        ivDes[0] = (ImageView) safeView.findViewById(R.id.ll1_image1);
        ivDes[1] = (ImageView) safeView.findViewById(R.id.ll2_image2);
        ivDes[2] = (ImageView) safeView.findViewById(R.id.ll3_image3);
        ivDes[3] = (ImageView) safeView.findViewById(R.id.ll4_image4);

        tvs = new TextView[4];
        tvs[0] = (TextView) safeView.findViewById(R.id.ll1_text1);
        tvs[1] = (TextView) safeView.findViewById(R.id.ll2_text2);
        tvs[2] = (TextView) safeView.findViewById(R.id.ll3_text3);
        tvs[3] = (TextView) safeView.findViewById(R.id.ll4_text4);

        lls = new LinearLayout[4];
        lls[0] = (LinearLayout) safeView.findViewById(R.id.ll1);
        lls[1] = (LinearLayout) safeView.findViewById(R.id.ll2);
        lls[2] = (LinearLayout) safeView.findViewById(R.id.ll3);
        lls[3] = (LinearLayout) safeView.findViewById(R.id.ll4);

        lls[0].setVisibility(View.GONE);
        lls[1].setVisibility(View.GONE);
        lls[2].setVisibility(View.GONE);
        lls[3].setVisibility(View.GONE);

        RelativeLayout rlTop = (RelativeLayout) safeView.findViewById(R.id.rlTop);
        /**
         * 展开条目的载体的点击事件
         */
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide) {
                    //隐藏到打开，标记到显示
                    isHide = false;
                    showSafe();
                } else {
                    //打开到隐藏
                    isHide = true;
                    hideSafe();
                }
            }
        };
        rlTop.setOnClickListener(listener);
        llFooter = (LinearLayout) safeView.findViewById(R.id.ll_footer);
        ViewGroup.LayoutParams layoutParams = llFooter.getLayoutParams();
        layoutParams.height = 0;
        llFooter.setLayoutParams(layoutParams);
        //llFooter.setVisibility(View.GONE);

        return safeView;
    }

    /**
     * 展示安全选项
     * 利用ObjectAnimator完成。简单，是ValueAnimator的子类
     */
    public void showSafe() {
        //llFooter.setVisibility(View.VISIBLE);
        int height = getHeight();
        ObjectAnimator oa = ObjectAnimator.ofInt(this,"slowSlide",0,height);
        oa.setDuration(1000);
        oa.start();
    }

    /**
     * 自定义滑动，动画
     */
    private void setSlowSlide(int height){
        ViewGroup.LayoutParams layoutParams = llFooter.getLayoutParams();
        layoutParams.height = height;
        llFooter.setLayoutParams(layoutParams);
    }

    /**
     * 隐藏安全选项
     * 原理不断的设置高度，实现，获取原来高度。利用测量获取
     */
    private void hideSafe() {
        //llFooter.setVisibility(View.GONE);
        final int height = getHeight();
        //创建属性动画。
        ValueAnimator va = ValueAnimator.ofInt(height, 0);
        //监听属性动画的监听。
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //不停的监听回掉中的高度，在设置的时间段内给一个值不停的返回指定范围
//                animation.setIntValues(height, 0);
                int animatedValue = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = llFooter.getLayoutParams();
                //自定义高度
                layoutParams.height = animatedValue;
                //将参数设给控件
                llFooter.setLayoutParams(layoutParams);
            }
        });
        //设置动画持续时间
        va.setDuration(1000);
        //int animatedValue = (int) va.getAnimatedValue();
        va.start();
    }

    /**
     * 抽取获取屏幕分辨率尺寸的方法，该方法中直接获取到了所需控件的高度，（安全控件的高度）
     * @return
     */
    private int getHeight() {
        //获取控件高度，利用测量获取，测量需要的宽慰屏幕宽度，获取屏幕宽度
        WindowManager wm = (WindowManager) UiUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int measureWidthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeigthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //测量安全板块控件的尺寸
        llFooter.measure(measureWidthSpec, measureHeigthSpec);
        //获取隐藏该控件的高度，用于在属性动画中进行隐藏
        return llFooter.getMeasuredHeight();
    }

    //自己实现类的getView中特有的操作。数据的传递就是构造函数传给父类。公用的数据
    @Override
    public void initData() {

        for (int i = 0; i < data.safe.size(); i++) {
            //添加安全信息中的安全图标
            BitmapSingle.getInstance().display(ivs[i], HttpHelper.URL + "image?name=" + data.safe.get(i).safeUrl);
            //添加安全信息的描述信息前面的对号图片
            BitmapSingle.getInstance().display(ivDes[i], HttpHelper.URL + "image?name=" + data.safe.get(i).safeDesUrl);
            //添加安全信息具体描述
            tvs[i].setText(data.safe.get(i).safeDes);
            //显示存在的安全信息条目
            lls[i].setVisibility(View.VISIBLE);
        }
    }


}
