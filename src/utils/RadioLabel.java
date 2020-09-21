package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.Photo;

public class RadioLabel extends JPanel
{
	private boolean selected = false;
	private Color color;
	private JLabel label;
	private JLabel icon;

	public RadioLabel(String text)
	{
		this(text, Color.white);
	}
	
	public RadioLabel(String text, Color color)
	{
		super(new BorderLayout());
		label = new JLabel();
		setText(text);
		setColor(color);
		add(label, BorderLayout.CENTER);
		
		icon = new JLabel() {
			int r = 8;
			public void paint(Graphics g)
			{
				g.setColor(Color.black);
				g.drawArc(0, 0, r*2-1, r*2-1, 0, 360);
				g.setColor(selected? Color.orange : Color.white);
				g.fillArc(1, 1, r*2-2, r*2-2, 0, 360);
			}
		};
		icon.setPreferredSize(new Dimension(16, 16));
		add(icon, BorderLayout.LINE_START);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(color);
		g.drawRect(1, 1, getWidth()-2, getHeight()-2);
	}
	
	public void setSelected(boolean flag)
	{
		selected = flag;
		if( flag )
		{
			label.setFont(Photo.activeFont);
			System.out.println(getText() + " selected");
		}
		else
		{
			label.setFont(Photo.inactiveFont);
			System.out.println(getText() + " deselected");
		}
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public void setText(String text)
	{
		label.setText(text);
	}
	
	public String getText()
	{
		return label.getText();
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
}
