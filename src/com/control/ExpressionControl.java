package com.control;
import com.entity.Expression;

public class ExpressionControl {
	//定义结果，由各个实体类返回，并且可以返回给边界类
	private String result;
	//用户输入，由边界类传来
	private String ex;
	
	public ExpressionControl() {
	}
	
	public ExpressionControl(String input) {
		this.ex = input;
	}
	
	public void setEx(String input) {
		this.ex = input;
	}
	
	public String getResult() {
		return result;
	}
	
	public String getEx() {
		return ex;
	}

	public void toExpression() {
		Expression myExpression = new Expression(ex);
		//进行预处理
		myExpression.preFormat();
		//进行简化合并
		myExpression.toResult();
		this.result = myExpression.getResult();
	}
	
}
