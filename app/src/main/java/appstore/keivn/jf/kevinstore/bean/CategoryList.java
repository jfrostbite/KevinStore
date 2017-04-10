package appstore.keivn.jf.kevinstore.bean;

/**
 * Created by Kevin on 2016/3/6.
 * 分类：请求网络获取到的数据封装的javabean
 *
 * 每个项目里，通常情况下都是3个图片加一个文本组成，
 * 特殊情况为文字
 * 涉及ListView，getView中填充项目类型。
 * 需要添加个数是通常类型+特殊类型，可以将特殊类型数据封装到通常类型中。作为单独数据
 * getcount()不用增加个数，当填充的数据类型不同时，getCount 需要单独累加集合个数
 *
 * 该bean封装ListView中每一行填充的数据
 *
 */
public class CategoryList {

    public String title;
    /**
     * 如果是标题，也可以封装在该类中，方便将单独的Title 和 通用数据 添加到同一个List中。
     */
    public boolean isTitle;

    public String url1,url2,url3;
    public String name1,name2,name3;
}
