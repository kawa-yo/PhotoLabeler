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
	protected JTextField field;
	protected JButton button;
	private JFileChooser chooser;
	private String format = "";

	public BrowsePanel()
	{
		// super(new GridBagLayout());

		// GridBagConstraints fieldGBC = new GridBagConstraints();
		// fieldGBC.weightx = 1.0;
		// fieldGBC.gridwidth = GridBagConstraints.RELATIVE;
		// fieldGBC.fill = GridBagConstraints.HORIZONTAL;
		// fieldGBC.insets = new Insets(2,2,2,2);

		// GridBagConstraints buttonGBC = new GridBagConstraints();
		// buttonGBC.weightx = 0.0;
		// buttonGBC.gridwidth = GridBagConstraints.REMAINDER;
		// buttonGBC.insets = new Insets(2,2,2,2);

		super(new BorderLayout());

		field = new JTextField();
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
		if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION )
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
