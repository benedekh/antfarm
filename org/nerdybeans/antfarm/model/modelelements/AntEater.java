package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.model.Movable;
import org.nerdybeans.antfarm.model.Replaceable;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.AntEaterView;

/**
 * Represents an ant eater in the game model.
 * 
 * @author Demarcsek, Horvath
 * @version 1.5
 **/
public class AntEater extends Movable implements Serializable {
	/**
	 * The graphical representation of an AntEater on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new AntEaterView();
	
	/**
	 * Returns the graphical representation of an AntEater.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of an AntEater.
	 */
	@Override
	public WorldElementView getView() {
		return AntEater.View;
	}
	
	/**
	 * How many ants have been consumed by the AntEater? The higher the value
	 * is, the less hungry the AntEater is.
	 **/
	private volatile int consumed;

	/**
	 * The maximal number of ants that can be consumed.
	 */
	private static final int MAX_CONSUMED = 4;

	/**
	 * The maximal number of tries, the AntEater can make when it decides where
	 * to move forward.
	 */
	private static final int MAX_TRIES = 7;
	
	/**
	 * Default constructor.
	 * @author Horvath
	 */
	public AntEater() {
		super();
		this.consumed = 0;
	}

	/**
	 * Consumes the given Target ant, if the AntEater is hungry.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @param Target the ant that should be consumed.
	 * @see AntLion.interact().
	 */
	public synchronized void eatAnt(Ant Target) {
		boolean hungry = this.isHungry();
		if (hungry == false) {
			return;
		}

		this.getField().removeAnt(Target);
		Timer.getInstance().removeSubscriber(Target);
		//Timer.getInstance().removeSubscriber(Target);

		this.consumed += 1;
	}

	/**
	 * Tells whether the AntEater is hungry or not.
	 * 
	 * @author Horvath
	 * @return true if hungry, false otherwise
	 */
	public boolean isHungry() {
		if (this.consumed < MAX_CONSUMED) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the number of ants eaten by this particular anteater
	 * 
	 * @author Horvath
	 * @return number of consumed ants
	 */
	public int getConsumed() {
		return this.consumed;
	}

	/**
	 * This method defines the moving algorithm of the movable object, so it
	 * decides the very next position of the object in question.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @return The next position of the object on the game map
	 */
	@Override
	public synchronized WorldField getNextMove() {
		Random randomGenerator = new Random(); // for deciding the direction
		boolean successMove = false; // check whether the movement was successful

		ArrayList<WorldField> neighbours = this.getField().getNeighbours();
		Set<Integer> triedNeighbours = new HashSet<Integer>(); // set of the tried neigbour indexes.
		int triedSize = triedNeighbours.size();
		int numberOfTries = 0;

		WorldField previousField = this.cameFrom;
		this.nextField = null;

		while ((triedSize < 6) && (numberOfTries != MAX_TRIES)
				&& (successMove == false)) {
			int i = randomGenerator.nextInt(6); // generate a random index:
												// [0,6)
			if (this.ForcedDirection >= 0)
				i = this.ForcedDirection;

			nextField = neighbours.get(i);
			triedNeighbours.add(i); // add to the tried neighbour indexes
			++numberOfTries; // for security reasons, not to get into an endless loop

			if ((nextField != null) && (nextField != previousField)) {
				boolean passable = nextField.isPassable();
				Irreplaceable stationary = nextField.getPermanentElement();

				// passable and there is no (warehouse or antlion) there
				if ((passable == true) && (stationary == null)) {
					successMove = true;
				} else {
					Replaceable pebble = nextField.getDynamicElement();

					if (pebble != null) {
						pebble.interact();

						// check whether the pebble could move forward
						pebble = nextField.getDynamicElement();
						if (pebble == null) {
							successMove = true;
						}
					}
				}
			}
		}

		// could not move forward, so move back.
		if ((triedSize == 6) && (successMove == false)) {
			nextField = this.cameFrom; // AntEater moves back.
		} else if (numberOfTries == MAX_TRIES) {
			nextField = this.getField(); // if an AntEater was created surrounded by a lots of unmovable elements, or a loads of pebbles.
		}

		return nextField;
	}

	/**
	 * Calls getNextMove() and conducts the actual move by deleting the object
	 * from its current position and adding it to the next one (returned by
	 * getNextMove()). It is also responsible for keeping references consistent
	 * between the concerned WorldField and WorldElement objects.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek, Horvath
	 */
	@Override
	public synchronized void makeNextMove() {
		// decide whether it is hungry
		if (this.isHungry() == false) {
			AntEaterSpawnPoint.decreaseAe();
			this.getField().removeAntEater(); // if not, remove itself
			Timer.getInstance().removeSubscriber(this);
			//this.setField(null);
			//Timer.getInstance().removeSubscriber(this);
			return;
		}

		// eat ants, it works just in this way: Iterator.remove();
		// @see Java docs about Iterator & remove.
		
		synchronized(this.getField().getAntList()) {
			/*Iterator<Ant> iter = this.getField().getAntList().iterator();
			while ((iter.hasNext()) && (this.isHungry())) {
				Ant CurrEnt = iter.next();
				//System.out.println("[DEBUG#AntEater.makeNextMove] Ant detected, killing him softly...");
				this.eatAnt(CurrEnt);
			}*/
			this.consumed += this.getField().getAntList().size();
			this.getField().getAntList().clear();
		}

		// move to next field and remove itself from the field it was on
		// if(this.ForcedDirection >= 0) {
		// System.out.println("[DEBUG#AntEater.makeNextMove] Moving to forced direction : "
		// + ForcedDirection);
		// this.nextField =
		// this.getField().getNeighbours().get(this.ForcedDirection);
		// } else
		this.nextField = this.getNextMove();

		if ((nextField != this.getField()) && (nextField != null)) {
			nextField.addAntEater(this);
			WorldField previousField = this.getCameFrom();
			previousField.removeAntEater();
		}

	}

	/**
	 * Sets the number of consumed ants (used in GameSerializer.unserialize)
	 * 
	 * @author Demarcsek
	 * @param newConsumed the desired number of consumed ants.
	 * @see GameSerializer.unserialize
	 */
	public void setConsumed(int newConsumed) {
		this.consumed = newConsumed;
	}

	/**
	 * Converts an AntEater object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "Ae";
	}
	
	/**
	 * Responding to the Tick event, which here means moving around on the Map.
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		this.makeNextMove();
	}
	
	/**
	 * Respond to every 2nd Tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 4;
	}

}
