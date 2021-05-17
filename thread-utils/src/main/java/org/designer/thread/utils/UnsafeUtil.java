package org.designer.thread.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: Designer
 * @date : 2021/5/15 22:43
 */
public class UnsafeUtil {

    private static final Unsafe UNSAFE;

    static {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        }
    }

    private UnsafeUtil() {
        throw new RuntimeException("UnsafeUtil");
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

}
