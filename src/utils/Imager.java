package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Imager
{
	public static BufferedImage broken_image = getImage(Imager.class.getResource("../broken_icon.png"));
	

	public static BufferedImage getImage(Object obj)
	{
		BufferedImage image = null;

		try
		{
			if( obj instanceof String ) image = ImageIO.read(new File((String)obj));
			if( obj instanceof File   ) image = ImageIO.read((File)obj);
			if( obj instanceof URL    ) image = ImageIO.read((URL)obj);
		}
		catch( IOException ex )
		{
			System.out.println("Illegal path: " + obj.toString());
			// ex.printStackTrace();
			image = broken_image;
		}

		return image;
	}
	
	
	
	public static BufferedImage getScaledImage(BufferedImage img, int width, int height)
	{
	    int imgWidth = img.getWidth();
	    int imgHeight = img.getHeight();

	    if( imgWidth*height < imgHeight*width )
	    {
	        width = Math.max(1, imgWidth*height/imgHeight);
	    }
	    else
	    {
	        height = Math.max(1, imgHeight*width/imgWidth);
	    }

	    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();

	    try
	    {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	    }
	    finally
	    {
	        g.dispose();
	    }

	    return newImage;
	}
	
	
	public static BufferedImage getScaledImage(File imgPath, int width, int height)
	{
		return getScaledImage(getImage(imgPath), width, height);
	}
	
	
}
