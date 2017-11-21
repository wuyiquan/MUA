import java.util.Scanner; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.String;

class Global{
	  public static Map<String, String> map = new HashMap<String, String>();
	  public static Scanner s = new Scanner(System.in);
	  public static Set<String> noReturn = new HashSet<String>();
	  public static Set<String> hasReturn = new HashSet<String>();
	  public final static String TRUE = "true";
	  public final static String  FALSE = "false";
	  public static String Input = null;//需要处理的命令
	}
class Operator{
	
	public String interpreter() //读取第一个指令
	{
		String str = this.nextWord();
		return this.control(str);
	}
	public String control(String str)//根据不同指令来调用相应指令函数 
	{
		if (str.equals("make"))
		{
			String Key = this.getValue();
			String Value = this.getValue();
			this.make(Key, Value);
			return null;
		}
		if (str.equals("thing"))
		{
			String Key = this.getValue();
			return this.thing(Key);
		}
		if (str.equals("erase"))
		{
			String Key = this.getValue();
			this.erase(Key);
			return null;
		}
		if (str.equals("isname"))
		{
			String Key = this.getValue();
			return this.isname(Key);
		}
		if (str.equals("print"))
		{
			String Value = this.getValue();
			this.print(Value);
			return null;
		}
		if (str.equals("read"))
			return this.read();
		if (str.equals("readlinst"))
			return this.readlinst();
		if (str.equals("add") || str.equals("sub") || str.equals("mul") || str.equals("div") || str.equals("mod"))
		{
			String Value1 = this.getValue();
			String Value2 = this.getValue();
			return this.calculate(str, Value1, Value2);
		}
		if (str.equals("eq") || str.equals("gt") || str.equals("lt"))
		{
			String Value1 = this.getValue();
			String Value2 = this.getValue();
			return this.compare(str, Value1, Value2);
		}
		if (str.equals("and") || str.equals("or") || str.equals("not"))
		{
			String Value1 = this.getValue();
			String Value2 = null;
			if (!str.equals("not"))
				Value2 = this.getValue();
			return this.logic(str, Value1, Value2);
		}
		System.out.println("编译器不知道你要干啥！"+ str);
		System.exit(0);
		return null;
	}
	public boolean isEmpty(String str)//判断一个字符串是否只含有无效字符
	{
		if (str == null) return true;
		for (int i=0; i<str.length(); i++)
			if ((str.charAt(i) != ' ')&&(str.charAt(i) != '\n')&&(str.charAt(i) != '\t'))
				return false;
		return true;
	}
	public String nextWord()//读取input字符串中下一个word
	{
		if (!this.isEmpty(Global.Input))
		{
			if (Global.Input.startsWith("//"))
				Global.Input = null;
			else if (Global.Input.contains("//"))
				Global.Input = Global.Input.substring(0, Global.Input.indexOf("//"));
			else Global.Input = Global.Input.replaceAll(" +", " ");
		}
		while (this.isEmpty(Global.Input))
		{
			while (this.isEmpty(Global.Input))
				Global.Input = Global.s.nextLine();
			if (Global.Input.startsWith("//"))
				Global.Input = null;
			else if (Global.Input.contains("//"))
				Global.Input = Global.Input.substring(0, Global.Input.indexOf("//"));
			else Global.Input = Global.Input.replaceAll(" +", " ");
		}
		String Value = Global.Input.split(" ")[0];
		if (Global.Input.contains(" "))
		{
			Global.Input = Global.Input.substring(Global.Input.indexOf(" "));
	   		Global.Input = Global.Input.trim();
		}
		else Global.Input = null;
	   	return Value;
	}
	public String getValue()//得到下一个值，如果需要执行别的指令来获得值，那么会先去进行别的指令
	{
	   	String Value = this.nextWord();

    	if (Global.hasReturn.contains(Value))
    	{
    		Value = this.control(Value);
    		return Value;
    	}	
    	if (this.isWord(Value))
    		return Value;
    	if (this.isNumber(Value))
    		return Value;
    	if (this.isBoolean(Value))
    		return Value;
    	if (Value.startsWith("["))
    	{
    	int left=1;
    	int right=0;
    	Value = Value.substring(1);
    	String str = new String(Value);
    	Value = "";
    	while (left > right)
    	{
    		for (int x = 0; x < str.length(); x++)
    		{
    			if (left == right)
	    		{
	    			System.out.println("[]结束后需要空格分开！  " + str);
	        		System.exit(0);
	    		}
    				if (str.charAt(x) == '[')
    					left++;
    				if (str.charAt(x) == ']')
    					right++;
    				if (right > left)
    	    		{
    	    			System.out.println("[]不对称  " + str);
    	        		System.exit(0);
    	    		}
    		 }
    		String str1= new String();
    		for (int x = 0; x < str.length(); x++)
    		{
			if (str.charAt(x) == '[')
				str1 = str1 + "[ ";
			if (str.charAt(x) == ']')
				str1 = str1 + " ]";
			if (str.charAt(x) != '[' && str.charAt(x)!= ']')
				str1 = str1 + str.charAt(x);
    		}
    		str1 = str1.replaceAll(" +", " ");
    		Value = Value + " " + str1;
    		if (left > right)
    			str = this.nextWord();
    	}
    	Value = "[" + Value;
    	Value = Value.replaceAll(" +", " ");
    	return Value;
    	}
    	if (Value.substring(0,1).equals(":"))
    	{
    		if (Global.map.containsKey("\"" + Value.substring(1)))
    			return Global.map.get("\"" + Value.substring(1));
    		else
    		{
    			System.out.println("Key值不存在！ " + Value.substring(1));
        		System.exit(0);
    		}
    	}
    	System.out.println("没有该类型的Value！ "+ Value);
		System.exit(0);
		return null;
	}


public boolean isNumber(String Value)
	{
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*"); 
		   Matcher isNum = pattern.matcher(Value);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}
public boolean isWord(String Value)
	{
		   if (Value.startsWith("\"") && !Value.substring(1).contains(":") && !Value.substring(1).contains("\"") && !Value.substring(1).contains("[") && !Value.substring(1).contains("]"))
		       return true; 
		   return false; 
	}
public boolean isBoolean(String Value)
	{
		if (Value.equals(Global.TRUE) || Value.equals(Global.FALSE))
			return true;
		return false;
	}
public boolean isList(String Value) {
	if (!Value.startsWith("["))
		return false;
	if (!Value.endsWith("]"))
		return false;
	return true;
}
    public void make(String Key, String Value) {
	if (!this.isWord(Key))
	{
		System.out.println("Key值不是word! " + Key);
		System.exit(0);
	}
	Global.map.put(Key, Value);
	//System.out.println(Global.map);
	return;
}
	public String thing(String Key){
		if (Global.map.containsKey(Key))
			return Global.map.get(Key);
		else
		{
			System.out.println("Key值不存在！" + Key);
    		System.exit(0);
		}
		return null;
	}
	public String erase(String Key){
		if (Global.map.containsKey(Key))
			return Global.map.remove(Key);
		else
		{
			System.out.println("Key值异常！" + Key);
    		System.exit(0);
		}
		return null;
	}
	public String isname(String Key){
		if (Global.map.containsKey(Key))
			return Global.TRUE;
		else
			return Global.FALSE;
	}
	public String readlinst(){
		String str = Global.s.nextLine();
		str = "[ " + str + " ]";
		str = str.replaceAll(" +", " ");
		return str;
	}
	public String print(String Value){
		if (this.isWord(Value))
			Value = Value.substring(1);
		System.out.println(Value);
		return null;
	}
	public String read(){
		String Value = this.nextWord();
		if (!this.isNumber(Value) && !this.isWord(Value)  && !this.isBoolean(Value))
		{	
			System.out.println("读取了非数字、单词！ " + Value);
    		System.exit(0);
    		return null;
    	}
		else 	return Value;
	}
	public String calculate(String str, String Value1, String Value2){
		double d1, d2;
		if (!this.isNumber(Value1))
    	{	
			System.out.println("Value1值异常！ " + Value1);
    		System.exit(0);
    	}
		d1 = new Double(Value1).doubleValue();
		if (!this.isNumber(Value2))
    	{	
			System.out.println("Value2值异常！ " + Value2);
    		System.exit(0);
    	}
		d2 = new Double(Value2).doubleValue();
		String result = new String();
		if (str.equals("add"))
			return result.valueOf(d1+d2);
		if (str.equals("sub"))
			return result.valueOf(d1-d2);
		if (str.equals("mul"))
			return result.valueOf(d1*d2);
		if (str.equals("div"))
		{
			if (d2==0.0)
			{
				System.out.println("被除数不能为0！");
    			System.exit(0);
			}
    		return result.valueOf(d1/d2);
		}
		if (str.equals("mod"))
		{
			if ((Value1.contains(".")) || (Value2.contains(".")))
			{
				System.out.println("取模必须是整数！ ");
    			System.exit(0);
			}
			return result.valueOf(d1 % d2);
		}
		return null;
	}
	public String compare(String str, String Value1, String Value2){
		if (str.equals("eq"))
			if (Value1.equals(Value2))
				return Global.TRUE;
			else
				return Global.FALSE;
		if (str.equals("gt"))
			if (Value1.compareTo(Value2) > 0)
				return Global.TRUE;
			else
				return Global.FALSE;
		if (Value1.compareTo(Value2) < 0)
			return Global.TRUE;
		else
			return Global.FALSE;
	}
	public String logic(String str, String Value1, String Value2){
		if (!Value1.equals(Global.TRUE) && !Value1.equals(Global.FALSE))
    	{	
			System.out.println("Value1值异常！ " + Value1);
    		System.exit(0);
    	}
		if (str.equals("not"))
		{
			if (Value1.equals(Global.TRUE))
					return Global.FALSE;
			else
				return Global.TRUE;
		}
		if (!Value2.equals(Global.TRUE) && !Value2.equals(Global.FALSE))
    	{	
			System.out.println("Value2值异常！ " + Value2);
    		System.exit(0);
    	}
		if (str.equals("and"))
		{
			if (Value1.equals(Global.TRUE) && Value2.equals(Global.TRUE))
					return Global.TRUE;
			else
				return Global.FALSE;
		}
		if (str.equals("or"))
		{
			if (Value1.equals(Global.TRUE) || Value2.equals(Global.TRUE))
					return Global.TRUE;
			else
				return Global.FALSE;
		}
		return null;
}
}

public class Main {

	public static void main(String[] args) {

		Operator op = new Operator();
		Global.noReturn.add("make");
		Global.noReturn.add("print");
		Global.noReturn.add("erase");
		Global.hasReturn.add("thing");
		Global.hasReturn.add("isname");
		Global.hasReturn.add("read");
		Global.hasReturn.add("readlinst");
		Global.hasReturn.add("add");
		Global.hasReturn.add("sub");
		Global.hasReturn.add("mul");
		Global.hasReturn.add("div");
		Global.hasReturn.add("mod");
		Global.hasReturn.add("eq");
		Global.hasReturn.add("gt");
		Global.hasReturn.add("lt");
		Global.hasReturn.add("and");
		Global.hasReturn.add("or");
		Global.hasReturn.add("not");
		
		
		while (Global.s.hasNext()){   
			Global.Input = Global.s.nextLine();
			Global.Input = Global.Input.trim();
			//System.out.println(str);
			if (Global.Input.equals("end")) {
	    		System.out.println("886");
	    		System.exit(0);}
			while (Global.Input != null)
			{
				String newstr = op.interpreter();
				if (newstr != null)
					if (op.isList(newstr))
					{
						if (Global.Input != null)
							Global.Input = newstr.substring(1,newstr.length()-1) +" " +  Global.Input;
						else
							Global.Input = newstr.substring(1,newstr.length()-1);
						Global.Input = Global.Input.trim();
					}
					else
					{
						System.out.println("编译器不知道你要干什么！"+ newstr);
			    		System.exit(0);
					}
			}
          } 
		Global.s.close();
	}

}
