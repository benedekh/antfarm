package org.nerdybeans.antfarm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.nerdybeans.antfarm.model.modelelements.Ant;
import org.nerdybeans.antfarm.model.modelelements.AntEater;
import org.nerdybeans.antfarm.model.modelelements.AntOdour;
import org.nerdybeans.antfarm.model.modelelements.FoodOdour;
import org.nerdybeans.antfarm.model.modelelements.Poison;

/**
 * Represents a field (unit) of the map.
 * 
 * @author Horvath, Szabo
 * @version 1.5
**/
public class WorldField implements Serializable {
	
	/**
	 * Constants for directions (neighbour list index)
	 * @see paper documentation neighbour indexing at hexagonal WorldFields
	**/
	public static final int NEIGHBOUR_UP_LEFT = 0;
	public static final int NEIGHBOUR_UP_RIGHT = 1;
	public static final int NEIGHBOUR_RIGHT = 2;
	public static final int NEIGHBOUR_DOWN_RIGHT = 3;
	public static final int NEIGHBOUR_DOWN_LEFT = 4;
	public static final int NEIGHBOUR_LEFT = 5;
	
	/**
	 * This variable should be used for comparing the Field's foodOdour to the
	 * maximal food odour, and it is used by the ant to decide it is time to
	 * eat.
	 */
	public static final int MAX_FOOD_ODOUR = 20;

	/**.
	 * Stores the food intensity of the field.
	 */
	private volatile FoodOdour FoodOdour;

	/**
	 * Stores the antodour intensity of the field.
	 */
	private volatile AntOdour AntOdour;

	/**
	 * Stores the poison intensity of the field.
	 */
	private volatile Poison Poison;
	
	/**
	 * Shows that the field is passable or not. (true if yes, false if not)
	 */
	private volatile boolean Passable;

	/**
	 * List of the neighbour fields, helps to navigate
	 */
	private ArrayList<WorldField> Neighbours;

	/**
	 * Stores the movable Pebble element.
	 */
	private Replaceable DynamicElement;

	/**
	 * Stores the absolutely Stationary elements that cannot be moved.
	 */
	private volatile Irreplaceable PermanentElement;

	/**
	 * Stores the ants which are on the field.
	 */
	private volatile List<Ant> AntElements;

	/**
	 * Stores the anteater if it is on this field.
	 */
	private volatile AntEater AntEaterObj;

	/**
	 * Default constructor, sets the default values of the attributes.
	 * 
	 * @author Horvath, Demarcsek
	 */
	public WorldField() {
		this.FoodOdour = new FoodOdour(this);
		this.AntOdour = new AntOdour(this);
		this.Poison = new Poison(this);
		this.FoodOdour.setIntensity(0);
		this.AntOdour.setIntensity(0);
		this.Poison.setIntensity(0);
		this.Passable = true;
		this.Neighbours = new ArrayList<WorldField>();
		this.DynamicElement = null;
		this.PermanentElement = null;
		this.AntElements = java.util.Collections.synchronizedList(new ArrayList<Ant>());
		this.AntEaterObj = null;
	}

	/**
	 * Adds the ant to the AntElements list, which has moved to this field.
	 * 
     * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @param newAnt that should be stored on the WorldField
	 */
	public synchronized void addAnt(Ant newAnt) {
		//System.out.println("[DEBUG#WorldField.addAnt]");
		if (newAnt != null) {
			WorldField previousField = newAnt.getField();
			newAnt.setCameFrom(previousField);
			newAnt.setField(this);
			synchronized(this.AntElements) {
				this.AntElements.add(newAnt);
			}
			//this.setAntOdour(this.getAntOdour() + 1);
		}
		
		//System.out.println("[DEBUG#WorldField.addAnt] Ant added to field: " + newAnt.getField());
	}
	

	/**
	 * Adds the anteater to the StatElement, which has moved to this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 *
	 * @author Horvath
	 * @param newAntEater store the AntEater on this Field.
	 */
	public synchronized void addAntEater(AntEater newAntEater) {
		
		if (newAntEater != null) {
			WorldField previousField = newAntEater.getField();
			this.AntEaterObj = newAntEater;
			this.AntEaterObj.setCameFrom(previousField);
			this.AntEaterObj.setField(this);
		}
	}

	/**
	 * Sets the neighbours of the field during map generating
	 * 
	 * @author Horvath
	 * @param newNeighbour add a neighbour to the neighbours of this WorldField.
	 * @see GameWorld.initNew()
	 */
	public void addNeighbour(WorldField newNeighbour) {
		if (this.Neighbours == null) {
			this.Neighbours = new ArrayList<WorldField>();
		}

		this.Neighbours.add(newNeighbour);
	}

	/**
	 * Returns the value of the FoodOdour attribute.
	 * 
	 * @author Szabo
	 * @return FoodOdour.intensity how intense is the foodOdour on this field?
	 */
	public float getFoodOdour() {
		return this.FoodOdour.getIntensity();
	}
	
	/**
	 * Returns the FoodOdour object of this field.
	 * 
	 * @author Szabo
	 * @return FoodOdour this object stores the foodOdour intensity of this field.
	 */
	public FoodOdour getFoodOdourObj() {
		return this.FoodOdour;
	}

	/**
	 * Returns the value of the AntOdour attribute.
	 * 
	 * @author Szabo
	 * @return AntOdour.intensity how intense is the antOdour on this field?
	 */
	public float getAntOdour() {
		return this.AntOdour.getIntensity();
	}
	
	/**
	 * Returns the AntOdour object of this field.
	 * 
	 * @author Szabo
	 * @return AntOdour this object stores the antOdour intensity of this field.
	 */
	public AntOdour getAntOdourObj() {
		return this.AntOdour;
	}

	/**
	 * Returns the value of the Poison attribute.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Szabo
	 * @return Poison.intensity how intense is the poison on this field?
	 */
	public synchronized float getPoison() {
		return this.Poison.getIntensity();
	}
	
	/**
	 * Returns the Poison object of this field.
	 * 
	 * @author Szabo
	 * @return Poison this object stores the poison intensity of this field.
	 */
	public Poison getPosionObj() {
		return this.Poison;
	}

	/**
	 * Returns the AntEater object of this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @return AntEaterObj the AntEater object of this field.
	 */
	public synchronized AntEater getAntEater() {
		return this.AntEaterObj;
	}

	/**
	 * Returns the Replaceable object of this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @return DynamicElement a reference for the DyanmicElement of this field
	 */
	public synchronized Replaceable getDynamicElement() {
		return this.DynamicElement;
	}

	/**
	 * Returns an iterator to the ants which are on the fields.
	 * 
	 * @author Horvath
	 * @return ListIterator<Ant> a ListIterator for the Ants on this field.
	 */
	public ListIterator<Ant> getAntIterator() {
		return this.AntElements.listIterator();
	}

	/**
	 * Returns an ArrayList of the neighbour fields.
	 * 
	 * @author Horvath
	 * @return Neighbours the list of the neighbour fields of this field.
	 */
	public ArrayList<WorldField> getNeighbours() {
		return this.Neighbours;
	}

	/**
	 * Returns the Irreplaceable object of this field.
	 * 
	 * @author Horvath
	 * @return PermanentElement a reference for the PermanentElement of this field
	 */
	public Irreplaceable getPermanentElement() {
		return this.PermanentElement;
	}

	/**
	 * Returns that actually does it have a movable element on the field or not.
	 * 
	 * @author Horvath
	 * @return hasMove 
	 * 	true = the field has a WorldElement that can makeNextMove; 
	 * 	false = the field doesn't contain any Movable object.
	 */
	public boolean hasMovable() {
		boolean hasMove = (AntElements != null) || (AntEaterObj != null);

		return hasMove;
	}

	/**
	 * Returns a logical value, whether it is a passable field or not.
	 * 
	 * @author Horvath
	 * @return Passable true = this field is passable ; false = this field is not passable
	 */
	public boolean isPassable() {
		return this.Passable;
	}

	/**
	 * Sets the value of the AntOdour attribute of the field.
	 * 
	 * @author Szabo
	 * @param newOdour the new AntOdour intensity of this field.
	 */
	public void setAntOdour(float newOdour) {
		this.AntOdour.setIntensity(newOdour);
	}

	/** 
	 * Sets the DynamicElement of this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 *
	 * @author Horvath
	 * @param element the new DynamicElement that should be stored on this field. If it is null, then it should remove the old one from the field.
	 */
	public synchronized void setDynamicElement(Replaceable element) {
		if (element != null) {
			// add to this field and remove from the previous one
			WorldField previousField = element.getField();
			
			this.DynamicElement = element;
			this.DynamicElement.setField(this);
			this.setPassable(false);
			
			if (previousField != null) {
				previousField.setDynamicElement(null);
			}
			
		} else {
			this.DynamicElement = null;
			this.setPassable(true);
		}
	}

	/**
	 * Sets the PermanentElement of this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @param element the new PermanentElement that should be stored on this field. If it is null, then it should remove the old one from the field.
	 */
	public synchronized void setPermanentElement(Irreplaceable element) {
		if (element != null) {
			this.PermanentElement = element;
			this.PermanentElement.setField(this);
		} else {
			this.PermanentElement = null;
			this.setPassable(true);
		}
	}

	/**
	 * Sets the value of the FoodOdour attribute of the field.
	 * 
	 * @author Szabo
	 * @param newOdour the new FoodOdour intensity of this field.
	 */
	public void setFoodOdour(float newOdour) {
		this.FoodOdour.setIntensity(newOdour);
	}

	/**
	 * Sets the value of the Poison attribute of the field.
	 * 
	 * @author Szabo
	 * @param newOdour the new Poison intensity of this field.
	 */
	public void setPoison(float newOdour) {
		this.Poison.setIntensity(newOdour);
	}

	/**
	 * Sets the passability of the field.
	 * 
	 * @author Horvath
	 * @param Passable set the Passable attribute for this value. 
	 */
	public void setPassable(boolean Passable) {
		this.Passable = Passable;
	}

	/**
	 * Removes the ant from the list, which has left this field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @param existingAnt the Ant that should be removed from the field.
	 * @deprecated
	 */
	public synchronized void removeAnt(Ant existingAnt) {
		//System.out.println("[DEBUG#WorldField.removeAnt] Removing ant " + existingAnt.hashCode());
		this.AntElements.remove(existingAnt);
	}

	/**
	 * Removes the AntEater from the field.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 */
	public synchronized void removeAntEater() {
		this.AntEaterObj = null;
	}

	/**
	 * Converts a WorldField object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "x";
	}
	
	/**
	 * Returns a List of containing ants on the given WorldField.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @return this.AntElements an ArrayList containing the Ants of the field.
	 */
	public synchronized List<Ant> getAntList() {
		return this.AntElements;
	}

}
