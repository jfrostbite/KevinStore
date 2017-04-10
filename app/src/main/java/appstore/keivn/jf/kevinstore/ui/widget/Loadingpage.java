package appstore.keivn.jf.kevinstore.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.manager.ThreadPoolManager;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 * 该界面就是填充给Fragment,间接填充到ViewPager的页面
 * 因为ViewPager的FragmentPagerAdapter需要Fragment，Fragment又需要布局文件填充，从而产生了该自定义布局类
 * <p/>
 * 控制界面的显示隐藏（设置标识状态：页面状态是错误还是加载）
 * <p/>
 * 成功页面的加载：加载页面布局只有ViewPager每一页面的Fragment知道怎么加载成功页面，所以，该页面的展示就是抽象方法
 * 该页面的加载应该在获取到网络数据后再加载，所以不应在页面一创建就加载，此时不知道怎么加载布局，没有数据。
 * <p/>
 * 要想获取页面状态，需要请求网络。请求网络的操作是在程序打开加载首页或者切换完毕页签ViewPager后的操作
 * 所以请求网络是在页面一创建就开始的，页面创建是在适配器中的getView方法中进行的。该方法中就是加载Fragment
 * Fragment加载又是创建LoadingPage实现的，所以请求网络的操作还是在自定义的帧布局（LoadingPage）也就是该界面
 * 中进行的。而该自定义控件在初始化时不知道请求的是那个页签的网络，所以该实现方法需要自实现类实现，实际上又是Fragment
 * 的子类实现的。
 *
 * 没改变一次状态就要刷新下页面
 */
public abstract class Loadingpage extends FrameLayout {

    //页面的状态
    public static final int PAGE_NONE = 0;
    public static final int PAGE_LOADING = 1;
    public static final int PAGE_SUCCESS = 2;
    public static final int PAGE_ERROR = 3;
    public static final int PAGE_EMPTY = 4;

    //当前页面状态
    private int currentPage = PAGE_NONE;
    private View errorPage;
    private View loadingPage;
    private View emptyPage;
    private View successPage;

    public Loadingpage(Context context) {
        super(context);
        init();
    }

    /**
     * 给自定义RrameLayout填充各种布局
     * 由于填充操作频繁，故抽取到工具类中进行。
     */
    private void init() {
        //View view = View.inflate(UiUtils.getContext(), R.layout.activity_main,null);  抽取到工具类
        //创建网络访问失败显示的错误页
        errorPage = createErrorPage();
        addView(errorPage);
        //创建网络访问失败显示的错误页
        loadingPage = createLoadingPage();
        addView(loadingPage);
        //创建网络访问失败显示的错误页
        emptyPage = createEmptyPage();
        addView(emptyPage);

        //先请求网络，然后根据页面状态加载不同的布局界面
        //getData();
        //展示正确的页面，根据当前页面状态
        showRightPage();

    }

    /**
     * 请求网络的方法，该方法返回状态码，利用枚举实现返回状态码，防止返回其他状态无法识别的码
     * 请求网络需要在子线程中进行
     */
    public void getData() {
        //如果正在加载数据，就返回
        if (currentPage == PAGE_LOADING) {
            return;
        }
        //改变状态为none
        currentPage = PAGE_NONE;
        showRightPage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentPage = PAGE_LOADING;
                try {
                    new Thread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StateCode statecode = requestData();
                if (statecode != null) {
                    currentPage = statecode.code;
                    //子线程操作UI，需要在主线成中进行
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRightPage();

                        }
                    });
                }
            }
        }).start();
        /*ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                currentPage = PAGE_LOADING;
                try {
                    new Thread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StateCode statecode = requestData();
                if (statecode != null) {
                    currentPage = statecode.code;
                    //子线程操作UI，需要在主线成中进行
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showRightPage();

                        }
                    });
                }
            }
        });*/
    }

    /**
     * 请求子类实现的请求网络的方法。
     */
    public abstract StateCode requestData();

    /**
     * 根据页面标识展示正确的页面
     */
    private void showRightPage() {
        /*if(currentPage==PAGE_NONE || currentPage == PAGE_LOADING){
            loadingPage.setVisibility(View.VISIBLE);
            errorPage.setVisibility(View.GONE);
            emptyPage.setVisibility(View.GONE);
        }else if(currentPage == PAGE_ERROR){
            loadingPage.setVisibility(View.GONE);
            errorPage.setVisibility(View.VISIBLE);
            emptyPage.setVisibility(View.GONE);
        }else if(currentPage == PAGE_EMPTY){
            loadingPage.setVisibility(View.GONE);
            errorPage.setVisibility(View.GONE);
            emptyPage.setVisibility(View.VISIBLE);
        }*/
        //简写：三元表达式
        loadingPage.setVisibility(currentPage == PAGE_NONE || currentPage == PAGE_LOADING ? View.VISIBLE : View.GONE);
        errorPage.setVisibility(currentPage == PAGE_ERROR ? View.VISIBLE : View.GONE);
        emptyPage.setVisibility(currentPage == PAGE_EMPTY ? View.VISIBLE : View.GONE);
        //加载成功页面：需要进行判断，如果成功页面为空，且当前获取数据的状态为SUCCESS那就创建成功页面并且添加到Fragment

        if (successPage == null && currentPage == PAGE_SUCCESS) {
            successPage = createSuccessPage();
            //这里successPage可能为空，
            if (successPage != null) {
                addView(successPage);
            }
        }
        //如果成功页面不为空，就展示或隐藏
        if (successPage != null) {
            successPage.setVisibility(currentPage == PAGE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 该加载成功页面的布局子实现类中完成，所以这里抽象
     *
     * @return
     */
    public abstract View createSuccessPage();

    /**
     * 错误页
     * <p/>
     * 加载失败请重试按钮的实现
     *
     * @return
     */
    private View createErrorPage() {
        View errorView = UiUtils.inflateView(R.layout.layout_error);
        Button btnRetry = (Button) errorView.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新加载布局
                //showRightPage();
                getData();
            }
        });
        return errorView;
    }

    /**
     * 加载页
     *
     * @return
     */
    private View createLoadingPage() {
        return UiUtils.inflateView(R.layout.layout_loading);
    }

    /**
     * 空页面
     *
     * @return
     */
    private View createEmptyPage() {
        return UiUtils.inflateView(R.layout.layout_empty);
    }

    /**
     * 枚举：里面创建自己的对象，有几个就返回几个对象
     */
    public static enum StateCode {
        LOADING(PAGE_LOADING), SUCCESS(PAGE_SUCCESS), EMPTY(PAGE_EMPTY), ERROR(PAGE_ERROR);
        public int code;

        private StateCode(int code) {
            this.code = code;
        }
    }


}
