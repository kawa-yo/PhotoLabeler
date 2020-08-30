package utils;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar
{
	public MenuBar()
	{
		super();
	}
	
	public MenuBar(JMenu[] menus)
	{
		this();
		for( JMenu menu : menus )
		{
			add(menu);
		}
	}
}
