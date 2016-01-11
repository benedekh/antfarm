package org.nerdybeans.antfarm.model;

import java.io.Serializable;

/**
 * Common parent of weapon classes
 * 
 * A class that represents a particular player weapon should extend the abstract
 * WeaponBase class and also implement the Weapon interface that provides access
 * to the firing behaviour of the weapon. The WeaponBase class stores common
 * information used to describe weapons and also implements some common routines
 * concerning them.
 * 
 * @author Demarcsek
 * @version 1.2
 **/
public abstract class WeaponBase implements Serializable {
	/**
	 * Load level of the weapon
	 */
	private volatile int load;

	/**
	 * Default constructor
	 * @author Demarcsek
	 */
	public WeaponBase() {
		this.load = 0;
	}

	/**
	 * Tells if the weapon is available (meaning not empty) Note, that a weapon
	 * should not be triggered, if it is not available, so the trigger routine
	 * (triggerAt) does not have to guarantee load-checking.
	 * 
	 * @author Demarcsek
	 */
	public abstract boolean isAvail();

	/**
	 * Sets the load level of the weapon
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @param newLoad The desired value of the load level in the weapon.
	 */
	public synchronized void setLoad(int newLoad) {
		assert (newLoad >= 0);
		//System.out.println("setLoad("+newLoad+") call from" + Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName() );
		this.load = newLoad;
	}

	/**
	 * Retrieves the load level of the weapon
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @return load How many shots does the Weapon have yet?
	 */
	public synchronized int getLoad() {
		return this.load;
	}
}
