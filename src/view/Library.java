package view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import main.Main;

public class Library extends JPanel implements MouseWheelListener
{
	private Main contents;
	private GridSolidLayout gl;
	
	private int active = 0;
	
	private final int npool = 2;

	private ExecutorService exec;
	private boolean skip = false;
	private long nskipped = 0;

	public static int ncol = 1;
	public static int gap = 2;
	
	private int n_photos = 0;
	

	public Library(Main c)
	{
		contents = c;
		gl = new GridSolidLayout(0, ncol, gap, gap, 200, 200);
		setLayout(gl);
		
		addMouseWheelListener(this);
	}
	
	public void keyBinding()
	{
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "UP");
		getActionMap().put("UP", new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				turnPhoto(-ncol);
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
		getActionMap().put("DOWN", new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				turnPhoto(+ncol);
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
		getActionMap().put("LEFT", new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				turnPhoto(-1);
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
		getActionMap().put("RIGHT", new AbstractAction() {
			public void actionPerformed(ActionEvent e)
			{
				turnPhoto(+1);
			}
		});
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if( e.isControlDown() )
		{
			if( e.getWheelRotation() < 0 ) ncol = Math.max(ncol-1, 1);
			if( e.getWheelRotation() > 0 ) ncol++;
			repaint();
		}
		else
		{
			getParent().dispatchEvent(e);
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		int size = (getWidth() - gap) / ncol - gap;

		if( size != gl.getCW() )
		{
			gl.setCW(size);
			gl.setCH(size + 10);
			gl.layoutContainer(this);
			revalidate();
		}

		if( ncol != gl.getColumns() )
		{
			gl.setColumns(ncol);
			revalidate();
		}
		super.paint(g);
	}
	
	public List<Integer> sortedIndices()
	{
		List<Integer> ids = new ArrayList<>();
		for( int i=0; i<getComponents().length; i++ )
		{
			ids.add(i);
		}
		Collections.sort(ids, (id1, id2) -> {
				Photo p1 = (Photo)(getComponent(id1));
				Photo p2 = (Photo)(getComponent(id2));
				return p1.getName().compareTo(p2.getName());
		});
		return ids;
	}
	
	public synchronized void add(Photo photo)
	{
		super.add(photo);
		if( photo.isLoaded() )
		{
			System.out.println(photo.getSource() + " is already loaded.");
			skip = false;
		}
		if( !photo.isSubmitted() )
		{
			submit(photo);
		}
		if( getComponents().length == 1 )
		{
			turnPhoto(-active);
		}
	}
	
	public void submit(Photo photo)
	{
		photo.setSubmitted(true);
		exec.execute(() -> {
			if( skip )
			{
				if( photo.getParent() == this )
				{
					skip = false;
				}
				else
				{
					submit(photo);
					System.out.println("skip " + (++nskipped));
					return;
				}
			}
			if( !photo.isLoaded() )
			{
				photo.load();
				photo.loadPrefs(contents.getViewer());
				revalidate();
				repaint();
			}
		});
	}
	
	public void reset()
	{
		if( getComponents().length > 0 )
		{
			getActive().setActive(false);
		}
		removeAll();
		active = 0;
		skip = true;
	}
	
	public void initialize()
	{
		if( exec != null ) exec.shutdown();
		reset();
		exec = Executors.newFixedThreadPool(npool, r -> {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		});
	}
	
	public void turnPhoto(int delta)
	{
		if( getComponents() == null || active+delta < 0 || active+delta >= getComponents().length )
		{
			return;
		}
		Photo prev = (Photo)getComponents()[active];
		if( prev != null )
		{
			prev.setActive(false);
		}

		Photo next = (Photo)getComponents()[active+=delta];
		next.setActive(true);
		if( !next.isLoaded() ) next.load();
		contents.getViewer().setPhoto(next);
		contents.getViewer().getRadioPanel().setSelected(next.getLabel());
		scrollToVisible(next);
	}

	
	public void turnPhoto(Photo photo)
	{
		List<Component> list = Arrays.asList(getComponents());
		turnPhoto(list.indexOf(photo) - active);
	}
	
	
	public void scrollToVisible(Photo photo)
	{
		Rectangle bounds = photo.getBounds();

		if( bounds.y + getY() > getParent().getHeight()/2 )
		{
			bounds.y += (getParent().getHeight() - bounds.height)/2;
		}
		else
		{
			bounds.y -= (getParent().getHeight() - bounds.height)/2;
		}
		scrollRectToVisible(bounds);
	}
	
	public Photo getActive()
	{
		return (Photo)getComponents()[active];
	}
}
