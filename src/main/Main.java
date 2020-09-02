package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import file_tree.FileTree;
import file_tree.TreeNode;
import utils.BorderPanel;
import utils.Empty;
import utils.Menu;
import utils.MenuBar;
import utils.MenuItem;
import utils.ScrollPanel;
import utils.SplitPanel;
import utils.TimerMenu;
import view.Library;
import view.Viewer;


public class Main extends JFrame
{
	public static final String rootNodeName = "/PhotoLabeler";
	public static final String DIRECTORY_KEY = "DIRECTORY";

	private MenuBar menubar;
	private TimerMenu timerMenu;
	private FileTree filetree;
	private Viewer viewer;
	private Library library;
	private BorderPanel mainPanel;
	
	
	public Main()
	{

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch( Exception e ) {
				e.printStackTrace();
			}
			initialize();
			keyBinding();
			loadPrefs();
		}); 
	}
	
	public void keyBinding()
	{
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "SAVE");
		mainPanel.getActionMap().put("SAVE", new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});
		viewer.getRadioPanel().keyBinding();
	}
	
	public void save()
	{
		TreeNode root = filetree.getRoot();
		if( root != null )
		{
			File file = new File(root.getSource());
			new SaveCSVDialog(file, library);
		}
	}
	
	public void loadPrefs()
	{
		Preferences prefs = Preferences.userRoot().node(rootNodeName);
		String prevDire = prefs.get(DIRECTORY_KEY, "");
		File file = new File(prevDire);
		if( file != null )
		{
			filetree.action(file);
		}
	}
	
	public void savePrefs()
	{
		Preferences prefs = Preferences.userRoot().node(rootNodeName);
		TreeNode root = filetree.getRoot();
		if( root != null )
		{
			File file = new File(root.getSource());
			prefs.put(DIRECTORY_KEY, file.toString());
		}
		timerMenu.savePrefs();
	}
	
	public void initialize()
	{
		timerMenu = new TimerMenu();

		menubar = new MenuBar(new Menu[] {
				new Menu("File", new MenuItem[] {
						new MenuItem("Save", e -> save()),
						new MenuItem("Close", e -> System.out.println("close"))
				}),
				new Menu("Edit", new MenuItem[] {
						new MenuItem("reset Timer", e -> timerMenu.reset()),
						new MenuItem("Close", e -> System.out.println("close"))
				})
		});
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.add(menubar, BorderLayout.CENTER);
		menuPanel.add(timerMenu, BorderLayout.LINE_END);
		filetree = new FileTree(this);
		viewer = new Viewer(this);
		library = new Library(this);
		
		// CSVPanel csvPanel = new CSVPanel();

		mainPanel = new BorderPanel();
		getContentPane().add(mainPanel);

		mainPanel.setContents(
				menuPanel,
				new Empty(),
				new SplitPanel(JSplitPane.HORIZONTAL_SPLIT, true, 0.75,
						new SplitPanel(JSplitPane.HORIZONTAL_SPLIT, true, 0.1,
								filetree,
								viewer
						),
						new ScrollPanel(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
								library
						)
				),
				new Empty(),
				new Empty()
		);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				viewer.savePrefs();
				savePrefs();
			}
		});
	}
	
	public Viewer getViewer()
	{
		return viewer;
	}
	
	public Library getLibrary()
	{
		return library;
	}


	public static void main(String[] args)
	{
		Main c = new Main();
		c.setSize(new Dimension(1500, 600));
		c.setVisible(true);
	}
}
