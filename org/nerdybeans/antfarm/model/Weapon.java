package org.nerdybeans.antfarm.model;

/**
 * Common interface for player weapons
 * 
 * Player may use so called weapons in the game. These weapons can be fired on
 * one particular field of the game world at one shot. They may modify certain
 * parameters of the target field or even the elements on it.
 * 
 * @author Demarcsek
 * @version 1.0
 **/
public interface Weapon {

	/**
	 * This method provides the behaviour of the weapon. It describes the
	 * actions that should be performed when the weapon is triggered at a
	 * user-given field.
	 * 
	 * @author Demarcsek
	 * @param Target The WorldField to fire at with the weapon
	 */
	public void triggerAt(WorldField Target);
}
