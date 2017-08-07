package com.kss.commons.util;


import java.util.ArrayList;
import java.util.List;

import com.kss.commons.exceptions.BeanException;
import org.springframework.beans.BeanUtils;

public class BeanUtil {

    public static <S, T> List<T> fromBeans(List<S> sources, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        if (sources == null || sources.size() == 0) return targetList;
        sources.forEach(source -> {
            T target = fromBean(source, targetClass);
            if (target != null) targetList.add(target);
        });
        return targetList;
    }

    public static <S, T> T fromBean(S source, Class<T> targetClass) {
        if (source == null) throw new BeanException("资源对象不可为null");
        T target;
        try {
            target = targetClass.newInstance();
        } catch (Exception e) {
            throw new BeanException("目标类实例化异常", e);
        }
        if (target != null) BeanUtils.copyProperties(source, target);
        return target;
    }
}
