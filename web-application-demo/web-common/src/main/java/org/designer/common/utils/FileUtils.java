package org.designer.common.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        File[] jars = lib.listFiles((file, name) -> name.endsWith(".jar"));
        Function<File, URL> tranFn = file -> {
            String s = file.isFile() ? "file:/" + file.getAbsolutePath() :
                    "file:/" + file.getAbsolutePath() + "/";
            try {
                return new URL(s);
            } catch (MalformedURLException e) {
                log.warn("URL exception {}", e);
                return null;
            }
        };
        Collection<URL> colUrls = Lists.newArrayList();
        if (jars != null) {
            colUrls.addAll(Lists.newArrayList(jars).stream().map(tranFn).collect(Collectors.toList()));
            for (File jar : jars) {
                if (UnzipJar.hasEntry(jar.getAbsolutePath(), ".jar")) {
                    List<File> inner_jars = extractInnerJars(home.getName(), jar.getAbsolutePath());
                    if (inner_jars != null) {
                        colUrls.addAll(inner_jars.stream().map(tranFn).collect(Collectors.toList()));
                    }
                }
            }
        }
        String classesPath = home.getAbsolutePath() + "/classes/";
        File classes = new File(classesPath);
        if (classes.exists()) {
            colUrls.add(tranFn.apply(classes));
        }

        URL[] urls = new URL[colUrls.size()];
        return Lists.newArrayList(Collections2.filter(colUrls, new Predicate<URL>() {
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
