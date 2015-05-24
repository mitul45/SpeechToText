/*	THIS IS MAIN CLASS OF OUR APPLICATION WHICH CONTAINS GUI PART
 *	AND CREATES DATABASE CONNECTION TO LIST OUT THE APPLICATIONS
 *	WHICH USER HAS ADDED BEFORE TO EXECUTE. YOU CAN ALSO ADD OR 
 *	REMOVE ANY APPLICATION BY YOUR OWN USING THIS GUI 		    */


package speechToTextApp;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class STTApplication_Main extends Frame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Button b1,b2,b3;		//b1 FOR LOADING GRAMMAR b2 FOR ADDING APPLICATION AND b3 FOR REMOVING
	Panel p1,p2,p3;
	TextArea t1,t2;
	
	String sw,alias,str;
	static String str1;
	
	static Connection c;	//DATABASE VARIABLES
	static Statement st;
	static ResultSet rs;
	
	static DialogBox db;
	FileDialogBox fdb;
	Add a1;
	Remove r1;
	STTLogic stt;

	//CONSTRUCTOR FOR CLASS
	
	public STTApplication_Main()
	{
		str="\t\tUse following keywords for the related application\n\n";
		t1=new TextArea(5,50);
		t2=new TextArea(30,90);
		b1=new Button("Speak To Open");
		b2=new Button("Add Application");
		b3=new Button("Remove Application");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		p1=new Panel();
		p2=new Panel();
		p3=new Panel();
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setBackground(Color.LIGHT_GRAY);
		setTitle("Speech To Text Application");
		addWindowListener(new STTWindowListener());
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			c=DriverManager.getConnection("Jdbc:Odbc:project","","");
			st=c.createStatement();
			rs=st.executeQuery("select Software.SoftwareName,Software.Alias from Software");
			while(rs.next())
			{
				sw=rs.getString("SoftwareName");
				alias=rs.getString("Alias");
				str=str + alias + "\t\t ( for " + sw + " )\n";
			}
			c.close();
		}
		catch(Exception e)
		{
			db=new DialogBox(this,"Database could not be loaded.");
			e.printStackTrace();
		}
		t1.setText("To open application click on speak \nbutton and speak a name from a list. \nTo add or remove application from \nlist use add or remove button.\n\n" + "\n");
		t1.setEditable(false);
		t2.setText(str);
		t2.setEditable(false);
		p1.add(b1);
		p1.add(b2);
		p1.add(b3);
		p2.add(t1);
		p3.add(t2);
		add(p1);
		add(p2);
		add(p3);
	}
	
	//FUNCTION WHICH IS CALLED EVERYTIME WHENEVER A NEW APPLICATION IS ADDED TO ADD ENTERY OF THAT ALIAS IN OUR GRAMMER FILE.
	
	public static void loadGrammar(String str1)
	{
		RandomAccessFile raf;
		System.out.println("str1 = " + str1);
		String temp=" | " + str1 + ";";
		str1=temp;
		try
		{
			raf=new RandomAccessFile("grammar.txt","rw");
			raf.seek((raf.length()-1));
			raf.writeBytes(str1);
			raf.close();
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(b1))
		{
			stt=new STTLogic(this);		//CALL THE FUNCTION WHICH ACTUALLY RECOGNIZES THE SPOKEN WORD OPENS THAT APPLICATION.
			str=stt.useSpeechToText();
//			str="hello";
		}

		if(e.getSource().equals(b2))
		{
			a1=new Add();				//FUNCTION TO ADD THE APPLICATION TO LIST AND UPDATE DATABASE.
			a1.setSize(700,400);
			a1.setVisible(true);
			repaint();
		}
		
		if(e.getSource().equals(b3))
		{
			r1=new Remove();			//FUNCTION FOR REMOVING THE APPLICATION FROM THE LIST.
			r1.setSize(700,400);	
			r1.setVisible(true);
			repaint();
		}
	}
	
	//METHOD TO CREATE DATABASE CONNECTION AND LIST OUT THE APPLICATIONS.
	
	public void paint(Graphics g)
	{
		str="\t\tUse following keywords for the related application\n\n";
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			c=DriverManager.getConnection("Jdbc:Odbc:project","","");
			st=c.createStatement();
			rs=st.executeQuery("select Software.SoftwareName,Software.Alias from Software");
			while(rs.next())
			{
				sw=rs.getString("SoftwareName");
				alias=rs.getString("Alias");
				str=str + alias + "\t\t ( for " + sw + " )\n";
			}
			c.close();
		}
		catch(Exception ee)
		{
			db=new DialogBox(this,"Database could not be loaded.");
			ee.printStackTrace();
		}
		t2.setEditable(true);
		t2.setText(str);
		t2.setEditable(false);
	}
	
	
	//MAIN FUNCTION.
	public static void main(String args[])
	{
		Frame f=new STTApplication_Main();
		f.setSize(700,500);
		f.setVisible(true);
	}
}