import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		String input;
		Scanner scan = new Scanner(System.in);
		//实例化Polynomial类
		Polynomial myresult = new Polynomial();
		System.out.print("> ");
		/* 算法不支持除法、括号
		 * 且乘数间的‘*’不可省略
		 * 输出均为最简格式
		 */
		while(scan.hasNext()){		
			input = scan.nextLine();
			if(input.indexOf("(") >= 0 || input.indexOf(")") >= 0){
				System.out.println("Format error! Please input again!");
			}
			else{
				try{
					myresult.expression(input);
					System.out.println(myresult);
				}
				catch(StringIndexOutOfBoundsException exception){
					System.out.println("Format error! Please input again!");
				}
				catch(NumberFormatException exception){
					System.out.println("Format error! Please input again!");
				}
			}
			System.out.print("> ");
		}
		
		scan.close();
	}
	System.out.println("HelloWorld");
}
