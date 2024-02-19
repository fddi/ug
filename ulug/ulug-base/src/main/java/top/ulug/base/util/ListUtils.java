package top.ulug.base.util;

import java.util.*;

/**
 * Created by liujf on 2017/9/29.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class ListUtils {


    /**
     * list去除重复的数据
     *
     * @param list 集合
     * @return 去重后的集合
     */
    public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }

    /**
     * 从list中随机抽取元素
     *
     * @param list list
     * @param n    数量
     * @return new list
     */
    public static List createRandomList(List list, int n) {
        Map map = new HashMap();
        List listNew = new ArrayList();
        if (list.size() <= n) {
            return list;
        } else {
            while (map.size() < n) {
                int random = (int) (Math.random() * list.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    listNew.add(list.get(random));
                }
            }
            return listNew;
        }
    }
}
