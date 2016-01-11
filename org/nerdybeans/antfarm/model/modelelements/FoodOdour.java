package org.nerdybeans.antfarm.model.modelelements;

import org.nerdybeans.antfarm.model.Odour;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.FoodOdourView;


/**
 * This class represents the odour of the Food coming from the Warehouse.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.1
 **/
public class FoodOdour extends Odour {
	/**
	 * The graphical representation of a FoodOdour on the WorldField in the View section of MVC.
	 * However FoodOdour does not have any graphical representation at the moment in the game.
	 */
	public static WorldElementView View = new FoodOdourView();
	
	/**
	 * Returns the graphical representation of a FoodOdour.
	 * 
	 * However FoodOdour does not have any graphical representation at the moment in the game.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of a FoodOdour.
	 */
	@Override
	public WorldElementView getView() {
		return FoodOdour.View;
	}
	
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public FoodOdour() {
		super();
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @author Demarcsek
	 * @param Parent which field holds the food odour.
	 */
	public FoodOdour(WorldField Parent) {
		super(Parent);
	}
	
	/**
	 * FoodOdour should do nothing on tick.
	 * 
	 * @author Szabo
	 * @param TickEventArgs the event arguments of the tick
	 * @see Warehouse.onEmpty
	 */
	@Override
	public void onTick(Object TickEventArgs) {		
		//Basically does nothing right now as the reduction of intensity is called by onEmpty.
		return;
	}
	
	/**
	 * FoodOdour should respond to each tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 30;
	}
	

}
