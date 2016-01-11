package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of an AntHill in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class AntHillView extends WorldElementView {
	/**
	 * The coordinates stored in a Point of the image of an AntHill.
	 */
	private static final Point SPRITE = new Point(0,27);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public AntHillView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of an AntHill.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of an AntHill.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}

}
