package appstore.keivn.jf.kevinstore.bean;

import android.os.Environment;

import java.io.File;

import appstore.keivn.jf.kevinstore.manager.DownloadManager;

/**
 * Created by Kevin on 2016/3/9.
 */
public class DownloadInfo {
    public String id; //1525490,
    public String name; //"有缘网",
    public String packageName; //"com.youyuan.yyhl",
    public String size; //3876203,
    public String downloadUrl; //"app/com.youyuan.yyhl/com.youyuan.yyhl.apk",

    /**下载状态
     *
     */
    public int currentState;
    //当前下载进度。
    public int currentProgress;
    /**
     * 文件保存路径
     */
    public String path;

    private static String getPath(String name){
        //创建文件保存路径
        String dir = Environment.getExternalStorageDirectory().getPath()+"/KevinStore/";
        //判断文件夹是否存在
        if(checkDir(dir)){
            return Environment.getExternalStorageDirectory().getPath()+"/KevinStore/"+name+".apk";
        }
        return null;
    }

    /**
     * 检查并创建目录
     * @param dir
     * @return
     */
    private static boolean checkDir(String dir){
        File file = new File(dir);
        if(file.exists()&&file.isDirectory()){
            return true;
        }else{
            //如果文件夹不存在，创建文件夹
            return file.mkdir();
        }
    }


    /**
     * 封装DownloadInfo对象
     */
    public static DownloadInfo createDownloadInfo (AppList.AppInfo appInfo){
        DownloadInfo downloadInfo = null;
        if(appInfo != null){
            downloadInfo = new DownloadInfo();
            downloadInfo.id = appInfo.id;
            downloadInfo.name = appInfo.name;
            downloadInfo.packageName = appInfo.packageName;
            downloadInfo.downloadUrl = appInfo.downloadUrl;
            downloadInfo.size = appInfo.size;
            downloadInfo.currentState = DownloadManager.DOWNLOAD_NORMAL;
            downloadInfo.currentProgress = 0;
            downloadInfo.path = getPath(appInfo.name);
        }
        return downloadInfo;
    }

}
