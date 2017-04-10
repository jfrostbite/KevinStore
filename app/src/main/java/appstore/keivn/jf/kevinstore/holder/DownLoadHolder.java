package appstore.keivn.jf.kevinstore.holder;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import appstore.keivn.jf.kevinstore.R;
import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.bean.DownloadInfo;
import appstore.keivn.jf.kevinstore.manager.DownloadManager;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;
import appstore.keivn.jf.kevinstore.ui.widget.ProgressHorizontal;

/**
 * Created by Kevin on 2016/3/9.
 * 管理下载的类
 * 观察者模式。观察下载管理器的状态，将状态实时反馈到观察者。
 *
 * 根据下载管理器反馈的状态信息更新ui界面
 * 1、进入详情页面的回显
 * 2、点击下载按钮需要进行的操作判断
 * 3、下载状态实时改变进行的操作。
 */
public class DownLoadHolder extends BaseHolder<AppList.AppInfo> implements View.OnClickListener, DownloadManager.OnDownloadListener {

    private Button btnDownload;
    private int currentState;
    private FrameLayout flDownload;
    private ProgressHorizontal ph;

    @Override
    public View initView() {
        View view = UiUtils.inflateView(R.layout.layout_detail_download);
        btnDownload = (Button) view.findViewById(R.id.download_bt);
        btnDownload.setOnClickListener(this);
        flDownload = (FrameLayout) view.findViewById(R.id.download_fl);
        FrameLayout download_fl = (FrameLayout) view.findViewById(R.id.download_fl);
        //创建出横向Progress的控件对象

        ph = new ProgressHorizontal(UiUtils.getContext());
        ph.setOnClickListener(this);
        ph.setProgressTextColor(Color.WHITE);
        ph.setProgressTextSize(UiUtils.dip2px(16));
        ph.setProgressResource(R.mipmap.progress_normal);//设置进度条的图片
        ph.setProgressBackgroundResource(R.mipmap.progress_bg);//设置进度条背景的图片
        download_fl.addView(ph);

        return view;
    }

    @Override
    public void initData() {
        //给下载控件添加进度条
        /*ph = new ProgressHorizontal(UiUtils.getContext());
        ph.setProgressResource(R.mipmap.progress_normal);
        ph.setProgressBackgroundResource(R.mipmap.progress_bg);
        flDownload.addView(ph);*/
        //给该应用程序注册监听器

        DownloadManager.getInstance().registerListener(this);
        //初始化详情界面状态：1获取应用下载信息，没有就创建一个；2、更新ui
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
                btnDownload.setText("暂停");
                DownloadManager.getInstance().startDownload(data);
            break;
            case DownloadManager.DOWNLOAD_WAIT:
                btnDownload.setText("下载");
                //如果当前状态为等待，就取消下载
                DownloadManager.getInstance().cancelDownload(data);
            break;
            case DownloadManager.DOWNLOAD_ING:
                btnDownload.setText("继续");
                //如果是正在下载，需要的点击效果是暂停
                DownloadManager.getInstance().pauseDownload(data);
            break;
            case DownloadManager.DOWNLOAD_PAUSE:
                btnDownload.setText("正在下载");
                DownloadManager.getInstance().startDownload(data);
            break;
            case DownloadManager.DOWNLOAD_SUCCESS:
                btnDownload.setText("安装");
                DownloadManager.getInstance().InstallApk(data);
            break;
            case DownloadManager.DOWNLOAD_ERROR:
                btnDownload.setText("暂停");
                DownloadManager.getInstance().startDownload(data);
            break;
        }
    }

    /**
     * 实现监听下载管理器对调用者的改变。
     * 当下在状态改变时在下载管理器内部会实时通知监听者状态改变。此时改变的操作需要调用者进行实现。
     * @param id
     * @param state
     */
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
                    int temp = (int) (progress * 1f / Long.parseLong(data.size)*100);
                //ph.setCenterText("当前进度："+temp);
                    ph.setProgress(temp);
                    ph.setCenterText(temp + "%");
                }
            });
        }


    }

    /**
     * 界面UI的更新，根据下载状态反馈 的数据进行
     * 思路：
     * 因为管理器中下载过程中实时的更新着下载状态并将下载信息保存在了集合中，所以此处直接调用下载管理器中
     * 下载信息的集合就可以获取到下载装填信息。
     *
     * 该更新ui就是下载过程中监听器实时做的修改
     * 还有进入详情页面的按钮状态的回显操作。
     */
    private void updateUI(int currentState){
        switch (currentState){
            case DownloadManager.DOWNLOAD_NORMAL:
                btnDownload.setText("下载");
                btnDownload.setVisibility(View.VISIBLE);
                ph.setVisibility(View.GONE);
                break;
            case DownloadManager.DOWNLOAD_WAIT:
                btnDownload.setText("等待");
                btnDownload.setVisibility(View.VISIBLE);
                ph.setVisibility(View.GONE);
                break;
            case DownloadManager.DOWNLOAD_ING:
                btnDownload.setVisibility(View.GONE);
                ph.setCenterText("暂停");
                ph.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_PAUSE:
                btnDownload.setVisibility(View.GONE);
                ph.setCenterText("继续");
                ph.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.DOWNLOAD_SUCCESS:
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                ph.setVisibility(View.GONE);
                break;
            case DownloadManager.DOWNLOAD_ERROR:
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("重试");
                ph.setVisibility(View.GONE);
                break;
        }
    }
}
