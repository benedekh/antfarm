package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;

import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.AntLionView;

/**
 * 
 * Represents an ant lion in the game model, a non-moving element, (so it
 * extends the Irreplaceable class) which can eat the ant if they are on the
 * same field.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.2
 **/
public class AntLion extends Irreplaceable implements Serializable {
	/**
	 * The graphical representation of an AntLion on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new AntLionView();
	
	/**
	 * Returns the graphical representation of an AntLion.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of an AntLion.
	 */
	@Override
	public WorldElementView getView() {
		return AntLion.View;
	}
	
	/**
	 * Default constructor.
	 * @author Szabo
	 */
	public AntLion() {
		super();
	}

	/**
	 * Removes the ant from the field.
	 * 
	 * @author Horvath
	 * @see AntLion.interact().
	 * @param Target which ant should be removed.
	 */
	@Deprecated
	private void eatAnt(Ant Target) {
		this.getField().removeAnt(Target);
	}

	/**
	 * Conducts the antlion's standard interaction that includes eating ants on
	 * its field.
	 * 
	 * The critical part of this method is thread-safe, due to possible multiple access. 
	 * (by the View and the Model as well)
	 * 
	 * @author Demarcsek, Horvath, Szabo
	 */
	@Override
	public void interact() {
		/** older version
		ArrayList<Ant> antList = this.field.getAntList();
		for (Ant iterator : antList){
			this.eatAnt(iterator);
		}*/
				
		synchronized(this.getField().getAntList()) {
			Iterator<Ant> iter = this.getField().getAntList().iterator();
			while (iter.hasNext()) {
				Ant CurrEnt = iter.next();
				Timer.getInstance().removeSubscriber(CurrEnt);
			}
			this.getField().getAntList().clear();
		}
		
	}

	/**
	 * Converts an AntLion object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "L";
	}

	/**
	 * AntLion should eat the ants on the field.
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		interact();
	}
	
	/**
	 * AntLion should respond to each tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 1;
	}
}
