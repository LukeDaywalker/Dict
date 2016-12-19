package com.siqi.pinyin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PinYin {

	private static Map<Integer, PinYinEle> map = new HashMap<Integer, PinYinEle>();

	/**
	 * 载入pinyin数据文件
	 */
	static {
		try {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					PinYin.class.getResourceAsStream("data.dat")));
			String aLine = null;
			while ((aLine = bReader.readLine()) != null) {
				PinYinEle ele = new PinYinEle(aLine);
				map.put(ele.getUnicode(), ele);
			}
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 去掉注释可以测试一下
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("　包含声调：" + PinYin.getPinYin("大家haome12345"));
		System.out.println("不包含声调：" + PinYin.getPinYin("大家haome12345", false));
	}

	/**
	 * 获取汉字字符串的拼音，containsNumber是否获取拼音中的声调1、2、3、4
	 * 
	 * @param str
	 * @param containsNumber
	 *            true = 包含声调，false = 不包含声调
	 * @return
	 */
	public static String getPinYin(String str, boolean containsNumber) {
		StringBuffer sb = new StringBuffer();
		for (Character ch : str.toCharArray()) {
			sb.append(getPinYin(ch, containsNumber));
		}

		return sb.toString();
	}

	/**
	 * 获取字符串的拼音
	 * 
	 * @param str
	 * @return
	 */
	public static String getPinYin(String str) {
		StringBuffer sb = new StringBuffer();
		for (Character ch : str.toCharArray()) {
			sb.append(getPinYin(ch));
		}

		return sb.toString();
	}

	/**
	 * 获取单个汉字的拼音，包含声调
	 * 
	 * @param ch
	 * @return
	 */
	public static String getPinYin(Character ch) {
		return getPinYin(ch, true);
	}

	/**
	 * 获取单个汉字的拼音
	 * 
	 * @param ch
	 *            汉字. 如果输入非汉字，返回ch. 如果输入null，返回空字符串；
	 * @param containsNumber
	 *            true = 包含声调，false = 不包含声调
	 * @return
	 */
	public static String getPinYin(Character ch, boolean containsNumber) {
		if (ch != null) {
			int code = ch.hashCode();
			if (map.containsKey(code)) {
				if (containsNumber) {
					return map.get(code).getPinyin();
				} else {
					return map.get(code).getPinyin().replaceAll("[0-9]", "");
				}
			} else {
				return ch.toString();
			}
		}
		return "";
	}
}
