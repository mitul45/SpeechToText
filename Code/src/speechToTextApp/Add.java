/* THIS CLASS IS FOR ADDIND ANY NEW APPLICATION TO LIST. */

package speechToTextApp;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Add extends Frame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Label l1,l2;
	TextField t1,t2;
	Button b1,b2;
	Panel p1,p2;
	
	String file,path,data,str;
			
	DialogBox db;
	FileDialogBox fdb;
	
	Connection c;
	Statement st;
	int rs;
	ResultSet r;
	
	public Add()
	{
		data="Enter data of application and press add";
		l1=new Label("Select File");
		l2=new Label("Alias");
		t1=new TextField(54);
		t2=new TextField(54);
		b1=new Button("Browse");
		b2=new Button("Add");
		b1.addActionListener(this);
		b2.addActionListener(this);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setTitle("Speech To Text Application - Add Program");
		addWindowListener(new STTWindowListener());
		p1=new Panel();
		p2=new Panel();
		p1.add(l1);
		p1.add(t1);
		p1.add(b1);
		p2.add(l2);
		p2.add(t2);
		p2.add(b2);
		add(p1);
		add(p2);
	}
	
	public void paint(Graphics g)
	{
		setBackground(Color.LIGHT_GRAY);
		setForeground(Color.BLACK);
		Font f1=new Font("Arial",Font.PLAIN,12);
		g.setFont(f1);
		g.drawString(data,20,(getSize().height-50));
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(b1))
		{
			fdb=new FileDialogBox(this,"Select Application");	//SELECT THE .exe PATH OF APPLICATION YOU WANT TO OPEN.
			file=fdb.getFile();
			path=fdb.getDirectory();
			if((file.endsWith(".exe"))||(file.endsWith(".EXE")))	//THE EXTENTION MUST BE .exe
			{
				t1.setText(path + "" + file);
				t1.setEditable(false);
			}
			else
			{
				db=new DialogBox(this,"Invalid file type selected.");
			}
		}
		if(e.getSource().equals(b2))
		{
			if(t1.getText().equals("") || t2.getText().equals(""))
			{
				db=new DialogBox(this,"Fill all the fields.");
			}
			else
			{
				//ADD ENTERY IN DATABASE.
				try
				{
					Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					c=DriverManager.getConnection("Jdbc:Odbc:project","","");
					st=c.createStatement();
					r=st.executeQuery("select * from Software where Software.SoftwareName='" + file + "'");
					if(r.next())
					{
						db=new DialogBox(this,"Already present in database.");
					}
					else
					{
						rs=st.executeUpdate("insert into Software values('" + file +"','" + t1.getText() + "','" + t2.getText() + "')");
						str=t2.getText();
						STTApplication_Main.loadGrammar(str);						
						System.out.println("str = " + str);
						if(rs>0)
						{
							data="Data has been updated suceesfully";
							repaint();
						}
					}
					c.close();
				}
				catch(Exception ee)
				{
					db=new DialogBox(this,"Application could not be added.");
					ee.printStackTrace();
				}
				this.dispose();
			}
		}
	}
}