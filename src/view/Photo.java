package view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import utils.RadioButton;

public class Photo extends JPanel
{
	public static final String LABEL_KEY = "LABEL";
	public static final int width = 200;
	public static final int height = 200;
	public static final Color activecolor = new Color(255, 128, 128);
	public static final Color inactivecolor = new Color(240, 240, 240);
	public static final int viewpad = 2;
	public static final Border activeBorder = BorderFactory.createLineBorder(Color.red);
	public static final Border inactiveBorder = BorderFactory.createLineBorder(Color.white);

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
		prefPath = Main.rootNodeName + toLinuxPath(src.toString());
		
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
		List<RadioButton> buttonList = viewer.getRadioPanel().getButtonList();
		String defaultLabel = (buttonList.size() > 0 ? buttonList.get(0).getText() : "-- no selection --");
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
	
	public String toLinuxPath(String path)
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
		}
		else
		{
			setBorder(inactiveBorder);
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
	}
	
	public String getLabel()
	{
		return label;
	}
}
