package appstore.keivn.jf.kevinstore.ui.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/6.
 * 需求：在自定义控件中填充行布局，行中填充添加进去的TextView，判断Tv宽度，如果超出自定义宽度，就另起一行布局。
 * 思路：自定义控件的尺寸测量、布局其中填充的控件在行所占尺寸，确定行数，确定整体宽高
 * 设定布局：行的位置。
 * 创建行对象，该对象中有封装传入控件，
 */
public class MyFlowLayout extends ViewGroup {

    private ArrayList<View> mChildViews;
    private Line mNewLine;
    private ArrayList<Line> mLineList = new ArrayList<Line>();
    private static final int LINE_SPACE = UiUtils.dip2px(10);

    public MyFlowLayout(Context context) {
        super(context);
        mChildViews = new ArrayList<View>();
    }

    //添加控件,将添加的所有view存放在一个list集合中
    public void addView(View view) {
        mChildViews.add(view);
    }

    /**
     * 该控件测量时，进行view的操作，给view设置尺寸。
     * 思路：获取该控件的宽度，获取传入的view的宽度，累加view宽度，如果超过该控件，就重新建一行，继续添加
     * 最后获得viewLine 高度，获取本控件高度。
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取该控件尺寸
        //获取宽模式，尺寸
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int mWidthMode = MeasureSpec.getMode(widthMeasureSpec);

        //获取高模式尺寸。
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int mHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //初始化行宽度
        int lineChildWidth = 0;

        //遍历所有传入控件的宽度，判断此宽度与该控件宽度大小
        for (int i = 0; i < mChildViews.size(); i++) {


            //获取传入控件尺寸，利用measure进行。
            View mChild = mChildViews.get(i);
            //参数1，宽高spec,根据模式指定size（例如：AT_MOST，size就是最大的宽度尺寸，即：该自定义控件的宽度）
            int viewWidthSpec = MeasureSpec.makeMeasureSpec(mWidthSize, MeasureSpec.AT_MOST);
            //高度无限制，size为0.
            int viewHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            //对该控件，进行测量，参数1，2：宽高spec
            mChild.measure(viewWidthSpec, viewHeightSpec);

            //以上操作为了获取传入控件的尺寸
            //获取测量得到的view的宽度，高度
            int mChildWidth = mChild.getMeasuredWidth();
            int mChildHeight = mChild.getMeasuredHeight();
            //累加宽度，如果累加的宽度大于了控件宽度，就新建一行
            lineChildWidth += mChildWidth + LINE_SPACE;
            if (lineChildWidth < mWidthSize) {
                //如果累加传入控件宽度小于本控件宽度，就添加到本行
                if(mNewLine==null){
                    //TODO 上次操作的文职
                    //mNewLine = newLine();
                }
                mNewLine.add(mChild);

                //添加一次，指定下被添加子控件的位置
                //mChild.layout(lineChildWidth,0,mChildWidth,mChildHeight);
            } else {
                //新的一行，重置每一行添加的child的总宽度。
                lineChildWidth = 0;
                //上一行排满了，就将上一行保存进List集合中
                mLineList.add(mNewLine);
                //否则新建一行，进行添加
                mNewLine = newLine(mChild);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Paint paint = new Paint();
        paint.getTextBounds("",0,1,new Rect());
    }

    /**
     * 创建新的一行。
     */
    private Line newLine(View child) {
        Line mLine = new Line();
        mLine.add(child);
        return mLine;
    }

    /**
     * 控件测量完毕，进行布局
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*for (int i = 0; i <; i++) {

        }*/
    }

    class Line {
        //一行包含的view个数
        ArrayList<View> mLineChild = new ArrayList<View>();
        public void add(View view) {
            mLineChild.add(view);
        }
        /**
         * 给每一行的child进行布局
         */
        public void setLayout(){
            int childLeft = 0;
            //遍历一行中的子控件个数
            for (int i = 0; i < mLineChild.size(); i++) {
                //获取单个子控件
                View mChild = mLineChild.get(i);
                //获取单个子控件的位置
                /*mChild.layout();
                childLeft = mChild.get*/
            }
        }
    }
}
