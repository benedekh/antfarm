package org.nerdybeans.antfarm.view.viewelements;

import java.awt.Point;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class stores the graphical representation of a Warehouse in the game.
 * 
 * @author David, Horvath
 * @version 1.1
 **/
public class WarehouseView extends WorldElementView {
	/**
	 * The coordinates stored in a Point of the image of a Warehouse.
	 */
	private static final Point SPRITE = new Point(0,54);
	
	/**
	 * Default constructor.
	 * @author David
	 */
	public WarehouseView(){
		
	}
	
	/**
	 * Returns the coordinates for the graphical representation of a Warehouse.
	 * 
	 * @author Horvath
	 * @return SPRITE the coordinates of the graphical representation of a Warehouse.
	 */
	@Override
	protected Point getSpriteCords() {
		return SPRITE;
	}

}
