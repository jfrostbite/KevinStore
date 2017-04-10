package appstore.keivn.jf.kevinstore.adapter;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.CategoryList;
import appstore.keivn.jf.kevinstore.holder.BaseHolder;
import appstore.keivn.jf.kevinstore.holder.CategoryItemHolder;
import appstore.keivn.jf.kevinstore.holder.CategoryTitleHolder;

/**
 * Created by Kevin on 2016/3/5.
 * 分类界面的适配器，该适配器需要给父类返回2中布局类型，供ListView 项目使用。
 */
public class CategoryAdapter extends MyBaseAdapter<CategoryList> {

    public CategoryAdapter(ArrayList<CategoryList> dataList) {
        super(dataList);
    }

    @Override
    public BaseHolder getHolder(int postion) {
        if(dataList.get(postion).isTitle){
            return new CategoryTitleHolder();
        }else{
            return new CategoryItemHolder();
        }
    }

    /**
     * 获取listView 项目类型数
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    protected int getInnerType(int position) {
        if(dataList.get(position).isTitle){
            return ITEM_TITLE;
        }else{
            return ITEM_NOMAL;
        }
    }
}
