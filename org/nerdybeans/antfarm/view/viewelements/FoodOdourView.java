package org.nerdybeans.antfarm.view.viewelements;

import org.nerdybeans.antfarm.view.WorldElementView;
import java.awt.Point;

/**
 * This class stores the graphical representation of grass in the game.
 * 
 * @author Demarcsek
 * @version 1.1
 **/
public class FoodOdourView extends WorldElementView {
	/**
	 * The coordinates stored in a Point of the image of a bunch of grass.
	 */
	private static final Point SPRITE = new Point(34,54);
		
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public FoodOdourView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of a bunch of grass.
	 * 
	 * @author Demarcsek
	 * @return SPRITE the coordinates of the graphical representation of a bunch of grass.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}
}
