import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class Main extends Frame {
  
  private static JFrame frame;
  private static Knowledge students;
  private static Knowledge notification;
  private static Worker[] staff;
  
    public static void main(String[] args){

      students = new Knowledge<Student>();
      notification = new Knowledge<Student>();
      readFileStaff();
      button();
    }

    @SuppressWarnings("unchecked")
    private static void button()
    {
     frame = new JFrame();
     frame.setLayout(new FlowLayout());
    
     frame.setTitle("Mix"); 
     frame.setSize(900, 250);           
     frame.setVisible(true);
     
     Label lbl= new Label("Please enter the following information."); 
     
     frame.setLayout(new BorderLayout());  
      
      Panel pnlbtn = new Panel(new GridLayout(1, 1));
      Panel pnltxt = new Panel(new GridLayout(5, 2));
      
      TextField txtName, txtPN, txtMajor, txtIssue, txtGNum;
      txtName = new TextField(10); 
      txtPN = new TextField(10); 
      txtMajor = new TextField(10); 
      txtIssue = new TextField(10); 
      txtGNum = new TextField(10); 

      Label lblName, lblPN, lblMajor, lblIssue, lblGNum;
      lblName = new Label("Name: "); 
      lblPN = new Label("Phone-Number (###-###-####): "); 
      lblMajor = new Label("Major:"); 
      lblIssue = new Label("Issue: "); 
      lblGNum = new Label("G-number (G########): "); 
  
      JButton btn = new JButton("Enter");
        btn.addActionListener(new ActionListener() 
        {
         public void actionPerformed(ActionEvent e) 
         {
           String name, number, major, issue, gnumber;
           name =  txtName.getText();
           number =  txtPN.getText();
           major =  txtMajor.getText();
           issue =  txtIssue.getText();
           gnumber =  txtGNum.getText();
           
           Student std = new Student(name, number, major, issue, gnumber);
           students.add(std);
           save();
           
           frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

           //wait screen
             frame = new JFrame();
             frame.setLayout(new FlowLayout());
             frame.setTitle("Results"); 
             frame.setSize(500, 200);           
             frame.setVisible(true);
     
             Label lbl1 = new Label("Please Wait.");
    
             frame.setLayout(new BorderLayout());  
             frame.add(lbl1, BorderLayout.CENTER); 
         
           String workerName = notification(std);
           frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
          
          frame = new JFrame();
          frame.setLayout(new FlowLayout());
          frame.setTitle("Results"); 
          frame.setSize(500, 200);           
          frame.setVisible(true);
     
          Label lbl2 = new Label(workerName + " is coming and will be with you shortly.");
    
          frame.setLayout(new BorderLayout());  
          frame.add(lbl2, BorderLayout.CENTER); 
         }
        }
        );


      
      pnlbtn.add(btn);
      
      
      pnltxt.add(lblName);
      pnltxt.add(txtName);
      
      pnltxt.add(lblPN);
      pnltxt.add(txtPN);
      
      pnltxt.add(lblMajor);
      pnltxt.add(txtMajor);
      
      pnltxt.add(lblIssue);
      pnltxt.add(txtIssue);
      
      pnltxt.add(lblGNum);
      pnltxt.add(txtGNum);

      frame.add(pnltxt, BorderLayout.CENTER);
      frame.add(lbl, BorderLayout.NORTH);
      frame.add(pnlbtn, BorderLayout.SOUTH);
      
    }
    
    @SuppressWarnings("unchecked")
    private static String notification(Student std)
    {

      String wrkr = "";

      for(int i = 0; i < staff.length; i++)
      {
        if (staff[i].getAvailability().equals("true"))
        {
          staff[i].addStudentHelped(std);
          wrkr = staff[i].getName();
          std.setWorker(staff[i]);
          notification.add(std);
          updateNot();

          return wrkr;//the worker that accepts the job
        }
      }

      return wrkr;//the worker that accepts the job
    }
    
    @SuppressWarnings("unchecked")
    private static void readFileStaff()
    {
      File workerFile = new File("staff.txt");
      Scanner workerScan = null;
       
      try 
      {
        workerScan = new Scanner(workerFile);  
      }
      catch (IOException ex) 
      {
        // Report
      } 
      finally
      {
        int numWorkers = Integer.parseInt(workerScan.nextLine());
        staff = new Worker[numWorkers];
        
        String str = "";
        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
      
        for(int i = 0; i < numWorkers; i++)
        {
          String name, number, position, available;
          //boolean available;
          str = "";
          name = "";
          number = "";
          position = "";
          available = "";
          

        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
          if(str.length() > 6 && str.substring(0,5).equals("Name:"))
          {
             name = str.substring(6);
          }
        
        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
          if(str.length() > 7 && str.substring(0,6).equals("Number:"))
          {
             number = str.substring(7);
          }
        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
          if(str.length() > 10 && str.substring(0,9).equals("Position:"))
          {
             position = str.substring(10);
          }
          
        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
          if(str.length() > 14 && str.substring(0,13).equals("Availability:"))
          {
            available = str.substring(14);
          }
          Worker worker = new Worker(name, number, position, available);
          staff[i] = worker;
     
        if(workerScan.hasNextLine())
          str = workerScan.nextLine();
        }
     }
 } 

 public static void save()
 { 
   Writer studentFile = null;
   try 
   {
    studentFile = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream("student.txt"), "utf-8"));
          studentFile.write(students.toString());
   } 
   catch (IOException ex) 
   {
    // Report
   } 
   finally 
   {
     try 
     {
       studentFile.close();
     } 
     catch (Exception ex) 
     {
     /*ignore*/
     }
   }
 }
 
  public static void updateNot()
 { 
   File notFile = new File("notification.txt");
      Scanner notificationScan = null;
      String file = "";
       
      try 
      {
        notificationScan = new Scanner(notFile);  
      }
      catch (IOException ex) 
      {
        // Report
      } 
      finally
      {
        while(notificationScan.hasNextLine())
        {
          file += notificationScan.nextLine() + "\n";
        }
      }
   Writer notificationFile = null;
   try 
   {
    notificationFile = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream("notification.txt"), "utf-8"));
          notificationFile.append(file + notification.toString());
   } 
   catch (IOException ex) 
   {
    // Report
   } 
   finally 
   {
     try 
     {
       notificationFile.close();
     } 
     catch (Exception ex) 
     {
     /*ignore*/
     }
   }
 }
     
    
}