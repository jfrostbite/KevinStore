package appstore.keivn.jf.kevinstore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.holder.BaseHolder;
import appstore.keivn.jf.kevinstore.holder.SubjectHolder;

/**
 * Created by Kevin on 2016/3/5.
 */
public class SubjectAdapter extends MyBaseAdapter {

    public SubjectAdapter(ArrayList dataList) {
        super(dataList);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SubjectHolder();
    }
}
