package appstore.keivn.jf.kevinstore.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;

/**
 * Created by Kevin on 2016/3/5.
 * 自定义图片载体，用于图片按比例显示，动态设置图片控件在布局中的大小，作用：图片展示在载体，显示全部，不变形
 * 给该控件顶一个自定义属性，该属性为原图片的宽高比，展示图片时动态的按照宽高比，设置控件的高度。
 * 接收自定义控件的自定义属性，li用该属性计算高度，设置控件显示高度。
 *
 * 自定义属性：attrs 添加自定义值。使用的自定义控件的布局添加命名空间，在自定义控件类中调用自定义属性
 *
 * 自定义控件尺寸
 * 步骤：1、OnMeasure 中利用利用MeasureSpec获取 宽高 spec对象， 从中获取 尺寸，模式
 *      2、利用MeasureSpec.makeMeasureSpec(),传递尺寸，获取新的尺寸值
 *      3、注意控件属性 padding，宽高属性对此操纵的影响。
 */
public class RatioLayout extends RelativeLayout {

    private float v;

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //float value = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res-auto", "child_ratio", 0);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        v = typedArray.getFloat(R.styleable.RatioLayout_child_ratio, 0);
        //测量控件，给控件设置宽高
        typedArray.recycle();
    }

    /**
     * 测量尺寸，具体测量高度，传地方时可以参考源代码。  思路：在onMeasure中重新给控件设置高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取控件宽度。
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //获取宽高模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //根据比例计算高度
        //如果用户设施 Padding，该高度不变，子控件宽度缩小。计算时宽度还是原宽度，此时计算宽度需要减去padding 左右的值
        //获取padding左右值
        int left = getPaddingLeft();
        int right = getPaddingRight();

        //抛去用户设置padding左右值，获取真是宽度
        width = width - left - right;
        //重新计算高度
        int height = (int) (width / v);
        //如果设置了padding上下值，最后计算的高度是父控件的高度，图片的高度实际是要减去padding的
        height = height + getPaddingTop() + getPaddingBottom();
        //给控件手动设置高度（尺寸）(设置测量尺寸),其中有子控件，无法调用
//        setMeasuredDimension(width,height);
        //重写heightMeasureSpec,因为是手动设置高度宽度，所以这里参数为 Exactly
        //只有宽度不变，高度可变，并且传来的比例不能小于等于0，才能操作
        if(v>0 && widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }
        //将给父控件处理



        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //手动画布局


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
