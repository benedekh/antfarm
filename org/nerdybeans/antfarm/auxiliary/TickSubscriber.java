/**
 * 
 */
package org.nerdybeans.antfarm.auxiliary;

import java.util.Observer;

/** 
 * Common interface for all subscribers to the Timer
 * @author Demarcsek, Szabo
**/
public interface TickSubscriber extends Observer {
	
	/**
	 * Final event handler (to be overloaded in the actual implementations) 
	 * @param Object TickEventArgs Tick event message passed by the Observable instance
	**/
	public void onTick(Object TickEventArgs);
	
	/**
	 * Returns tick interval length for the particular subscriber.
	 * Please note, that interval lengths (or period time) can be
	 * only specified in a per-type (per-class) basis!
	**/
	public int getPeriod();
}
