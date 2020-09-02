package utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BrowsePanel extends JPanel
{
	public static final int OPEN_MODE = 0;
	public static final int SAVE_MODE = 1;

	private int mode = OPEN_MODE;

	protected JTextField field;
	protected JButton button;
	private JFileChooser chooser;
	private String format = "";

	public BrowsePanel()
	{
		super(new BorderLayout());

		field = new JTextField(30);
		field.setEditable(false);
		add(field, BorderLayout.CENTER);
		// add(field, fieldGBC);

		button = new JButton("browse");
		button.addActionListener(e -> fullAction(e));
		add(button, BorderLayout.LINE_END);
		// add(button, buttonGBC);

		chooser = new JFileChooser(new File(System.getProperty("user.home")));
	}
	
	public BrowsePanel(String fileFormat)
	{
		this();
		format = fileFormat;
		String exp = "*." + fileFormat;
		chooser.setFileFilter(new FileNameExtensionFilter(exp, fileFormat));
	}
	
	public BrowsePanel(String fileFormat, int mode)
	{
		this(fileFormat);
		this.mode = mode;
	}
	
	public void fullAction(ActionEvent e)
	{
		File file = browse();
		if( file != null )
		{
			field.setText(file.toString());
			action(file);
		}
	}
	
	public void action(File file)
	{
		System.out.println("please set Action for " + file);
	}
	
	public File browse()
	{
		File file = null;
		int option;
		if( mode == SAVE_MODE )
		{
			option = chooser.showSaveDialog(null);
		}
		else
		{
			option = chooser.showOpenDialog(null);
		}
		if( option == JFileChooser.APPROVE_OPTION )
		{
			file = chooser.getSelectedFile();
			String path = file.toString();
			if( !format.equals("") && !path.endsWith(format) )
			{
				file = new File(path + "." + format);
			}
			chooser.setCurrentDirectory(file.getParentFile());
		}
		return file;
	}
	
	public AbstractAction getButtonAction()
	{
		ActionListener[] listeners = button.getActionListeners();

		return new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				for( ActionListener l : listeners )
				{
					l.actionPerformed(e);
				}
			}
		};
	}
	
	public void setInitialDirectory(File file)
	{
		chooser.setCurrentDirectory(file);
	}
	
	public String getText()
	{
		return field.getText();
	}
}
