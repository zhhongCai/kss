package com.kss.commons.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanUtils {

    /**
     * 实体类转换
     * @param sourceObject
     * @param targetClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T converObject(Object sourceObject, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        if (null == sourceObject) {
            return null;
        }
        T bean = null;
        bean = targetClass.newInstance();
        org.springframework.beans.BeanUtils.copyProperties(sourceObject, bean);

        return bean;
    }
    /**
     * 实体list转换
     * @author chenchw
     * @date 2017年5月19日20:48:29
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <T,E> List<T> convertList(List<E> sourceList, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        List<T> targetList =new ArrayList<T>();
        if (CollectionUtils.isNotEmpty(sourceList)) {
            for(E sourceObject :sourceList){
                    T bean = targetClass.newInstance();
                    org.springframework.beans.BeanUtils.copyProperties(sourceObject, bean);
                    targetList.add(bean);
            }
        }
        return targetList;
    }
}
