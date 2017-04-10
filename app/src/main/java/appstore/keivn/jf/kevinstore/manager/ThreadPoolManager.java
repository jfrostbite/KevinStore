package appstore.keivn.jf.kevinstore.manager;

import android.bluetooth.BluetoothClass;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 2016/3/8.
 * 线程池管理，单例
 */
public class ThreadPoolManager {

    private ThreadPoolExecutor executor;

    //私有构造，禁止创建对象
    private ThreadPoolManager() {
    }

    private static ThreadPoolManager instance;

    //提供静态方法创建本类对象
    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new ThreadPoolManager();
        }
        return instance;
    }

    //提供需要添加到线程池中的任务进来。
    public void execute(Runnable r) {
        if (executor == null) {
            //线程池最大执行数
            int core = Runtime.getRuntime().availableProcessors() * 2;
            int max = core * 2;
            executor = new ThreadPoolExecutor(core, max, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        }
        executor.execute(r);
    }

    /**
     * 取消线程池中消息队列等待的线程
     */
    public void cancel(Runnable r) {
        executor.getQueue().remove(r);
    }
}
