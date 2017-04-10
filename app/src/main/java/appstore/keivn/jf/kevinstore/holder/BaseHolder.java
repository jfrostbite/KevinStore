package appstore.keivn.jf.kevinstore.holder;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 * getView方法中抽取的相同操作 数据。
 *
 * 1、填充布局：convertView
 * 2、初始化控件 convertView，ViewHolder
 * 3、设置tag，将ViewHolder设置给convertView  convertView，ViewHolder
 * 4、设置数据，数据类型，ViewHolder。
 *
 * 方法中操作的抽取：将所以涉及操作，变量封装到一个Holder类中，
 * 在该类中进行方法中的操作，相同操作直接进行，不同操作抽取抽象类，给实现类实现，
 * 然后getView中获取该Holder对象，进行对象调用方法的操作。
 *
 *
 */
public abstract class BaseHolder<T> {
    public T data;
    //getView中需要的变量抽取
    public View convertView;
    //该控件在被填充布局中也是未知的。当获取到布局对象，该控件就得到了
    //public TextView tv;

    public BaseHolder(){
        convertView = initView();
        convertView.setTag(this);
    }
    //getView中对变量操作的抽取  ，该操作在对象一出事就执行，此处在各个界面填充的布局文件不相同。所以抽取
    /*public void initView() {
        convertView = UiUtils.inflateView(R.layout.layout_home_item);
        tv = (TextView) convertView.findViewById(R.id.tv_home_lvitem);
        convertView.setTag(this);
    }*/
    //需要返回convertView
    public abstract View initView();

    //需要给TextView设置数据，该数据怎么获取？对外提供方法，设置该数据
    //此处就是给ListVIew项目添加的数据，有时候是个对象，给项目设置很多值
    public void setData(T data){
        this.data = data;
        initData();
    }

    //相当于getView中的给控件填充数据的操作 vh.tvTitle.setText("测试数据");
    public abstract void initData();
    /*{
        //需要对控件进行数据操作，该数据可通过构造函数传入
        tv.setText(text);
    }*/
}
