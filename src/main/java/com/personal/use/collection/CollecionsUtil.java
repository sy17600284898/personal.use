package com.personal.use.collection;

import java.util.Collections;
import java.util.List;

/**
 * CollecionsUtil
 *
 * @author: shiyan
 * @version: 2019-10-12 17:21
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class CollecionsUtil {


    /**
     * indexOfSubList(List list,List subList)方法的使用(含义：查找subList在list中首次出现位置的索引)。
     *
     * @param list
     * @param subList
     * @param <T>
     * @return
     */
    public static <T> Integer subList(List<T> list, List<T> subList) {
        return Collections.indexOfSubList(list, subList);
    }
}
