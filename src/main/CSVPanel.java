package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JPanel;

import utils.BrowsePanel;

public class CSVPanel extends JPanel
{
	private BrowsePanel browsePanel;
	
	public CSVPanel()
	{
		super(new GridBagLayout());
		
		GridBagConstraints browseGBC = new GridBagConstraints();
		browseGBC.gridy = 0;
		browseGBC.fill = GridBagConstraints.HORIZONTAL;
		browseGBC.gridwidth = GridBagConstraints.REMAINDER;
		
		GridBagConstraints tableGBC = new GridBagConstraints();
		browseGBC.gridy = 1;
		browseGBC.fill = GridBagConstraints.BOTH;
		browseGBC.gridwidth = GridBagConstraints.REMAINDER;
		
		
		browsePanel = new BrowsePanel("csv") {
			@Override
			public void action(File file)
			{
				// tablePanel.load(file);
			}
		};
		add(browsePanel, browseGBC);
		// add(tablePanel, tableGBC);
	}
}
