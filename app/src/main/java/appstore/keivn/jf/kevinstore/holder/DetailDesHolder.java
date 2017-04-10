package appstore.keivn.jf.kevinstore.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/8.
 * 详情页的描述信息
 */
public class DetailDesHolder extends BaseHolder<AppList.AppInfo> {

    private ScrollView svDetail;
    private TextView tvlDes;
    private RelativeLayout rlBottom;
    private ImageView arrow;
    //展开收缩标记
    private boolean isHide;

    public DetailDesHolder(ScrollView svDetail) {
        this.svDetail = svDetail;
    }

    @Override
    public View initView() {
        View desView = UiUtils.inflateView(R.layout.layout_detail_des);
        tvlDes = (TextView) desView.findViewById(R.id.des);
        rlBottom = (RelativeLayout) desView.findViewById(R.id.rlBottom);

        arrow = (ImageView) desView.findViewById(R.id.arrow);
        return desView;
    }

    @Override
    public void initData() {
        tvlDes.setText(data.des);
        //设置控件默认隐藏一半
        ViewGroup.LayoutParams layoutParams = tvlDes.getLayoutParams();
        layoutParams.height = get7LineHeight();
        tvlDes.setLayoutParams(layoutParams);
        rlBottom.setOnClickListener(new RlBottomListener());
    }

    class RlBottomListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isHide) {
                //如果是隐藏就显示
                isHide = false;
                hideView();
            } else {
                isHide = true;
                showView();
            }
        }

    }

    /**
     * 展示控件
     */
    private void showView() {
        int height = getMaxHeight(tvlDes);
        ObjectAnimator oa = ObjectAnimator.ofInt(this, "slowSlide", get7LineHeight(), height);
        oa.setDuration(500);
        oa.addListener(listener);
        oa.start();

    }

    /**
     * 隐藏控件
     */
    private void hideView() {
        int height = getMaxHeight(tvlDes);
        ObjectAnimator oa = ObjectAnimator.ofInt(this, "slowSlide", height, get7LineHeight());
        oa.setDuration(500);
        oa.addListener(listener);
        oa.start();

    }

    /**
     * 属性动画监听
     */
    Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        /**
         * 动画结束时
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animator animation) {
            svDetail.fullScroll(ScrollView.FOCUS_DOWN);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * 该组件中的组件缓慢滑动效果
     */
    public void setSlowSlide(int height) {
        ViewGroup.LayoutParams layoutParams = tvlDes.getLayoutParams();
        layoutParams.height = height;
        tvlDes.setLayoutParams(layoutParams);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getDisplayWidth() {
        //获取系统管理系
        WindowManager wm = (WindowManager) UiUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取指定控件数的高度 例如：获取7行TextView的高度
     * 1、测量TextView，闲置Tv宽度为固定，然后获得每个Tv高度，*7
     */
    private int get7LineHeight() {
        TextView tv = new TextView(UiUtils.getContext());
        tv.setLines(7);
        int measureWidthSpec = View.MeasureSpec.makeMeasureSpec(getDisplayWidth(), View.MeasureSpec.EXACTLY);
        int measureHeightSpec = View.MeasureSpec.makeMeasureSpec(getMaxHeight(tv), View.MeasureSpec.EXACTLY);
        //测量
        tv.measure(measureWidthSpec, measureHeightSpec);
        int height = tv.getMeasuredHeight();
        return height;
    }


    /**
     * 获取指定控件高度
     */
    private int getMaxHeight(View v) {
        int width = getDisplayWidth();

        //获取测量指定控件尺寸所需要的Spec
        int measureWidthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        //测量
        v.measure(measureWidthSpec, measureHeightSpec);
        //获取控件高度
        int height = v.getMeasuredHeight();
        return height;
    }
}
