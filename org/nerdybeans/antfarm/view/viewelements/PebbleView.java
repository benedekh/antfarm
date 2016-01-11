package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of a Pebble in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class PebbleView extends WorldElementView{
	/**
	 * The coordinates stored in a Point of the image of a Pebble.
	 */
	private static final Point SPRITE = new Point(34,81);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public PebbleView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of a Pebble.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of a Pebble.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}

}
