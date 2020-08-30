package utils;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Menu extends JMenu
{
	public Menu(String title)
	{
		super(title);
	}
	
	public Menu(String title, JMenuItem[] items)
	{
		this(title);
		for( JMenuItem item : items )
		{
			add(item);
		}
	}
}
