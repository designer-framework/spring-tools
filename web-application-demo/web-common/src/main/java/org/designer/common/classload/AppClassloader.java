package org.designer.common.classload;

import lombok.extern.log4j.Log4j2;
import org.designer.common.utils.FileUtils;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: x
 * 1. 调用 {@link #loadClass(String)}
 * 2. 若[1]未找到目标类, 则调用父类方法进行加载, 如果加载失败会报错,但异常信息会被捕获并直接吞掉, 加载成功则返回。
 * 若加载失败, 继续调用 {@link #findClass(String)}, 如果该方法加载失败会抛出异常, 加载成功则返回。
 * {@link #findClass(String)} 会查找类文件,并将文件的字节流加载至虚拟机。
 * 如果加载失败, 调用 {@link #resolveClass(Class)}
 * 3. 完成
 * @author: Designer
 * @date : 2021/4/23 23:54
 */
@Log4j2
public class AppClassloader extends URLClassLoader {

    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    public AppClassloader(File url) {
        super(FileUtils.getClassPath(url));
    }

    /**
     * 在本加载器范围内加载bean
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    /**
     * 先从自定义类加载器加载, 加载失败再从父类加载.
     * 需要注意的是: 虽然是自定义加载器, 但父类的类加载器优先级依旧是最高的
     * , 只有在父类找不到Class时才会从当前类加载器加载Class.
     * 如有必要, 也可以优先从本类的类加载器加载Class
     *
     * @param name
     * @param resolve
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (classMap.containsKey(name)) {
            return classMap.get(name);
        }
        try {
            //从父类加载Class
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            return classMap.putIfAbsent(name, super.findClass(name));
        }

    }


}
