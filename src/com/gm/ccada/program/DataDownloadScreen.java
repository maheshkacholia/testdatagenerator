package com.gm.ccada.program;

import com.gm.ccada.program.CreateTestFiles;
import com.gm.ccada.program.SampleFileConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by QZ9Y83 on 6/19/2017.
 */

  public class DataDownloadScreen extends JFrame implements ActionListener
  {
    public JPanel jcontentpane=null;
    public JProgressBar progress=null;
    public JButton install = null;
    public JTextArea message = null;
    public JButton cancel=null;
    public static int fileprocessed=0;
    public static int maxvalue=1500;
    ArrayList v = new ArrayList();
    public String selectedCategory;
    JComboBox resolution;
    public static void main(String args[])
    {
      DataDownloadScreen grme = new DataDownloadScreen();
      grme.show();
    }
    int width,height;
    JButton generateButton;
    JLabel error ;
    private DataDownloadScreen()
    {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      width = (int)(screenSize.getWidth() *.9);
      height = (int)(screenSize.getHeight() * .9);
      //	super(maingraph,s,b);
      this.setTitle("Create Sample Files ");
      setBackground(Color.lightGray);
      this.setJMenuBar(createMenuBar());
      this.setSize((int)width,(int)height);
      this.move(50,25);
     // Image im = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/toolicon.jpg"));
     // this.setIconImage(im);

      this.setContentPane(getJContentPane());
      this.setVisible(true);
      this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public JMenuBar createMenuBar()
    {
      JMenuBar menuBar;
      JMenu menu, submenu;
      JMenuItem menuItem;
      // Create the menu bar.
      menuBar = new JMenuBar();
      // Build the first menu.
      menu = new JMenu("File");
      menuBar.add(menu);
      // a group of JMenuItems
      menuItem = new JMenuItem("Open Property");
      //	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
      menuItem.addActionListener(this);
      menuItem.setToolTipText("Open existing property file");
      menu.add(menuItem);
      return menuBar;
    }
    @Override public void actionPerformed(ActionEvent actionEvent) {
      Object obj = actionEvent.getSource();
      if(obj instanceof JButton) {

        JButton obj1 = (JButton) obj;
        String name = obj1.getName();
        if (name.equals("generate")) {
          try
          {
            CreateTestFiles testFiles = new CreateTestFiles(propertyArea.getText());
            testFiles.createSampleFiles();
            error.setText("Files generated successfully");

          }
          catch(Exception e)
          {
            error.setText(e.getMessage());
          }
          this.repaint();
        }
      }
      else if (obj instanceof JMenuItem)
      {
        // FileDialog(MainScreen.getSingleton(),)
        FileDialog fd = new FileDialog(new Frame(), "Open Formula", FileDialog.LOAD);
        fd.setFile("*.*");
        fd.setDirectory(SampleFileConstants.FORMULA_DIR);
        fd.setLocation(50, 50);
        fd.show();

        String fileName1 = fd.getFile();
        String dirName1 = fd.getDirectory();
        if (fileName1 == null || dirName1 == null || fileName1.equals("") || dirName1.equals(""))
        {
          return;
        }
        try
        {
          File f = new File(dirName1 + "/" + fileName1);
          FileInputStream file = new FileInputStream(f);
          long len = f.length();
          byte[] b = new byte[(int) len];
          file.read(b, 0, b.length);
          propertyArea.setText(new String(b));

         // fileName = fd.getFile();
         // dirName = fd.getDirectory();


          file.close();

         // this.lable.setText(this.mainStr + "Selected Formula  " + this.dirName +  this.fileName);

        }
        catch (Exception e)
        {

          // TODO Auto-generated catch block
          // e.printStackTrace();
        }
        // String s1 = buyformula.getText();
        // JFrame openformula = new OpenFormulaEditorPanel(this);
      }
    }
    int inc = 20;
    JTextArea propertyArea;
    private javax.swing.JPanel getJContentPane() {
      if (jcontentpane == null) {
        jcontentpane = new JPanel();
        jcontentpane.setLayout(null);
      }
      int yinc = 50;
      int xinc = 20;

      JLabel label = new JLabel("Property Configuration");
      label.setBounds(xinc, yinc, 300, 20);
      jcontentpane.add(label, null);
      yinc = yinc + inc;


       propertyArea = new JTextArea(xinc,yinc);
      Font font = new Font("Verdana", Font.BOLD, 12);
      propertyArea.setFont(font);
      //propertyArea.setForeground(Color.BLUE);

      JScrollPane scroll = new JScrollPane (propertyArea);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

      scroll.setBounds(xinc,yinc,width - xinc -100,height - yinc-150);
      jcontentpane.add(scroll,null);
      yinc = yinc + height - yinc-150 + inc;

      generateButton = new JButton("Generate");
      generateButton.setName("generate");
      generateButton.setBounds(xinc,yinc,200,25);
      jcontentpane.add(generateButton);
      generateButton.addActionListener(this);
     // yinc = yinc + inc;
      error = new JLabel();
      Font errfont = new Font("Verdana", Font.PLAIN, 9);
      error.setForeground(Color.RED);
      error.setFont(errfont);
      error.setBounds(xinc,20,800,25);
      jcontentpane.add(error);
      return jcontentpane;
    }

  }
