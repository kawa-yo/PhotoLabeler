package utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TablePanel extends ScrollPanel
{
	private File src;
	private JTable table;
	private DefaultTableModel model;
	private JTableHeader header;
	private String[][] rawData;
	
	private boolean loaded = false;

	
	public TablePanel()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);

		model = new DefaultTableModel();
		table = new JTable(model);
		// table.setPreferredScrollableViewportSize(new Dimension(1600, 10));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		header = new JTableHeader();

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints headerGBC = new GridBagConstraints();
		headerGBC.weightx = 1.0;
		headerGBC.gridwidth = GridBagConstraints.REMAINDER;
		headerGBC.fill = GridBagConstraints.HORIZONTAL;
		headerGBC.insets = new Insets(3, 3, 3, 3);
		panel.add(header, headerGBC);
		
		GridBagConstraints tableGBC = new GridBagConstraints();
		tableGBC.weightx = 1.0;
		tableGBC.weighty = 1.0;
		tableGBC.fill = GridBagConstraints.BOTH;
		tableGBC.insets = new Insets(3, 3, 3, 3);
		panel.add(new ScrollPanel(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER,
				table
				), tableGBC);

		setContent(panel);
	}
	
	public TablePanel(Dimension d)
	{
		this();
		table.setPreferredScrollableViewportSize(d);
	}

	public TablePanel(File csvfile)
	{
		this();
		src = csvfile;
	}
	
	public CSVParser getParser(File csvfile)
	{
		CSVParser parser = null;
		try
		{
			FileReader reader = new FileReader(csvfile);
			parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		return parser;
	}
	
	public String[] getColumn(int j)
	{
		String[] col = new String[rawData.length];
		for( int i=0; i<rawData.length; i++ )
		{
			col[i] = rawData[i][j];
		}
		return col;
	}
	
	public void load(File src)
	{
		this.src = src;

		if( !src.exists() )
		{
			return;
		}
		CSVParser parser = getParser(src);
		try
		{
			List<CSVRecord> records = parser.getRecords();
			int height = records.size();
			int width = records.get(0).size();
			
			String[] names = new String[width];
			for( int i=0; i<width; i++ )
			{
				names[i] = String.format("%02d", i);
			}
			
			rawData = new String[height][width];
			for( int i=0; i<height; i++ )
			{
				for( int j=0; j<width; j++ )
				{
					rawData[i][j] = records.get(i).get(j);
				}
			}
			model.setDataVector(rawData, names);

			loaded = true;
			revalidate();
			repaint();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public String[][] getRawData()
	{
		return rawData;
	}
	
	public String[] getHeader()
	{
		String[] header = new String[rawData[0].length];
		for( int j=0; j<header.length; j++ )
		{
			header[j] = table.getColumnName(j);
		}
		return header;
	}
	
	public boolean isloaded()
	{
		return loaded;
	}
	
	public DefaultTableModel getModel()
	{
		return model;
	}
}