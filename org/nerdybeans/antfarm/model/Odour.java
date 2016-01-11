package org.nerdybeans.antfarm.model;

/**
 * This class represents the odour as a WorldElement on certain WorldFields.
 * 
 * @author Demarcsek, Szabo
 * @version 1.0
 **/
public abstract class Odour extends WorldElement {
	/**
	 * Intensity of the Odour.
	 */
	protected float intensity;
	
	/**
	 * Auxiliary ID for spreading odour.
	 */
	protected int auxID;
	
	/**
	 * Period for the timer.
	 */
	protected static final int period = 1; 
	
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public Odour() {
		super();
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @author Demarcsek
	 * @param Parent which WorldField holds the odour.
	 */
	public Odour(WorldField Parent) {
		super(Parent);
	}
	
	/**
	 * Gets the Period of the response.
	 * 
	 * @author Szabo
	 * @return period the period value of the odour, which shows how often shall it respond to tick.
	 */
	public int getPeriod() {
		return period;
	}
	
	
	/**
	 * Sets the Intensity for the Odour.
	 * 
	 * @author Szabo
	 * @param value The chosen Intensity.
	 */
	public void setIntensity(float value) {
		/// DEBUG
		//if(value != 0)
		//	System.out.println(Thread.currentThread().getStackTrace());
		
		this.intensity = value;
	}
	
	/**
	 * Gets the Intensity of the Odour.
	 * 
	 * @author Szabo
	 * @return intensity the intensity value of the odour.
	 */
	public float getIntensity() {
		return this.intensity;
	}
	
	/**
	 * What should the odour do, when a tick event arises?
	 * @author Szabo
	 * @param TickEventArgs arguments for the tick event.
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		
	}
	
}
