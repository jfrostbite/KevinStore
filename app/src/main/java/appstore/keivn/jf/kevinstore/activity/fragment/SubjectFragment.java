package appstore.keivn.jf.kevinstore.activity.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.adapter.MyBaseAdapter;
import appstore.keivn.jf.kevinstore.adapter.SubjectAdapter;
import appstore.keivn.jf.kevinstore.bean.SubjectList;
import appstore.keivn.jf.kevinstore.http.httputils.SubjectHttpRequest;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.Loadingpage;
import appstore.keivn.jf.kevinstore.ui.widget.MyListView;

/**
 * Created by Kevin on 2016/3/3.
 * 专题页面，
 * 需求：解决图片拉伸，裁剪的问题。
 * 原理：按照宽高比例进行展示。
 *         需要自定义一个控件来包裹图片。然后在该控件中设置宽高比
 */
public class SubjectFragment extends BaseFragment<SubjectList.SubjectInfo>{

    private SubjectAdapter adapter;

    @Override
    public Loadingpage.StateCode fragmentRequestDate() {
        SubjectHttpRequest subjectHttp = new SubjectHttpRequest();
        SubjectList subjectList = subjectHttp.getData();
        if(subjectList!=null){
            listData = subjectList.list;
        }

        return getStateCode(listData);
    }

    @Override
    public View fragmentCreateSuccessPage() {
        /*TextView textView = new TextView(UiUtils.getContext());
        textView.setText("专题页数据");*/
        MyListView listView = new MyListView(UiUtils.getContext());
        adapter = new SubjectAdapter(listData);
        adapter.setOnLoadMoreListener(new MyBaseAdapter.OnLoadmoreListener<SubjectList.SubjectInfo>() {
            @Override
            public ArrayList<SubjectList.SubjectInfo> loadMore() {
                return moreData();
            }
        });
        //设置加载更多
        listView.setAdapter(adapter);
        return listView;
    }

    /**
     * 加载更多数据
     * @return
     */
    private ArrayList<SubjectList.SubjectInfo> moreData() {
        ArrayList<SubjectList.SubjectInfo> moreList = new ArrayList<SubjectList.SubjectInfo>();
        SubjectHttpRequest subHttp = new SubjectHttpRequest();
        subHttp.setIndex(getListSize());
        SubjectList subjectList = subHttp.getData();
        if(subjectList!=null){
            return subjectList.list;
        }
        return null;
    }
}
