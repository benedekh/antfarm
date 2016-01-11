package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;

import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.PuddleView;


/**
 * Puddle represents a static obstacle on the game map, so it extends the
 * Irreplaceable class.
 * 
 * @author Demarcsek, Horvath
 * @version 1.1
 **/
public class Puddle extends Irreplaceable implements Serializable {
	/**
	 * The graphical representation of a Puddle on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new PuddleView();
	
	/**
	 * Returns the graphical representation of a Puddle.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of a Puddle.
	 */
	@Override
	public WorldElementView getView() {
		return Puddle.View;
	}
	
	/**
	 * Default constructor
	 * @author Demarcsek
	 */
	public Puddle() {
		super();
	}

	/**
	 * How should a Puddle interact?
	 * @author Demarcsek
	 */
	@Override
	public void interact() {

	}

	/**
	 * Converts a Puddle object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "Pu";
	}

	/**
	 * What should the Puddle do when it the timer gives a tick?
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs arguments of the tick event
	 */
	@Override
	public void onTick(Object TickEventArgs) {

	}
	
	/**
	 * Puddle should respond to each tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 1;
	}
	
}
