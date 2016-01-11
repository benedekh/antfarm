package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of a Puddle in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class PuddleView extends WorldElementView {
	/**
	 * The coordinates stored in a Point of the image of a Puddle.
	 */
	private static final Point SPRITE = new Point(0,81);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public PuddleView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of a Puddle.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of a Puddle.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}

}
