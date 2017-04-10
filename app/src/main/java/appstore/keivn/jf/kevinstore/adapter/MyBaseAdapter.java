package appstore.keivn.jf.kevinstore.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.holder.BaseHolder;
import appstore.keivn.jf.kevinstore.holder.LoadHolder;
import appstore.keivn.jf.kevinstore.manager.ThreadPoolManager;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/3.
 * <p/>
 * adapter 的抽取操作，涉及getView方法中的各种共同操作的抽取
 * getView 共有操作
 * 1、填充布局：convertView
 * 2、初始化控件 convertView，ViewHolder
 * 3、设置tag，将ViewHolder设置给convertView  convertView，ViewHolder
 * 4、设置数据，数据类型，ViewHolder。
 * 抽取以上数据 到一个类（BaseHolder）中
 * <p/>
 * ListView条目由一个集合或者数组确定，每个项目中的数据由集合中的数据确定，该集合中数据类型不一定可能是对象，可能是基本数据
 * <p/>
 * 给listView添加上拉头，原理：判断上拉中当前页中最后一个项目索引是否是总项目个数的最后一个，如果是就需要加载一个
 * ”正在加载“ 脚的项目。
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    //需要上啦刷新 的项目类型
    public final int ITEM_NOMAL = 0;
    public final int ITEM_LOAD = 1;
    public final int ITEM_TITLE = 2;
    //上拉加载中
    public static final int LOADING = 0;
    //上拉加载失败
    public static final int LOADERROR = 1;
    //上拉加载成功(有更多数据)
    public static final int LOADSUCCESS = 2;
    //上拉加载成功,没有数据了
    public static final int LOADNODATA = 3;
    //上拉加载成功,没有数据了
    public static final int LOADHIDE = 4;
    //当前加载状态
    private int currentLoadState = LOADING;


    //上拉加载失败
    public static final int ERROR = 0;
    //上拉加载成功(有更多数据)
    public static final int HAS_MORE = 1;
    //上拉加载成功(没有更多数据)
    public static final int NO_MORE = 2;
    private boolean isLoading = false;

    private OnLoadmoreListener listener;

    private LoadHolder loadHolder;

    ArrayList<T> dataList;

    public MyBaseAdapter(ArrayList<T> dataList) {
        super();
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * getView的抽取：复用缓存
     * <p/>
     * 给ListView添加上拉加载  就是给listview多加一个比实际数据多的一行
     * 如果是最后一项 convertView就是load 的控件
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if (convertView == null) {
            //初始化该抽取Holder已将布局填充，控件初始化，存储Holder的操作完成。
            //此处填充的Holder不仅相同，如果填充BaseHolder，就固定死了。所以根据子类Adapter填充所需要的Holder
            //convertView = UiUtils.inflateView(R.layout.layout_home_item);
            //vh.textView = (TextView) convertView.findViewById(R.id.tv_home_lvitem);
            //convertView.setTag(vh);

            if (getItemViewType(position) != ITEM_LOAD) {
                holder = getHolder(position);
            } else {
                //严谨判断，加载数据中，如果该对象存在就不在创建了
                if (loadHolder == null) {
                    loadHolder = new LoadHolder();
                    //初始化状态
                    loadHolder.setData(listener != null ? (hasMore() ? HAS_MORE : NO_MORE) : LOADHIDE);
                    //加载失败，重新加载的点击事件
                    loadHolder.convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //如果LoadHolder 状态为错误，可以点击
                            if (loadHolder.data == ERROR) {
                                loadHolder.setData(HAS_MORE);
                                loadMore();
                            }
                        }
                    });
                }
                holder = loadHolder;
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        //vh.textView.setText(temp.get(position));
        //设置数据从Holder中进行了

        //加载不同的布局，设置不同的数据
        if (getItemViewType(position) != ITEM_LOAD) {
            holder.setData(dataList.get(position));
        } else {
            //如果当前条目状态为加载，并且加载状态为失败
            // if (currentLoadState != LOADING && getItemViewType(position) == ITEM_LOAD) {
            //    currentLoadState = LOADING;
            //   holder.setData(currentLoadState);
            //正在加载数据，这里请求网络。该操作是网络操作，所以需要在子线程中进行
            loadMore();
            //}
        }
        //此时返回的View是Holder获取到布局文件的convertView。
        return holder.convertView;
    }

    private void loadMore() {
        if (isLoading) {
            return;
        }
        //如果LoadHolder中 data标记为HAS_MORE，就访问网络去加载更多数据
        if (loadHolder.data == HAS_MORE) {
            isLoading = true;
            //网路访问放在子线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //获取加载到的数据
                    final ArrayList<T> moreData = moreData();
                    //一下操作需要在主线成中进行
                    UiUtils.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (moreData == null) {
                                        loadHolder.setData(ERROR);
                                    } else if (moreData.size() > 0) {
                                        loadHolder.setData(HAS_MORE);
                                        dataList.addAll(moreData);
                                        notifyDataSetChanged();
                                    } else {
                                        loadHolder.setData(NO_MORE);
                                    }
                                }
                            }
                    );
                    isLoading = false;
                }
            }).start();

            /*ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //获取加载到的数据
                    final ArrayList<T> moreData = moreData();
                    //一下操作需要在主线成中进行
                    UiUtils.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (moreData == null) {
                                        loadHolder.setData(ERROR);
                                    } else if (moreData.size() > 0) {
                                        loadHolder.setData(HAS_MORE);
                                        dataList.addAll(moreData);
                                        notifyDataSetChanged();
                                    } else {
                                        loadHolder.setData(NO_MORE);
                                    }
                                }
                            }
                    );
                    isLoading = false;
                }
            });*/
        }

    }


    /**
     * 抽取获取更多数据的方法,自己写的
     */
    private void loadMore(boolean isMe) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ArrayList<T> listData = moreData();
                if (listData == null) {
                    //请求失败,加载数据失败
                    currentLoadState = LOADERROR;
                    //loadHolder.setData(currentLoadState);
                } else if (listData.size() == 0) {
                    //请求成功，服务器返回数据为0个
                    currentLoadState = LOADNODATA;
                    //loadHolder.setData(LOADNODATA);
                } else if (listData.size() > 0) {
                    //请求成功
                    currentLoadState = LOADSUCCESS;
                    //loadHolder.setData(LOADSUCCESS);
                    //请求成功，加载完数据，更新下数据
                    dataList.addAll(listData);
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
                //修改界面需要在主线程执行
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadHolder.setData(currentLoadState);
                    }
                });
            }
        }).start();
    }

    /**
     * 该方法返回一个Holder，该Holder由子类确定
     *
     * @return
     */
    public abstract BaseHolder getHolder(int position);

    /**
     * 获取listView 项目类型数
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    /**
     * 获取listView 项目类型
     * 如果用户传入了监听刷新更多的监听器，就需要上啦加载操作，否则不需要。
     */
    @Override
    public int getItemViewType(int position) {
        //return (listener!=null && position == getCount() - 1) ? ITEM_LOAD : ITEM_NOMAL;
        return (position == getCount() - 1) ? ITEM_LOAD : getInnerType(position);
    }

    /**
     * 子类实现，进一步判断子类需要在listView中展示的条目类型。
     * @return
     */
    protected int getInnerType(int position) {
        return ITEM_NOMAL;
    }


    /**
     * 提供判断是否有更多数据，默认有
     */
    public boolean hasMore() {
        return true;
    }


    //子类必须实现的访问网络的方法，因为适配器加载到最后一条时候，需要请求网络
    protected ArrayList<T> moreData() {
        if (listener != null) {
            return listener.loadMore();
        }
        return null;
    }

    /**
     * 自定义加载更多数据的接口，给adapter使用
     */
    public interface OnLoadmoreListener<T> {
        public ArrayList<T> loadMore();
    }

    /**
     * 对外提供加载刷新数据的接口
     *
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadmoreListener listener) {
        this.listener = listener;
    }


    public int getListSize() {
        return dataList.size();
    }
}
