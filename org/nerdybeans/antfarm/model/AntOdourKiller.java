package org.nerdybeans.antfarm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * AntOdourKiller represents a player weapon called ant killer in the game
 * model.
 * 
 * AntOdourKiller is capable of removing the odour left by ants on a fields on
 * the map. It extends the abstract WeaponBase class and also implements the
 * Weapon interface.
 * 
 * @author Horvath, Szabo
 * @version 1.0
 **/
public class AntOdourKiller extends WeaponBase implements Weapon, Serializable {
	/**
	 * How much shoots does the AntOdourKiller have by default?
	 */
	private static final int DEFAULT_LOAD = 15;
	
	/**
	 * Default constructor, sets the initial load amount.
	 * @author Horvath
	 */
	public AntOdourKiller() {
		this.setLoad(DEFAULT_LOAD);
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @author Horvath
	 * @param initialLoad the initial load amount for the weapon
	 */
	public AntOdourKiller(int initialLoad) {
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
	 * This method should be called, when the weapon is triggered, so the user
	 * fires on a target map field. In this case, this method will reduce the
	 * 'ant odour' of the target world field (WorldField).
	 * 
	 * @author Horvath
	 * @param Target which field's neighbours' antodour should be set due to the target. 
	 */
	@Override
	public void triggerAt(WorldField Target) {
		assert Target != null;
		
		boolean avail = this.isAvail();
		if (avail == false) {
			return;
		}
		
		int deepness = 0; // current deepness level of recursion (deepness = [0,2])
		this.recursiveSetAntOdour(Target, deepness);
		
		// decrease the load of the AntOdourKiller
		int newLoad = this.getLoad() - 1;
		this.setLoad(newLoad);
	}
	
	/**
	 * It is for replacing the iterative way of setting the antOdour value.
	 * @author Horvath
	 * @param field on which field should the recursion be called
	 * @param deepness how deep are we in the recursion = how far away are we from the algorithm's starting field.
	 */
	private void recursiveSetAntOdour(WorldField field, int deepness){
		if (deepness == 2)
			return;
		
		ListIterator<WorldField> neighboursIterator = field.getNeighbours().listIterator();
		
		while(neighboursIterator.hasNext()){
			WorldField nField = neighboursIterator.next();
			
			if (nField != null){
				deepness += 1;
				this.recursiveSetAntOdour(nField, deepness);
				deepness -= 1;
				nField.setAntOdour(0); // set the ant odour
			}
		}
		
	}

}
