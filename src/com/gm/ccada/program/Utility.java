package com.gm.ccada.program;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by QZ9Y83 on 7/13/2017.
 */
public class Utility {
  private static HashMap<String,ArrayList<String>> fileMap= new HashMap<String,ArrayList<String>>();

  public static String getFileContent(String fileName) throws Exception
  {
    File file = new File(fileName);
    FileInputStream fis = new FileInputStream(file);
    byte[] data = new byte[(int) file.length()];
    int count = fis.read(data);
    fis.close();

    String str = new String(data, "UTF-8");
    return str;
  }
  public static ArrayList getDataForProperty(String placeholder,Properties actualProperty ) throws Exception
  {

    ArrayList<String> propertyData= fileMap.get(placeholder);
    ArrayList<String> outDataList= new ArrayList<>();

    if(propertyData!= null) return propertyData;

    String fileDir = actualProperty.get("files.outputdir")+"";
    String fileName = placeholder.substring("file_".length());
    String[] filesplitparts = fileName.split("_");

    fileName = filesplitparts[0];
    int columnno = Integer.parseInt(filesplitparts[1]);

    int startpos = 1;
    if(filesplitparts.length>2)
      startpos =Integer.parseInt(filesplitparts[2]);
    String fileData = getFileContent(fileDir+"\\"+fileName);
    String[] fileRecords = fileData.split("\n");
    int i=1;
    for(String lineRecord:fileRecords)
    {
      if(i<=startpos)
      {
        i++;
        continue;
      }
      String[] csvSepratedData = lineRecord.split(",");
      outDataList.add(csvSepratedData[columnno-1]);
    }
    fileMap.put(placeholder,outDataList);
    return outDataList;
  }
}
