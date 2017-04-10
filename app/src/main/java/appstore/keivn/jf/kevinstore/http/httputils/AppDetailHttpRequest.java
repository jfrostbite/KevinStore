package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.AppList;

/**
 * Created by Kevin on 2016/3/7.
 */
public class AppDetailHttpRequest extends BaseHttpRequest<AppList.AppInfo> {

    private String packageName;
    //传入package名字
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected String getVirtualDir() {
        return "";
    }

    @Override
    protected String getKey() {
        return "detail";
    }

    @Override
    protected String getValue() {
        return "?packageName="+packageName;
    }

    @Override
    protected AppList.AppInfo processJson(String string) {

        AppList.AppInfo appInfo = new AppList().new AppInfo();
        appInfo.safe = new ArrayList<AppList.AppInfo.Safe>();
        appInfo.screen = new ArrayList<String>();
        try {
            JSONObject jo = new JSONObject(string);
            appInfo.des = jo.getString("des");
            appInfo.author = jo.getString("author");
            appInfo.date = jo.getString("date");
            appInfo.downloadNum = jo.getString("downloadNum");
            appInfo.iconUrl = jo.getString("iconUrl");
            appInfo.id = jo.getString("id");
            appInfo.name = jo.getString("name");
            appInfo.packageName = jo.getString("packageName");
            appInfo.stars = jo.getString("stars");
            appInfo.size = jo.getString("size");
            appInfo.version = jo.getString("version");
            appInfo.downloadUrl = jo.getString("downloadUrl");

            JSONArray ja = jo.getJSONArray("safe");
            for (int i = 0; i < ja.length(); i++) {
                AppList.AppInfo.Safe safe = appInfo.new Safe();
                JSONObject jsonSafe = ja.getJSONObject(i);
                safe.safeUrl = jsonSafe.getString("safeUrl");
                safe.safeDesUrl = jsonSafe.getString("safeDesUrl");
                safe.safeDes = jsonSafe.getString("safeDes");
                safe.safeDesColor = jsonSafe.getString("safeDesColor");
                appInfo.safe.add(safe);
            }

            JSONArray ja2 = jo.getJSONArray("screen");
            for (int j = 0; j < ja2.length(); j++) {
                String screenUrl = ja2.getString(j);
                appInfo.screen.add(screenUrl);
            }

            return appInfo;

        } catch (Exception e){
            return null;
        }
    }
}
