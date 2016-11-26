package com.entity;
import java.util.List;
import java.util.Map;

public class Instruction {
	// 定义结果，返回给控制类
	private String result;
	// 用户输入，由控制类传来
	private String ins;
	private String ex;
	// 申明一个表达式实体类
	private Expression myExpression;
	// 定义表达式的处理结果，其中tmp*用于重新赋值，result*才是结果
	private List<Map<String, String>> tmpFormat;
	private List<Map<String, String>> resultFormat;

	public Instruction(String ex, String ins) {
		this.ex = ex;
		this.ins = ins;
	}

	// 初始化各种数据
	public void setItem() {
		myExpression = new Expression(ex);
		myExpression.preFormat();
		tmpFormat = myExpression.getTmp();
		resultFormat = ResultFormat.restore(tmpFormat);
	}

	// 赋值
	public void simplify() {
		String[] assignment = ins.split(" ");
		String key;
		String value;
		String num;
		double sum;
		if (assignment.length > 1) {
			for (int i = 1; i < assignment.length; i++) {
				key = assignment[i].substring(0, assignment[i].indexOf('='));
				num = assignment[i].substring(assignment[i].indexOf('=') + 1);
				for (Map<String, String> mdata : resultFormat) {
					if (mdata.containsKey(key)) {
						value = mdata.get(key);
						sum = Integer.parseInt(mdata.get("sum"))
								* Math.pow(Integer.parseInt(num), Integer.parseInt(value));
						mdata.put("sum", Integer.toString((int) Math.abs(sum)));
						mdata.remove(key);
						if (sum > 0 && mdata.get("status").equals("+")
								|| (sum < 0 && mdata.get("status").equals("-"))) {
							mdata.put("status", "+");
						} else {
							mdata.put("status", "-");
						}
					}
				}
			}
		}
	}

	// 求导
	public void derivative() {
		String key = ins.substring(4);
		String value;
		int valueNew;
		int sum;
		for (int i = 0; i < resultFormat.size(); i++) {
			if (resultFormat.get(i).containsKey(key)) {
				value = resultFormat.get(i).get(key);
				valueNew = Integer.parseInt(value) - 1;
				sum = Integer.parseInt(resultFormat.get(i).get("sum")) * (valueNew + 1);
				resultFormat.get(i).put("sum", Integer.toString(sum));
				if (valueNew > 0) {
					resultFormat.get(i).put(key, Integer.toString(valueNew));
				} else {
					resultFormat.get(i).remove(key);
				}
			} else {
				resultFormat.remove(i);
				i--;
			}
		}
	}

	public void toResult() {
		ResultFormat.combine(resultFormat);
		this.result = ResultFormat.show(resultFormat);
	}
	
	public String getResult() {
		return result;
	}
}
