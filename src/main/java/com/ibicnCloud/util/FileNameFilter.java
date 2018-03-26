package com.ibicnCloud.util;

import java.io.File;
import java.io.FileFilter;

public class FileNameFilter implements FileFilter {

	private String keyword;

	private boolean isExtension;

	/**
	 * 全名搜索过滤
	 * @param keyword
	 * @return
	 */
	public static FileNameFilter getFullNameFilter(String keyword) {
		return new FileNameFilter(keyword, false);
	}

	/**
	 * 以扩展名进行过滤
	 * @param keyword
	 * @return
	 */
	public static FileNameFilter getExtensionNameFilter(String keyword) {
		return new FileNameFilter(keyword, true);
	}

	/**
	 * 不让实例化
	 */
	private FileNameFilter() {
	}

	private FileNameFilter(String keyword, boolean isExtension) {
		this.keyword = StringUtil.format(keyword).toLowerCase();
		this.isExtension = isExtension;
	}

	@Override
	public boolean accept(File file) {
		if (this.isExtension)
			return file.getName().toLowerCase().endsWith(this.keyword);

		return (file.getName().toLowerCase().indexOf(this.keyword) >= 0);
	}

}