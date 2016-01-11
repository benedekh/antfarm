package org.nerdybeans.antfarm.model.modelelements;

import org.nerdybeans.antfarm.model.Odour;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * This class represents the poisonous odour of the AntKiller.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.1
 **/
public class Poison extends Odour {
	/**
	 * The graphical representation of a Poison on the WorldField in the View section of MVC.
	 * However Poison does not have any graphical representation at the moment in the game.
	 */
	public static WorldElementView View = null;
	
	/**
	 * Returns the graphical representation of a Poison.
	 * 
	 * However Poison does not have any graphical representation at the moment in the game.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of a Poison.
	 */
	@Override
	public WorldElementView getView() {
		return Poison.View;
	}
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public Poison() {
		super();
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @author Demarcsek
	 * @param Parent which field holds the food odour.
	 */
	public Poison(WorldField Parent) {
		super(Parent);
	}
	
	/**
	 * Reduces the Intensity by one, in case the Period is correct.
	 * 
	 * @author Szabo
	 * @Param TickEventArgs Current timer tick value;
	 */
	public void onTick(Object TickEventArgs) {
		// Real-world behaviour:
		 if(this.intensity > 0)
			this.intensity--;
		// Ignored in 'Prototype test environment'
	}
	
	/**
	 * Poison should respond to every fourth tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 9999;
	}
	
}
