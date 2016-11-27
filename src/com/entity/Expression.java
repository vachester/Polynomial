package com.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
	// 定义结果，返回给控制类
	private String result;
	// 用户输入，由控制类传来
	private String ex;
	// 定义表达式的处理结果，其中tmp*用于重新赋值，result*才是结果
	private List<Map<String, String>> tmpFormat;
	private List<Map<String, String>> resultFormat;

	public Expression(String input) {
		ex = input;
	}

	public String getEx() {
		return ex;
	}

	public String getResult() {
		return result;
	}
	
	public List<Map<String, String>> getTmp() {
		return tmpFormat;
	}
	
	public boolean isCorrect(String input) {
		Pattern pattern = Pattern.compile("[\\^+*a-z0-9.^-]*");
		Matcher matcher = pattern.matcher(input);
		boolean b = matcher.matches();
		return b;
	}
	
	/*
	 * 进行初次处理 得到的是一个嵌套着Map的List 每一个Map对应着表达式的每一项
	 * sum,status是每个Map的必有key，表示常数系数和正负号
	 */
	public void preFormat() {
		String operato = ex.replaceAll("\\s*", "");
		tmpFormat = new ArrayList<Map<String, String>>();
		// 表达式以"+"分割并且存在mclassify字符串数组中
		String[] mclassify = operato.split("\\+");
		String[] nclassify;
		Map<String, String> temp;
		// 将常量相乘存于sum
		int sum = 1;
		int count;
		String value;
		for (final String mdata : mclassify) {
			// 对于mclassify中每项再用"-"分割
			String[] msplit = mdata.split("-");
			for (int i = 0; i < msplit.length; i++) {
				if (!msplit[i].equals("")) {
					temp = new HashMap<String, String>();
					// status表明了该项的正负情况
					if (i == 0) {
						temp.put("status", "+");
					} else {
						temp.put("status", "-");
					}
					// 最后以"*"分割每一个乘数
					nclassify = msplit[i].split("\\*");
					for (String ndata : nclassify) {
						if (ndata.charAt(0) >= '0' && ndata.charAt(0) <= '9') {
							sum = sum * Integer.parseInt(ndata);
						} else if (ndata.charAt(0) >= 'a' && ndata.charAt(0) <= 'z') {
							if (ndata.indexOf('^') >= 0) {
								value = ndata.substring(0, ndata.indexOf('^'));
								if (temp.containsKey(value)) {
									count = Integer.parseInt(temp.get(value))
											+ Integer.parseInt(ndata.substring(ndata.indexOf('^') + 1));
								} else {
									count = Integer.parseInt(ndata.substring(ndata.indexOf('^') + 1));
								}
								temp.put(value, Integer.toString(count));
							} else {
								if (temp.containsKey(ndata)) {
									count = Integer.parseInt(temp.get(ndata)) + 1;
								} else {
									count = 1;
								}
								temp.put(ndata, Integer.toString(count));
							}
						}
					}
					temp.put("sum", Integer.toString(sum));
					sum = 1;
					tmpFormat.add(temp);
				}
			}
		}
	}
	
	public void toResult() {
		this.resultFormat = ResultFormat.restore(tmpFormat);
		ResultFormat.combine(resultFormat);
		this.result = ResultFormat.show(resultFormat);
	}
}
