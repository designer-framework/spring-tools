package org.designer.common.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/24 2:22
 */
@Log4j2
public class FileUtils {

    private static final String TMP_PATH = new File("./tmp").getAbsolutePath();

    public static URL[] getClassPath(File home) {
        File lib = new File(home.getAbsoluteFile() + "/jar");
        File[] jars = lib.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".jar");
            }
        });
        Function<File, URL> tran_fn = new Function<File, URL>() {
            @Override
            public URL apply(File file) {
                String s = file.isFile() ? "file:/" + file.getAbsolutePath() :
                        "file:/" + file.getAbsolutePath() + "/";
                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    log.warn("URL exception {}", e);
                    return null;
                }
            }
        };
        Collection<URL> col_urls = Lists.newArrayList();
        if (jars != null) {
            col_urls.addAll(Lists.transform(Lists.newArrayList(jars), tran_fn));
            for (File jar : jars) { // add inner jar to class path
                if (UnzipJar.hasEntry(jar.getAbsolutePath(), ".jar")) {
                    List<File> inner_jars = extractInnerJars(home.getName(), jar.getAbsolutePath());
                    if (inner_jars != null) {
                        col_urls.addAll(Lists.transform(inner_jars, tran_fn));
                    }
                }
            }
        }
        String classes_path = home.getAbsolutePath() + "/classes/";
        File classes = new File(classes_path);
        if (classes.exists()) {
            col_urls.add(tran_fn.apply(classes));
        }

        URL[] urls = new URL[col_urls.size()];
        return Lists.newArrayList(Collections2.filter(col_urls, new Predicate<URL>() {
            @Override
            public boolean apply(URL url) {
                return url != null;
            }
        })).toArray(urls);
    }

    private static List<File> extractInnerJars(String name, String jarFile) {
        log.debug("extract inner jars {}", jarFile);
        String dst = TMP_PATH + File.separator + name;
        try {
            return UnzipJar.unzipJar(dst, jarFile, ".jar");
        } catch (IOException e) {
            log.warn("extract jar file {} failed", jarFile);
        }
        return null;
    }

}
