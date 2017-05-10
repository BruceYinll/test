package com.sptd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;

/**
 * 验证工具类
 * 
 * @author lanyan
 * 
 */
public class VerifyUtil {
	/**
	 * 邮箱验证
	 * 
	 * @param target
	 * @return
	 */

	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {			
			return false;		}
 	return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	/**
	 * 检验手机号是否有效
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((\\(\\d{2,3}\\))|(\\d{4}\\-))?1[3,4,5,8]\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证电话号码 ^[\\d+-]*$
	 */
	public static boolean isPhone(String str) {
		Pattern p = Pattern.compile("^[\\d+-]*$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：true 无效：false
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static boolean IDCardValidate(String IDStr) {
		IDStr = IDStr.toLowerCase(Locale.CHINA);
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		// String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
		String Ai = "";

		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "号码长度应该为15位或18位。";
			System.out.println(errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			System.out.println(errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份

		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "生日无效。";
			System.out.println(errorInfo);
			return false;
		}

		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = "生日不在有效范围。";
				System.out.println(errorInfo);
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "月份无效";
			System.out.println(errorInfo);
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "日期无效";
			System.out.println(errorInfo);
			return false;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "地区编码错误。";
			System.out.println(errorInfo);
			return false;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，最后一位字母错误";
				System.out.println(errorInfo);
				return false;
			}
		} else {
			System.out.println("所在地区:" + h.get(Ai.substring(0, 2).toString()));
			System.out.println("新身份证号:" + Ai);
			return true;
		}
		// =====================(end)=====================
		System.out.println("所在地区:" + h.get(Ai.substring(0, 2).toString()));
		return true;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		}
		return false;
		/*
		 * 判断一个字符时候为数字 if(Character.isDigit(str.charAt(0))) { return true; }
		 * else { return false; }
		 */
	}

	/**
	 * 判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		return m.matches();
	}

	/**
	 * 设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	// 密码只能是6-12位英文或数字
	public static boolean isPassword(String str) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,12}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 手机号校验，只能是9位的。
	public static boolean isPhoneNo(String str) {
		Pattern pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 姓名校验，2-8位字符。
	public static boolean isName(String str) {
		String regex = "[\u4e00-\u9fa5]{2,8}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/*
	 * 验证身份证号码 居民身份证号码15位或18位，最后一位可能是数字或字母
	 * 
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean isCardID(String str) {
		String regex = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 校验第一位汉字，第二位英文
	public static boolean isCarFrontNo(String str) {
		String regex = "^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z0-9]{1,9}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	//字符串是英文字母和汉字
	public static boolean isEnglishOrChinese(String str){
		String regexEng="^[a-zA-Z]{1}$";
		String regexChi="^[\u4e00-\u9fa5]{1}$";
		Pattern patternEng = Pattern.compile(regexEng);
		Pattern patternChi = Pattern.compile(regexChi);
		for(int i=0;i<str.length();i++){
			String subString=str.substring(i, i+1);			
			Matcher matcherEng = patternEng.matcher(subString);			
			Matcher matcherChi = patternChi.matcher(subString);
			if(!matcherEng.matches() && !matcherChi.matches())
			  return false;
		}
		return true;
	}

	// 吨位校验，可有一位小数。
	public static boolean isCarLoad(String str) {
		String regex = "^\\d+(\\.(\\d){1}){0,1}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 价格校验，可有两位小数。
	public static boolean isPrice(String str) {
		String regex = "\\d+(\\.(\\d){0,2}){0,1}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 数字校验
	public static boolean isNum(String str) {
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证金额
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

}
