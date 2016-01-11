package org.nerdybeans.antfarm.auxiliary;
import java.util.Observable;
import java.util.Observer;


/** 
 * Abstract class that implements the TickSubscriber interface.
 * So it is a partial implementation of a TickSubscriber 'object'
 * that is structured with respect to the Observer pattern.
 * @author Demarcsek, Szabo
**/
public abstract class TickObserver implements Observer, TickSubscriber {

	/**
	 * Receiving the Tick event. This methods implements the
	 * standard event handler 'update' in the Observer pattern
	 * implemented in java.util.Observer. 
	 * In this method the task of event handling is passed toward
	 * the onTick() abstract method of the object. Please note, that
	 * this implementation requires msg to be an Integer that
	 * is the value of the current tick counter of the timer. This is,
	 * because update() does the interval checks so that each subscriber
	 * gets only those ticks that they were subscribed to (defined by getPeriod())
	 * @param observable The Timer which publishes the event
	 * @param msg An event message passed by the Timer (TickObservable)
	 * @see java.util.Observer, TickObservable, TickSubscriber
	 **/
	@Override
	public void update(Observable observable, Object msg) {
		// Filter 'illegal' calls
		//System.out.println("TickObserver.update");
		if(msg == null)
			return;
		
		
		if ( Timer.getInstance().equals(observable) ) {
			Integer Message = Integer.MAX_VALUE;
			try {
				Message = (Integer)msg;
			} catch(ClassCastException TypeError) {
				System.err.println("TickObserver: Invalid message");
			}
			
			if((Message % this.getPeriod()) == 0)
				this.onTick(Message); //calling the response.
		}
	}
	
	
	
	public abstract void onTick(Object TickEventArgs);
	
	
	public abstract int getPeriod();
}
