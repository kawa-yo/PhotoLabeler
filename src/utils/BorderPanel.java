package utils;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

public class BorderPanel extends JPanel
{
	public static final String TOP = BorderLayout.PAGE_START;
	public static final String LEFT = BorderLayout.LINE_START;
	public static final String CENTER = BorderLayout.CENTER;
	public static final String RIGHT = BorderLayout.LINE_END;
	public static final String BOTTOM = BorderLayout.PAGE_END;

	public static final String[] keys = {TOP, LEFT, CENTER, RIGHT, BOTTOM};

	public BorderPanel()
	{
		super(new BorderLayout());
	}
	
	public BorderPanel(Component top, Component left, Component center, Component right, Component bottom)
	{
		super();
		setContents(top, left, center, right, bottom);
	}
	
	public void setContents(Component top, Component left, Component center, Component right, Component bottom)
	{
		Component[] carray = new Component[] {top, left, center, right, bottom};
		for( int i=0; i<keys.length; i++ )
		{
			if( carray[i] instanceof Empty )
			{
				continue;
			}
			add(carray[i], keys[i]);
		}
	}
	
	public void setTop(Component c) { add(c, TOP); }
	public void setLeft(Component c) { add(c, LEFT); }
	public void setCenter(Component c) { add(c, CENTER); }
	public void setRight(Component c) { add(c, RIGHT); }
	public void setBottom(Component c) { add(c, BOTTOM); }
}
