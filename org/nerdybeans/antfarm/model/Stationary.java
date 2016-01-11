package org.nerdybeans.antfarm.model;

import java.io.Serializable;

/**
 * Common parent of static [meaning not movable = irreplaceable, and movable at
 * some case = replaceable] WorldElement classes.
 * 
 * @author Demarcsek, Szabo
 * @version 1.0
 **/
public abstract class Stationary extends WorldElement implements Serializable {
	/**
	 * Default constructor
	 * @author Demarcsek
	 */
	public Stationary() {

	}

	/**
	 * This methods defines the behaviour of the stationary element. It desribes
	 * how the particular Stationary objects should interact with the
	 * neighbouring elements on the game map. The children classes must override
	 * it.
	 * 
	 * @author Szabo
	 */
	public abstract void interact();
}
