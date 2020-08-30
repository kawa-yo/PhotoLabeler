package file_tree;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

import main.Main;
import view.Photo;

public class TreeNode extends DefaultMutableTreeNode
{
	private Main contents;
	private Photo photo;
	
	private String src;
	private String name;

	public TreeNode(File file)
	{
		src = file.toString();
		name = file.getName();
	}
	
	public static TreeNode getSelectedNodes(TreeNode org)
	{
		File file = new File(org.getSource());
		TreeNode node = new TreeNode(file);
		if( org.isLeaf() )
		{
			// if( org.getPhoto().isSelected() )
			// {
			// 	node.setPhoto(org.getPhoto());
			// }
			// else
			// {
			// 	node = null;
			// }
			node.setPhoto(org.getPhoto());
		}
		else
		{
			for( int i=0; i<org.getChildCount(); i++ )
			{
				TreeNode child = (TreeNode)org.getChildAt(i);
				TreeNode selected = getSelectedNodes(child);
				if( selected != null )
				{
					node.add(selected);
				}
			}
			if( node.getChildCount() == 0 )
			{
				node = null;
			}
		}
		return node;
	}
	
	public Photo getPhoto()
	{
		return photo;
	}
	
	public void setPhoto(Photo photo)
	{
		this.photo = photo;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
	
	public String getSource()
	{
		return src;
	}
}
