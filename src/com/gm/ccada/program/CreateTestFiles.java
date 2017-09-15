package com.gm.ccada.program;

import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URLDecoder;
import java.nio.FloatBuffer;
import java.util.*;

/**
 * Created by QZ9Y83 on 6/15/2017.
 */
public class CreateTestFiles {
  JSONObject mainObject = new JSONObject();
  HashMap samplePropertyMap = new HashMap();
  Properties property = null;
  public static void main(String[] args) throws Exception
  {
   CreateTestFiles testFiles = new CreateTestFiles();
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
  public CreateTestFiles() throws Exception
  {
    property = new Properties();
    property.load(new FileInputStream("C:\\ndi\\production\\tools\\samplegen.properties"));
    createDirectory();
  }
  public CreateTestFiles(String propertyStr) throws Exception
  {
    property = new Properties();
    property.load(new StringReader(propertyStr));
    createDirectory();
  }
  public void createSampleFiles() throws Exception {

    int nooffiles = Integer.parseInt(property.get("files.nooffiles") + "");
    String fileType = property.getProperty("files.type") ;

    for (int i = 0; i < nooffiles; i++)
    {
      // System.out.println(mainObject);
      FileOutputStream fout = null;
      if(fileType.equals("json")) {
        samplePropertyMap = new HashMap();
        mainObject = (JSONObject)this.createObject("");
        String fileName = TestUtil.replacePlaceHolders(property.getProperty("files.outputfile")+"",samplePropertyMap,property);
        String finalFile = property.getProperty("files.outputdir") +"\\" + fileName;
        fout = new FileOutputStream(finalFile);

        fout.write(mainObject.toString().getBytes());
        fout.close();

      }
      else if(fileType.equals("csv"))
      {
        samplePropertyMap = new HashMap();
       this.createCSVFile();
   }
      else if(fileType.equals("xml"))
      {
        samplePropertyMap = new HashMap();
        mainObject = (JSONObject)this.createObject("");
        String fileName = TestUtil.replacePlaceHolders(property.getProperty("files.outputfile")+"",samplePropertyMap,property);
        String finalFile = property.getProperty("files.outputdir") +"\\" + fileName;
        fout = new FileOutputStream(finalFile);
        XML xstream = new XML();
        String xml = xstream.toString(mainObject,"apptList");
        System.out.print(xml);
        fout.write(xml.getBytes());
        fout.close();

      }
    }


  }
  public void createSampleFilesCSV() throws Exception {

    int nooffiles = Integer.parseInt(property.get("files.nooffiles") + "");
    for (int i = 0; i < nooffiles; i++)
    {


      String fileName = TestUtil.replacePlaceHolders(property.getProperty("files.outputfile")+"",samplePropertyMap,property);
      String finalFile = property.getProperty("files.outputdir") +"\\" + fileName;
      // System.out.println(mainObject);
      FileOutputStream fout = new FileOutputStream(finalFile);

      fout.write(mainObject.toString().getBytes());
      fout.close();
    }


  }
  public void createCSVFile(String header,String filesFormat,String fileName ) throws Exception
  {
    StringBuilder csvFileContent=new StringBuilder();
    if(header != null) {
      header = TestUtil.replacePlaceHolders(property.get("files.header")+"",samplePropertyMap,property);
      csvFileContent.append(header + "\r\n");
    }
    int noOfRecords = Integer.parseInt(returnSamplePropertyValue("files.noofRecords",((String)property.get("files.noofRecords"))));
    for(int i=0;i<noOfRecords;i++)
    {
      ArrayList<String> variablesWithProperty = TestUtil.getAllPropertyVariables(filesFormat);
      ArrayList<Properties> expandedList = expandObject(variablesWithProperty);
      for(Properties rowProperties :expandedList)
      {
        String fileFormat = TestUtil.replacePlaceHolders(filesFormat,rowProperties,property);
        csvFileContent.append(fileFormat+"\r\n");

      }

    }

    fileName = TestUtil.replacePlaceHolders(fileName+"",samplePropertyMap,property);
    String finalFile = property.getProperty("files.outputdir") +"\\" + fileName;
    FileOutputStream fout = new FileOutputStream(finalFile);

    fout.write(csvFileContent.toString().getBytes());
    fout.close();

  }
  public void createCSVFileNew() throws Exception{
    samplePropertyMap = new HashMap();
    mainObject = (JSONObject)this.createObject("");

  }
  ArrayList<HashMap> findAllFilesProperties()
  {
    getAllProperTiesStartWith("files.header");
    getAllProperTiesStartWith("files.format");
    getAllProperTiesStartWith("files.outputfile");

      return null;

  }

  public void createCSVFile()
    throws Exception {
    StringBuilder csvFileContent=new StringBuilder();
    String header = (String)property.get("files.header");
    if(header != null) {
      header = TestUtil.replacePlaceHolders(property.get("files.header")+"",samplePropertyMap,property);
      csvFileContent.append(header + "\r\n");
    }
    int noOfRecords = Integer.parseInt(returnSamplePropertyValue("files.noofRecords",((String)property.get("files.noofRecords"))));
    for(int i=0;i<noOfRecords;i++)
    {
      samplePropertyMap = new HashMap();
      mainObject = (JSONObject)this.createObject("");
      String filesample= property.get("files.format")+"";
      ArrayList<String> variablesWithProperty = TestUtil.getAllPropertyVariables(filesample);
      ArrayList<Properties> expandedList = expandObject(variablesWithProperty);
      for(Properties rowProperties :expandedList)
      {
        String fileFormat = TestUtil.replacePlaceHolders(filesample,rowProperties,property);
        csvFileContent.append(fileFormat+"\r\n");

      }

    }

    String fileName = TestUtil.replacePlaceHolders(property.getProperty("files.outputfile")+"",samplePropertyMap,property);
    String finalFile = property.getProperty("files.outputdir") +"\\" + fileName;
    FileOutputStream fout = new FileOutputStream(finalFile);

    fout.write(csvFileContent.toString().getBytes());
    fout.close();
  }
  public ArrayList<Properties > expandObject(ArrayList<String> variablesWithProperty ) throws Exception {
    ArrayList<Properties> expandedList = new ArrayList<Properties>();
    Properties rowpropTopLevel = new Properties();

    ArrayList<String> listOfOneLevelProperties = new ArrayList<String>();
    ArrayList<String> listOfTwoLevelProperties = new ArrayList<String>();
    String nameOfTwoLevelParent = null;

    for (String variable : variablesWithProperty) {

      String[] field_detail = variable.split("\\.");
      if (field_detail.length == 1) {
        rowpropTopLevel.put(variable,mainObject.get(variable));
        listOfOneLevelProperties.add(variable);
      } else if (field_detail.length == 2) {
        listOfTwoLevelProperties.add(field_detail[1]);
        if (nameOfTwoLevelParent == null) {
          nameOfTwoLevelParent = field_detail[0];
        } else {
          if (!nameOfTwoLevelParent.equals(field_detail[0])) {
            throw new Exception(
              "parent must be same for all child in one level ,parent is " + field_detail[0] +
                " and child is " + field_detail[1]);
          }
        }
      }
    }
    if (listOfTwoLevelProperties.size() > 0) {
      takeCareSecondLevelData(nameOfTwoLevelParent,rowpropTopLevel,listOfTwoLevelProperties,expandedList);
    }
    else
    {
      expandedList.add(rowpropTopLevel);
    }

    return expandedList;
  }
  public void takeCareSecondLevelData(String nameOfTwoLevelParent,Properties rowpropTopLevel,ArrayList<String> listOfTwoLevelProperties,ArrayList<Properties> expandedList)
  {
    Object child = mainObject.get(nameOfTwoLevelParent);
    if (child instanceof JSONArray) {
      JSONArray childArray = (JSONArray)child;
      for(Object rowItem : childArray)
      {
        JSONObject rowItemJSON = (JSONObject)(rowItem);
        Properties rowProp = new Properties();

        Iterator it = rowpropTopLevel.keySet().iterator();
        while(it.hasNext())
        {
          String proName = it.next()+"";
          rowProp.put(proName,rowpropTopLevel.get(proName));
        }
        for(String propName : listOfTwoLevelProperties)
        {
          rowProp.put(nameOfTwoLevelParent+"."+propName,rowItemJSON.get(propName));

        }
        expandedList.add(rowProp);
      }
    }

  }
  Object createSampleValue(String propertyName) throws Exception
  {
    String propValue = property.getProperty(propertyName);
    if(propValue==null ) return "null";
    if(propValue.equals("")) return "";
    if(propValue.startsWith("array"))
    {
      String[] para = propValue.split(",");
      int minnoofrecords = Integer.parseInt(para[1]);
      int maxnoofrecords = minnoofrecords;
      try
      {
        maxnoofrecords = Integer.parseInt(para[2]);
      }
      catch(Exception e)
      {

      }
      Random r = new Random();
      int diff = maxnoofrecords - minnoofrecords;
      if(diff <= 0) diff=1;
      int randNo = r.nextInt(diff);
      return createArrayObject(minnoofrecords+randNo,propertyName);
    }
    else if(propValue.startsWith("object"))
    {
      String[] para = propValue.split(",");
      int noofrecords = Integer.parseInt(para[1]);
      return createObject(propertyName);
    }
    return returnSamplePropertyValue(propertyName,propValue);
  }
  String returnSamplePropertyValue(String propertyName,String propValue) throws Exception
  {

    if(samplePropertyMap.containsKey(propertyName))
    {
      return samplePropertyMap.get(propertyName)+"";
    }
    propValue= TestUtil.replacePlaceHolders(propValue,samplePropertyMap,property);
    String[] values = propValue.split(",");
    if(values==null || values.equals(""))
    {
      samplePropertyMap.put(propertyName,"");
      return "";
    }
    Random rand = new Random();

    int  recodno = rand.nextInt(values.length) ;
    String propFinaValue = TestUtil.replacePlaceHolders(values[recodno],samplePropertyMap,property);
    samplePropertyMap.put(propertyName,propFinaValue);
    return propFinaValue;
  }
  private Object createArrayObject(int range, String propertyName) {
   // int noofrecordstogen = geTRandomNumber(range);
    int noofrecordstogen = range;

    JSONArray outputarray = new JSONArray();
    ArrayList<String> propList = getAllProperTiesStartWith(propertyName+".");
    for(int i=0;i<noofrecordstogen;i++)
    {
 //     System.out.println("i="+i);
      JSONObject arrayitem = new JSONObject();

      for(String prpnames:propList)
      {
        try {
          samplePropertyMap.remove(prpnames);
          Object samValue = createSampleValue(prpnames);
          arrayitem.put(getFinalStringFromProperty(prpnames), samValue);
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      };
   //   System.out.println("ggoing to add new object");
      outputarray.add(arrayitem);

    }
    return outputarray;
  }
  private Object createObject(String propertyName) throws Exception {
    ArrayList<String> propList =null;
    if(propertyName.equals(""))
    {
      propList = getAllProperTiesStartWith("");

    }
    else
    {
      propList = getAllProperTiesStartWith(propertyName+".");

    }
      JSONObject arrayitem = new JSONObject();
      Collections.sort(propList);
      for(String prpnames:propList)
      {
        Object samValue= createSampleValue(prpnames);
        arrayitem.put(getFinalStringFromProperty(prpnames),samValue);
      }
    return arrayitem;
  }

  public String getFinalStringFromProperty(String properyName)
  {
    String[] field_detail = properyName.split("\\.");
    System.out.println(field_detail.length);
    return field_detail[field_detail.length-1];

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
