package appstore.keivn.jf.kevinstore.http.httputils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appstore.keivn.jf.kevinstore.bean.SubjectList;
import appstore.keivn.jf.kevinstore.ui.utils.MyLog;

/**
 * Created by Kevin on 2016/3/4.
 * 应用页面特有的请求网路的方式
 */
public class SubjectHttpRequest extends BaseHttpRequest<SubjectList> {

    private int index;
    /*@Override
    protected String getVirtualDir() {
        return "home?";
    }*/

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    protected String getVirtualDir() {
        return "";
    }

    @Override
    protected String getKey() {
        return "subject";
    }

    @Override
    protected String getValue() {
        return "?index="+index;
    }

    @Override
    public SubjectList processJson(String str) {


        SubjectList subjectList = new SubjectList();
        subjectList.list = new ArrayList<SubjectList.SubjectInfo>();
        try {
            JSONArray list = new JSONArray(str);
            //获取每个应用程序信息，类型是AppInfo
            for (int i = 0; i < list.length(); i++) {
                //appInfo = appList.new AppInfo();
                SubjectList.SubjectInfo subjectInfo = subjectList.new SubjectInfo();
                JSONObject subjectObject = list.getJSONObject(i);
                subjectInfo.des = subjectObject.getString("des");
                subjectInfo.url = subjectObject.getString("url");
                subjectList.list.add(subjectInfo);
            }

            if (subjectList != null) {
                return subjectList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 1、{} ---JSONObject
     * 2、[] ---JSONArray
     * 3、无任何符号---基础数据类型
     */
    /*private ArrayList<AppInfo> dataList;
    private ArrayList<String> picList;

    public ArrayList<AppInfo> processJson(String jsonString) {
        try{
            JSONObject jo = new JSONObject(jsonString);
            JSONArray ja = jo.getJSONArray("list");
            dataList = new ArrayList<AppInfo>();
            for(int i=0;i<ja.length();i++) {
                JSONObject jo2 = ja.getJSONObject(i);
                AppInfo info = new AppInfo();
                info.des = jo2.getString("des");
                info.downloadUrl = jo2.getString("downloadUrl");
                info.iconUrl = jo2.getString("iconUrl");
                info.id = jo2.getString("id");
                info.name = jo2.getString("name");
                info.packageName = jo2.getString("packageName");
                info.size = jo2.getString("size");
                info.stars = jo2.getString("stars");
                dataList.add(info);
            }


            JSONArray ja2 = jo.getJSONArray("picture");
            picList = new ArrayList<String>();
            for(int i=0;i<ja2.length();i++) {
                String strPic = ja2.getString(i);
                picList.add(strPic);
            }

        }catch(Exception e) {

        }

        return dataList;
    }*/
}
