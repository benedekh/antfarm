package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of an Ant in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class AntView extends WorldElementView {	
	/**
	 * The coordinates stored in a Point of the image of an Ant.
	 */
	private static final Point SPRITE = new Point(0,0);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public AntView(){
		
	}

	/**
	 * Returns the coordinates for the graphical representation of an Ant.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of an Ant.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}
	

}
