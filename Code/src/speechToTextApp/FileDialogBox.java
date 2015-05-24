//CLASS TO BROWSE THE ANY FILE.

package speechToTextApp;

import java.awt.FileDialog;
import java.awt.Frame;

public class FileDialogBox extends FileDialog
{
	private static final long serialVersionUID = 1L;
	public FileDialogBox(Frame f,String s)
	{
		super(f,s);
		setSize(200,200);
		setVisible(true);
	}
}
