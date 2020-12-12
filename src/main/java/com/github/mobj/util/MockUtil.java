package com.github.mobj.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.mobj.util.enums.TypeEnum;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 通过反射获取对象，并填充属性
 *
 * @author luoruihua
 */
@UtilityClass
public class MockUtil {

    /**
     * 生成mock对象，按照默认规则随机生成各个属性的值
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public <T> T getObject(final Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return getObject(clazz, null);
    }

    /**
     * 通过反射生成mock对象，根据模板规则填充属性
     * @param clazz
     * @param template 模板
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private <T> T getObject(final Class<T> clazz, final Map<String, String> template) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final T t;
        if (clazz == null) {
            return null;
        }
        final Optional<TypeEnum> clazzType = TypeEnum.parse(clazz);
        if (clazzType.isPresent()) {
            return (T) clazzType.get().apply();
        }
        //需要有无参的构造器
        t = clazz.newInstance();
        final Field[] allFields = ArrayUtils.addAll(clazz.getDeclaredFields(), clazz.getSuperclass().getDeclaredFields());
        //符合JavaBean规则的属性
        for (final Field field : allFields) {
            final PropertyDescriptor property;
            final String fileName = field.getName();
            try {
                property = new PropertyDescriptor(fileName, clazz);
            } catch (final IntrospectionException e) {
                continue;
            }
            final Method method = property.getWriteMethod();
            final Optional<TypeEnum> filedType = TypeEnum.parse(field.getType());
            if (filedType.isPresent()) {
                method.invoke(t, filedType.get().apply());
            } else if (field.getType().isAssignableFrom(List.class)) {
                setList(template, t, field, method);
            } else if (field.getType().isAssignableFrom(Map.class)) {
                setMap(template, t, field, method);
            } else {
                final Object obj = getObject(field.getType(), template);
                method.invoke(t, obj);
            }
        }
        return t;
    }

    private <T> void setMap(final Map<String, String> template, final T t, final Field field, final Method method) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //获取泛型
        final Type types = field.getGenericType();
        //如果不是泛型的话则不处理
        if (!(types instanceof ParameterizedType)) {
            return;
        }
        final int length = 1;
        final ParameterizedType parameterizedType = (ParameterizedType) types;
        final Map<Object, Object> map = new HashMap<>(16);
        for (int j = 0; j < length; j++) {
            map.put(getObject((Class<?>) parameterizedType.getActualTypeArguments()[0], template),
                    getObject((Class<?>) parameterizedType.getActualTypeArguments()[1], template));
        }
        try {
            method.invoke(t, map);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private <T> void setList(final Map<String, String> template, final T t, final Field field, final Method method) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //获取泛型
        final Type type = field.getGenericType();
        //如果不是泛型，不做处理
        if (!(type instanceof ParameterizedType)) {
            return;
        }
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Type actualType = parameterizedType.getActualTypeArguments()[0];
        if (!(actualType instanceof Class)) {
            return;
        }
        final Class<?> genericClazz = (Class) actualType;
        final int length = 2;
        final List<Object> list = new ArrayList<>(2);
        for (int j = 0; j < length; j++) {
            list.add(getObject(genericClazz, template));
        }
        try {
            method.invoke(t, list);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}