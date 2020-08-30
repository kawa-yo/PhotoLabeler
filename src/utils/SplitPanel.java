package utils;

import java.awt.Component;

import javax.swing.JSplitPane;

public class SplitPanel extends BorderPanel
{
	protected JSplitPane splitPane;
	
	public SplitPanel(int spolicy, boolean smooth, double weight, Component l, Component r)
	{
		splitPane = new JSplitPane(spolicy, smooth, l, r);
		splitPane.setResizeWeight(weight);
		setCenter(splitPane);
	}
}
