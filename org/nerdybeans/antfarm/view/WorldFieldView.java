package org.nerdybeans.antfarm.view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.controller.ModelController;
import org.nerdybeans.antfarm.model.WorldField;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * @author Demarcsek, Horvath, Szabo
 **/
public class WorldFieldView extends JPanel implements MouseListener {
	/*private Color BgColor;*/
	
	/**
	 * @deprecated
	 */
	private WorldField field;
	
	private Stack<WorldElementView> ElementGraphics;
	
	/**
	 * @author Demarcsek, Horvath
	 */
	public WorldFieldView() {
		super();
		//this.BgColor = Color.blue;
		this.ElementGraphics = new Stack<WorldElementView>();
		this.addMouseListener(this);
	}
	
	/**
	 * @author Horvath
	 * @param newElem
	 */
	public void addElement(WorldElementView newElem){
		if (this.ElementGraphics == null){
			this.ElementGraphics = new Stack<WorldElementView>();
		}
		
		this.ElementGraphics.push(newElem);
	}
	
	/**
	 * @author Horvath
	 * @return
	 */
	public WorldElementView getElement(){
		assert(this.ElementGraphics != null);
		return this.ElementGraphics.peek();
	}
	
	/**
	 * @author Horvath
	 * @return
	 */
	public WorldElementView removeElement(){
		assert(this.ElementGraphics != null);
		return this.ElementGraphics.pop();
	}
	
	/**
	 * @author Horvath
	 * TODO nincsen benne az osztalydiagramban!
	 */
	public void clearElements() {
		if (this.ElementGraphics != null){
			this.ElementGraphics.clear();
		}
	}
	
	/**
	 * @author Demarcsek
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Write draw algorithm
		super.paint(g);
		//System.out.println("WorldFieldView.paint");
		Graphics2D g2 = (Graphics2D) g;
		
		g2.clearRect(0, 0, this.getVisibleRect().width, this.getVisibleRect().height);
		
		/*System.out.println("[DEBUG#WorldFieldView.paint]");
		
		//g2.setBackground(BgColor);
		g2.setColor(this.BgColor);
		g2.fill(this.getVisibleRect());
		*/
		
		
		
		//g2.setColor(Color.black)
		
		//g2.drawRect(0, 0, 5, 10);
		//System.out.println("FO @ " + (this) + " :" + Integer.toString((ModelController.getInstance().getModel(this).getFoodOdour())));
		
		/*WorldField Me = ModelController.getInstance().getModel(this);
		g2.setFont(new java.awt.Font("Arial", Font.PLAIN, 10));
		g2.drawString(
				"ao=" + Float.toString(Me.getAntOdour())
				, 2, 12);
		g2.drawString(
				"fo= " + Float.toString(Me.getFoodOdour())
				, 2, 24);*/
		
		
		for(WorldElementView elem : this.ElementGraphics) {
			if(elem == null)
				break;
			
			BufferedImage Img = elem.getSprite();
			if(Img != null)
				g2.drawImage(Img, null, 0, 0);
		}
		
		WorldField Me = ModelController.getInstance().getModel(this);
		/*if(!Me.getAntList().isEmpty()) {
			String A = Integer.toString(Me.getAntList().size());
			g2.setColor(Color.white);
			g2.drawString(A, 12, 12);
		}*/
		//g2.setColor(Color.white);
		//g2.setFont(new java.awt.Font("Arial", Font.PLAIN, 10));
		this.setToolTipText(
			"AntOdour:" + Float.toString(Me.getAntOdour()) + "\r\n" +
			"FoodOdour: " + Float.toString(Me.getFoodOdour()) + "\r\n" +
			"Poison: " + Float.toString(Me.getPoison())
		);
		
		//g2.setColor(Color.black);
		//g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
		
		if(Me.getPoison() > 0) {
			g2.setColor(new Color(0.0f, 1.0f, 1.0f, 0.33f));
			g2.fillRect(0, 0, this.getWidth()-1, this.getHeight()-1);
			//g2.fillRect(x, y, width, height)
		}
		
		g2.dispose();
	}
	
	/**
	 * @author Demarcsek
	 */
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		System.out.println("[DEBUG#WorldFieldView.mouseClicked]");
		/*JOptionPane.showMessageDialog(this,
			    "Click on WorldFieldView ",
			    "Event info",
			    JOptionPane.INFORMATION_MESSAGE);*/
		if(evt.getButton() == MouseEvent.BUTTON1 || evt.getButton() == MouseEvent.BUTTON3) {
			ModelController.getInstance().userCommand("fire", new Object[] { evt, this });
		}
	}

	/**
	 * @author Demarcsek, Szabo
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		//System.out.println("[DEBUG#WorldFieldView.mouseEntered]");
		//this.BgColor = Color.red;
		//this.repaint();
	}
	
	/**
	 * @author Demarcsek, Szabo
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		//System.out.println("[DEBUG#WorldFieldView.mouseExited]");
		//this.BgColor = Color.blue;
		//this.repaint();
	}
	
	/**
	 * @author Demarcsek, Szabo
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		return;
	}
	
	/**
	 * @author Demarcsek, Szabo
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		return;
	}
	
}
