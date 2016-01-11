package org.nerdybeans.antfarm.model.modelelements;

import org.nerdybeans.antfarm.model.Odour;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;


/**
 * This class represents the Odour of the Ant that is left behind.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.2
 **/
public class AntOdour extends Odour {
	/**
	 * The maximal intensity of the AntOdour.
	 */
	public static int LIMIT = 20;
	
	/**
	 * The graphical representation of an AntOdour on the WorldField in the View section of MVC.
	 * However AntOdour does not have any graphical representation at the moment in the game.
	 */
	public static WorldElementView View = null;
	
	/**
	 * Returns the graphical representation of an AntOdour.
	 * 
	 * However AntOdour does not have any graphical representation at the moment in the game.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of an AntOdour.
	 */
	@Override
	public WorldElementView getView() {
		return AntOdour.View;
	}
	
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public AntOdour() {
		super();
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @author Demarcsek
	 * @param Parent which field contains the ant odour
	 */
	public AntOdour(WorldField Parent) {
		super(Parent);
	}
	
	/**
	 * Reduces the Intensity by one, in case the Period is correct.
	 * 
	 * @author Szabo
	 * @Param TickEventArgs Current timer tick value;
	 */
	public void onTick(Object TickEventArgs) {
		if(this.intensity > LIMIT) {
			this.intensity = 0.0f;
			return;
		}
		// Real-world behaviour
		 if(this.intensity > 0.0f)
		 	this.intensity -= Ant.OdourIncr;
		 else
			 this.intensity = 0.0f;
		// Ignored in 'Prototype test environment'
		
	}
	
	/**
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	public int getPeriod() {
		return 16;
	}
}
