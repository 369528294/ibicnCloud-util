package com.ibicnCloud.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class BigDecimalUtil {
	public static BigDecimal parseBig(BigDecimal value) {
		return new BigDecimal(format(value, 2, false));
	}

	public static BigDecimal parseBig(BigDecimal value, int minnum) {
		return new BigDecimal(format(value, minnum, false));
	}

	public static String formatBig(BigDecimal value, int minnum) {
		return format(value, minnum, false);
	}

	public static String format(BigDecimal value, int minnum, boolean b) {
		if (value == null) {
			return "0.00";
		}
		NumberFormat nbf = NumberFormat.getInstance();
		nbf.setMinimumFractionDigits(minnum);
		nbf.setMaximumFractionDigits(minnum);
		nbf.setGroupingUsed(b);
		return nbf.format(value);
	}
}
