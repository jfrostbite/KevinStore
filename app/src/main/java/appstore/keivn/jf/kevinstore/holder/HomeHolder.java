package appstore.keivn.jf.kevinstore.holder;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.lidroid.xutils.BitmapUtils;

import java.net.PortUnreachableException;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.bean.DownloadInfo;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.manager.BitmapSingle;
import appstore.keivn.jf.kevinstore.manager.DownloadManager;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.ProgressArc;

/**
 * Created by Kevin on 2016/3/3.
 * Home页的getView的操作
 */
public class HomeHolder extends BaseHolder<AppList.AppInfo> implements View.OnClickListener , DownloadManager.OnDownloadListener{

    private TextView tvHomeItemTitle;
    private TextView tvHomeItemsize;
    private ImageButton ibHomeItemDownload;
    private TextView tvHomeItemdes;
    private TextView tvHomeItemDown;
    private ImageView ivHomeItemIcon;
    private FrameLayout flHomeItemDownload;
    private RatingBar rbHomeItemStart;
    private int currentState;
    private ProgressArc pa;

    //Home页面 ListView项目填充的布局
    @Override
    public View initView() {
        View homeItem = UiUtils.inflateView(R.layout.layout_home_item);
        ivHomeItemIcon = (ImageView) homeItem.findViewById(R.id.iv_home_item_icon);
        tvHomeItemTitle = (TextView) homeItem.findViewById(R.id.tv_home_item_title);
        rbHomeItemStart = (RatingBar) homeItem.findViewById(R.id.rb_home_item_start);
        tvHomeItemsize = (TextView) homeItem.findViewById(R.id.tv_home_item_size);
        flHomeItemDownload = (FrameLayout) homeItem.findViewById(R.id.fl_home_item_download);
        tvHomeItemdes = (TextView) homeItem.findViewById(R.id.tv_home_item_des);
        tvHomeItemDown = (TextView) homeItem.findViewById(R.id.tv_home_item_download);
        flHomeItemDownload.setOnClickListener(this);
        /*ibHomeItemDownload = new ImageButton(UiUtils.getContext());
        ibHomeItemDownload.setBackgroundColor(Color.TRANSPARENT);
        ibHomeItemDownload.setImageResource(R.mipmap.ic_download);
        ibHomeItemDownload.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ibHomeItemDownload.setOnClickListener(this);
        flHomeItemDownload.addView(ibHomeItemDownload);*/

        pa = new ProgressArc(UiUtils.getContext());
        pa.setArcDiameter(UiUtils.dip2px(26));
        pa.setProgressColor(Color.BLUE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(UiUtils.dip2px(27),UiUtils.dip2px(27));
        flHomeItemDownload.addView(pa, params);
        //注册下载管理器
        DownloadManager.getInstance().registerListener(this);
        return homeItem;
    }

    //自己实现类的getView中特有的操作。数据的传递就是构造函数传给父类。公用的数据
    @Override
    public void initData() {
        tvHomeItemTitle.setText(data.name);
        String size = Formatter.formatFileSize(UiUtils.getContext(), Long.parseLong(data.size));
        tvHomeItemsize.setText(size);
        tvHomeItemdes.setText(data.des);
        rbHomeItemStart.setRating(Float.parseFloat(data.stars));
        //从网络获取图片加载到icon
        BitmapUtils bitmapUtils = BitmapSingle.getInstance();
        String uri = HttpHelper.URL + "image?name=" + data.iconUrl;
        bitmapUtils.display(ivHomeItemIcon, uri);
        //初始化 UI
        DownloadInfo downloadInfo = DownloadManager.getInstance().downloadInfoMap.get(data.id);
        if(downloadInfo==null){
            downloadInfo = DownloadInfo.createDownloadInfo(data);
        }

        updateUI(downloadInfo.currentState);
    }


    /**
     * 点击下载按钮的点击事件
     * 需要判断当前状态，是下载还是暂停，还是取消，还是安装
     * 思路：
     * 1、获取该应用在下载管理器中的下载信息
     * 2、判断该应用的下载状态
     * @param v
     */
    @Override
    public void onClick(View v) {
        //获取该应用的下载信息
        DownloadInfo downloadInfo = DownloadManager.getInstance().downloadInfoMap.get(data.id);
        if(downloadInfo==null){
            currentState = DownloadManager.DOWNLOAD_NORMAL;
        }else{
            currentState = downloadInfo.currentState;
        }
        //利用该标记确定具体是下载还是暂停，还是取消
        switch (currentState){
            case DownloadManager.DOWNLOAD_NORMAL:
                tvHomeItemDown.setText("暂停");
                DownloadManager.getInstance().startDownload(data);
                break;
            case DownloadManager.DOWNLOAD_WAIT:
                tvHomeItemDown.setText("下载");
                //如果当前状态为等待，就取消下载
                DownloadManager.getInstance().cancelDownload(data);
                break;
            case DownloadManager.DOWNLOAD_ING:
                tvHomeItemDown.setText("继续");
                //如果是正在下载，需要的点击效果是暂停
                DownloadManager.getInstance().pauseDownload(data);
                break;
            case DownloadManager.DOWNLOAD_PAUSE:
                tvHomeItemDown.setText("正在下载");
                DownloadManager.getInstance().startDownload(data);
                break;
            case DownloadManager.DOWNLOAD_SUCCESS:
                tvHomeItemDown.setText("安装");
                DownloadManager.getInstance().InstallApk(data);
                break;
            case DownloadManager.DOWNLOAD_ERROR:
                tvHomeItemDown.setText("暂停");
                DownloadManager.getInstance().startDownload(data);
                break;
        }
    }

    /**
     * 更新UI
     * @param currentState
     */
    private void updateUI(int currentState){
        switch (currentState){
            case DownloadManager.DOWNLOAD_NORMAL:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_download);
                pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvHomeItemDown.setText("下载");
                break;
            case DownloadManager.DOWNLOAD_WAIT:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_download);
                tvHomeItemDown.setText("等待");
                break;
            case DownloadManager.DOWNLOAD_ING:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_pause);
                pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                tvHomeItemDown.setText("暂停");
                break;
            case DownloadManager.DOWNLOAD_PAUSE:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_resume);
                pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                tvHomeItemDown.setText("继续");
                break;
            case DownloadManager.DOWNLOAD_SUCCESS:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_install);
                pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                tvHomeItemDown.setText("安装");
                break;
            case DownloadManager.DOWNLOAD_ERROR:
                flHomeItemDownload.setBackgroundResource(R.mipmap.ic_redownload);
                pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvHomeItemDown.setText("重试");
                break;
        }
    }

    @Override
    public void downloadStateChanged(String id, final int state) {
        if(data.id.equals(id)){
            UiUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI(state);
                }
            });
        }
    }

    @Override
    public void downloadProgressChanged(String id, final int progress) {
        if(data.id.equals(id)){
            UiUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float temp = ((progress * 1.0f / Long
                            .parseLong(data.size)) );
                    pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                    pa.setProgress(temp,true);
                }
            });
        }
    }
}
