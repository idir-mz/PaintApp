package com.idirmeziani;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Hello world!
 *
 */
public class App extends JFrame
{
	
	private JButton brushBtn, lineBtn, ellipseBtn, rectBtn, strokeBtn, fillBtn;
	private int currentAction = 1;
	private Color strokeColor = Color.black, fillColor = Color.black;
	
	
    public static void main( String[] args )
    {
    	new App();
    }
    
    public App() {
    	this.setSize(500, 500);
    	this.setTitle("Java Paint");
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	JPanel btnPanel = new JPanel();
    	Box theBox = Box.createHorizontalBox();
    	
    	brushBtn = makeMeButtons("./src/bruch.png", 1);
    	lineBtn = makeMeButtons("./src/line.png", 2);
    	ellipseBtn = makeMeButtons("./src/ellipse.png", 3);
    	rectBtn = makeMeButtons("./src/rectangle.png", 4);
    	
    	strokeBtn = makeMeColorButton("./src/stroke.png", 5, true);
    	fillBtn = makeMeColorButton("./src/fill.png", 6, false);
    	
    	theBox.add(brushBtn);
    	theBox.add(lineBtn);
    	theBox.add(ellipseBtn);
    	theBox.add(rectBtn);
    	theBox.add(strokeBtn);
    	theBox.add(fillBtn);
    	
    	btnPanel.add(theBox);
    	
    	this.add(btnPanel, BorderLayout.SOUTH);
    	this.add(new DrawingBoard(), BorderLayout.CENTER);
    	
    	this.setVisible(true);
    	
    }


    
	private JButton makeMeColorButton(String iconFile, final int actionNum, final boolean stroke) {
		JButton btn = new JButton();
		Icon btnIcon = new ImageIcon(iconFile);
		btn.setIcon(btnIcon);
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (stroke) {
					strokeColor = JColorChooser.showDialog(null, "pick a stroke", Color.black);
				}else {
					fillColor = JColorChooser.showDialog(null, "pick a fill color", Color.black);
				}
			}
		});
		return btn;
	}

	private JButton makeMeButtons(String iconFile, final int actionNum) {
		JButton btn = new JButton();
		Icon btnIcon = new ImageIcon(iconFile);
		btn.setIcon(btnIcon);
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				currentAction = actionNum;
			}
		});
		
		return btn;
	}
	
	
	
	private class DrawingBoard extends JComponent{
		
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		ArrayList<Color> shapeFill = new ArrayList<Color>();
		ArrayList<Color> shapeStroke = new ArrayList<Color>();
		Point drawStart, drawEnd;
		
		
		public DrawingBoard() {
			
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					drawStart = new Point(e.getPoint());
					drawEnd = drawStart; // handle drawing a dot on the screen
					repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					Shape aShape = drawRectangle(drawStart.x, drawStart.y, e.getX(), e.getY());
					
					shapes.add(aShape);
					shapeFill.add(fillColor);
					shapeStroke.add(strokeColor);
					
					drawStart = null;
					drawEnd = null;
					
					repaint();
				}

			}); // END OF addMouseListener
			
			this.addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseDragged(MouseEvent e) {
					drawEnd = new Point(e.getPoint());
					repaint(); 
				}
	
			}); // END OF addMouseMotionListener
		}


		@Override
		public void paint(Graphics g) {
			Graphics2D graphSettings = (Graphics2D)g;
			
			graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			graphSettings.setStroke(new BasicStroke(2 ));
			
			Iterator<Color> strokeCounter = shapeStroke.iterator();
			Iterator<Color> fillCounter = shapeFill.iterator();
			
			// transparency
			graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
			for (Shape s : shapes) {
				graphSettings.setPaint(strokeCounter.next());
				graphSettings.draw(s);
				
				graphSettings.setPaint(fillCounter.next());
				graphSettings.fill(s);
			}
			
			if (drawStart != null && drawEnd  != null) {
				graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				
				graphSettings.setPaint(Color.gray);
				
				Shape aShape = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
			}
		}


		private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
			
			int x = Math.min(x1, x2);
			int y = Math.min(y1, y2);
			int width = Math.abs(x1-x2);
			int height = Math.abs(y1-y2);
			
			return new Rectangle2D.Float(x, y, width, height);
		}
		
		
	}

    
}
