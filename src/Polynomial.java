import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Polynomial {
	/*用Map数组来分项存储多项式，save_format是原始表达式格式
	 * result_format是计算后的表达式格式
	 */
	private String result;
	private ArrayList<Map<String,String>> save_format,result_format;
	
	
	public void expression(String opp){
		if(opp.charAt(0) != '!'){
			String op = opp.replaceAll("\\s*", "");
			save_format = new ArrayList<Map<String,String>>();
			//表达式以"+"分割并且存在mclassify字符串数组中
			String[] mclassify = op.split("\\+");
			String[] nclassify;
			Map<String,String> temp;
			//将常量相乘存于sum
			int sum = 1;
			int count;
			String value;
			for(String mdata : mclassify){
				//对于mclassify中每项再用"-"分割
				String[] msplit = mdata.split("-");
				for(int i = 0; i < msplit.length; i++){
					if(!msplit[i].equals("")){
						temp = new HashMap<String,String>();
						//status表明了该项的正负情况
						if(i == 0){
							temp.put("status","+");
						}
						else{
							temp.put("status", "-");
						}
						//最后以"*"分割每一个乘数
						nclassify = msplit[i].split("\\*");
						for(String ndata : nclassify){
							if(ndata.charAt(0) >= '0' && ndata.charAt(0) <= '9'){
								sum = sum*Integer.parseInt(ndata);
							}
							else if(ndata.charAt(0) >= 'a' && ndata.charAt(0) <= 'z'){
								if(ndata.indexOf("^") >= 0){
									value = ndata.substring(0, ndata.indexOf("^"));
									if(temp.containsKey(value)){
										count = Integer.parseInt(temp.get(value)) + Integer.parseInt(ndata.substring(ndata.indexOf("^")+1));
									}
									else{
										count = Integer.parseInt(ndata.substring(ndata.indexOf("^")+1));
									}
									temp.put(value, Integer.toString(count));
								}
								else{
									if(temp.containsKey(ndata)){
										count = Integer.parseInt(temp.get(ndata)) + 1;
									}
									else{
										count = 1;
									}
									temp.put(ndata, Integer.toString(count));
								}
							}
						}
						temp.put("sum", Integer.toString(sum));
						sum = 1;
						save_format.add(temp);
					}
				}	
			}	
			restore();
			combine();
			show();
		}
		else{
			restore();
			if(opp.charAt(1) == 's'){
				simplify(opp);
			}
			else{
				derivative(opp);
			}
			combine();
			show();
			restore();
		}
	}
	
	
	/*合并操作，将可以合并的两项合并
	 * 通过两层循环进行遍历比较，深拷贝每一个Map项，并且将status和sum移去然后比较
	 * 已经被合并的项被加入{"no","no"}
	 */
	public void combine(){
		Map<String,String> temp1,temp2;
		int sum=0;
		String status1,status2;
		for(int i = 0; i < result_format.size(); i++){
			temp1 = new HashMap<String,String>(result_format.get(i));
			temp1.remove("sum");
			status1 = temp1.get("status");
			temp1.remove("status");
			for(int j = i+1; j < result_format.size(); j++){
				temp2 = new HashMap<String,String>(result_format.get(j));
				temp2.remove("sum");
				status2 = temp2.get("status");
				temp2.remove("status");
				if(temp1.equals(temp2) && !result_format.get(j).containsKey("no") && !result_format.get(i).containsKey("no")){
					if(status1.equals(status2)){
						sum = Integer.parseInt(result_format.get(i).get("sum")) + Integer.parseInt(result_format.get(j).get("sum"));
					}
					else{
						if(status1.equals("+")){
							sum = Integer.parseInt(result_format.get(i).get("sum")) - Integer.parseInt(result_format.get(j).get("sum"));
						}
						else{
							sum = Integer.parseInt(result_format.get(j).get("sum")) - Integer.parseInt(result_format.get(i).get("sum"));
						}
						if(sum >= 0){
							result_format.get(i).put("status", "+");
						}
						else{
							result_format.get(i).put("status", "-");
							sum = Math.abs(sum);
						}
					}
					result_format.get(i).put("sum", Integer.toString(sum));
					result_format.get(j).put("no", "no");
				}
			}
		}
	}
	
	public void simplify(String op){
		String[] assignment = op.split(" ");
		String key,value,num;
		double sum;
		if(assignment.length > 1){
			for(int i = 1; i < assignment.length; i++){
				key = assignment[i].substring(0,assignment[i].indexOf("="));
				num = assignment[i].substring(assignment[i].indexOf("=")+1);
				for(Map<String,String> mdata : result_format){
					if(mdata.containsKey(key)){
						value = mdata.get(key);
						sum = Integer.parseInt(mdata.get("sum"))*Math.pow(Integer.parseInt(num), Integer.parseInt(value));
						mdata.put("sum", Integer.toString((int)sum));
						mdata.remove(key);
						if(sum > 0){
							mdata.put("status", "+");
						}
						else{
							mdata.put("status", "-");
						}
					}			
				}
			}
		}		
	}
	
	public void derivative(String op){
		String key = op.substring(4);
		String value;
		int value_new;
		int sum;
		for(int i = 0; i < result_format.size(); i++){
			if(result_format.get(i).containsKey(key)){
				value = result_format.get(i).get(key);
				value_new = Integer.parseInt(value) - 1;
				sum = Integer.parseInt(result_format.get(i).get("sum"))*(value_new + 1);
				result_format.get(i).put("sum", Integer.toString(sum));
				if(value_new > 0){
					result_format.get(i).put(key, Integer.toString(value_new));					
				}
				else{
					result_format.get(i).remove(key);
				}
			}
			else{
				result_format.remove(i);
				i --;
			}
		}
	}
	
	
	//将result_format回复到save_format状态，以便下一次操作
	public void restore(){
		result_format = new ArrayList<Map<String,String>>();
		Map<String,String> temp;
		for(Map<String,String> mdata : save_format){
			temp = new HashMap<String,String>();
			temp.putAll(mdata);
			result_format.add(temp);
		}
	}
	
	public void show(){
		result = "";
		int count = 0;
		//将含有{"no","no"}或者系数为0的Map项移去
		for(int i = 0; i < result_format.size(); i++){
			if(result_format.get(i).containsKey("no") || Integer.parseInt(result_format.get(i).get("sum")) == 0){
				result_format.remove(i);
				i--;
			}
		}
		for(Map<String,String> mdata : result_format){
			String sum = mdata.get("sum");
			mdata.remove("sum");
			String status = mdata.get("status");
			mdata.remove("status");
			if(count == 0 && status.equals("-")){
				result = result + "-";
			}
			else if(count > 0){
				if(status.equals("-")){
					result = result + "-";
				}
				else{
					result = result + "+";
				}
			}
			if(Math.abs(Integer.parseInt(sum)) > 1 ){
				if(sum.charAt(0) != '-'){
					result = result + sum;
				}
				else {
					result = result + sum.substring(1);
				}
			}
			else if(Integer.parseInt(sum) == 1 && mdata.isEmpty()){
				result = result + "1";
			}
			boolean flag = true;
			if(!mdata.isEmpty()){
				for(String key : mdata.keySet()){
					if(flag && Math.abs(Integer.parseInt(sum)) == 1){	
						result = result + key;
						flag = false;
						if(Integer.parseInt(mdata.get(key)) > 1){
							result = result + "^"+Integer.parseInt(mdata.get(key));
						}
					}
					else{
						result = result + "*" + key;
						if(Integer.parseInt(mdata.get(key)) > 1){
							result = result + "^"+Integer.parseInt(mdata.get(key));
						}
					}
				}
			}
			count ++;
		}
		if(result.equals("")){
			result = "0";
		}
	}
	
	public String toString(){
		return result;
	}
}
