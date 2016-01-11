package org.nerdybeans.antfarm.auxiliary;

import java.io.Serializable;


/**
 * This class basically implements a Timer based on java.util.Observable.
 * For further information, read the method summaries.
 * @author Demarcsek, Szabo
**/
public class TimerObservable extends Timer {	
	private boolean killed = false;
	
	
	/**
	 * Maximal length of a timer cycle (in ticks)
	 **/
	private int section_length;
	/**
	 * The current tick's ordinal number (phase)
	 **/
	private int current_tick;
	
	/**
	 * Resolution in ms.
	 * 
	 * Default is 100 ms (0.1 sec)
	 **/
	private int precision;
	
	/**
	 * Set resolution
	 * @param pre
	 **/
	public void setPrecision( int pre ) {
		this.precision = pre;
	}
	
	public int getPrecision() {
		return this.precision;
	}
	
	/**
	 * Constructor
	 * @param len_sec section legth parameter
	 **/
	public TimerObservable( int len_sec ) {
		this.scheduling = true;
		this.section_length = len_sec;
		this.precision = 75; // msec
		this.current_tick = 1;
	}
	
	public TimerObservable( int len_sec, int prec ) {
		this(len_sec);
		this.setPrecision(prec);
	}
	
	/**
	 * Default constructor. Default section length is 16.
	 **/
	private TimerObservable() {
		this(16);
	}
	

	/**
	 * Method for calling the "tick", whick basically notifies all the subscribed Observers that they should carry out 
	 * what is implemented in their onTick method.
	 **/
	public void tick() {
		//System.out.println("[Timer] TimerObservable.tick() current_tick = " + this.current_tick + " notifying " + this.countObservers() + " event handlers...");;
		setChanged();
		
		notifyObservers(new Integer(this.getTick()));
		if( this.current_tick == this.section_length ) {
			// Turning
			this.current_tick = 0;
			
			//System.out.println("[DEBUG#TimerObservable.tick] # = " + this.countObservers());
		} else {
			// Advancing (increment)
			this.current_tick++;
		}
	}
	
	/**
	 * Returns the current tick value (counter value).
	 **/
	public int getTick() {
		return this.current_tick;
	}
	
	/**
	 * Running the Timer while it is scheduled to operate.
	 **/
	@Override
	public void run() {
		while(true) {
			try {
				if(scheduling) this.tick();
				if(this.killed) break;
				disable();
				synchronized(this.Lock) {
					this.Lock.notifyAll();
				}
				Thread.sleep(this.precision);
			} catch( InterruptedException e ) {
				System.out.println("[DEBUG#TimerObservable] Timer interrupted.");
			}
		}
	}
	
	/**
	 * Adding a Subscriber.
	 * @param Observer The observer to be added.
	 **/
	public void addSubscriber(TickObserver Observer) {		
		if(this.subscribe)
			addObserver(Observer);
	}
	
	/**
	 * Removing a Subscriber.
	 * @param Subscriber The observer to be removed.
	 **/
	public void removeSubscriber(TickObserver Subscriber) {
			deleteObserver(Subscriber);
	}
	
	/**
	 * Stopping the Timer.
	 **/
	public void kill() {
		this.scheduling = false;
		this.killed = true;
	}
	
	/**
	 * Check if Timer is in operation (scheduling)
	 **/
	public boolean isActive() {
		return this.scheduling;
	}

	
	@Override
	public void addSubscriber(TickSubscriber Subscriber) {
		// System.out.println("TimerObservable.addSubscriber()");
		if(this.subscribe)
			addObserver(Subscriber);
	}

	@Override
	public void removeSubscriber(TickSubscriber Subscriber) {
		deleteObserver(Subscriber);
	}

	@Override
	public void reset() {
		this.deleteObservers();
		this.current_tick = 1;
	}
	
}
