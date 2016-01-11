package org.nerdybeans.antfarm.view;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * 
 * The common base of the graphical representation for each WorldElement.
 * 
 * @author Demarcsek, Horvath
 * @version 1.0
 **/
public abstract class WorldElementView {
	/**
	 * It returns the coordination of the sprite of the given WorldElement, in the SpriteBase of Common.
	 *
	 * @author Demarcsek
	 * @return Point the coordination of the sprite of the given WorldElement.
	 * @see View.Common.SpriteBase
	 */
	protected abstract Point getSpriteCords();
	
	/**
	 * Returns the graphical representation of the given WorldElement. 
	 * It is an image that was cut from the SpriteBase in Common.
	 * 
	 * @author Horvath
	 * @return BufferedImage that is the graphical representation of the given WorldElement.
	 * @see View.Common
	 */
	public BufferedImage getSprite() {
		return Common.getInstance().getImageFromCoordinate(this.getSpriteCords());
	}
}
