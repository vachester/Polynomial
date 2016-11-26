package com.boundary;
import java.util.Scanner;

import com.control.ExpressionControl;
import com.control.InstructionControl;


public class ExpressionLab {
	
	public static void main(String[] args)
    {
        String input;
        Scanner scan = new Scanner(System.in);
        System.out.print("> ");
        /*
         * 算法不支持除法、括号 且乘数间的‘*’不可省略 输出均为最简格式
         */
        ExpressionControl myExpressionControl = new ExpressionControl();
        InstructionControl myInstructionControl = new InstructionControl();
        while (scan.hasNext())
        {
            input = scan.nextLine();
            if(preTreat(input)) {
            	//若是表达式，实例化表达式控制类
            	myExpressionControl.setEx(input);
            	myExpressionControl.toExpression();
            	System.out.println(myExpressionControl.getResult());
            }
            else {
            	//否则，实例化指令控制类
            	myInstructionControl.setIns(input);
            	myInstructionControl.setEx(myExpressionControl.getEx());
            	myInstructionControl.toInstruction();
            	System.out.println(myInstructionControl.getResult());
            }
            System.out.print("> ");
        }

        scan.close();
    }
	
	/*
	 * 对用户的输入进行判断
	 * 若是表达式，返回真
	 * 若是指令，返回假
	 */
	public static Boolean preTreat(String input) {
    	if(input.charAt(0) == '!') {
    		return false;
    	}
    	return true;
    }
}
