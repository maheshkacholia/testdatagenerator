package com.gm.ccada.program;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QZ9Y83 on 6/16/2017.
 */
public class TestUtil {
  private static HashMap staticValueMap = new HashMap();
  public static String replaceVariableWithValue(String query,Map<String, String> valueMap)
  {
    Iterator<String> it = valueMap.keySet().iterator();
    String updatedQuery = query;
    while(it.hasNext())
    {
      String key = it.next();
      String value = valueMap.get(key);
      if (key != null && value != null)
        updatedQuery = updatedQuery.replaceAll("\\$" + key + "\\$", value);
    }
    return updatedQuery;
  }
  public static ArrayList<String> getVariables(String template)
  {
    //String input = "Hi John Doe, how are you? I'm Jane.";
    Matcher m = Pattern.compile("\\$([\\w.]*?)\\$").matcher(template);
    ArrayList<String> vars = new ArrayList<String>();
    while (m.find()) {
      System.out.println(m.group(1));
      if(!vars.contains(m.group(1)))
        vars.add(m.group(1));
    }
    return vars;
  }
  public static ArrayList<String> getAllPropertyVariables(String varString) {
    ArrayList<String> varList = getVariables(varString);
    ArrayList<String> finalVarList = new ArrayList<String>();

    HashMap<String, String> varMap = new HashMap<String, String>();
    for (String variable : varList) {
      if(variable.startsWith("property_")){
        finalVarList.add(variable.substring("property_".length()));
      }
    }
    return finalVarList;
  }

  public static String replacePlaceHolders(String varString,Map propertyMap,Properties actualProperty) throws Exception
  {
    ArrayList<String> varList = getVariables(varString);
    HashMap<String,String> varMap = new HashMap<String,String>();
    for(String variable:varList)
    {
      String varValue = generateStringFromPlaceHolder(variable,propertyMap,actualProperty);
      varMap.put(variable,varValue);
    }
    String updatedStr = replaceVariableWithValue(varString,varMap);
    return updatedStr;
  }
  public static String generateStringFromPlaceHolder(String placeholder,Map propertyMap,Properties actualProperty) throws Exception
  {


    String retString=placeholder;
 //   placeholder=placeholder.toLowerCase();
    if(placeholder.startsWith("property"))
    {
      String propertyname = placeholder.substring("property".length()+1);
      if(!propertyMap.containsKey(propertyname))
      {
        String propertyActualval = actualProperty.getProperty(propertyname);
        String replacedvalue = TestUtil.replacePlaceHolders(propertyActualval,propertyMap,actualProperty);
        propertyMap.put(propertyname,replacedvalue);
      }
      return (String)propertyMap.get(propertyname);

    }
    else if (placeholder.startsWith("inc"))
    {
      //   String number = placeholder.substring("num".length());
      String[] numarray = placeholder.split("_");
      String varName = numarray[1];
      String numdigit = numarray[2];
      String currValue = (String)staticValueMap.get(varName);
      if(currValue==null)
      {
         String val = generateNumric(Integer.parseInt(numdigit));
        staticValueMap.put(varName,val);
      }
      else
      {
        long val = Long.parseLong(currValue);
        val = val +1;
        staticValueMap.put(varName,val+"");
      }
      return staticValueMap.get(varName)+"";

    }
    else if(placeholder.startsWith("num"))
    {
   //   String number = placeholder.substring("num".length());
     String[] numarray = placeholder.split("_");
      return generateNumric(Integer.parseInt(numarray[1]));
    }
    else if (placeholder.startsWith("vin"))
    {
      return generateVIN();
    }
    else if(placeholder.startsWith("name"))
    {
      return generateString();
    }
    else if(placeholder.startsWith("date"))
    {
      return generateDate();
    }
    else if(placeholder.startsWith("timestamp"))
    {
      return generateTime();
    }
    else if(placeholder.startsWith("file"))
    {
      return generateDataFromFile(placeholder,actualProperty);
    }
    else if(placeholder.startsWith("fun"))
    {
      return generateDataFromFunction(placeholder,propertyMap, actualProperty);
    }

    return retString;

  }
  public static String generateDataFromFunction(String placeholder,Map propertyMap,Properties actualProperty)
  {

      return null;
  }
  private static String generateDataFromFile(String placeholder,Properties actualProperty) throws Exception{
    ArrayList<String> fileData= Utility.getDataForProperty(placeholder,actualProperty);
    Random r = new Random();
    long pos= r.nextInt(fileData.size());
    return fileData.get((int)pos);
  }

  public static String generateNumric(int noofdigit)
  {
    if(noofdigit==1)
    {
      Random r = new Random();
      return r.nextInt(10)+"";

    }
    long base = (long)Math.pow(10,noofdigit-1);
    long  range = (long)(base * 0.9) ;
    Random r = new Random();
    long randlong = r.nextInt((int)range);
    return (base + randlong)+"";
  }
  public static void main(String[] args) throws Exception
  {
//    System.out.println("1="+generateNumric(1));
//    System.out.println("2="+generateNumric(2));
//    System.out.println("3="+generateNumric(3));
//    System.out.println("4="+generateNumric(4));
//    System.out.println("5="+generateNumric(5));
//    System.out.println("6="+generateNumric(6));
//    System.out.println("7="+generateNumric(7));
//    System.out.println("8="+generateNumric(8));
//    System.out.println("9="+generateNumric(9));
//      System.out.println(generateVIN());
//    System.out.println(generateVIN());
//    System.out.println(generateVIN());
//    System.out.println(generateVIN());
//    System.out.println(generateVIN());
//    System.out.println(generateVIN());

    //    System.out.println(generateString());
//    System.out.println(generateString());
//    System.out.println(generateString());
//    System.out.println(generateString());
    HashMap keyValue = new HashMap();
    keyValue.put("namea.b.c","Mahesh kacholia");
    Properties p = new Properties();
  System.out.println(replacePlaceHolders("my vin is $namea.b.c$ and my name is $property_name$",keyValue,p));
  }
  public static String generateString()
  {
      int size = 10;
      int upper = 'A';
      int lower ='a';
      StringBuilder sb = new StringBuilder();
      Random r = new Random();
      char x =(char)( upper + r.nextInt(26));
      sb.append(x);
      for(int i=0;i<size;i++)
      {
        x =(char)( lower + r.nextInt(26));
        sb.append(x);
      }
      return sb.toString();
  }
  public static String generateVIN()
  {
    StringBuilder sb = new StringBuilder();
    int size =17;
    for(int i=0;i<size;i++)
    {
      Random r = new Random();
      int x = r.nextInt(3);
      char c='A';
      if(x==0)
      {
        c = generateNumber();
      }
      else
      {
        c = generateUpperChar();
      }
      sb.append(c);
    }
    return sb.toString();
//    return "1GKKVRKD6GJ207970";

  }
  public static String generateTime()
  {
    return "2017-05-22 21:17";
  }
  public static String generateDate()
  {
    return "2017-05-22";
  }
  public static char generateUpperChar()
  {
    Random r = new Random();
    char x =(char)( 'A' + r.nextInt(26));
    return x;
  }
  public static char generateLowerChar()
  {
    Random r = new Random();
    char x =(char)( 'a' + r.nextInt(26));
    return x;
  }
  public static char generateNumber()
  {
    Random r = new Random();
    char x =(char)( '0' + r.nextInt(10));
    return x;
  }



}
