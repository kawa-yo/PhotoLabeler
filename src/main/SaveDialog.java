package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import file_tree.TreeNode;

/*
 * 	panel0
 * 		panel1(CENTER)
 * 			panel1_1(CENTER)
 * 				scroll1_1
 * 					panel1_1_1
 * 						tree
 * 			panel1_2(BOTTOM)
 * 				directorychooser
 * 		panel2(BOTTOM)(FlowLayout)
 * 			save
 * 			cancel
 */
public class SaveDialog extends JFrame
{
	private JPanel panel0;
	private JPanel panel1;
	private JPanel panel2;
	
	private JPanel panel1_1;
	private JPanel panel1_2;
	
	private JScrollPane scroll1_1;
	private JPanel panel1_1_1;
	
	private JTree tree;
	private JTextField field;
	private JButton browse;
	
	private JButton save;
	private JButton cancel;
	
	public SaveDialog(TreeNode root)
	{
		layout(root);
		save.setEnabled(false);
		File pardir = new File(root.getSource() + "_selected");
		// padir = increase(pardir);
		setDirectory(pardir.toString());
		getContentPane().add(panel0);
		setSize(new Dimension(600, 400));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void layout(TreeNode root)
	{
		panel0 = new JPanel(new BorderLayout());

		panel1 = new JPanel(new BorderLayout());
		panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		panel0.add(panel1, BorderLayout.CENTER);
		panel0.add(panel2, BorderLayout.PAGE_END);
		
		panel1_1 = new JPanel(new BorderLayout());
		panel1_2 = new JPanel(new BorderLayout());
		panel1.add(panel1_1, BorderLayout.CENTER);
		panel1.add(panel1_2, BorderLayout.PAGE_END);
		
		panel1_1_1 = new JPanel(new BorderLayout());
		scroll1_1 = new JScrollPane(panel1_1_1);
		panel1_1.add(scroll1_1);
		
		tree = new JTree(TreeNode.getSelectedNodes(root));
		panel1_1_1.add(tree);
		
		field = new JTextField("/directory/to/save/here");
		field.setOpaque(true);
		field.setBackground(Color.white);
		field.setForeground(new Color(100, 100, 100));
		field.setEditable(false);
		browse = new JButton("browse");
		browse.addActionListener(e -> {
			File file = browse();
			setDirectory(file.toString());
		});
		panel1_2.add(field, BorderLayout.CENTER);
		panel1_2.add(browse, BorderLayout.LINE_END);
		
		save = new JButton("save");
		save.addActionListener(e -> {
			boolean succeeded = save();
			if( succeeded ) { dispose(); }
		});
		cancel = new JButton("cancel");
		cancel.addActionListener(e -> {
			dispose();
		});
		panel2.add(save);
		panel2.add(cancel);
	}
	
	public File increase(File src)
	{
		File out = src;
		for( int i=1; out.exists(); i++ )
		{
			String str = src.toString() + "(" + i + ")";
			out = new File(str);
		}
		
		return out;
	}
	
	public void setDirectory(String dir)
	{
		((TreeNode)tree.getModel().getRoot()).setName(dir);
		field.setText(dir);
		field.setForeground(new Color(50, 50, 50));
		save.setEnabled(true);
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
	
	public Map<File, File> getSaveMap(File dir, TreeNode node)
	{
		Map<File, File> map = new HashMap<>();
		File out = new File(dir, node.toString());
		if( node.isLeaf() )
		{
			map.put(new File(node.getSource()), out);
		}
		else
		{
			for( int i=0; i<node.getChildCount(); i++ )
			{
				map.putAll(getSaveMap(out, (TreeNode)node.getChildAt(i)));
			}
		}
		return map;
	}
	
	public boolean save()
	{
		boolean flag = true;
		if( tree == null )
		{
			flag = false;
		}
		else
		{
			TreeNode root = (TreeNode)tree.getModel().getRoot();
			Map<File, File> savemap = getSaveMap(new File(""), root);
			for( Map.Entry<File, File> entry : savemap.entrySet() )
			{
				File src = entry.getKey();
				File out = entry.getValue();
				File dir = out.getParentFile();
				if( !dir.exists() )
				{
					dir.mkdirs();
				}
				try
				{
					Files.copy(src.toPath(), out.toPath());
				}
				catch( FileAlreadyExistsException e )
				{
					System.out.println(out + " already exists.");
				}
				catch( IOException ex )
				{
					ex.printStackTrace();
					flag = false;
				}
			}
		}
		return flag;
	}
}
