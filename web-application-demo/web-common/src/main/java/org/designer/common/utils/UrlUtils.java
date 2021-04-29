package org.designer.common.utils;

/**
 * @Project: spring-tools
 * @Package: org.designer.common.utils
 * @Author: Designer
 * @CreateTime: 2021-04-24 22
 * @Description:
 */

public class UrlUtils {

    private static final String URI_PREFIX = "/";

    public static String paddingUriPrefix(String uri) {
        if (uri == null) {
            return URI_PREFIX;
        }
        return uri.startsWith(URI_PREFIX) ? uri : URI_PREFIX + uri;
    }


    public static String getPath(String html) {
        String uriSplit = getUri(html);
        String[] appNameSplit = uriSplit.split(getAppName(html));
        return appNameSplit.length > 1 ? appNameSplit[1] : URI_PREFIX;
    }

    /**
     * GET /appName/pay HTTP1.1
     *
     * @param html
     * @return appName
     */
    public static String getAppName(String html) {
        String uri = getUri(html);
        String[] pathSplit = uri.split(URI_PREFIX);
        return pathSplit.length > 1 ? pathSplit[1] : "";
    }

    /**
     * GET /appName/pay HTTP1.1
     *
     * @param html
     * @return /appName/pay
     */
    public static String getUri(String html) {
        String[] body = html.split("\r\n");
        if (body.length > 0) {
            String uri = body[0];
            String[] uriSplit = uri.split(" ");
            return uriSplit.length > 1 ? uriSplit[1] : "";
        }
        return "";
    }

    public static String getBody(String html) {
        String[] htmlSplit = html.split("\r\n");
        return htmlSplit[htmlSplit.length - 1];
    }


}
