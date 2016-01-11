package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.Random;

import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.view.WorldElementView;

/**
 * 
 * This class is responsible for creating the AntEaters. Only one AntEater can be on the map at a time,
 * so it keeps it in mind. 
 * This is not a visible WorldElement on the map, so it has not got any graphical representation in the
 * game.
 * 
 * @author Demarcsek
 * @version 1.0
 **/
public class AntEaterSpawnPoint extends Irreplaceable implements Serializable {
	/**
	 * The graphical representation of a AntEaterSpawnPoint on the WorldField in the View section of MVC.
	 * However AntEaterSpawnPoint does not have any graphical representation at the moment in the game.
	 */
	public static final WorldElementView View = null;
	
	/**
	 * How many AntEaters can be on the map at the same time?
	 */
	public static final int MAX_ANTEATERS = 1;
	
	/**
	 * How many AntEaters are there on the map at the moment?
	 */
	private static int aes_created = 0;
	
	/**
	 * Default constructor.
	 * @author Demarcsek
	 */
	public AntEaterSpawnPoint() {
		
	}
	
	/**
	 * This method decreases the number of alive AntEaters.
	 * @author Demarcsek
	 */
	public static void decreaseAe() {
		if(aes_created > 0)
			aes_created--;
	}
	
	
	/**
	 * Returns the graphical representation of an AntEaterSpawnPoint.
	 * 
	 * However AntEaterSpawnPoint does not have any graphical representation at the moment in the game.
	 * 
	 * @author Demarcsek
	 * @return WorldElementView the graphical representation of an AntEaterSpawnPoint.
	 */
	@Override
	public WorldElementView getView() {
		return View;
	}
	
	/**
	 * AntEaterSpawnPoint should create an AntEater if there is not any on the map, with a given 
	 * possibility for each tick.
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		interact();
	}
	
	/**
	 * AntEaterSpawnPoint creates a new AntEater if there is not any on the map at the moment.
	 * @author Demarcsek
	 */
	@Override
	public void interact() {
		if(aes_created >= MAX_ANTEATERS)
			return;
		
		Random randomGenerator = new Random();
		if(randomGenerator.nextInt(50) == 42) { // not too often :)
			AntEater newAntEater = new AntEater();
			this.getField().addAntEater(newAntEater);
			aes_created++;
		}
	}
	
	/**
	 * AntEaterSpawnPoint should respond to every 29th tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 29;
	}
}
