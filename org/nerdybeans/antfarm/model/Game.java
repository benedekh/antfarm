package org.nerdybeans.antfarm.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.ListIterator;

import org.nerdybeans.antfarm.auxiliary.Timer;
import org.nerdybeans.antfarm.model.modelelements.Ant;
import org.nerdybeans.antfarm.model.modelelements.AntHill;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;
import org.nerdybeans.antfarm.view.viewelements.AntHillView;

/**
 * The Game class is the central state storage of the application.
 * 
 * It is mainly responsible for storing global game data throughout the AntFarm
 * game which contains of the following components: State of the map (game
 * world) State of the weapons Time elapsed since the beginning of the game A
 * Game object actually represents a session, an instance of an AntFarm game. So
 * a game can be saved and loaded by storing a Game object in a file on the
 * disk.
 * 
 * @author Demarcsek, Horvath
 * @version 1.5
 **/
public class Game implements Serializable {
	/**
	 * Indicates whether the initialization has been done before.
	 */
	private boolean init_done = false;
	
	/**
	 * Standard map height (measured in # of fields) 
	**/
	public static final int SIZE_H = 20; 
	
	/**
	 * Standard map width (measured in #of fields)
	**/
	public static final int SIZE_W = 20;
	
	/**
	 * The first line of a file containing a stored Game object should start
	 * with this string so the system can identify saved Game objects properly
	 **/
	public static final String FILE_BANNER = "# org.nerdybeans.antfarm.model.Game";

	/**
	 * Time elapsed since the beginning of the game (in milliseconds)
	 **/
	private long time_elapsed;
	
	/**
	 * When was the game started.
	 */
	private long start_time;

	/**
	 * The current GameWorld object. It stores the state of the game world (game
	 * map)
	 */
	private GameWorld GameWorldState;

	/**
	 * The current AntKiller object. It stores the state of the ant killer
	 * weapon
	 */
	private AntKiller AntKillerState;

	/**
	 * The current AntOdourKiller object. It stores the state of the ant odour
	 * killer
	 */
	private AntOdourKiller AntOdourKillerState;

	/**
	 * Default constructor
	 * Initializes member variables with their default values.
	 * 
	 * @author Horvath
	 */
	public Game() {
		Date start = new Date();

		this.time_elapsed = 0;
		this.start_time = start.getTime();

		this.GameWorldState = new GameWorld();
		this.AntKillerState = new AntKiller();
		this.AntOdourKillerState = new AntOdourKiller();
		AntHill.reset();
		Warehouse.reset();
		Warehouse.NUM_OF_WAREHOUSES = 0;
	}

	/**
	 * Initiates a new game by creating a brand new game world.
	 * 
	 * @author Horvath
	 */
	public void initNew() {
		// for the sake of a new game start, to begin with a clean map.
		this.GameWorldState = new GameWorld();
		this.AntKillerState = new AntKiller();
		this.AntOdourKillerState = new AntOdourKiller();
		
		// generate the random map.
		GameWorldState.doGenesis();
		init_done = true;
	}

	/**
	 * Loads a saved game from a file by deserializing the stored Game object
	 * and copying the data.
	 * 
	 * @author Horvath, Szabo
	 */
	public void load() {
		try {
			FileInputStream fis = new FileInputStream("AntFarmSave.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);

			this.time_elapsed = (Long) ois.readObject();
			this.start_time = (Long) ois.readObject();
			this.GameWorldState = (GameWorld) ois.readObject();
			this.AntKillerState = (AntKiller) ois.readObject();
			this.AntOdourKillerState = (AntOdourKiller) ois.readObject();
			Warehouse.NUM_OF_WAREHOUSES = ois.readInt();
			

			System.out.println("Number of WareHouses: " + Warehouse.NUM_OF_WAREHOUSES);


			ois.close();

			
			for (int i = 0; i < SIZE_H; ++i) {
				for (int j = 0; j < SIZE_W; ++j) {
						if ( this.GameWorldState.getMap()[i][j].getPermanentElement() != null ) {
							Timer.getInstance().addSubscriber( this.GameWorldState.getMap()[i][j].getPermanentElement() );
						}
						if ( this.GameWorldState.getMap()[i][j].getDynamicElement() != null ) {
							Timer.getInstance().addSubscriber( this.GameWorldState.getMap()[i][j].getDynamicElement() );
						}
						if ( this.GameWorldState.getMap()[i][j].getAntOdourObj() != null ) {
							Timer.getInstance().addSubscriber( this.GameWorldState.getMap()[i][j].getAntOdourObj() );
						}
						if ( this.GameWorldState.getMap()[i][j].getPosionObj() != null ) {
							Timer.getInstance().addSubscriber( this.GameWorldState.getMap()[i][j].getPosionObj() );
						}
						if ( this.GameWorldState.getMap()[i][j].getAntEater() != null ) {
							Timer.getInstance().addSubscriber( this.GameWorldState.getMap()[i][j].getAntEater() );
						}
						if ( this.GameWorldState.getMap()[i][j].getAntIterator() != null ) {
							ListIterator<Ant> iter = this.GameWorldState.getMap()[i][j].getAntIterator();
							
							while( iter.hasNext() ) {
								Ant current = iter.next();
								//System.out.println(current.toString());
								//System.out.println("DEBUG: " + Timer.getInstance().countObservers());
								Timer.getInstance().addSubscriber( current );
							}
							
						}
						
					
				}
			}
			//Timer.setInstance((Timer)ois.readObject());
			System.out.println("Number of Observers: " + Timer.getInstance().countObservers());

			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current game object to a file via serialization.
	 * @author Horvath
	 */
	public void save() {
		try {
			FileOutputStream fos = new FileOutputStream("AntFarmSave.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			this.getTimeElapsed(); // set the time_elapsed variable

			oos.writeObject(this.time_elapsed);
			oos.writeObject(this.start_time);
			oos.writeObject(this.GameWorldState);
			oos.writeObject(this.AntKillerState);
			oos.writeObject(this.AntOdourKillerState);
			oos.writeInt(Warehouse.NUM_OF_WAREHOUSES);
			//oos.writeObject(Timer.getInstance());
			oos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the amount of time elapsed since the beginning of the current
	 * game
	 * 
	 * @author Horvath
	 * @return now How much time was spent from the beginning of the game?
	 */
	public Date getTimeElapsed() {
		Date now = new Date();
		this.time_elapsed = now.getTime() - this.start_time;

		now = new Date(time_elapsed);
		return now;
	}

	/**
	 * Returns the stored AntOdourKiller object
	 * 
	 * @author Horvath
	 * @return AntOdourKillerState the current AntOdourKiller object of Game
	 */
	public AntOdourKiller getAntOdourKiller() {

		return this.AntOdourKillerState;
	}

	/**
	 * Returns the stored AntKiller object
	 * 
	 * @author Horvath
	 * @return AntKillerState the current AntKiller object of the Game
	 */
	public AntKiller getAntKiller() {

		return this.AntKillerState;
	}
	
	/**
	 * Replaces the AntKiller for a new one.
	 * 
	 * @author Demarcsek
	 * @param newAntKiller the new AntKiller that should be used instead of the old one.
	 */
	public void setAntKiller(AntKiller newAntKiller) {
		this.AntKillerState = newAntKiller;
	}
	
	/**
	 * Replaces the AntOdourKiller for a new one.
	 * 
	 * @author Demarcsek
	 * @param newAntOdourKiller the new AntOdourKiller that should be used instead of the old one.
	 */
	public void setAntOdourKiller(AntOdourKiller newAntOdourKiller) {
		this.AntOdourKillerState = newAntOdourKiller;
	}

	/**
	 * Returns the current game world (GameWorld object).
	 * 
	 * @author Horvath
	 * @return GameWorldState the current game map, that contains all the elements and fields.
	 */
	public GameWorld getMap() {

		return this.GameWorldState;
	}
	
	
	/**
	 * Indicates whether the Game object has already been initialized before.
	 * 
	 * @return init_done attribute of the class
	 */
	public boolean isInitialized() {
		return this.init_done;
	}
	
	/**
	 * Sets the init_done false, when the game comes to an end. GameOver
	 */
	public void end() {
		this.init_done = false;
	}
}
