package org.nerdybeans.antfarm.auxiliary;


import java.util.Observable;

/**
 * An abstract ancestor class of different timer variants.
 * Perhaps, different timer implementations will be tested before finding the right one.
 * For example, the GUI version may require a much different timer than the prototype version.
 * The class also serves as a common access interface for other classes that need to invoke timer 
 * services.
 * @author Demarcsek, Szabo
**/
public abstract class Timer extends Observable implements Runnable {
	public Object Lock = new Object();
	
	/**
	 * True if the timer is still working, false otherwise
	**/
	protected volatile boolean scheduling;
	
	/**
	 * The Timer that is used in the current implementation.
	 **/
	private volatile static Timer InstanceObj;
	
	protected volatile boolean subscribe = true;
	
	protected Timer() {
		
	}
	
	/**
	 * Fires the tick event 
	**/
	public abstract void tick();
	
	/**
	 * @return Value of tick counter
	**/
	public abstract int getTick();
	
	
	/**
	 * Adds a subscriber.
	 * Tick periods can be specified for subscribers in a per-type basis using
	 * TickSubscriber.getPeriod()
	 * @param Subscriber
	 * @see TickSubscriber.getPeriod
	**/
	public abstract void addSubscriber(TickSubscriber Subscriber);
	
	/**
	 * Removes a subscriber. Once a subscriber is removed, it should not be sent
	 * tick events anymore
	 * @param Subscriber The subscriber to delete
	**/
	public abstract void removeSubscriber(TickSubscriber Subscriber);
	
	/**
	 * Stops timing (scheduling) 
	**/
	public abstract void kill();
	
	/**
	 * Disables scheduling AND subscription
	**/
	public void disable() {
		subscribe = false;
		this.scheduling = false;
	}
	
	/**
	 * Enables scheduling AND subscription
	**/
	public void enable() {
		subscribe = true;
		this.scheduling = true;
	}
	
	/**
	 * Resets the timer to its initial state
	 **/
	public abstract void reset();
	
	/**
	 * Setting the Timer of the certain implementation.
	 * @param Implementation The Timer to be used.
	 **/
	public static void setInstance(Timer Implementation) {
		assert Implementation != null;
		InstanceObj = Implementation;
	}
	
	/**
	 * Retrieving the Timer that is used.
	 **/
	public static Timer getInstance() {
		return InstanceObj;
	}
}