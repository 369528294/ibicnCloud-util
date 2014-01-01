package com.ibicnCloud.util;

import java.util.Collection;

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
	
}
