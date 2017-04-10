package appstore.keivn.jf.kevinstore.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;

/**
 * Created by Kevin on 2016/3/3.
 * 每个ViewPager需要添加的Fragment都有共同需要展示的页面，所以抽取基类，该基类继承自Fragment
 *
 * Fragment初始化时要填充布局界面，这个布局界面中有每个页签对应的多个布局，所以需要填充帧布局
 * 该布局中有很多界面控件，所以自定义一个帧布局。在其中进行不同界面的填充。
 *
 * 当需要给已知布局中填充很多组件，布局套布局时，可以自定义该已知布局。在已知布局中用代码实现
 * 总之就是用代码写一个布局，不是用xml写布局
 *
 * 通过position填充的还是子类，只不过抽取了子类共有的内容
 *
 * 访问网络：思路：主界面切换ViewPager 访问网络 该操作有添加的Fragment完成，返回的状态需要反给LoadingPager
 * 用于判断展示不同界面
 * LoadingPager需要访问网络返回不同状态码，该操作由添加的Fragment完成。
 * LoadingPager访问网络的操作传递给BaseFragment,BaseFragment访问网络的操作由子类实现。故抽象
 *
 */
public abstract class BaseFragment<T> extends Fragment{

    private Loadingpage loadingpage;
    public ArrayList<T> listData;
    /**
     * 给各个Fragment填充布局文件，此布局文件有代码实现，所以直接new即可
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingpage = new Loadingpage(UiUtils.getContext()) {

            //实现返回成功页面的方法，该成功页由于在不同的页签中显示的不同数据，所以此页面布局有子类完成
            @Override
            public View createSuccessPage() {
                return fragmentCreateSuccessPage();
            }
            //复写 Loading的访问网络方法，BaseFragment不知子类如何实现，将给子类去做
            @Override
            public StateCode requestData() {
                return fragmentRequestDate();
            }
        };
        return loadingpage;
    }

    public abstract Loadingpage.StateCode fragmentRequestDate();

    /**
     * 需要子类实现的访问网络的方法
     * @return
     */
    public abstract View fragmentCreateSuccessPage();

    /**
     * 对外提供LoadingPager 中访问网络的方式
     */
    public void onLoad(){
        if(loadingpage!=null){
            loadingpage.getData();
        }
    }

    public Loadingpage.StateCode getStateCode(ArrayList<T> listData) {
        if(listData!=null){
            //判断数据是否是list集合
            if(listData instanceof ArrayList){
                if(listData.size()>0) {
                    return Loadingpage.StateCode.SUCCESS;
                }else{
                    return Loadingpage.StateCode.EMPTY;
                }
            }
        }else {
            return Loadingpage.StateCode.ERROR;
        }
        return null;
    }

    public int getListSize(){
        return listData.size();
    }
}
