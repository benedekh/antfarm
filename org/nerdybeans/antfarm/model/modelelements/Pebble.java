package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.ArrayList;

import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.model.Replaceable;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.PebbleView;


/**
 * Pebble represents a static, but movable obstacle on the game map, so it
 * extends the Replaceable class.
 * 
 * @author Demarcsek, Horvath
 * @version 1.1
 **/
public class Pebble extends Replaceable implements Serializable {
	/**
	 * The graphical representation of a Pebble on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new PebbleView();
	
	/**
	 * Returns the graphical representation of a Pebble.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of a Pebble.
	 */
	@Override
	public WorldElementView getView() {
		return Pebble.View;
	}
	
	/**
	 * Default constructor
	 * @author Demarcsek
	 */
	public Pebble() {
		super();
	}

	/**
	 * The way we handle interactions. Once the AntEater is close, it may push the pebbles around.
	 * 
	 * @author Horvath
	 */
	@Override
	public void interact() {
		// find out where the AntEater is
		ArrayList<WorldField> neighbours = this.getField().getNeighbours();
		//ListIterator<WorldField> iterator = neighbours.listIterator();
		boolean found = false; // Have we found the AntEater?
		int index = 0; // direction of the AntEater
		
		for (index = 0; index < 6; ++index) {
			WorldField next = neighbours.get(index);
			
			if (next != null) {
				boolean passable = next.isPassable();
				
				// if the field is passable
				if (passable == true){
					AntEater ae = next.getAntEater();
					
					// there may be an anteater there
					if (ae != null){
						found = true;
						break;
					}
				}
			}
		}
		
		// if we could not find the anteater on the neigbouring fields.   
		if (found == false){
			return;
		}
		
		/** 
		 * set the direction of the pebble check
		 * @see GameWorld.GameWorld() -> neighbour indexing
		 */
		if (index <= 2){
			index += 3;
		} else {
			index -= 3;
		}
		

		// now we need to check forward in that direction
		WorldField nextField = neighbours.get(index);
		
		// if the field exists
		if (nextField != null){
			// check forward, looking for pebble or something on nextField
			boolean passable = nextField.isPassable();
			
			if (passable == true){
				Irreplaceable stationary = nextField.getPermanentElement();
				
				// nobody was there, so move.
				if (stationary == null){
					nextField.setDynamicElement(this);
				}
			} else {
				// if the field was reserved, check whether it is a pebble
				Replaceable pebble = nextField.getDynamicElement();
				
				// if there is a pebble
				if (pebble != null){
					// check the neighbour field in that direction
					ArrayList<WorldField> nextNeighbours = nextField.getNeighbours();
					WorldField nextNextField = nextNeighbours.get(index);
					
					// if the field exists next to nextField
					if (nextNextField != null){
						boolean nextPassable = nextNextField.isPassable();
						
						if (nextPassable == true){
							Irreplaceable nextStationary = nextNextField.getPermanentElement();
							
							// if no one lives there
							if (nextStationary == null){
								nextNextField.setDynamicElement(this);
							}	
						}
					}
				}	
			}
		}
	
	}

	/**
	 * Converts a Pebble object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "Pe";
	}
	
	/**
	 * What should the Pebble do when it the timer gives a tick?
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs arguments of the tick event
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		
	}

	/**
	 * Pebble should respond to each tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 1;
	}
}
