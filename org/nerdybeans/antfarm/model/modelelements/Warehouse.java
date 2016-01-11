package org.nerdybeans.antfarm.model.modelelements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.Irreplaceable;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.view.WorldElementView;
import org.nerdybeans.antfarm.view.viewelements.WarehouseView;

/**
 * Represents a warehouse in the game model
 * 
 * The class also extends Irreplaceable, since warehouse objects shouldn't be
 * capable of moving on the game map.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.5
 **/
public class Warehouse extends Irreplaceable implements Serializable {
	/**
	 * The graphical representation of a Warehouse on the WorldField in the View section of MVC.
	 */
	public static WorldElementView View = new WarehouseView();
	
	/**
	 * Returns the graphical representation of a Warehouse.
	 * 
	 * @author Horvath
	 * @return WorldElementView the graphical representation of a Warehouse.
	 */
	@Override
	public WorldElementView getView() {
		return Warehouse.View;
	}
	
	/**
	 * Stores the amount of food which is still in the warehouse
	 */
	private int food_amount;
	
	/**
	 * This attribute is required in loading a previous game and setting the Warehouse on the map,
	 * and spreading its smell all over the fields. 
	 */
	private volatile boolean broadcast_done = true;
	
	/**
	 * Has the Ants emptied the Warehouse?
	 */
	private boolean emptied = false;
	
	/**
	 * The maximal food amount a Warehouse can store. 
	 */
	public static final int MAX_FOOD_AMOUNT = 10;
	
	/**
	 * Number of warehouses 
	 */
	public volatile static int NUM_OF_WAREHOUSES = 0;
	

	/**
	 * Has the game come to an end?
	*/
	private volatile static boolean END_OF_GAME_SIGNAL = false;


	/**
	 * Default constructor
	 * 
	 * @author Demarcsek
	 * @see GameSerializer.unserialize()
	*/
	public Warehouse() {
		super();
		Warehouse.NUM_OF_WAREHOUSES++;
		this.food_amount = MAX_FOOD_AMOUNT;
	}
	
	/**
	 * Sets the foodAmount in the Warehouse for the given value.
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 *
	 * @author Demarcsek
	 * @param newAmount the new food amount value
	*/
	public synchronized void setFoodAmount(int newAmount) {
		if(newAmount >= 0)
			this.food_amount = newAmount;
	}
	
	/**
	 * Returns the amount of food (which is equal with food_amount attribute)
	 * 
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 * @return food_amount how much food is stored in the warehouse yet
	 */
	public synchronized int getFoodAmount() {
		return this.food_amount;
	}

	/**
	 * Decrease the value of food_amount with one.
	 *
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 * 
	 * @author Horvath
	 */
	public synchronized void decreaseFoodAmount() {
		//System.out.println("[DEBUG#Warehouse.decreaseFoodAmount]");
		if(this.food_amount > 0)
			this.food_amount--;
	}

	/**
	 * When the food_amount is equal to zero (when the ants eat all of the
	 * food), it decreases the food odour of the nearest fields.	
	 *  
	 * This method is thread-safe, due to possible multiple access. (by the View and the Model as well)
	 *  
	 * @author Horvath, Szabo, Demarcsek
	 */
	public synchronized void onEmpty() {
		if(this.emptied)
			return;
		NUM_OF_WAREHOUSES--;
		if((NUM_OF_WAREHOUSES) == 0) {
			END_OF_GAME_SIGNAL = true;
		}
		Timer.getInstance().removeSubscriber(this);
		this.getField().setPermanentElement(null);
		
		/*if((NUM_OF_WAREHOUSES--) == 0) {
			System.out.println("[DEBUG#Warehouse.onEmpty] Sending end of game signal...");
			END_OF_GAME_SIGNAL = true;
		} else {
			System.out.println("[DEBUG#Warehouse.onEmpty] # of warehouses: " + NUM_OF_WAREHOUSES);
			AntHill.reset();
		}*/
		
		this.emptied = true;
		this.spreadFoodOdour(Warehouse.NUM_OF_WAREHOUSES, true);
		AntHill.reset();
	}
	
	/**
	 * Returns whether the game has come to an end?
	 * @author Demarcsek
	 * @return END_OF_GAME_SIGNAL which indicated whether the game has come to an end?
	 */
	public synchronized static boolean isEndOfGame() {
		return Warehouse.END_OF_GAME_SIGNAL;
	}

	/**
	 * Implements the interaction between an ant and a warehouse
	 * 
	 * If this methods was called by the Timer, then it removes the ants living
	 * on the field. Otherwise it can be called by an Ant, then it removes the
	 * ant living on the field.
	 * 
	 * The crucial part of this method is thread-safe, due to possible multiple access. 
	 * (by the View and the Model as well)
	 * 
	 * @author Demarcsek, Horvath
	 */
	@Override
	public synchronized void interact() {
		if(this.getField() == null) {
			System.out.println("[Warehouse.getField==null] removing from timer");
			Timer.getInstance().removeSubscriber(this);
			return;
		}
		if(NUM_OF_WAREHOUSES == 0) {
			END_OF_GAME_SIGNAL = true;
			return;
		}
		
		this.getField().setFoodOdour(Warehouse.MAX_FOOD_AMOUNT);
		
		// minimize the number of instructions in the critical section
		int times = 0;
		synchronized(this.getField().getAntList()) {
			times = this.getField().getAntList().size();
			//this.getField().getAntList().clear();
			Iterator<Ant> it = this.getField().getAntList().iterator();
			while(it.hasNext()) {
				Timer.getInstance().removeSubscriber(it.next());
			}
			this.getField().getAntList().clear();
		}
		
		for(int i = 0; i < times; ++i)
			this.decreaseFoodAmount();

		// check whether the warehouse empty is
		if (this.food_amount <= 0) {
			onEmpty();
		}
	}

	/**
	 * Converts a Warehouse object to String (supports custom serialization)
	 * 
	 * @author Demarcsek
	 * @return String representation of the class.
	 * @see GameSerializer
	 */
	@Override
	public String toString() {
		return "W";
	}
	
	/**
	 * Performs food odour spreading.
	 * The FoodSpread algorithm traverses the graph and
	 * spreads food odour in a balanced, uniform way conforming
	 * to the following rules:
	 *  1. The more distant a field is from the source (warehouse),
	 *     the lower food odour level will be added to its food odour
	 *  2. None of the fields can have higher food odour level than
	 *     the food odour of any of the sources (warehouse fields)
	 *  3. The algorithm should spread odour in a 'fair' way, meaning that
	 *     each field recieves some food odour during one round of the
	 *     algorithm
	 * @param num_of_sources Number of warehouses on the map is a parameter of the algorithm (used in calculations)
	 * @param inverted Runs the algorithm in 'inverted mode' meaning it un-spreads (undoes) the food odour 
	 * @author Demarcsek
	**/
	public void spreadFoodOdour(int num_of_sources, boolean inverted) {		
		// Algorithm setup (constants)
		final float MAX_VAL = Warehouse.MAX_FOOD_AMOUNT; // The max. food odour of the Warehouse.
		final float EPSILON = 0.942f;
		final float MAGIC = 0.1618033f; // golden section divided by ten
		final int INFINITY = - 1; // used for indicating infinity distances
		
		// Data structure initialization
		HashMap<WorldField, Integer> Distances = new HashMap<WorldField, Integer>();
		HashMap<WorldField, Boolean> Visited = new HashMap<WorldField, Boolean>();
		
		// BFS-Init
		Distances.put(this.getField(), new Integer(0));
		Visited.put(this.getField(), new Boolean(false));
		LinkedList<WorldField> nextVisits = new LinkedList<WorldField>();
		nextVisits.add(this.getField());
		
		if(!inverted)
			this.getField().setFoodOdour(MAX_VAL);
		else
			this.getField().setFoodOdour(0);
		
		// BFS Traverse
		while(!nextVisits.isEmpty()) {
			WorldField c = nextVisits.poll();
			
			//if(!Visited.containsKey(c)) Visited.put(c, new Boolean(false));
			if( c == null )
				System.err.println("(0) !!! CRITICAL ERROR: The execution should never reach this branch !!!!");
			
			if(Visited.get(c))
				continue;
			
			Visited.put(c, true);
			
			if(c != this.getField()) {
				if(!Distances.containsKey(c))
					System.err.println("(1) !!! CRITICAL ERROR: The execution should never reach this branch !!!!");
				
				/** Spread **/
				/// Original: needs odours to be float
				float ep = EPSILON;
				
				float t = 2.5f;
				
				if(inverted)
					t = (-1);
				
				if(c.getFoodOdour() < MAX_VAL) {
					c.setFoodOdour(
							//c.getFoodOdour() + ((ep*((float)1/(num_of_sources))*((float)MAX_VAL-ep))/(float)Math.pow(Distances.get(c),MAGIC)) 
							c.getFoodOdour() + t*((1.0f/(num_of_sources*(float)Math.sqrt(Distances.get(c))))*MAX_VAL)
						);
					if(c.getFoodOdour() > MAX_VAL)
						c.setFoodOdour(MAX_VAL*ep);
					else if(c.getFoodOdour() < 0)
						c.setFoodOdour(0);
				} else {
					if(!inverted)
						c.setFoodOdour( MAX_VAL ); 
					else
						c.setFoodOdour(MAX_VAL-2*ep);
				}
				
				//c.setFoodOdour(Distances.get(c));
				
				/// Temporary: to supress build errors
				//c.setFoodOdour(1);	
			}
			
			for(WorldField n : c.getNeighbours()) {
				if(n == null)		// Skipping unattached neighbours
					continue;
				
				if(!Visited.containsKey(n))
					Visited.put(n, false);
				
				if(!Visited.get(n)) {
					/*if(!Distances.containsKey(n))
						Distances.put(n, INFINITY);*/
					
					Distances.put(n, Distances.get(c) + 1);	
					
					nextVisits.add(n);
				}
			}

		}
	}
	
	
	
	/**
	 * Warehouse should interact when a tick arises.
	 * 
	 * The crucial part of this method is thread-safe, due to possible multiple access. 
	 * (by the View and the Model as well)
	 * 
	 * @author Demarcsek
	 * @param TickEventArgs the event arguments of the tick
	 */
	@Override
	public void onTick(Object TickEventArgs) {
		if(!this.broadcast_done) {
			System.err.println("broadcasting");
			synchronized(this) {
				this.broadcast_done = true;
				Warehouse.NUM_OF_WAREHOUSES++;
			}
			
		}
		
		this.interact();
	}
	
	/**
	 * 
	 * @author Demarcsek
	 * @return how often should the object respond
	 */
	@Override
	public int getPeriod() {
		return 2;
	}
	
	public synchronized static void reset() {
		//synchronized(Warehouse.END_OF_GAME_SIGNAL) {
			Warehouse.END_OF_GAME_SIGNAL = false;
		//}
	}

}
