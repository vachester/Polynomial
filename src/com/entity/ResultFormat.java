package com.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultFormat {

	public static List<Map<String, String>> restore(List<Map<String, String>> tmpFormat) {
		List<Map<String, String>> resultFormat = new ArrayList<Map<String, String>>();
		Map<String, String> temp;
		for (Map<String, String> mdata : tmpFormat) {
			temp = new HashMap<String, String>();
			temp.putAll(mdata);
			resultFormat.add(temp);
		}
		return resultFormat;
	}

	public static void combine(List<Map<String, String>> resultFormat) {
		Map<String, String> temp1;
		Map<String, String> temp2;
		int sum = 0;
		String status1;
		String status2;
		for (int i = 0; i < resultFormat.size(); i++) {
			temp1 = new HashMap<String, String>(resultFormat.get(i));
			temp1.remove("sum");
			status1 = temp1.get("status");
			temp1.remove("status");
			for (int j = i + 1; j < resultFormat.size(); j++) {
				temp2 = new HashMap<String, String>(resultFormat.get(j));
				temp2.remove("sum");
				status2 = temp2.get("status");
				temp2.remove("status");
				if (temp1.equals(temp2) && !resultFormat.get(j).containsKey("no")
						&& !resultFormat.get(i).containsKey("no")) {
					if (status1.equals(status2)) {
						sum = Integer.parseInt(resultFormat.get(i).get("sum"))
								+ Integer.parseInt(resultFormat.get(j).get("sum"));
					} else {
						if (status1.equals("+")) {
							sum = Integer.parseInt(resultFormat.get(i).get("sum"))
									- Integer.parseInt(resultFormat.get(j).get("sum"));
						} else {
							sum = Integer.parseInt(resultFormat.get(j).get("sum"))
									- Integer.parseInt(resultFormat.get(i).get("sum"));
						}
						if (sum >= 0) {
							resultFormat.get(i).put("status", "+");
						} else {
							resultFormat.get(i).put("status", "-");
							sum = Math.abs(sum);
						}
					}
					resultFormat.get(i).put("sum", Integer.toString(sum));
					resultFormat.get(j).put("no", "no");
				}
			}
		}
	}

	public static String show(List<Map<String, String>> resultFormat) {
		String result = "";
		int count = 0;
		// 将含有{"no","no"}或者系数为0的Map项移去
		for (int i = 0; i < resultFormat.size(); i++) {
			if (resultFormat.get(i).containsKey("no") || Integer.parseInt(resultFormat.get(i).get("sum")) == 0) {
				resultFormat.remove(i);
				i--;
			}
		}
		for (Map<String, String> mdata : resultFormat) {
			String sum = mdata.get("sum");
			mdata.remove("sum");
			String status = mdata.get("status");
			mdata.remove("status");
			if (count == 0 && status.equals("-")) {
				result = result + "-";
			} else if (count > 0) {
				if (status.equals("-")) {
					result = result + "-";
				} else {
					result = result + "+";
				}
			}
			if (Math.abs(Integer.parseInt(sum)) > 1) {
				if (sum.charAt(0) == '-') {
					result = result + sum.substring(1);
				} else {
					result = result + sum;
				}
			} else if (Integer.parseInt(sum) == 1 && mdata.isEmpty()) {
				result = result + "1";
			}
			boolean flag = true;
			if (!mdata.isEmpty()) {
				for (final String key : mdata.keySet()) {
					if (flag && Math.abs(Integer.parseInt(sum)) == 1) {
						result = result + key;
						flag = false;
						if (Integer.parseInt(mdata.get(key)) > 1) {
							result = result + '^' + Integer.parseInt(mdata.get(key));
						}
					} else {
						result = result + "*" + key;
						if (Integer.parseInt(mdata.get(key)) > 1) {
							result = result + '^' + Integer.parseInt(mdata.get(key));
						}
					}
				}
			}
			count++;
		}
		if (result.equals("")) {
			result = "0";
		}
		return result;
	}
}
