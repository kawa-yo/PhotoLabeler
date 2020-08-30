package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;

import utils.BorderPanel;
import utils.BrowsePanel;
import utils.Empty;
import utils.FlowPanel;
import utils.RadioPanel;
import utils.TablePanel;
import view.Library;
import view.Photo;

public class SaveCSVDialog extends JFrame
{
	private boolean loaded = false;
	private TablePanel tablePanel;
	private BrowsePanel browsePanel;
	
	private JButton saveButton;
	private JButton cancelButton;

	public SaveCSVDialog(File dir, Library library)
	{
		tablePanel = new TablePanel(new Dimension(500, 200));
		browsePanel = new BrowsePanel("csv");
		browsePanel.setInitialDirectory(dir);
		saveButton = new JButton("save");
		cancelButton = new JButton("cancel");
		saveButton.addActionListener(e -> save());
		cancelButton.addActionListener(e -> setVisible(false));
		
		BorderPanel panel = new BorderPanel();
		getContentPane().add(panel);

		panel.setContents(
				new Empty(),
				new Empty(),
				new BorderPanel(
						new Empty(),
						new Empty(),
						tablePanel,
						new Empty(),
						browsePanel
				),
				new Empty(),
				new FlowPanel(FlowLayout.RIGHT, new Component[] {
						saveButton,
						cancelButton
				})
		);
		load(library.getComponents());

		setSize(600, 400);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
	}
	
	public void load(Component[] carray)
	{
		String[][] data = new String[carray.length][2];
		int cnt = 0;
		for( Component c : carray )
		{
			Photo photo = (Photo) c;
			File file = new File(photo.getSource());
			data[cnt][0] = file.getName();
			data[cnt][1] = photo.getLabel();
			cnt++;
		}
		String[] header = {"file name", "label"};
		tablePanel.getModel().setDataVector(data, header);
		loaded = true;
	}
	
	public String toLine()
	{
		String line = "";
		if( loaded )
		{
			Vector<Vector> data = tablePanel.getModel().getDataVector();
			int height = data.size();
			int width = data.get(0).size();
			for( int i=0; i<height; i++ )
			{
				for( int j=0; j<width-1; j++ )
				{
					line += (String) data.get(i).get(j) + ",";
				}
				line += (String) data.get(i).get(width-1) + "\n";
			}
		}
		return line;

	}
	
	public void save()
	{
		String text  = browsePanel.getText();
		if( !text.endsWith(".csv") )
		{
			text += ".csv";
		}
		File file = new File(text);
		String line = toLine();
		try
		{
			FileWriter writer = new FileWriter(file);
			writer.write(line);
			writer.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
