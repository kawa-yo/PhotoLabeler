package utils;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem
{
	public MenuItem(String title)
	{
		super(title);
	}
	
	public MenuItem(String title, ActionListener action)
	{
		this(title);
		addActionListener(action);
	}
}
