package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import file_tree.FileTree;
import file_tree.TreeNode;
import utils.BorderPanel;
import utils.Empty;
import utils.RadioPanel;
import utils.ScrollPanel;
import utils.SplitPanel;
import view.Library;
import view.Viewer;


public class Main extends JFrame
{
	public static final String rootNodeName = "/PhotoLabeler";

	private JMenuBar menubar;
	private FileTree filetree;
	private Viewer viewer;
	private Library library;
	private BorderPanel mainPanel;
	private RadioPanel selection;
	
	public Main()
	{

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SwingUtilities.invokeLater(() -> {
			initialize();
			keyBinding();
		}); 
	}
	
	public void keyBinding()
	{
		mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "SAVE");
		mainPanel.getActionMap().put("SAVE", new SaveAction());
	}
	
	public class SaveAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TreeNode root = filetree.getRoot();
			if( root != null )
			{
				File file = new File(root.getSource());
				new SaveCSVDialog(file, library);
			}
		}
	}
	
	public void initialize()
	{
		menubar = new JMenuBar();
		filetree = new FileTree(this);
		viewer = new Viewer(this);
		library = new Library(this);
		selection = new RadioPanel();
		
		// CSVPanel csvPanel = new CSVPanel();

		mainPanel = new BorderPanel();
		getContentPane().add(mainPanel);

		mainPanel.setContents(
				menubar,
				new Empty(),
				new SplitPanel(JSplitPane.HORIZONTAL_SPLIT, true, 0.15,
						filetree,
						new SplitPanel(JSplitPane.HORIZONTAL_SPLIT, true, 0.85,
								viewer,
								new ScrollPanel(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
										library
								)
						)
				),
				new Empty(),
				new Empty()
		);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				viewer.savePrefs();
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
