package appstore.keivn.jf.kevinstore.manager;

import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import appstore.keivn.jf.kevinstore.bean.AppList;
import appstore.keivn.jf.kevinstore.bean.DownloadInfo;
import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.ui.utils.IOUtils;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/9.
 * 下载数据的具体实现。单利
 * 思路:
 * 考虑多个应用同时下载，需要将要下载的应用存到集合中
 * 1、点击下载按钮，将要下载的应用信息传入下载管理器
 * 2、在已经存在的下载信息的集合中寻找欲下载应用的下载信息。如果存在就直接调用，否则就创建新的下载应用信息，然后存入集合中。
 * 3、拿到下载信息后，建立下载任务（创建该应用的下载Runnable，丢到线程池中进行），因为要维护该下载信息。所以将下载信息保存在集合中
 * 4、对下载任务进行详细完善。
 *
 * 下载思路：
 * 1、活到下载信息，获取下载信息中的文件路径
 * 2、判断该文件是否存在于本地，判断是否是继续下载
 *      第一次下载（如果文件不存在，文件存在大小和内存中存储的下载位置不一样，文件中位置为0）
 *      否则：是继续下载
 * 3、调用下载方法，获取result
 * 4、判断下载连接是否成功
 *       判断下载流是否为空（链接服务器成功，但是服务起没有指定文件，会返回null流）
 *       接收流完毕，判断流是否和应用大小相同，相同：下载完成，不同：暂停或下载失败
 *              判断下载信息中的当前状态如果为pause，暂停；否则为失败
 *
 *
 * 此下载管理包装类，在下载过程中产生的事件由 需要进行一些操作，这些操作由观察者提供。
 * 外部提供操作如：setOnclickListener , 此例类似操作，是将该方法获取到的listener存放在集合中。因为该例中会涉及到同时操作
 * 多个监听器。
 *
 * 注意：下载管理器中3个集合：观察者集合，应用程序对应的下载信息集合，应用程序对应的下载线程池中任务集合
 * 输出流的追加保存。
 */
public class DownloadManager {
    private OnDownloadListener listener;
    public static final int DOWNLOAD_NORMAL = 0;
    public static final int DOWNLOAD_WAIT = 1;
    public static final int DOWNLOAD_ING = 2;
    public static final int DOWNLOAD_PAUSE = 3;
    public static final int DOWNLOAD_SUCCESS = 4;
    public static final int DOWNLOAD_ERROR = 5;
    //当前下载状态
    public int currentState = DOWNLOAD_NORMAL;

    private DownloadManager() {
    }

    //定义接收下载信息的集合。
    private static DownloadManager instance;

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    //利用map保存下载信息
    public HashMap<String,DownloadInfo> downloadInfoMap = new HashMap<String,DownloadInfo>();
    //保存下载任务集合
    HashMap<String,DownloadTask> downloadTaskMap = new HashMap<String,DownloadTask>();
    /**
     * 提供下载方法，接收欲下载项目的封装信息
     */
    public void startDownload (AppList.AppInfo appInfo) {
        //从已知的下载信息集合中获取欲下载应用对应的下载信息，如果存在直接调用那个，如果不存在就创建一个新的
        // 保存进集合并进行调用
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        if(downloadInfo == null){
            downloadInfo = DownloadInfo.createDownloadInfo(appInfo);
            downloadInfoMap.put(downloadInfo.id,downloadInfo);
        }
        //点击开始下载说明用户有下载应用的动作，此时需要给用户一个反馈，就是监听器需要监听下下载状态并反馈给调用者
        //此时的状态为等待
        downloadInfo.currentState = DOWNLOAD_WAIT;
        notifyDownloadStateChanged(downloadInfo);
        //创建该下载信息的下载任务（Runnable接口）用于传递给线程池
        DownloadTask downloadTask = new DownloadTask(downloadInfo);
        //丢到线程池
        ThreadPoolManager.getInstance().execute(downloadTask);
        //保存该任务。
        downloadTaskMap.put(downloadInfo.id,downloadTask);
    }

    /**
     * 暂停下载
     * 改变状态，更新状态
     */
    public void pauseDownload(AppList.AppInfo appInfo){
        //根据传进来的应用，获取下载信息
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        //改变该应用下载信息的下载状态为暂停
        downloadInfo.currentState = DOWNLOAD_PAUSE;
        //通知监听
        notifyDownloadStateChanged(downloadInfo);
    }

    /**
     * 取消下载
     * 原理：将等待线程从线程池中销毁
     */
    public void cancelDownload(AppList.AppInfo appInfo){
        //根据应用id获取任务集合中的该任务
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);
        DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.id);
        ThreadPoolManager.getInstance().cancel(downloadTask);
        //notifyDownloadStateChanged(downloadInfo);
        downloadInfo.currentState = DOWNLOAD_NORMAL;
        notifyDownloadStateChanged(downloadInfo);
    }

    /**
     * 安装应用
     * 原理：将等待线程从线程池中销毁
     */
    public void InstallApk(AppList.AppInfo appInfo){
        DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.id);


        //安装
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(
                Uri.fromFile(new File(downloadInfo.path)),
                "application/vnd.android.package-archive");
        UiUtils.getContext().startActivity(intent);
    }

    /**
     * 封装下载任务接口，将下载任务封装在Runnable中方便使用
     * 下载任务需要有被操作的下载信息，所以需要想办法将调用者的信息传递进来，可以使用构造函数，也可以使用对外提供
     * 设置信息的方法，这里使用构造函数传递
     *
     * 下载过程中 正在下载，暂停下载。
     */
    class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;
        //构造函数传递需要被操作的下载信息
        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        //具体的下载任务开始，如果线程池开始下载任务，就说明状态从等待转到正在下载
        @Override
        public void run() {
            //将状态置为正在下载
            downloadInfo.currentState = DOWNLOAD_ING;
            notifyDownloadStateChanged(downloadInfo);
            FileOutputStream fos = null;
            String url = "";
            //开始下载
            try {
                File file = new File(downloadInfo.path);
                //考虑第一次下载和暂停后的继续下载的情况。判断依据是程序中保存的下载信息中的进度信息是否和本地文件
                //大小一致

                if(!file.exists() || downloadInfo.currentProgress == 0 || file.length()!=downloadInfo.currentProgress){
                    url = HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl;
                    file.delete();
                    downloadInfo.currentProgress = 0;
                }else{
                    url = HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl+"&range="+downloadInfo.currentProgress;
                }

                HttpHelper.HttpResult result = HttpHelper.download(url);
                if(result!=null){
                    //连接成功，进而判断返回的流数据是否存在
                    InputStream is = result.getInputStream();
                    if(is!=null){
                        //有数据，进而判断是否下载成功
                        //创建输出流，保存下载文件
                        fos = new FileOutputStream(file,true);
                        byte[] buffer = new byte[1024*4];
                        int leng = 0;
                        //加个判断下载成立的条件，除了文件长度不为-1，还有我这里的下载状态是正在下载，才成立
                        while((leng = is.read(buffer))!=-1 && downloadInfo.currentState == DOWNLOAD_ING){
                            fos.write(buffer, 0,leng);
                            fos.flush();
                            //下载过程中更改下载进度
                            downloadInfo.currentProgress += leng;
                            notifyDownloadProgressChanged(downloadInfo);
                        }
                        //下载完成，判断下载文件大小是否和一直文件大小一致，否则判定下载失败
                        long sourceSize = Long.parseLong(downloadInfo.size);
                        long currentSize = downloadInfo.currentProgress;
                        if(currentSize==sourceSize){
                            downloadInfo.currentState = DOWNLOAD_SUCCESS;
                            notifyDownloadStateChanged(downloadInfo);
                        }else{
                            //文件大小不一样还是有可能是暂停下载
                            if(downloadInfo.currentState == DOWNLOAD_PAUSE){
                                downloadInfo.currentState = DOWNLOAD_PAUSE;
                                notifyDownloadStateChanged(downloadInfo);
                            }else{
                                //如果不是暂停，大小又不一样，那么就是下载的失败的文件
                                downloadInfo.currentState = DOWNLOAD_ERROR;
                                notifyDownloadStateChanged(downloadInfo);
                            }
                        }
                    }else{
                        //数据不存在
                        downloadInfo.currentState = DOWNLOAD_ERROR;
                        notifyDownloadStateChanged(downloadInfo);
                    }
                }else{
                    //链接失败
                    downloadInfo.currentState = DOWNLOAD_ERROR;
                    notifyDownloadStateChanged(downloadInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                downloadInfo.currentState = DOWNLOAD_ERROR;
                notifyDownloadStateChanged(downloadInfo);
            } finally {
                IOUtils.close(fos);
            }
        }
    }

    public void download(DownloadInfo downloadInfo) {
        HttpHelper.HttpResult result = HttpHelper.download(HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl);
        if (result != null) {
            //success
            downloadInfo.currentState = DOWNLOAD_ING;
            notifyDownloadStateChanged(downloadInfo);
            FileOutputStream fos = null;
            try {
                InputStream is = result.getInputStream();
                File file = new File(downloadInfo.path);
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024 * 10];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    MyLog.info("本次下载长度：" + len);
                    fos.write(buf, 0, len);
                    fos.flush();
                    //下载进度改变。
                    //notifyDownloadProgressChanged(len);
                }
                //下载完成的状态
                downloadInfo.currentState = DOWNLOAD_SUCCESS;
                notifyDownloadStateChanged(downloadInfo);
            } catch (IOException e) {
                e.printStackTrace();
                downloadInfo.currentState = DOWNLOAD_ERROR;
                notifyDownloadStateChanged(downloadInfo);
            } finally {
                IOUtils.close(fos);
            }
        } else {
            //false
            downloadInfo.currentState = DOWNLOAD_ERROR;
            notifyDownloadStateChanged(downloadInfo);
        }
    }


    /**
     * 以下方法就是本类调用接口中方法
     */
    /**
     * 下载状态改变是通知观察者进行的改变
     * 就是内部进行的外部实现的方法调用
     *
     *
     */
    private void notifyDownloadStateChanged(DownloadInfo info) {
        if (listener != null) {
            listener.downloadStateChanged(info.id,info.currentState);
        }
        if (listeners != null && listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).downloadStateChanged(info.id,info.currentState);
            }
        }
    }

    /**
     * 下载状态改变是通知观察者进行的改变
     * 就是内部进行的外部实现的方法调用
     */
    private void notifyDownloadProgressChanged(DownloadInfo info) {
        if (listener != null) {
            listener.downloadProgressChanged(info.id,info.currentProgress);
        }
        if (listeners != null && listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).downloadProgressChanged(info.id,info.currentProgress);
            }
        }
    }

    /**
     * 监听下载状态的监听器
     *
     * 因为判断下载状态需要知道是那个应用程序的状态，所以需要传递一个应用程序的特有标识。就是id或包名
     *
     * 监听状态需要知道监听的是那个应用的状态。
     */
    public interface OnDownloadListener {
        //下载状态改变,
        void downloadStateChanged(String id,int state);

        //下载进度改变
        void downloadProgressChanged(String id,int progress);
    }

    /**
     * 对外提供设置下载状态改变时需要进行的操作的接口
     * 注册监听的方法
     * 1、单个监听
     * 2、多个监听
     */
    public void setOnDownloadListener(OnDownloadListener listener) {
        this.listener = listener;
    }

    /**
     * 有多个监听器,先创建一个存储监听者的容器
     */
    ArrayList<OnDownloadListener> listeners = new ArrayList<OnDownloadListener>();

    /**
     * 存储多个监听，setOnDownloadListener 只存储一个。
     *
     * @param listener
     */
    public void registerListener(OnDownloadListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

}
