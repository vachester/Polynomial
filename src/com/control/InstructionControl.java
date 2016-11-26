package com.control;

import com.entity.Instruction;

public class InstructionControl {
	// 定义结果，由各个实体类返回，并且可以返回给边界类
	private String result;
	// 用户输入和表达式，由边界类传来
	private String ins;
	private String ex;

	public InstructionControl() {
	}

	public InstructionControl(String ins, String ex) {
		this.ins = ins;
		this.ex = ex;
	}

	public void setIns(String input) {
		this.ins = input;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public String getResult() {
		return result;
	}

	public void toInstruction() {
		Instruction myInstruction = new Instruction(ex, ins);
		// 先初始化各种数据
		myInstruction.setItem();
		// 判断操作类型
		if (ins.charAt(1) == 's') {
			myInstruction.simplify();
		} else {
			myInstruction.derivative();
		}
		myInstruction.toResult();
		this.result = myInstruction.getResult();
	}
}
