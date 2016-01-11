package org.nerdybeans.antfarm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * AntKiller represents a player weapon called ant killer in the game model.
 * 
 * AntKiller is capable of poisoning fields on the map. It extends the abstract
 * WeaponBase class and also implements the Weapon interface.
 * 
 * @author Horvath
 * @version 1.0
 **/
public class AntKiller extends WeaponBase implements Weapon, Serializable {
	
	/**
	 * Default load of the spray.
	 */
	private static final int DEFAULT_LOAD = 10;
	
	/**
	 * Level of max Poison.
	 */
	private static final int MAX_POISON = 6;
	
	/**
	 * Default constructor, sets the initial load amount.
	 * @author Horvath
	 */
	public AntKiller() {
		this.setLoad(DEFAULT_LOAD);
	}
	
	/**
	 * Parameterized constructor, sets the initial load amount.
	 * 
	 * @author Horvath
	 * @param initialLoad the initial load amount of the weapon.
	 */
	public AntKiller(int initialLoad) {
		this.setLoad(initialLoad);
	}

	/**
	 * Retrieves if the user can use the weapon or not.
	 * 
	 * @author Horvath
	 * @return true if the weapon can be used, false otherwise
	 */
	@Override
	public boolean isAvail() {
		int level = this.getLoad();

		if (level > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method provides the behaviour of the weapon. It describes the
	 * actions that should be performed when the weapon is triggered at a
	 * user-given field.
	 * 
	 * @author Horvath
	 * @param Target The WorldField to fire at with the weapon
	 */
	@Override
	public void triggerAt(WorldField Target) {
		if(Target == null) return;
		
		boolean avail = this.isAvail();
		if (avail == false) {
			return;
		}
		
		int deepness = 0; // current deepness level of recursion (deepness = [0,3])
		this.recursiveSetPoison(Target, deepness);
	
		// decrease the load of the AntKiller
		int newLoad = this.getLoad() - 1;
		this.setLoad(newLoad);

	}
	
	/**
	 * It is for replacing the iterative way of setting the poison value.
	 * 
	 * @author Horvath
	 * @param field on which field should the recursion be called
	 * @param deepness how far away are we from the starting field
	 */
	private void recursiveSetPoison(WorldField field, int deepness){
		if (deepness == 3)
			return;
		
		ListIterator<WorldField> neighboursIterator = field.getNeighbours().listIterator();
		
		while(neighboursIterator.hasNext()){
			WorldField nField = neighboursIterator.next();
			if (nField != null){
				deepness += 1;
				this.recursiveSetPoison(nField, deepness);
				deepness -= 1;
				nField.setPoison(MAX_POISON); // set the poison
			}
		}
		
	}
	
}
