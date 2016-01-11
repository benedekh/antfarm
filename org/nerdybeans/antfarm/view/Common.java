package org.nerdybeans.antfarm.view;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

/**
 * 
 * The Common class for storing all the graphical representations of the WorldElements
 * in a BufferedImage file. Each WorldElementView cuts its own graphical representation
 * from the SpriteBase using the getImageFromCoordinate method, that gives back an Image
 * based on the top left corner's coordinate of it.
 * 
 * @author Demarcsek
 * @version 1.5
 **/
public class Common {
	/**
	 * A reference to the sprite base image object 
	**/
	private BufferedImage SpriteBase;
	
	/**
	 * Common single instance obj
	**/
	private static Common Instance;
	
	/**
	 * The width of each sprite.
	 * This value may change due to the SpriteBase.
	 */
	public static final int SPRITE_WIDTH = 34;
	
	/**
	 * The height of each sprite.
	 * This value may change due to the SpriteBase.
	 */
	public static final int SPRITE_HEIGHT = 27;
	
	/**
	 * Default constructor that loads the sprite base image that contains
	 * sprite graphics for WorldFieldView children
	 * @author Demarcsek
	 */
	protected Common() {
		try {
			this.SpriteBase = ImageIO.read(new File("res/sprites.png"));
		} catch(IIOException e) {
			this.SpriteBase = null;
			JOptionPane.showMessageDialog(null,
				    "Unable to load sprites image",
				    "I/O Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			this.SpriteBase = null;
			JOptionPane.showMessageDialog(null,
				    "Unable to load sprites image",
				    "I/O Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Retrieves a sprite image graphic 
	 * @author Demarcsek
	 * @param p Coordinates of the sprite in SpriteBase
	 * @return The graphical data as a BufferedImage object
	 */
	public BufferedImage getImageFromCoordinate(Point p) {
		if(this.SpriteBase == null)
			return null;
		
		return SpriteBase.getSubimage(p.x, p.y, Common.SPRITE_WIDTH, Common.SPRITE_HEIGHT);
	}
	
	/**
	 * Returns the single instance object
	 * @author Demarcsek
	 * @return The Common instance
	 */
	public static Common getInstance() {
		if(Common.Instance == null) 
			Common.Instance = new Common();
		
		return Common.Instance;
	}
	
}
