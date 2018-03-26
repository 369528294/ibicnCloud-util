package com.ibicnCloud.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectionUtil {

	/**
	 * 获取指定集合的大小；
	 */
	public static int size(Collection collection) {
		if (CollectionUtil.isEmpty(collection))
			return 0;
		else
			return collection.size();
	}

	public static int size(Object[] object) {
		if (CollectionUtil.isEmpty(object))
			return 0;
		else
			return object.length;
	}

	public static boolean isEmpty(Object[] object) {
		if (object == null || object.length == 0)
			return true;
		else
			return false;
	}

	public static boolean isEmpty(Collection collection) {
		if (collection == null || collection.size() == 0)
			return true;
		else
			return false;
	}

	/***************************************************************************
	 * 把Map的key转换成一个List
	 *
	 * @param args
	 * @return Vector
	 */
	public static List getList(Set set) {
		List v = new ArrayList();
		if (size(set) > 0) {
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				v.add(iterator.next());
			}
		}
		return v;
	}

}
