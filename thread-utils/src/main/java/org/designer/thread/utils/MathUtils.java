package org.designer.thread.utils;

import java.text.DecimalFormat;

/**
 * @description:
 * @author: Designer
 * @date : 2021/4/21 15:00
 */
public class MathUtils {

    /**
     * 计算百分比
     *
     * @param count
     * @param sum
     * @return
     */
    public static String computePercentage(long count, long sum) {
        double fen = (count * 1.0) / (sum * 1.0);
        DecimalFormat df1 = new DecimalFormat("##.00%");
        String format = df1.format(fen);
        return format.startsWith(".") ? "0" + format : format;
    }

}
