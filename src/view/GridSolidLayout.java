package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

public class GridSolidLayout implements LayoutManager, Serializable
{
	int cw;
	int ch;

	int rows;
	int cols;
	
	int hgap;
	int vgap;
	
	public GridSolidLayout()
	{
		this(1, 0, 0, 0, -1, -1);
	}
	
	public GridSolidLayout(int rows, int cols)
	{
		this(rows, cols, 0, 0, -1, -1);
	}
	
	public GridSolidLayout(int rows, int cols, int hgap, int vgap)
	{
		this(rows, cols, hgap, vgap, -1, -1);
	}
	
	public GridSolidLayout(int rows, int cols, int hgap, int vgap, int cw, int ch)
	{
		if( rows < 0 || cols < 0 || hgap < 0 || vgap < 0 )
			throw new IllegalArgumentException("rows, cols, hgap and vgap shoud be >=0.");
		if( rows == 0 && cols == 0 )
			throw new IllegalArgumentException("either rows or cols should not be 0.");
		if( cw == 0 || ch == 0 )
			throw new IllegalArgumentException("both cw or ch should be >0.");
		this.rows = rows;
		this.cols = cols;
		this.hgap = hgap;
		this.vgap = vgap;
		this.cw = cw;
		this.ch = ch;
	}
	
	
	public void addLayoutComponent(String name, Component c)
	{

	}
	
	/** Lay out the container's components based on current settings.
	 * The free space in the container is divided evenly into the specified
	 * number of rows and columns in this object.
	 * @param parent The container to lay out
	 */
	public void layoutContainer(Container parent)
	{
		synchronized( parent.getTreeLock() )
		{
			// for( StackTraceElement ele : Thread.currentThread().getStackTrace() )
			// {
			// 	System.out.println(ele);
			// }
			// System.out.println("now lay out...");
			int num = parent.getComponentCount();
			
			if( num == 0 ) return;
			Component[] comps = parent.getComponents();
			
			int real_rows = rows;
			int real_cols = cols;

			if( real_rows == 0 )
				real_rows = (num + real_cols - 1) / real_cols;
			else
				real_cols = (num + real_rows - 1) / real_rows;
			
			if( num < real_cols )
				real_cols = num;
			
			Dimension d = parent.getSize();
			Insets ins = parent.getInsets();
			
			int tw, th;
			
			if( cw > 0 ) {
				tw = cw;
			}
			else {
				tw = d.width - ins.left - ins.right;
				tw = (tw - (real_cols - 1) * hgap) / real_cols;
			}

			if( ch > 0 ) {
				th = ch;
			}
			else {
				th = d.height - ins.top - ins.bottom;
				th = (th - (real_rows - 1) * vgap) / real_rows;
			}
			
			if( tw < 0 ) tw = 1;
			if( th < 0 ) th = 1;
			
			
			int x = ins.left;
			int y = ins.top;
			for( int i=0, recount=0; i<num; i++)
			{
				comps[i].setBounds(x, y, tw, th);
				
				recount++;
				if( recount == real_cols )
				{
					recount = 0;
					y += vgap + th;
					x = ins.left;
				}
				else
				{
					x += hgap + tw;
				}
			}
		}
	}
	
	public Dimension minimumLayoutSize(Container cont)
	{
		return getSize(cont, true);
	}
	
	public Dimension preferredLayoutSize(Container cont)
	{
		return getSize(cont, false);
	}
	
	public void removeLayoutComponent(Component comp)
	{
		
	}
	
	public Dimension getSize(Container parent, Boolean is_min)
	{
		synchronized( parent.getTreeLock() )
		{
			int w = 0;
			int h = 0;
			int num = parent.getComponentCount();
			Component[] comps = parent.getComponents();
			
			if( cw == -1 || ch == -1 )
			{
				for(int i=0; i<num; i++)
				{
					Dimension d;
					
					if( is_min )
						d = comps[i].getMinimumSize();
					else
						d = comps[i].getPreferredSize();
					w = Math.max(d.width, w);
					h = Math.max(d.height, h);
				}
			}
			else
			{
				w = cw;
				h = ch;
			}
			
			int real_rows = rows;
			int real_cols = cols;

			if( real_rows == 0 )
				real_rows = (num + real_cols - 1) / real_cols;
			else
				real_cols = (num + real_rows - 1) / real_rows;
			
			Insets ins = parent.getInsets();
			w = ins.left + ins.right + real_cols * (w + hgap) - hgap;
			h = ins.top + ins.bottom + real_rows * (h + vgap) - vgap;
			return new Dimension(w, h);
		}
	}
	
	public String toString()
	{
		return (getClass().getName() + "["
				+ "hgap=" + hgap + ",vgap=" + vgap
				+ ",rows=" + rows + ",cols=" + cols
				+ ",cw=" + cw + ",ch=" + ch + "]");
	}
	
	
	public int getRows()
	{
		return rows;
	}
	
	public int getColumns()
	{
		return cols;
	}
	
	public int getHgap()
	{
		return hgap;
	}
	
	public int getVgap()
	{
		return vgap;
	}
	
	public int getCW()
	{
		return cw;
	}
	
	public int getCH()
	{
		return ch;
	}
	
	
	public void setRows(int rows)
	{
		this.rows = rows;
	}
	
	public void setColumns(int cols)
	{
		this.cols = cols;
	}
	
	public void setHgap(int hgap)
	{
		this.hgap = hgap;
	}
	
	public void setVgap(int vgap)
	{
		this.vgap = vgap;
	}
	
	public void setCW(int cw)
	{
		this.cw = cw;
	}
	
	public void setCH(int ch)
	{
		this.ch = ch;
	}
}
