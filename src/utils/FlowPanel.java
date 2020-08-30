package utils;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class FlowPanel extends JPanel
{
	public FlowPanel()
	{
		super(new FlowLayout());
	}
	
	public FlowPanel(Component[] carray)
	{
		this(FlowLayout.LEFT, carray);
	}
	
	public FlowPanel(int align, Component[] carray)
	{
		super(new FlowLayout(align));
		for( Component c : carray )
		{
			add(c);
		}
	}
}
