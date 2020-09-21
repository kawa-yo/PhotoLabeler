package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import ext.StretchIcon;
import main.Main;
import utils.Imager;
import utils.RadioLabel;

public class Photo extends JPanel
{
	public static final String LABEL_KEY = "LABEL";
	public static final int width = 200;
	public static final int height = 200;
	public static final Color activecolor = new Color(255, 128, 128);
	public static final Color inactivecolor = new Color(240, 240, 240);
	public static final int viewpad = 2;
	public static final Border activeBorder = BorderFactory.createLineBorder(activecolor, 3);
	public static final Border inactiveBorder = BorderFactory.createLineBorder(inactivecolor, 3);
	public static final Font activeFont = new Font("Serif", Font.BOLD, 14);
	public static final Font inactiveFont = new Font("Serif", Font.PLAIN, 12);

	private JLabel view;
	private JLabel title;
	
	private int radio_id = -1;
	private String label;
	
	private boolean active = false;
	private boolean loaded = false;
	private boolean submitted = false;
	
	private String src;
	private String prefPath;
	
	
	public Photo(File file)
	{
		super(new BorderLayout());
		src = file.toString();
		prefPath = toPreferencePath(src);
		setBackground(Color.white);
		
		view = new JLabel();
		view.setBorder(BorderFactory.createEmptyBorder(viewpad, viewpad, viewpad, viewpad));
		view.setOpaque(true);
		title = new JLabel();
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(view, BorderLayout.CENTER);
		add(title, BorderLayout.PAGE_END);
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if( getParent() != null )
				{
					((Library)getParent()).turnPhoto(Photo.this);
				}
			}
		});
		setActive(false);
	}
	
	public void load()
	{
		System.out.println("load :" + getSource());
		File file = new File(src);
		Image image = Imager.getScaledImage(file, width, height);
		view.setIcon(new StretchIcon(image));
		title.setText("[" + file.getParentFile().getName() + "]" + file.getName());
		loaded = true;
	}
	
	public void loadPrefs(Viewer viewer)
	{
		Preferences prefs = Preferences.userRoot().node(prefPath);
		Map<String, Color> label2color = viewer.getLabelToColor();
		List<RadioLabel> labels = viewer.getRadioPanel().getLabelList();
		String defaultLabel = (labels.size() > 0 ? labels.get(0).getText() : "-- no selection --");
		label = prefs.get(LABEL_KEY, defaultLabel);
		if( label2color.containsKey(label) )
		{
			view.setBackground(label2color.get(label));
		}
	}
	
	public void savePrefs()
	{
		Preferences prefs = Preferences.userRoot().node(prefPath);
		prefs.put(LABEL_KEY, label);
	}
	
	public String toPreferencePath(String src)
	{
		String orgpath = toLinuxPath(src.toString());
		String[] nodes = orgpath.split("/");
		String path = "";
		for( int i=1; i<nodes.length; i++ )
		{
			String add = "/" + insertSlash(nodes[i], 80);
			path += add;
		}
		return Main.rootNodeName + path;
	}
	
	public static String insertSlash(String org, int length)
	{
		int n = (org.length() - 1) / length + 1;
		String out = "";
		for( int i=0; i<n; i++ )
		{
			int start = i * length;
			int end = Math.min(org.length(), (i + 1) * length);
			if( i != 0 )
			{
				out += "/";
			}
			out += org.substring(start, end);
		}
		return out;
	}
	
	public static String toLinuxPath(String path)
	{
		if( !System.getProperty("file.separator").equals("/") )
		{
			path = "/" + path.replace("\\", "/");
		}
		return path;
	}
	
	public void setActive(boolean flag)
	{
		active = flag;
		if( active )
		{
			setBorder(activeBorder);
			setBackground(activecolor);
			title.setFont(activeFont);
		}
		else
		{
			setBorder(inactiveBorder);
			setBackground(inactivecolor);
			title.setFont(inactiveFont);
		}
	}
	
	public boolean isLoaded()
	{
		return loaded;
	}
	
	public boolean isSubmitted()
	{
		return submitted;
	}
	
	public void setSubmitted(boolean flag)
	{
		submitted = flag;
	}

	public Image getImage()
	{
		return ((ImageIcon)view.getIcon()).getImage();
	}
	
	public String getSource()
	{
		return src;
	}
	
	public void setOptions(String label, Color color)
	{
		this.label = label;
		view.setBackground(color);
		savePrefs();
		System.out.println(label + " " + color);
	}
	
	public String getLabel()
	{
		return label;
	}
	
	
	// public static void main(String[] args)
	// {
	// 	String in = "test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_test_";
	// 	String out = insertSlash(in, 80);
	// 	System.out.println("out: " + out);
	// 	
	// 	in = "home";
	// 	out = insertSlash(in, 80);
	// 	System.out.println("out: " + out);
	// }
}
