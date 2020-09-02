package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ext.StretchIcon;
import main.Main;
import utils.Imager;
import utils.RadioButton;
import utils.RadioPanel;

public class Viewer extends JPanel
{
	private Main contents;
	private JLabel view;
	private JLabel title;
	private JLabel subTitle;
	private Photo photo;
	
	private RadioPanel radios;
	
	private ExecutorService exec = Executors.newSingleThreadExecutor(r -> {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	});
	
	private static final String prefPath = Main.rootNodeName + "/LABEL_OPTION";
	private static final String LABEL_NUM_KEY = "NUM";
	private static final String LABEL_KEY = "LABEL";
	private static final String COLOR_KEY = "COLOR";
	
	private Map<String, Color> label2color;

	public Viewer(Main c)
	{
		super(new BorderLayout());
		contents = c;
		radios = new RadioPanel() {
			@Override
			public void addRadioButton(RadioButton button)
			{
				super.addRadioButton(button);
				button.addItemListener(e -> {
					if( e.getStateChange() == ItemEvent.SELECTED )
					{
						if( photo != null )
						{
							photo.setOptions(button.getText(), button.getColor());
						}
					}
				});
			}
		};

		loadPrefs();

		view = new JLabel();
		title = new JLabel();
		title.setHorizontalAlignment(SwingConstants.CENTER);
		subTitle = new JLabel();
		subTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(view, BorderLayout.CENTER);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(subTitle, BorderLayout.PAGE_START);
		panel.add(title, BorderLayout.PAGE_END);
		add(panel, BorderLayout.PAGE_END);
		add(radios, BorderLayout.LINE_END);
		addMouseWheelListener(new MouseWheelListener() {
			public void	mouseWheelMoved(MouseWheelEvent e)
			{
				Library library = contents.getLibrary();
				if( library != null )
				{
					if( e.getWheelRotation() < 0 ) library.turnPhoto(-1);
					if( e.getWheelRotation() > 0 ) library.turnPhoto(+1);
				}
			}
		});
	}
	
	public void loadPrefs()
	{
		Preferences prefs = Preferences.userRoot().node(prefPath);
		int num = prefs.getInt(LABEL_NUM_KEY, 4);
		label2color = new HashMap<>();

		for( int i=0; i<num; i++ )
		{
			final int id = i;

			Preferences node = prefs.node(Integer.toString(i));
			String label = node.get(LABEL_KEY, "label " + i);
			Color color = new Color(node.getInt(COLOR_KEY, 0x000000));
			RadioButton button = new RadioButton(label, color);
			radios.addRadioButton(button);
			label2color.put(label, color);
		}
	}
	
	public void savePrefs()
	{
		Preferences prefs = Preferences.userRoot().node(prefPath);

		List<RadioButton> buttonlist = radios.getButtonList();
		prefs.putInt(LABEL_NUM_KEY, buttonlist.size());
		for( int i=0; i<buttonlist.size(); i++ )
		{
			Preferences node = prefs.node(Integer.toString(i));
			RadioButton button = buttonlist.get(i);
			node.put(LABEL_KEY, button.getText());
			node.putInt(COLOR_KEY, button.getColor().getRGB());
		}
	}
	
	public void setPhoto(Photo photo)
	{
		this.photo = photo;
		ImageIcon icon = new StretchIcon(photo.getImage());
		view.setIcon(icon);
		File file = new File(photo.getSource());
		subTitle.setText("[" + file.getParentFile().getName() + "]");
		title.setText(file.getName());
		load(photo);
	}
	
	private void load(Photo photo)
	{
		exec.execute(() -> {
			if( photo == contents.getLibrary().getActive() )
			{
				Image image = Imager.getImage(photo.getSource());
				ImageIcon icon = new StretchIcon(image);
				if( photo == contents.getLibrary().getActive() )
				{
					view.setIcon(icon);
				}
			}
		});
	}
	
	public RadioPanel getRadioPanel()
	{
		return radios;
	}
	
	public Map<String, Color> getLabelToColor()
	{
		return label2color;
	}
}
