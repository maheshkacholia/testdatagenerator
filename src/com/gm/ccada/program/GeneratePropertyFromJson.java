package com.gm.ccada.programs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by QZ9Y83 on 6/17/2017.
 */
public class GeneratePropertyFromJson {
  // properties are now loaded from the command line arguments in constructor

  public static void main(String[] args) throws Exception
  {
    GeneratePropertyFromJson genjson = new GeneratePropertyFromJson();
    genjson.createPropertyFromSample("C:\\ccada\\files\\SDR_DMS_SERVAPPT_00000131041_20170522221340_7.json");
  }
  JSONParser parser = new JSONParser();
  StringBuilder finalPropertyBuilder = new StringBuilder();

  public void createPropertyFromSample(String inputFileName) throws  Exception

  {
    JSONParser parser = new JSONParser();

    Object obj = parser.parse(new FileReader(inputFileName));
    readObjectAndCreateProperties(obj,"");
    System.out.println(finalPropertyBuilder);

  }
  public void readObjectAndCreateProperties(Object obj,String baseProperty)
  {
    if(obj==null)
    {
      return;
    }
    else if(obj instanceof JSONObject) {
      Iterator it = ((JSONObject) obj).keySet().iterator();
      while (it.hasNext()) {
        String s = (String) it.next();
        Object childObj = ((JSONObject) obj).get(s);
        if(childObj instanceof String)
        {
          finalPropertyBuilder.append(baseProperty+s+"="+childObj+"\n");
        }
        else
        {
          readObjectAndCreateProperties(childObj,baseProperty+s+".");
        }
      }
    }
    else if (obj instanceof JSONArray)
    {
      JSONArray array = (JSONArray)obj;
      if(array==null ) return;
      if(array.size()==0) return;
      Object childObj = array.get(0);
      readObjectAndCreateProperties(childObj,baseProperty);
    }

  }

}
