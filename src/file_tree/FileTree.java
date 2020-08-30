package file_tree;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import main.Main;
import view.Photo;

public class FileTree extends JPanel
{
	private Main contents;
	private JPanel panel1;
	private JTextField field;
	private JButton button;
	
	private JPanel panel2;
	private JScrollPane scroll2;
	private JPanel panel2_1;
	private JTree filetree;
	
	private Thread treethread;
	private boolean running = false;
	
	
	public FileTree(Main c)
	{
		super(new BorderLayout());
		contents = c;
		
		panel1 = new JPanel(new BorderLayout());
		field = new JTextField();
		field.setEditable(false);
		button = new JButton("Browse");
		button.addActionListener(e -> {
			File file = browse();
			if( file != null )
			{
				field.setText(file.toString());
				loadTree(file);
			}
		});
		panel1.add(field, BorderLayout.CENTER);
		panel1.add(button, BorderLayout.LINE_END);
		add(panel1, BorderLayout.PAGE_START);

		panel2 = new JPanel(new BorderLayout());
		panel2_1 = new JPanel(new BorderLayout());
		scroll2 = new JScrollPane(
				panel2_1,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel2.add(scroll2, BorderLayout.CENTER);
		add(panel2, BorderLayout.CENTER);
		
		filetree = new JTree();
		filetree.setScrollsOnExpand(false);
		filetree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				TreePath path = filetree.getPathForLocation(e.getX(), e.getY());
				if( path != null )
				{
					if( e.getClickCount() == 2 && filetree.isExpanded(path) )
					{
						TreeNode node = (TreeNode)path.getLastPathComponent();
						contents.getLibrary().reset();
						setLibrary(node);
					}
				}
			}
		});
		((DefaultTreeModel)filetree.getModel()).setRoot(null);
		panel2_1.add(filetree);
	}
	
	
	public File browse()
	{
		File file = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION )
		{
			file = chooser.getSelectedFile();
		}
		return file;
	}
	
	public void loadTree(File file)
	{
		contents.getLibrary().initialize();
		if( treethread != null && treethread.isAlive() )
		{
			running = false;
			try {
				treethread.join();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		treethread = new Thread(() -> {
				TreeNode root = new TreeNode(file);
				addChildren(root);
				System.out.println("added");
				DefaultTreeModel model = (DefaultTreeModel) filetree.getModel();
				model.setRoot(root);
		});
		running = true;
		treethread.start();
	}
	
	public void setLibrary(TreeNode root)
	{
		if( root.isLeaf() )
		{
			contents.getLibrary().add(root.getPhoto());
		}
		else
		{
			for( int i=0; i<root.getChildCount(); i++ )
			{
				TreeNode child = (TreeNode)root.getChildAt(i);
				setLibrary(child);
			}
		}
	}
	
	public void addChildren(TreeNode node)
	{
		if( !running ) return;
		
		File src = new File(node.getSource());
		if( src.isFile() && isImage(src) )
		{
			Photo photo = new Photo(src);
			node.setPhoto(photo);
			contents.getLibrary().add(photo);
			return;
		}
		if( src.isDirectory() )
		{
			File[] children = src.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String suffix)
				{
					File file = new File(dir, suffix);
					return (!suffix.startsWith(".")) && (file.isDirectory() || isImage(file));
				}
			});
			Arrays.sort(children);

			for( File child : children )
			{
				TreeNode childnode = new TreeNode(child);
				addChildren(childnode);
				if( child.isFile() || childnode.getChildCount() > 0 )
				{
					node.add(childnode);
				}
			}
		}
	}
	
	public TreeNode getRoot()
	{
		return (TreeNode)filetree.getModel().getRoot();
	}
	
	
	public static final String[] formats = {
		".png", ".jpeg", ".jpg", ".PNG", ".JPEG", ".JPG"
	};
	
	public static boolean isImage(File file)
	{
		String name = file.getName();
		boolean flag = false;
		for( String f : formats )
		{
			if( name.endsWith(f)) flag = true;
		}
		return flag;
	}
}
