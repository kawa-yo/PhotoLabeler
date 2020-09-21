package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import view.Photo;

public class RadioPanel extends BorderPanel
{
	public class EditDialog extends JFrame
	{
		public Image trash = Imager.getImage(RadioPanel.class.getResource("/delete.png"));

		public class ColorButton extends JButton
		{
			private Color color = Color.white;

			public ColorButton()
			{
				this(Color.white);
			}
			
			public ColorButton(Color color)
			{
				setIcon(createIcon(color, 16, 16));
				addActionListener(e -> {
					JColorChooser chooser = new JColorChooser(color);
					Color picked = chooser.showDialog(null, "pick color", color);
					if( picked != null )
					{
						this.color = picked;
						setIcon(createIcon(this.color, 16, 16));
						action(this.color);
					}
				});
			}
			
			public void action(Color color)
			{
				System.out.println("do somdething here");
			}

			public Color getColor()
			{
				return color;
			}

			public ImageIcon createIcon(Color main, int width, int height)
			{
			    BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
			    Graphics2D graphics = image.createGraphics();
			    graphics.setColor(main);
			    graphics.fillRect(0, 0, width, height);
			    graphics.setXORMode(Color.DARK_GRAY);
			    graphics.drawRect(0, 0, width-1, height-1);
			    image.flush();
			    ImageIcon icon = new ImageIcon(image);
			    return icon;
			}
		}
		private GridBagConstraints titleGBC;
		private GridBagConstraints middleGBC;
		private GridBagConstraints lastGBC;
		
		private JPanel panel;
		
		
		public EditDialog()
		{
			panel = new JPanel(new GridBagLayout());

			titleGBC = new GridBagConstraints();
			titleGBC.weightx = 0.0;
			titleGBC.gridwidth = 1;
			titleGBC.insets = new Insets(2,2,2,2);
			
			middleGBC = new GridBagConstraints();
			middleGBC.weightx = 1.0;
			middleGBC.gridwidth = 1;
			middleGBC.fill = GridBagConstraints.HORIZONTAL;
			middleGBC.insets = new Insets(2,2,2,2);
			
			lastGBC = new GridBagConstraints();
			lastGBC.weightx = 0.0;
			lastGBC.gridwidth = GridBagConstraints.REMAINDER;
			lastGBC.insets = new Insets(2,2,2,2);
			
			DefaultListModel<RadioLabel> model = group.model;
			
			for( int i=0; i<model.getSize(); i++ )
			{
				RadioLabel label = model.getElementAt(i);
				addLabel(label);
			}
			
			setSize(new Dimension(400, 300));
			setLayout(new BorderLayout());
			add(panel, BorderLayout.CENTER);
			
			JButton close = new JButton("close");
			close.addActionListener(e -> {
				setVisible(false);
			});
			JButton add = new JButton("Add label");
			add.addActionListener(e -> {
				RadioLabel label = new RadioLabel("NEW LABEL", Color.white);
				addLabel(label);
				addRadioLabel(label);
				panel.revalidate();
				panel.repaint();
			});
			add(new FlowPanel(FlowLayout.RIGHT, new Component[] {
					close,
					add
			}), BorderLayout.PAGE_END);
			setVisible(true);
		}
		
		public void addLabel(RadioLabel label)
		{
			ColorButton cbutton = new ColorButton(label.getColor()) {
				@Override
				public void action(Color color)
				{
					label.setColor(color);
					RadioPanel.this.repaint();
					System.out.println(color);
				}
			};
			JTextField field = new JTextField(label.getText());
			field.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) { action(); }
				public void removeUpdate(DocumentEvent e) { action(); }
				public void insertUpdate(DocumentEvent e) { action(); }
				public void action()
				{
					label.setText(field.getText());
					RadioPanel.this.repaint();
				}
			});
			JButton trashButton = new JButton(new ImageIcon(trash));
			trashButton.addActionListener(e -> {
				removeRadioLabel(label);
				// panel.remove(label);
				panel.remove(cbutton);
				panel.remove(field);
				panel.remove(trashButton);
				panel.repaint();
			});
			// panel.add(label, titleGBC);
			panel.add(cbutton, titleGBC);
			panel.add(field, middleGBC);
			panel.add(trashButton, lastGBC);
			field.requestFocus();
		}
	}
	
	public class MyButtonGroup extends JList<RadioLabel>
	{
		DefaultListModel<RadioLabel> model;
		private RadioLabel selected;
		
		public MyButtonGroup()
		{
			model = new DefaultListModel<>();
			setModel(model);
			setCellRenderer((list, value, index, selected, focus) -> {
				value.setBackground(selected? list.getSelectionBackground() : null);
				// value.setSelected(selected);
				return value;
			});
		}

		public void add(RadioLabel label)
		{
			model.addElement(label);
			label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					setSelected(label);
				}
			});
		}
		
		public void remove(RadioLabel label)
		{
			model.removeElement(label);
		}
		public void setSelected(RadioLabel label)
		{
			System.out.println("trying set " + label.getText() + " selected");
			if( selected != null )
			{
				selected.setSelected(false);
			}
			selected = label;
			label.setSelected(true);
			System.out.println("end");
		}
		
		public RadioLabel getSelected()
		{
			return selected;
		}
	}

	private JButton settingButton;
	private Image settingImage = Imager.getImage(RadioPanel.class.getResource("/settings.png"));
	
	protected MyButtonGroup group;
	
	public RadioPanel()
	{
		group = new MyButtonGroup();
		setCenter(group);
		
		settingButton = new JButton(new ImageIcon(settingImage));
		settingButton.addActionListener(e -> {
			new EditDialog();
		});
		setTop(new FlowPanel(FlowLayout.RIGHT, new Component[] {
				settingButton
		}));
	}
	
	public void keyBinding()
	{
		for( int i=0; i<10; i++ )
		{
			final int index = i;
			String key = Integer.toString(i);
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
			getActionMap().put(key, new AbstractAction() {
				public void actionPerformed(ActionEvent e)
				{
					DefaultListModel<RadioLabel> model = group.model;
					System.out.println(index + "/" + model.getSize() + " entered");
					if( model.getSize() > index )
					{
						RadioLabel label = model.getElementAt(index);
						group.setSelected(label);
						repaint();
					}
				}
			});
		}
	}
	
	public void addRadioLabel(RadioLabel label)
	{
		group.add(label);
	}
	
	public void removeRadioLabel(RadioLabel label)
	{
		group.remove(label);
	}
	
	public List<RadioLabel> getLabelList()
	{
		List<RadioLabel> labels = new ArrayList<>();
		DefaultListModel<RadioLabel> model = group.model;
		for( int i=0; i<model.getSize(); i++ )
		{
			labels.add(model.getElementAt(i));
		}
		return labels;
	}
	
	public RadioLabel getSelected()
	{
		return group.getSelected();
	}
	
	public void setSelected(RadioLabel label)
	{
		System.out.println("start RadioPanel.setSelected");
		group.setSelected(label);
		repaint();
		System.out.println("end RadioPanel.setSelected");
	}
	
	public void setSelected(String text)
	{
		DefaultListModel<RadioLabel> model = group.model;
		RadioLabel selected = null;
		for( int i=0; i<model.getSize(); i++ )
		{
			RadioLabel label = model.getElementAt(i);
			if( label.getText().equals(text) )
			{
				selected = label;
				break;
			}
		}
		if( selected != null )
		{
			setSelected(selected);
		}
	}
}
