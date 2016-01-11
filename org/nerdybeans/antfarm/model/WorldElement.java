package org.nerdybeans.antfarm.model;

import java.io.Serializable;

import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * Represents the element of the map. It stores and tells to other classes that
 * they are exactly where, on which field (WorldField) they are staying.
 * 
 * @author Demarcsek, Horvath
 * @version 1.2
 **/
public abstract class WorldElement extends org.nerdybeans.antfarm.auxiliary.TickObserver implements Serializable {
	/**
	 * Makes a connection between field and the element of the field. They can
	 * call each other.
	 */
	private WorldField field;

	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public WorldElement() {
		Timer.getInstance().addSubscriber(this);
	}

	/**
	 * Constructor, sets the value of the field attribute.
	 * 
	 * @author Demarcsek
	 * @param Parent Which field stores the given WorldElement?
	 */
	public WorldElement(WorldField Parent) {
		this();
		assert Parent != null;
		this.field = Parent;
	}

	/**
	 * Sets the value of the field attribute.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @param newField set which field stores the WorldElement now.
	 */
	public synchronized void setField(WorldField newField) {
		/*if(newField == null) {
			System.err.println("(newField == null) SHOULD NEVER BE");
			throw new RuntimeException();
		}*/
		
		this.field = newField;
	}

	/**
	 * Returns the value of the field attribute.
	 *
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @return field Which field stores the WorldElement at the moment?
	 */
	public synchronized WorldField getField() {
		return this.field;
	}
	
	/**
	 * Add WorldElement to the list of Subscribers.
	 * 
	 * @author Szabo
	 * @return void
	 */
	public void onLoad() {
		Timer.getInstance().addSubscriber(this);
	}
	
	/**
	 * Returns the View representation of each WorldElement.
	 * 
	 * @return WorldElementView the graphical representation of the given WorldElement
	 */
	public abstract WorldElementView getView();
	
	/**
	 * Removes the WorldElement from the Timer's subscriber list.
	 * This should be done, because of the Observer design pattern we use in the AntFarm.
	 */
	@Override
	public void finalize() {
		Timer.getInstance().removeSubscriber(this);
	}
	
}
