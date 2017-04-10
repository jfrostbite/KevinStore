package appstore.keivn.jf.kevinstore.activity.fragment;

import java.util.HashMap;

/**
 * Created by Kevin on 2016/3/3.
 * 该工厂类用于给ViewPager添加不同的界面Fragment
 *
 * 避免重复调用重复创建无用对象，可以在创建对象后添加到map集合中，再次获取从map中获取，如果没有在创建
 */
public class FragmentFactory {
    static HashMap<Integer,BaseFragment> fragmentMap = new HashMap<Integer,BaseFragment>();
    public static BaseFragment getFragment(int position) {
        //首先从map集合中获取BaseFragment子类
        BaseFragment fragment = fragmentMap.get(position);
        if(fragment==null){
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new RecommentFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;
            }
            //拿到创建好的Fragment，保存到map中，以便下次调用时使用
            fragmentMap.put(position,fragment);
        }
        return fragment;
    }
}
