package org.nerdybeans.antfarm.model;

import java.io.Serializable;

/**
 * An abstract class that distinguishes all the Stationary Objects that cannot
 * be pushed around on the Map.
 * 
 * @author Szabo
 * @version 1.0
 **/
public abstract class Irreplaceable extends Stationary implements Serializable {
	/**
	 * The Irreplaceable object should act for the ticks. Concrete action
	 * depends on the class of the given object, which interact is called.
	 * 
	 * @author Szabo
	 * @param tickEventArgs the arguments of the tick event
	 */
	public void onTick(Object tickEventArgs) {
		this.interact();
	}

}
