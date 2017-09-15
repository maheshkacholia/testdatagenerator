package com.gm.ccada.program;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.*;

/**
 * Created by QZ9Y83 on 6/15/2017.
 */
public class CreateCSVTestFiles {
  HashMap mainObject = new HashMap();
  HashMap samplePropertyMap = new HashMap();
  Properties property = null;
  public static void main(String[] args) throws Exception
  {
   CreateCSVTestFiles testFiles = new CreateCSVTestFiles();
 //   System.out.println(testFiles.getAllProperTiesStartWith("apptList.parties.comms."));
 //System.out.println(testFiles.createObject(""));
    testFiles.createSampleFiles();
  }
  public void createDirectory() throws Exception
  {
    String outPutDir = (String)property.get("files.outputdir");
    if(outPutDir != null)
    {
        File outDir = new File(outPutDir);
        if(!outDir.exists())
        {
          outDir.mkdirs();
        }

    }

  }
  int geTRandomNumber(int numberrange)
  {
    Random rand = new Random();

    return rand.nextInt(numberrange) ;
  }
  public CreateCSVTestFiles() throws Exception
  {
    property = new Properties();
    property.load(new FileInputStream("C:\\ndi\\production\\tools\\samplegencsv.properties"));
    createDirectory();
  }
  public CreateCSVTestFiles(String propertyStr) throws Exception
  {
    property = new Properties();
    property.load(new StringReader(propertyStr));
    createDirectory();
  }
  public void createSampleFiles() throws Exception {

    int nooffiles = Integer.parseInt(property.get("files.nooffiles") + "");
    for (int i = 0; i < nooffiles; i++) {

    }

  }
  

  private ArrayList<String> getAllProperTiesStartWith(String propertyName) {
    Enumeration e = property.propertyNames();
    ArrayList<String> retUrnList = new ArrayList();
    if(propertyName.equals("")) {
      while (e.hasMoreElements()) {
        String name = (String) e.nextElement();
        if(name.indexOf(".") == -1)
        {
          retUrnList.add(name);
        }
      }
      return retUrnList;
    }
    while(e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      if(name.startsWith("files"))
        continue;
      if(name.startsWith(propertyName))
      {
        String s = name.substring(propertyName.length());
        if(s.indexOf(".") == -1)
        {
          retUrnList.add(name);
        }
      }
    }
    return retUrnList;
  }

}
