package utils;

import java.awt.Component;

import javax.swing.JScrollPane;

public class ScrollPanel extends BorderPanel
{
	public JScrollPane scrollPane;
	
	public ScrollPanel(Component c)
	{
		scrollPane = new JScrollPane(c);
		setCenter(scrollPane);
	}
	
	public ScrollPanel(int vpolicy, int hpolicy, Component c)
	{
		scrollPane = new JScrollPane(c, vpolicy, hpolicy);
		setCenter(scrollPane);
	}
	
	public ScrollPanel(int vpolicy, int hpolicy)
	{
		scrollPane = new JScrollPane(vpolicy, hpolicy);
		setCenter(scrollPane);
	}
	
	public void setContent(Component c)
	{
		scrollPane.setViewportView(c);
	}
}
