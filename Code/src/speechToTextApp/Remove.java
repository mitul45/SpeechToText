/* THIS FUNCTION OF THIS CLASS IS CALLED WHEN USER WANTS TO REMOVE ANY
 *  APPLICATION FROM THE LIST AS WELL AS DATABASE.  */

package speechToTextApp;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Remove extends Frame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Label l1;
	TextArea t1;
	Choice c1;
	Button b1;
	Panel p1,p2;
	
	String str,data;
	
	Connection c;
	Statement st;
	ResultSet rs;
	int r;
	
	DialogBox db;
	
	public Remove()
	{
		data="";
		l1=new Label("Select Application");
		c1=new Choice();
		t1=new TextArea(10,70);
		b1=new Button("Remove");
		b1.addActionListener(this);
		p1=new Panel();
		p2=new Panel();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setTitle("Speech To Text Application - Remove Program");
		addWindowListener(new STTWindowListener());
		c1.add("");
		c1.add("None");
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			c=DriverManager.getConnection("Jdbc:Odbc:project","","");
			st=c.createStatement();
			rs=st.executeQuery("select Software.SoftwareName from Software");
			while(rs.next())
			{
				str=rs.getString("SoftwareName");
				c1.add(str);
			}
			c.close();
		}
		catch(Exception e)
		{
			db=new DialogBox(this,"Error while loading applications.");
			e.printStackTrace();
		}
		p1.add(l1);
		p1.add(c1);
		p1.add(b1);
		t1.setText("Select an application from choice list \nand press remove button to remove \nit from database.\n\n");
		t1.setEditable(false);
		p2.add(t1);
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
			str=c1.getSelectedItem();
			if(str.equals("") || str.equals("None"))
			{
				data="You have selected " + str +"."; 
				try
				{
					Thread.sleep(2000);
				}
				catch(Exception ee)
				{
					ee.printStackTrace();
				}
				repaint();
			}
			else
			{
				try
				{
					Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					c=DriverManager.getConnection("Jdbc:Odbc:project","","");
					st=c.createStatement();
					r=st.executeUpdate("delete from Software where Software.SoftwareName='" + str + "'");
					if(r>0)
					{
						Thread.sleep(2000);
						data="Successfully removed from database.";
						repaint();
					}
					else
					{
						db=new DialogBox(this,"It is not present in database.");
					}
				}
				catch(Exception ee)
				{
					db=new DialogBox(this,"Applocation could not be removed.");
					ee.printStackTrace();
				}
			}
			c1.add("");
			c1.add("None");
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				c=DriverManager.getConnection("Jdbc:Odbc:project","","");
				st=c.createStatement();
				rs=st.executeQuery("select Software.SoftwareName from Software");
				while(rs.next())
				{
					str=rs.getString("SoftwareName");
					c1.add(str);
				}
				c.close();
			}
			catch(Exception ee)
			{
				db=new DialogBox(this,"Error while re-loading applications.");
				ee.printStackTrace();
			}
			this.dispose();
		}
	}
}