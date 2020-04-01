package com.example.clever_permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class CreateObject {
    public <T> void instanceValClass(@NonNull T t,
                                     @Nullable CallValue<T> call) {
        /* 遍历当前类中的所有变量，包括了私有变量 */
        for (Field f : t.getClass().getDeclaredFields()) {
            Object val;
            //接口为空或者返回值为空，则跳过当前变量，保证不会影响到无关的变量
            if (call == null || (val = call.value(t, f)) == null) continue;
            //允许被访问
            f.setAccessible(true);
            try {
                f.set(t, val);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> Object obtainInstance(@NonNull T t,
                                    @Nullable CallValue<T> call) {

        /* 遍历当前类中的所有变量，包括了私有变量 */
        for (Field f : t.getClass().getDeclaredFields()) {
            Object val;
            //接口为空或者返回值为空，则跳过当前变量，保证不会影响到无关的变量
            if (call == null || (val = call.value(t, f)) == null) continue;

            return val;
        }
        return null;
    }

    public <T> T newClass(Class<T> cls) {
        return newClass(cls, null, (Object) null);
    }

    /**
     * 带参构造一个泛型的Class
     *
     * @param cls            构造的Class
     * @param parameterTypes 参数类型    eg: String.class
     * @param initargs       参数       eg: "AaBb"
     * @param <T>            类的泛型
     * @return 返回创建好的泛型
     */
    @Nullable
    public <T> T newClass(@Nullable Class<T> cls,
                          @Nullable Class<?>[] parameterTypes,
                          @Nullable Object... initargs) {
        try {
            if (cls == null) return null;
            //构造对象
            if (parameterTypes == null || initargs == null ||
                    parameterTypes.length != initargs.length) {
                return cls.getConstructor().newInstance();
            } else {
                return cls.getConstructor(parameterTypes).newInstance(initargs);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface CallValue<T> {
        Object value(T t, @NonNull Field f);
    }

}
