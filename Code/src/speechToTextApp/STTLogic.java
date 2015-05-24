/* THIS CLASS IS RESPLONSIBLE FOR RECOGNIZING SPEECH AND EXECUTING THE APPLICATION. */

package speechToTextApp;

import java.awt.Frame;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.speech.Central;
import javax.speech.EngineList;
import javax.speech.EngineModeDesc;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import javax.speech.recognition.RuleGrammar;



public class STTLogic extends ResultAdapter
{
	Frame f;
	
	FileReader reader;
	RuleGrammar gram;
	
	String str;
	Recognizer rec;
	
	DialogBox db;
	
	static Connection c;	//DATABASE VARAIBLES.
	static Statement st;
	static ResultSet rs;
	
	static Runtime ru;		//VARIABLE TO EXECUTE APPLICATION.
	static Process pr;
	
	//THIS FUNCTION IS FOR RECOGNIZING IT LIST OUTS THE AVAILABLE RECOGNIZER IN YOUR SYSTEM AND CHOOSES THE FIRST ONE.
	
	public STTLogic()
	{
		str="";
		try
		{
			EngineList e1=Central.availableRecognizers(null);
			EngineModeDesc desc=(EngineModeDesc) e1.firstElement();
			rec=Central.createRecognizer(desc);
			rec.allocate();
			System.out.println("allocated");
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Engine could not be loaded.");
			ee.printStackTrace();
		}
		try
		{											
			reader=new FileReader("grammar.txt");	//LOAD GRAMMAR
		    gram=rec.loadJSGF(reader);
			System.out.println("Grammar Loaded.");
		    gram.setEnabled(true);
			rec.addResultListener(this);
			rec.commitChanges();
			rec.requestFocus();
			rec.resume();
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Speech could not be processed.");
			ee.printStackTrace();
		}
	}
	
	public STTLogic(Frame f)
	{
		this.f=f;
		str="";
		try
		{
			EngineList e1=Central.availableRecognizers(null);
			EngineModeDesc desc=(EngineModeDesc) e1.firstElement();
			rec=Central.createRecognizer(desc);
			rec.allocate();
			System.out.println("allocated");
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Engine could not be loaded.");
			ee.printStackTrace();
		}
		try
		{			
			reader=new FileReader("grammar.txt");
		    gram=rec.loadJSGF(reader);
			System.out.println("Grammar Loaded.");
		    gram.setEnabled(true);
			rec.addResultListener(this);
			rec.commitChanges();
			rec.requestFocus();
			rec.resume();
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Speech could not be processed.");
			ee.printStackTrace();
		}
	}
	
	//WHEN ANY INPUT IS GIVEN THIS LISTENER IS CALLED TO CONVERT THE SPEECH TO TEXT. 
	
	public void resultAccepted(ResultEvent e)
	{
		System.out.println("Listener invoked.");
		Result r=(Result) e.getSource();
		ResultToken tokens[]=r.getBestTokens();
		for(int i=0;i<tokens.length;i++)
		{
			System.out.println("In result accepted :: "+tokens[i].getSpokenText());
			str="" + tokens[i].getSpokenText();
			//System.out.println("" + tokens[i].getSpokenText());
		}
		System.out.println("After for loop in resultAccepted.");
		executeApplication(str);	//FUNCTION TO START THE APPLICATION.
	}
	
	//THIS FUNCTION CREATES DATABASE CONNECTION TO FIND OUT THE ACTUAL PATH FROM THE ALIAS AND THEN OPEN THAT APPLICATION.
	
	public static void executeApplication(String str)
	{
		try
		{
		//	str="control";
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			c=DriverManager.getConnection("Jdbc:Odbc:project","","");
			st=c.createStatement();
			rs=st.executeQuery("select Software.SoftwarePath from Software where Software.Alias='" + str + "'");
			if(rs.next())
			{
				str=rs.getString("SoftwarePath");
				ru=Runtime.getRuntime();
				pr=null;
				if(str!=null && (!str.equals("")))
				{
					try
					{
						pr=ru.exec(str);
					}
					catch(Exception ee)
					{
						System.out.println("1:Application could not be found.1");
					}
				}
				else
					System.out.println("2:Application not found.");
			}
			else
				System.out.println("3:Application not found.");
		}
		catch(Exception ee)
		{
			System.out.println("4:Application could not be opened.");
			ee.printStackTrace();
		}
	}
	
	
	public String useSpeechToText()
	{	
		System.out.println("In useSpeechToText.");
		str="";
		try
		{
			EngineList e1=Central.availableRecognizers(null);
			EngineModeDesc desc=(EngineModeDesc) e1.firstElement();
			rec=Central.createRecognizer(desc);
			rec.allocate();
			System.out.println("allocated");
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Engine could not be loaded.");
			ee.printStackTrace();
		}
		try
		{			
			reader=new FileReader("grammar.txt");
		    gram=rec.loadJSGF(reader);
			System.out.println("Grammar Loaded.");
		    gram.setEnabled(true);
		   	rec.addResultListener(new STTLogic());
			rec.commitChanges();
			rec.requestFocus();
			rec.resume();
		}
		catch(Exception ee)
		{
			db=new DialogBox(f,"Speech could not be processed.");
			ee.printStackTrace();
		}
		return str;
	}
}