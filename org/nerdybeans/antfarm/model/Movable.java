package org.nerdybeans.antfarm.model;

import java.io.Serializable;

/**
 * Common parent of movable WorldElement classes.
 * 
 * @author Demarcsek
 * @version 1.0
 **/
public abstract class Movable extends WorldElement implements Serializable {
	/**
	 * Which nextField will we visit?
	 */
	protected WorldField nextField;
	
	/**
	 * Do we want to make the Movable element move in the forced direction?
	 * If ForcedDirection = [0,5], then it moves to the forced direction indexed neighbour field.
	 */
	protected int ForcedDirection;
	
	/**
	 * The Movable object's latest position on the map. This variable stores a
	 * reference to the field the Movable was on, last time.
	 **/
	protected WorldField cameFrom;

	/**
	 * Default constructor
	 * @author Demarcsek
	 */
	public Movable() {
		this.cameFrom = null;
		this.nextField = null;
		this.ForcedDirection = -1;
	}

	/**
	 * Describes the next move of the movable element. Returns the reference of
	 * the field, where the object will move to during its next step.
	 * This method is not to be called from "outside".
	 * 
	 * @author Demarcsek
	 */
	protected abstract WorldField getNextMove();

	/**
	 * Defines a moving behavior. Child classes can implement their own moving
	 * mechanism overriding this method.
	 * 
	 * @author Demarcsek
	 */
	public abstract void makeNextMove();

	/**
	 * Sets the cameFrom member variable. It should only be called when the
	 * Movable is moved.
	 * 
	 * @author Demarcsek
	 * @param newPos The field from where the Movable element was on before the move.
	 **/
	public void setCameFrom(WorldField newPos) {
		this.cameFrom = newPos;
	}

	/**
	 * Returns the latest position of the Movable. Note, that it is NOT the
	 * current position of the Movable on the map - that can be retrieved by
	 * calling getField(). This one is where the Movable was, before.
	 * 
	 * @author Demarcsek
	 * @return cameFrom WorldField reference for the previous field the Movable element was on.
	 **/
	public WorldField getCameFrom() {
		return this.cameFrom;
	}
	
	/**
	 * The only reason of this function is to support deterministic
	 * tests over Ant.getNextMove (aka Ant moving algorithm that is partly randomized)
	 * (It made some additional (minor) modifications necessary.)
	 * 
	 * @author Demarcsek
	 * @param dir index of the neighbour field which should be visited on the next move.
	 * @see GameWorld.GameWorld()
	**/
	public void setNextMove( int dir ) {
		this.ForcedDirection = dir;
	}
}
