package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.Movable;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.WorldFieldView;
import org.nerdybeans.antfarm.view.viewelements.AntView;

/**
 * Represents an ant in the game model
 * 
 * The class also extends Movable, since Ant objects should be capable of moving
 * on the game map. 
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.5
**/
public class Ant extends Movable implements Serializable {
	/**
	 * The graphical representation of an Ant on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new AntView();
	
	/**
	 * Each ant gives a constant AntOdour to the field, when it steps on that.
	 */
	public static final float OdourIncr = 0.1f;
	
	/**
	 * Returns the graphical representation of an Ant.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of an Ant.
	 */
	@Override
	public WorldElementView getView() {
		return Ant.View;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @author Szabo
	 */
	public Ant() {
		super();
		//System.err.println("[DEBUG#Ant.Ant()]");
	}

	/**
	 * 
	 * This method defines the moving algorithm of the movable object, so it
	 * decides the very next position of the object in question.
	 * 
	 * @author Horvath
	 * @return The next position of the object on the game map
	 */
	@Override
	/*protected WorldField getNextMove() {
		ArrayList<WorldField> neighbours = this.getField().getNeighbours();
		ArrayList<Float> weightedOdours = new ArrayList<Float>();
		
		// receive and weight the odour values from the neighbour fields
		for (int i = 0; i < neighbours.size(); ++i){
			WorldField neighbourField = neighbours.get(i);
			
			if ((neighbourField != null) && (neighbourField != this.cameFrom)){
				boolean passable = neighbourField.isPassable();
				
				if (passable == true){
					float antOdour = neighbourField.getAntOdour();
					float foodOdour = neighbourField.getFoodOdour();
					
					float weightedOdour = (float)((0.6)*foodOdour + (0.4)*antOdour);
					weightedOdours.add(i, weightedOdour);
				} else {
					weightedOdours.add(i, (float)-1.0);
				}
			} else {
				weightedOdours.add(i, (float)-1.0);
			}
		}
		
		// decide whether neither fields can be visited
		boolean possibleVisit = false;
		for (Float iterator : weightedOdours){
			if (iterator != (float)-1.0){
				possibleVisit = true;
				break; // <-- optimization (if we found one possible way, we do not need to continue the search:)) by Demarcsek
			}
		}
		
		// if there is not any way to move
		if (possibleVisit == false) {
			nextField = this.cameFrom;
			return nextField;
		}
		
		// else decide where to move
		// get the max of weightedOdours
		float maxOdour = (float)-1.0;
		
		// find out the max weightedOdour value
		for (Float iter : weightedOdours){
			if (iter > maxOdour){
				maxOdour = iter;
			}
		}
		
		// decide whether the max is stored multiple times in the array
		int indexOfMax = weightedOdours.indexOf(maxOdour);
		int lastIndexOfMax = weightedOdours.lastIndexOf(maxOdour);
		
		// if it is stored only once
		if (indexOfMax == lastIndexOfMax){
			nextField = neighbours.get(indexOfMax);
		} else {
			ArrayList<Integer> possibleIndexesToMove = new ArrayList<Integer>();
			
			for (int i = 0; i < weightedOdours.size(); ++i){
				float value = weightedOdours.get(i);
				
				if (value == maxOdour){
					possibleIndexesToMove.add(i);
				}
			}
			
			Random randomGenerator = new Random(); // random generator for deciding the nextField
			int maxIndex = possibleIndexesToMove.size() - 1;
			int index = randomGenerator.nextInt(maxIndex); // random from [0,maxIndex]
			
			int indexOfNextField = possibleIndexesToMove.get(index);
			nextField = neighbours.get(indexOfNextField);
		}

		return nextField;
		
	}*/
	
	/**
	 * This method defines the thread-safe moving algorithm of the movable object, so it
	 * decides the very next position of the object in question.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @return The next position of the object on the game map
	 */
	protected synchronized WorldField getNextMove() {
		Random r = new Random();
		
		WorldField next = null;
		
		// Weights to decide by
		float weights[] = new float[] {		// 6 directions
			0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f
		};
		
		// Array of indexes pointing to 'valid' (passable, not null) adjs
		ArrayList<Integer> valids = new ArrayList<Integer>();
		
		// Calculating weights and filling valids
		final float max_w = 10000f;
		boolean no_random = false;
		for(int i = 0; i < 6; ++i) {
			WorldField n = this.getField().getNeighbours().get(i);
			weights[i] = -1.0f;
			if(n != null)
				if(n.isPassable()) {
					valids.add(i);
					if(n.getFoodOdour() >= Warehouse.MAX_FOOD_AMOUNT-0.001f) {	// if a warehouse is close, do not hesitate
						weights[i] = max_w; no_random = true;
					} else if(this.getCameFrom() == n) {						// avoid going back
						weights[i] = -2f*max_w;				
					} else													// consider fo and ao
						weights[i] = r.nextFloat()*10f*(n.getAntOdour()*20f + n.getFoodOdour()*100f);
				}
			
		}
		
		// If AO is quite small, just pick a random valid neighbour ... maybe :)
		if((!no_random) && this.getField().getAntOdour() <= Ant.OdourIncr) {
			if(r.nextInt(10)==0) {
				this.nextField = this.getField().getNeighbours().get(valids.get(r.nextInt(valids.size())));
				return this.nextField;
			}
		}
		
		
		
		// In rare cases (10%), still pick randomly
		if((!no_random) && r.nextInt(10)==1) {
			this.nextField = this.getField().getNeighbours().get(valids.get(r.nextInt(valids.size())));
			return this.nextField;
		}
		
		// Otherwise choose the maximal weighted neighbour
		int maxindex = 0;
		for(int i = 1; i < 6; ++i) {
			if(weights[i] > weights[maxindex]) {
				maxindex = i;
			}
		}
		next = this.getField().getNeighbours().get(maxindex);
		this.nextField = next;
		return this.nextField;
		
	}

	/**
	 * Calls getNextMove() and conducts the actual move by deleting the object
	 * from its current position and adding it to the next one (returned by
	 * getNextMove()). It is also responsible for keeping references consistent
	 * between the concerned WorldField and WorldElement objects.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 *
	 * @author Horvath, Demarcsek
	 */
	@Override
	public synchronized void makeNextMove() {		
		/**
		 * Check whether it is a poisoned field. If so then remove himself.
		**/
		if(this.getField() == null) {
			Timer.getInstance().removeSubscriber(this);
			return;
		}
		
		float poison = this.getField().getPoison();
		if (poison > 0) {
			//System.err.println("POSION DETECTED");
			this.getField().removeAnt(this);
			this.setField(null);
			Timer.getInstance().removeSubscriber(this);
			return;
		}
		
		/**
		 * Commit suicide if AntEater detected
		**/
		if(this.getField().getAntEater() != null) {
			//System.out.println("[DEBUG#Ant.makeNextMove] AntEater detected. Committing suicide...");
			this.getField().getAntEater().eatAnt(this);
			return;
		}
		
		
		/**
		 * Else move and remove himself from the field he was on.
		**/
		if(this.ForcedDirection >= 0) 
			this.nextField = this.getField().getNeighbours().get(this.ForcedDirection);
		else {
			//this.getNextMove();
			this.getNextMove();
		}
			
		WorldField next = this.nextField;
			
		
		// if we stay on this field, do not move!
		synchronized(this.getField().getAntList()) {
			if(next == null) {
				this.getField().removeAnt(this);
				//this.setField(null);
				//Timer.getInstance().removeSubscriber(this);
			} else if ((next != this.getField())) {
				this.getField().setAntOdour(this.getField().getAntOdour() + Ant.OdourIncr);
				next.addAnt(this);
				WorldField previousField = this.getCameFrom();
				previousField.removeAnt(this);
			}
		}
	}

	/**
	 * Converts an Ant object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "A";
	}
	
	/**
	 * Responding to the Tick event, which here means moving around on the Map.
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	public void onTick(Object TickEventArgs) {
		//System.out.println("[DEBUG] Ant.onTick() [tick=" + (Integer)TickEventArgs + ", num_of_subscribers=" + Timer.getInstance().countObservers() + "]");
		
		this.makeNextMove();
		
	}

	/**
	 * Respond to every 2nd Tick.
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		//System.out.println("[DEBUG#Ant.getPeriod()] tick = " + Timer.getInstance().getTick());
		return 5;
	}

}
