package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of an AntLion in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class AntLionView extends WorldElementView {	
	/**
	 * The coordinates stored in a Point of the image of an AntLion.
	 */
	private static final Point SPRITE = new Point(34,27);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public AntLionView(){
		
	}

	/**
	 * Returns the coordinates for the graphical representation of an AntLion.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of an AntLion.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}

}
