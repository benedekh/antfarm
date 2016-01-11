package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.GameWorld;
import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.AntHillView;

/**
 * Represents an ant hill in the game model
 * 
 * AntHill is the spawn point of the ants, they come from the AntHill and move
 * towards warehouses.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.2
 **/
public class AntHill extends Irreplaceable implements Serializable {
	/**
	 * The graphical representation of an AntHill on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new AntHillView();
	
	/**
	 * How many Ants have been created by the AntHill?
	 */
	private static int ants_created = 0;
	
	/**
	 * The maximal number of Ants can be created by the AntHill.
	 */
	public static final int MAX_ANTS = 1200;
	
	/**
	 * Resets the number of Ants have been created by the AntHill.
	 */
	public static void reset() {
		ants_created = 0;
	}
	
	/**
	 * Returns the graphical representation of an AntHill.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of an AntHill.
	 */
	@Override
	public WorldElementView getView() {
		return AntHill.View;
	}
	
	/**
	 * The maximal number of tries for placing the ant on a neighbouring field.
	 */
	private static final int MAX_TRIES = 42;
	
	/**
	 * Default constructor
	 * @author Szabo
	 */
	public AntHill() {
		super();
	}

	/**
	 * Creates a new Ant object and returns it.
	 * 
	 * @author Horvath
	 * @return The brand new Ant object
	 */
	private Ant createAnt() {
		Ant tmp = new Ant();

		return tmp;
	}

	
	
	/**
	 * Conducts proper interactions with the neighboring elements on the map.
	 * 
	 * In this case, the proper interaction means creating a new Ant object by
	 * calling createAnt and placing it onto a neighbouring field. That
	 * particular field is selected in a random manner.
	 * 
	 * The crucial part of this method is thread-safe, due to possible multiple access. 
	 * (by the View and the Model as well)
	 * 
	 * @author Horvath
	 */
	@Override
	public void interact() {
		Random randomGenerator = new Random(); // randomGenerator to decide the direction
		/*if(randomGenerator.nextInt(7) % 2 != 0)
			return;*/
		
		if(ants_created < MAX_ANTS) {		
			Ant ant = this.createAnt();
	
			ArrayList<WorldField> neighbours = this.getField().getNeighbours();
			Set<Integer> triedNeighbours = new HashSet<Integer>(); // set of the tried neigbour indexes.
			int triedSize = triedNeighbours.size();
			int numberOfTries = 0;
	
			
			boolean successPlace = false; // check whether the placement was successful
	
			while ((triedSize < 6) && (numberOfTries != MAX_TRIES) && (successPlace == false)) {
				int i = randomGenerator.nextInt(6); // generate a random index: [0,6)
	
				WorldField nextField = neighbours.get(i);
				triedNeighbours.add(i); // add to the tried neighbour indexes
				++numberOfTries; // for security reasons, not to get into an endless loop
				
				// if the nextField exists and passable
				if (nextField != null) {
	
					boolean passable = nextField.isPassable();
	
					if (passable == true) {
						// ant cannot move back to the anthill, so its cameFrom
						// field = nextField also.
						// ant.setField(nextField); // already in WorldField.addAnt
						synchronized(nextField.getAntList()) {
							nextField.addAnt(ant);
						}
						// end the loop
						successPlace = true;
						ants_created++;
					} else {
						ant = null;
					}
					
				}
			}
		}
	}

	/**
	 * Converts an AntHill object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "H";
	}

	/**
	 * AntHill should create a new ant at tick.
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		//System.out.println("[DEBUG#AntHill.onTick] # = " + this.ants_created );
		this.interact();
	}
	
	/**
	 * AntHill should respond to each tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 10;
	}
	
}
