package appstore.keivn.jf.kevinstore.http.httputils;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;

import appstore.keivn.jf.kevinstore.http.HttpHelper;
import appstore.keivn.jf.kevinstore.ui.utils.IOUtils;
import appstore.keivn.jf.kevinstore.ui.utils.MD5Encoder;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;
import appstore.keivn.jf.kevinstore.ui.utils.StringUtils;
import appstore.keivn.jf.kevinstore.ui.utils.UiUtils;

/**
 * Created by Kevin on 2016/3/4.
 * 多个页面请求网络的共同点
 */
public abstract class BaseHttpRequest<T> {
    private String cachaeName = "temp.cache";

    /**
     * 获取页面需要的数据
     * 1、先从本地缓存中获取
     *      利用文件村的缓存
     * 2、再去网络中获取
     */
    public T getData() {
        String hostUrl = HttpHelper.URL;
        //每个页面访问网络的url由主机 + 模块 + 参数组成 //http://127.0.0.1:8090/home?index=0;
        //String url = hostUrl + getVirtualDir() + getKey() + getValue();
        String url = hostUrl + getKey() + getValue();
        try {
            cachaeName = MD5Encoder.encode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //1、从缓存中获取
        String result = getDataFromCache();
        //result = null;
        if(result==null){
            //2、从网络获取
            result = getDataFromNet(url);
            if(result!=null){
                //如果请求道数据，存到本地
                setDataToCache(result);
                return processJson(result);
            }

        }else{
            //解析获取到的数据
            return processJson(result);
        }
        return null;
    }
    /**
     * 请求网络获取数据
     * @param url
     */
    private String getDataFromNet(String url) {
        String result = httpRequest(url);
        if(result!=null){
            /*//解析获取到的数据
            String ret = processJson(result);*/
            return result;
        }
        return null;
    }

    /**
     * 请求网络获取数据，最终返回已经解析的数据
     * @param url
     * @return
     */
    public String httpRequest(String url) {
        HttpHelper.HttpResult httpResult = HttpHelper.get(url);
        String result = null;
        if(httpResult!=null){
            result = httpResult.getString();
            if(result!=null){
                return result;
            }
        }
        return null;
    }

    /**
     * 获取本地缓存
     */
    private String getDataFromCache() {
        //利用BufferedReader读取本地资源
        File cachePath = UiUtils.getContext().getCacheDir();
        File file = new File(cachePath, cachaeName);
        if(!file.exists()){
            return null;
        }
        String cache = getCache(file);
        if (cache!=null){
            return cache;
        }
        return null;
    }

    /**
     * 将数据保存在本地
     * @param cache
     */
    private void setDataToCache(String cache) {
        File cachePath = UiUtils.getContext().getCacheDir();
        File file = new File(cachePath, cachaeName);
        setCache(cache, file);
    }

    /**
     * 判断缓存的是否有效，原理：存的时候在头部存入时间戳，读取时先读时间戳，判断是否在规定时间内读取的
     * @param file
     * @return
     */
    @NonNull
    private String getCache(File file) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(file));
            //获取文件到期时间
            long matureTime = Long.parseLong(br.readLine());
            //获取现行时间戳，入股现行时间戳大于缓存时间戳，无效缓存，返回空
            long currentTime = System.currentTimeMillis();
            if(currentTime>matureTime){
                return null;
            }
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line+"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
        }
        return sb.toString();
    }

    /**
     * 保存缓存
     * @param cache
     * @param file
     */
    private void setCache(String cache, File file) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.valueOf(System.currentTimeMillis()+5*60*1000));
            bw.newLine();//换行
            bw.write(cache);
            bw.newLine();//换行
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bw);
        }
    }

    //获取虚拟目录
    protected abstract String getVirtualDir();
    //获取key
    protected abstract String getKey();
    //获取值
    protected abstract String getValue();
    //解析json
    protected abstract T processJson(String string);
}
