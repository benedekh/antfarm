package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of an AntEater in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class AntEaterView extends WorldElementView {
	/**
	 * The coordinates stored in a Point of the image of an AntEater.
	 */
	private static final Point SPRITE = new Point(34,0);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public AntEaterView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of an AntEater.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of an AntEater.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}
	

}
