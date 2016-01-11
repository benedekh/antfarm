package org.nerdybeans.antfarm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.nerdybeans.antfarm.model.modelelements.AntEaterSpawnPoint;
import org.nerdybeans.antfarm.model.modelelements.AntHill;
import org.nerdybeans.antfarm.model.modelelements.AntLion;
import org.nerdybeans.antfarm.model.modelelements.Pebble;
import org.nerdybeans.antfarm.model.modelelements.Puddle;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;

/**
 * Stores the game map data structure and provides accessors to it.
 * 
 * @author Demarcsek, Horvath, Szabo
 * @version 1.5
 **/
public class GameWorld implements Serializable {
	/**
	 * The Map is a two-dimensional array of WorldField elements by definition
	 */
	private WorldField[][] Map;

	/**
	 * A reference to the AntHill object located on the game map. This is where
	 * the ants come from so it serves as an entry point for some graph
	 * algorithms used in the implementation of the moving mechanism of the
	 * ants.
	 */
	private AntHill EntryPoint;

	/**
	 * A list of references to Warehouse objects located on the game map. These
	 * objects are the destination of ants, so it serves as the exit nodes in
	 * some graph algorithms used in the implementation of the moving mechanism
	 * of the ants.
	 */
	private ArrayList<Warehouse> ExitPoints;
	
	/**
	 * Number of Warehouses in the GameWorld, on the map.
	 */
	public static final int NUM_OF_WAREHOUSES = 3;
	
	/**
	 * Number of Warehouses in the GameWorld, on the map.
	 */
	public static final int NUM_OF_PUDDLES = 10;
	
	/**
	 * Number of Pebbles in the GameWorld, on the map.
	 */
	public static final int NUM_OF_PEBBLES = 10;
	
	/**
	 * Number of AntLions in the GameWorld, on the map.
	 */
	public static final int NUM_OF_ANTLIONS = 4;
	
	/**
	 * Number of AntEater spawn points in the GameWorld on the map.
	 * Each spawn point means an AntEater, with a given possibility to be appeared.
	 */
	public static final int NUM_OF_AE_SPAWNPOINTS = 1;
	
	/**
	 * Standard map height (measured in # of fields) 
	**/
	public static final int SIZE_H = 20; 
	
	/**
	 * Standard map width (measured in #of fields)
	**/
	public static final int SIZE_W = 20;

	/**
	 * Default constructor. 
	 * Constructs and initializes the map.
	 * 
	 * @author Horvath
	 * @see paper documentation for details
	 */
	public GameWorld() {
		System.out.println("[DEBUG#GameWorld.ctor]");
		this.Map = new WorldField[SIZE_H][SIZE_W];

		for (int i = 0; i < SIZE_H; ++i) {
			for (int j = 0; j < SIZE_W; ++j) {
				this.Map[i][j] = new WorldField();
			}
		}
		
		for (int i = 0; i < SIZE_H; ++i) {
			for (int j = 0; j < SIZE_W; ++j) {

				/**
				 * Little map: Every second row is pushed one field right,
				 * because fields are hexagonal.
				 * 
				 * X X X X ... X X;
				 *  X X X X ... X X;
				 * X X X X ... X X;
				 * .... 
				 * X X X X ... X X;
				 *  X X X X ... X X;
				 * 
				 * Neighbour indexes (x = current field; - = doesn't exist):
				 * 0 - 1;
				 * 5 x 2;
				 * 4 - 3;
				 */

				// add neighbour fields
				for (int k = 0; k < 6; ++k) {
					if (i == 0) {
						if (k == 0 || k == 1) {
							this.Map[i][j].addNeighbour(null);
						} else if (j == 19 && k == 2) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 19 && k == 2) {
							this.Map[i][j].addNeighbour(this.Map[i][j + 1]);
						} else if (j == 0 && (k == 4 || k == 5)) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 0 && k == 5) {
							this.Map[i][j].addNeighbour(this.Map[i][j - 1]);
						} else if (j != 0 && k == 4) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j - 1]);
						} else if (k == 3) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j]);
						}
					}
					
					else if (i%2 == 0) {
						if (j == 0 && (k == 0 || k == 4 || k == 5)) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 0 && k == 0) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j - 1]);
						} else if (j != 0 && k == 4) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j - 1]);
						} else if (j != 0 && k == 5) {
							this.Map[i][j].addNeighbour(this.Map[i][j - 1]);
						} else if (j == 19 && k == 2) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 19 && k == 2) {
							this.Map[i][j].addNeighbour(this.Map[i][j + 1]);
						} else if (k == 1) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j]);
						} else if (k == 3) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j]);
						}
					}
					
					else if (i == 19) {
						if (k == 3 || k == 4) {
							this.Map[i][j].addNeighbour(null);
						} else if (j == 19 && (k == 1 || k == 2)) {
							this.Map[i][j].addNeighbour(null);
						} else if (j!= 19 && k == 1) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j + 1]);
						} else if(j!= 19 && k == 2) {
							this.Map[i][j].addNeighbour(this.Map[i][j + 1]);
						} else if (j == 0 && k == 5) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 0 && k == 5) {
							this.Map[i][j].addNeighbour(this.Map[i][j - 1]);
						} else if (k == 0) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j]);
						}
					} 
					
					else if (i%2 == 1) {
						if (j == 0 && k == 5) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 0 && k == 5) {
							this.Map[i][j].addNeighbour(this.Map[i][j - 1]);
						} else if (j == 19 && (k == 1 || k == 2 || k == 3)) {
							this.Map[i][j].addNeighbour(null);
						} else if (j != 19 && k == 1) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j + 1]);
						} else if (j != 19 && k == 2) {
							this.Map[i][j].addNeighbour(this.Map[i][j + 1]);
						} else if (j != 19 && k == 3) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j + 1]);
						} else if (k == 4) {
							this.Map[i][j].addNeighbour(this.Map[i + 1][j]);
						} else if (k == 0) {
							this.Map[i][j].addNeighbour(this.Map[i - 1][j]);
						}
					}
				}
			}
		}
		
		this.ExitPoints = new ArrayList<Warehouse>();
	}

	/**
	 * Initializes and places the elements onto the newly created map.
	 * 
	 * @author Horvath, Szabo, Demarcsek
	 * @see paper documentation for details
	 */
	public void doGenesis() {		
		// It is better to make the random object placements outside of the i,j loop.
		
		// random generator for random / uniform distribution.
		Random randomGenerator = new Random();
		
		// AntLion
		int antlion_number = 0;
		
		while (antlion_number < NUM_OF_ANTLIONS) {
			int i = randomGenerator.nextInt(GameWorld.SIZE_H);
			int j = randomGenerator.nextInt(GameWorld.SIZE_W);
			
			AntLion al = new AntLion();
			
			if (this.Map[i][j].isPassable() == true){
				this.Map[i][j].setPermanentElement(al);
				this.Map[i][j].setPassable(true);
				antlion_number += 1;
			} 
		}

		
		// 1 AntHill
		int anthill_number = 0;
		
		while (anthill_number < 1) {
			int i = randomGenerator.nextInt(GameWorld.SIZE_H);
			int j = randomGenerator.nextInt(GameWorld.SIZE_W);
			
			AntHill ah = new AntHill();
		
			if (this.Map[i][j].isPassable() == true){
				this.Map[i][j].setPermanentElement(ah);
				this.Map[i][j].setPassable(false);
				
				this.EntryPoint = ah;
				anthill_number += 1;
				//System.err.println("AntHill added to field (" + i + "," + j + ")");
			}
		}
		
		
		// Warehouse
		int warehouse_number = 0;
		
		
		this.ExitPoints = new ArrayList<Warehouse>();
		
		while (warehouse_number < NUM_OF_WAREHOUSES) {
			int i = randomGenerator.nextInt(GameWorld.SIZE_H);
			int j = randomGenerator.nextInt(GameWorld.SIZE_W);
			
			Warehouse wh = new Warehouse();
			
			boolean passable = this.Map[i][j].isPassable();
			Irreplaceable whoIsThere = this.Map[i][j].getPermanentElement();
			
			if ((passable == true) && (whoIsThere == null)){
				this.Map[i][j].setPermanentElement(wh);
				this.Map[i][j].setPassable(true);
				
				//this.ExitPoints.add(wh);
				warehouse_number += 1;
				wh.spreadFoodOdour(NUM_OF_WAREHOUSES, false);
				this.ExitPoints.add(wh);
				//System.err.println("Warehouse added to field (" + i + ", " + j + ")");
			}
		}
		
		
		// Puddle (pocsolya)
		int puddle_number = 0;
		
		while (puddle_number < NUM_OF_PUDDLES) {
			int i = randomGenerator.nextInt(GameWorld.SIZE_H);
			int j = randomGenerator.nextInt(GameWorld.SIZE_W);
		
			Puddle pu = new Puddle();
		
			boolean passable = this.Map[i][j].isPassable();
			Irreplaceable whoIsThere = this.Map[i][j].getPermanentElement();
			
			if ((passable == true) && (whoIsThere == null)){
				this.Map[i][j].setPermanentElement(pu);
				this.Map[i][j].setPassable(false);
				puddle_number += 1;
			}
		}
		
		
		// Pebble (kavics)
		int pebble_number = 0;
		
		while (pebble_number < NUM_OF_PEBBLES) {
			int i = randomGenerator.nextInt(SIZE_H);
			int j = randomGenerator.nextInt(SIZE_W);
		
			Pebble pe = new Pebble();
			
			boolean passable = this.Map[i][j].isPassable();
			Irreplaceable whoIsThere = this.Map[i][j].getPermanentElement();
			
			if ((passable == true) && (whoIsThere == null)){
				this.Map[i][j].setDynamicElement(pe);
				pebble_number += 1;
			}
		}
		
		// AntEaterSpawnPoint
		int num_ae_sp = 0;
		while(num_ae_sp != NUM_OF_AE_SPAWNPOINTS) {
			boolean success = false;
			while(!success) {
				int i = randomGenerator.nextInt(SIZE_H);
				int j = randomGenerator.nextInt(SIZE_W);
				if(this.Map[i][j].getPermanentElement() == null) {
					this.Map[i][j].setPassable(true);
					this.Map[i][j].setPermanentElement( new AntEaterSpawnPoint() );
					success = true;
					num_ae_sp++;
				}
			}
		}
	}

	/**
	 * Retrieves the map data structure
	 * 
	 * @author Horvath
	 * @return this.Map the current game map, that contains all the elements and fields.
	 */
	public WorldField[][] getMap() {
		return this.Map;
	}

	/**
	 * Retrieves the AntHill
	 * 
	 * @author Horvath
	 * @return A reference to the EntryPoint AntHill object
	 */
	public AntHill getEntryPoint() {
		return this.EntryPoint;
	}

	/**
	 * Retrieves the list of Warehouses
	 * 
	 * @author Horvath
	 * @return A reference to the list of Warehouse object references
	 */
	public ArrayList<Warehouse> getExitPoints() {
		return this.ExitPoints;
	}
	
	/**
	 * Replace the EntryPoint AntHill.
	 * 
	 * @author Demarcsek
	 * @param ep which EntryPoint should be used instead of the current one.
	 */
	public void setEntryPoint(AntHill ep) {
		this.EntryPoint = ep;
	}
}
