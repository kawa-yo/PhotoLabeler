package utils;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JRadioButton;

public class RadioButton extends JRadioButton
{
	private Color color;

	public RadioButton(String text, Color color)
	{
		super(text);
		this.color = color;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(color);
		g.drawRect(1, 1, getWidth()-2, getHeight()-2);
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
