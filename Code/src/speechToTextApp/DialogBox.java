package speechToTextApp;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogBox extends Dialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	Button b1;
	Label l1;
	Panel p1,p2;
	
	public DialogBox(Frame f,String s)
	{
		super(f,"Message",true);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		p1=new Panel();
		p2=new Panel();
		l1=new Label(s);
		b1=new Button("Ok");
		b1.addActionListener(this);
		p1.add(l1);
		p2.add(b1);
		add(p1);
		add(p2);
		setSize(300,200);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(b1))
			this.dispose();
	}
}
