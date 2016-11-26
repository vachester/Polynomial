import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangsheng chenqiming
 */
public class Polynomial
{
    /* 用Map数组来分项存储多项式， */
    /**
     * saveFormat是原始表达式格式
     */
    private List<Map<String, String>> saveFormat;
    /**
     * result是结果字符串
     */
    private String                    result;
    /**
     * resultFormat是计算后的表达式格式
     */
    private List<Map<String, String>> resultFormat;

    /**
     * @Method: expression @Description: TODO @param operatop @return
     *          void @throws
     */
    public void expression(String operatop)

    {
        if (operatop.charAt(0) == '!')
        {
            restore();
            if (operatop.charAt(1) == 's')

            {
                simplify(operatop);
            }
            else

            {
                derivative(operatop);
            }
            combine();
            show();
            restore();
        }
        else

        {
            String operato = operatop.replaceAll("\\s*", "");
            saveFormat = new ArrayList<Map<String, String>>();
            // 表达式以"+"分割并且存在mclassify字符串数组中
            String[] mclassify = operato.split("\\+");
            String[] nclassify;
            Map<String, String> temp;
            // 将常量相乘存于sum
            int sum = 1;
            int count;
            String value;
            for (final String mdata : mclassify)

            {
                // 对于mclassify中每项再用"-"分割
                String[] msplit = mdata.split("-");
                for (int i = 0; i < msplit.length; i++)

                {
                    if (!msplit[i].equals(""))

                    {
                        temp = new HashMap<String, String>();
                        // status表明了该项的正负情况
                        if (i == 0)

                        {
                            temp.put("status", "+");
                        }
                        else

                        {
                            temp.put("status", "-");
                        }
                        // 最后以"*"分割每一个乘数
                        nclassify = msplit[i].split("\\*");
                        for (String ndata : nclassify)

                        {
                            if (ndata.charAt(0) >= '0' && ndata.charAt(0) <= '9')

                            {
                                sum = sum * Integer.parseInt(ndata);
                            }
                            else if (ndata.charAt(0) >= 'a' && ndata.charAt(0) <= 'z')

                            {
                                if (ndata.indexOf('^') >= 0)

                                {
                                    value = ndata.substring(0, ndata.indexOf('^'));
                                    if (temp.containsKey(value))

                                    {
                                        count = Integer.parseInt(temp.get(value))
                                                + Integer.parseInt(ndata.substring(ndata.indexOf('^') + 1));
                                    }
                                    else

                                    {
                                        count = Integer.parseInt(ndata.substring(ndata.indexOf('^') + 1));
                                    }
                                    temp.put(value, Integer.toString(count));
                                }
                                else

                                {
                                    if (temp.containsKey(ndata))

                                    {
                                        count = Integer.parseInt(temp.get(ndata)) + 1;
                                    }
                                    else

                                    {
                                        count = 1;
                                    }
                                    temp.put(ndata, Integer.toString(count));
                                }
                            }
                        }
                        temp.put("sum", Integer.toString(sum));
                        sum = 1;
                        saveFormat.add(temp);
                    }
                }
            }
            restore();
            combine();
            show();
        }
    }

    /*
     * 合并操作，将可以合并的两项合并 通过两层循环进行遍历比较，深拷贝每一个Map项，并且将status和sum移去然后比较
     * 已经被合并的项被加入{"no","no"}
     */
    /**
     * @Method: combine @Description: TODO @param @throws
     */
    public void combine()

    {
        Map<String, String> temp1;
        Map<String, String> temp2;
        int sum = 0;
        String status1;
        String status2;
        for (int i = 0; i < resultFormat.size(); i++)

        {
            temp1 = new HashMap<String, String>(resultFormat.get(i));
            temp1.remove("sum");
            status1 = temp1.get("status");
            temp1.remove("status");
            for (int j = i + 1; j < resultFormat.size(); j++)

            {
                temp2 = new HashMap<String, String>(resultFormat.get(j));
                temp2.remove("sum");
                status2 = temp2.get("status");
                temp2.remove("status");
                if (temp1.equals(temp2) && !resultFormat.get(j).containsKey("no")
                        && !resultFormat.get(i).containsKey("no"))

                {
                    if (status1.equals(status2))

                    {
                        sum = Integer.parseInt(resultFormat.get(i).get("sum"))
                                + Integer.parseInt(resultFormat.get(j).get("sum"));
                    }
                    else

                    {
                        if (status1.equals("+"))

                        {
                            sum = Integer.parseInt(resultFormat.get(i).get("sum"))
                                    - Integer.parseInt(resultFormat.get(j).get("sum"));
                        }
                        else

                        {
                            sum = Integer.parseInt(resultFormat.get(j).get("sum"))
                                    - Integer.parseInt(resultFormat.get(i).get("sum"));
                        }
                        if (sum >= 0)

                        {
                            resultFormat.get(i).put("status", "+");
                        }
                        else

                        {
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

    /**
     * @Method: simplify @Description: TODO @param operato @return void @throws
     */
    public void simplify(String operato)

    {
        String[] assignment = operato.split(" ");
        String key;
        String value;
        String num;
        double sum;
        if (assignment.length > 1)

        {
            for (int i = 1; i < assignment.length; i++)

            {
                key = assignment[i].substring(0, assignment[i].indexOf('='));
                num = assignment[i].substring(assignment[i].indexOf('=') + 1);
                for (Map<String, String> mdata : resultFormat)

                {
                    if (mdata.containsKey(key))

                    {
                        value = mdata.get(key);
                        sum = Integer.parseInt(mdata.get("sum")) * Math.pow(Integer.parseInt(num), Integer.parseInt(value));
                        mdata.put("sum", Integer.toString((int) Math.abs(sum)));
                        mdata.remove(key);
                        if (sum > 0 && mdata.get("status").equals("+") || (sum < 0 && mdata.get("status").equals("-")))

                        {
                            mdata.put("status", "+");
                        }
                        else

                        {
                            mdata.put("status", "-");
                        }
                    }
                }
            }
        }
        for (int i = 0; i < resultFormat.size(); i++) {
			System.out.println(resultFormat.get(i));
		}
    }

    /**
     * @Method: derivative @Description: TODO @param operato @return
     *          void @throws
     */
    public void derivative(String operato)
    {
        String key = operato.substring(4);
        String value;
        int valueNew;
        int sum;
        for (int i = 0; i < resultFormat.size(); i++)

        {
            if (resultFormat.get(i).containsKey(key))

            {
                value = resultFormat.get(i).get(key);
                valueNew = Integer.parseInt(value) - 1;
                sum = Integer.parseInt(resultFormat.get(i).get("sum")) * (valueNew + 1);
                resultFormat.get(i).put("sum", Integer.toString(sum));
                if (valueNew > 0)

                {
                    resultFormat.get(i).put(key, Integer.toString(valueNew));
                }
                else

                {
                    resultFormat.get(i).remove(key);
                }
            }
            else

            {
                resultFormat.remove(i);
                i--;
            }
        }
    }

    // 将resultFormat回复到saveFormat状态，以便下一次操作
    /**
     * @Method: restore @Description: TODO @throws
     */
    public void restore()

    {
        resultFormat = new ArrayList<Map<String, String>>();
        Map<String, String> temp;
        for (Map<String, String> mdata : saveFormat)

        {
            temp = new HashMap<String, String>();
            temp.putAll(mdata);
            resultFormat.add(temp);
        }
    }

    /**
     * @Method: show @Description: TODO @throws
     */
    public void show()

    {
        result = "";
        int count = 0;
        // 将含有{"no","no"}或者系数为0的Map项移去
        for (int i = 0; i < resultFormat.size(); i++)

        {
            if (resultFormat.get(i).containsKey("no") || Integer.parseInt(resultFormat.get(i).get("sum")) == 0)

            {
                resultFormat.remove(i);
                i--;
            }
        }
        for (Map<String, String> mdata : resultFormat)

        {
            String sum = mdata.get("sum");
            mdata.remove("sum");
            String status = mdata.get("status");
            mdata.remove("status");
            if (count == 0 && status.equals("-"))

            {
                result = result + "-";
            }
            else if (count > 0)

            {
                if (status.equals("-"))

                {
                    result = result + "-";
                }
                else

                {
                    result = result + "+";
                }
            }
            if (Math.abs(Integer.parseInt(sum)) > 1)

            {
                if (sum.charAt(0) == '-')

                {
                    result = result + sum.substring(1);
                }
                else

                {
                    result = result + sum;
                }
            }
            else if (Integer.parseInt(sum) == 1 && mdata.isEmpty())

            {
                result = result + "1";
            }
            boolean flag = true;
            if (!mdata.isEmpty())

            {
                for (final String key : mdata.keySet())

                {
                    if (flag && Math.abs(Integer.parseInt(sum)) == 1)

                    {
                        result = result + key;
                        flag = false;
                        if (Integer.parseInt(mdata.get(key)) > 1)

                        {
                            result = result + '^' + Integer.parseInt(mdata.get(key));
                        }
                    }
                    else

                    {
                        result = result + "*" + key;
                        if (Integer.parseInt(mdata.get(key)) > 1)

                        {
                            result = result + '^' + Integer.parseInt(mdata.get(key));
                        }
                    }
                }
            }
            count++;
        }
        if (result.equals(""))

        {
            result = "0";
        }
    }

    @Override
    public String toString()

    {
        return result;
    }
}