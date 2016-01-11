package org.nerdybeans.antfarm.controller;

import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.nerdybeans.antfarm.auxiliary.*;
import org.nerdybeans.antfarm.model.*;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;
import org.nerdybeans.antfarm.view.*;

/**
 * Main controller class for the GUI Application. 
 * The ModelController object (singleton) connects
 * the GUI and the BOM. It also dispatches and transmits
 * user commands (events) between the two components. It
 * conducts communication between model and view elements.
 * @author Demarcsek, Horvath
 **/
public class ModelController {
	public static final int TIMER_SECTION_LENGTH = 30;
	public static final int TIMER_PRECISION = 30;
	
	/**
	 * ModelController single instance 
	**/
	private static ModelController Instance;
	
	/**
	 * A reference to the model object (engine) 
	**/
	private Game EngineRef;
	
	private Thread ModelThread;
	
	private Thread ControlThread;
	
	/**
	 * A reference to the view object (main window)
	**/
	private MainWindow GuiRef;

	private volatile boolean controller_thread_running = true;
	
	/**
	 * Represents model element<->view element bidirectional connections
	 * @author Demarcsek
	 */
	static class Connections {
		private static Hashtable<WorldField, WorldFieldView> ModelToView;
		private static Hashtable<WorldFieldView, WorldField> ViewToModel;
	}
	
	/**
	 * Default constructor for ModelController that initializes ModelController.Connections
	 * @author Demarcsek
	 */
	protected ModelController() {
		/*
		 * Constructing connection stores 
		*/
		ModelController.Connections.ModelToView 
			= new Hashtable<WorldField, WorldFieldView>(
				org.nerdybeans.antfarm.model.GameWorld.SIZE_H * org.nerdybeans.antfarm.model.GameWorld.SIZE_W
			);
		ModelController.Connections.ViewToModel 
		= new Hashtable<WorldFieldView, WorldField>(
			org.nerdybeans.antfarm.model.GameWorld.SIZE_H * org.nerdybeans.antfarm.model.GameWorld.SIZE_W
		);
		
		TimerObservable T = new TimerObservable(ModelController.TIMER_SECTION_LENGTH, ModelController.TIMER_PRECISION);
		Timer.setInstance(T);
	}
	
	/**
	 * (Re-)initialize controller by creating a new game and
	 * model-view element bindings
	 * @author Demarcsek, Horvath
	 * @param game
	 * @param mainwin
	 */
	public void init(Game game, MainWindow mainwin) {
		this.EngineRef = game;
		this.GuiRef = mainwin;
		
		// clear the connections - not really needed
		//Connections.ModelToView.clear();
		//Connections.ViewToModel.clear();
		
		// set the WorldField <-> WorldFieldView connections
		GameWorld world = this.EngineRef.getMap();
		WorldField[][] ModelElems = world.getMap();
		
		WorldFieldView[][] ViewElems = this.GuiRef.getFields();
		
		
		for (int i = 0; i < GameWorld.SIZE_H; ++i) {
			for (int j = 0; j < GameWorld.SIZE_W; ++j) {
				Connections.ModelToView.put(ModelElems[i][j], ViewElems[i][j]);
				Connections.ViewToModel.put(ViewElems[i][j], ModelElems[i][j]);
			}
		}
	}
	
	/**
	 * @author Demarcsek
	 */
	private void saveGame() {
		
		if(this.EngineRef != null && this.ControlThread != null) {
			this.controller_thread_running = false;
			if(!EngineRef.isInitialized()) {
				System.out.println("[DEBUG#ModelController.saveGame] Nothing to save.");
				return;
			}
			
			System.out.println("[DEBUG#ModelController.saveGame] Called");
			System.out.println("[DEBUG#ModelController.saveGame] Controller thread disabled");
			System.out.println("[DEBUG#ModelController.saveGame] Waiting to stop...");
			if(ControlThread.isAlive()) {
				try {
					this.ControlThread.join(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("[DEBUG#ModelController.saveGame] Saving game state to file...");
			this.EngineRef.save();
		}
	}
	
	/**
	 * @author Szabo 
	 */
	private void loadGame() { 
		
		System.out.println("[DEBUG#ModelController.loadGame]");
		
		if(this.ModelThread != null) {
			this.controller_thread_running = false;
			System.out.println("[DEBUG#ModelController.loadGame] Waiting for ContolThread to exit...");
			if(this.ControlThread.isAlive()) {
				try {
					this.ControlThread.join(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("[DEBUG#ModelController.loadGame] Ok");
			
			System.out.println("[DEBUG#ModelController.loadGame] Interrupting ModelThread...");
			if(this.ModelThread.isAlive())
				this.ModelThread.interrupt();
			this.ModelThread = null;
			System.out.println("[DEBUG#ModelController.loadGame] ModelThread killed.");
		}
		
		TimerObservable T = new TimerObservable(ModelController.TIMER_SECTION_LENGTH, ModelController.TIMER_PRECISION);
		Timer.setInstance(T);
		
		//Creating EngineRef for the first time
		this.EngineRef = new Game();
		System.out.println("[DEBUG#ModelController.loadGame] Game object created");
		this.EngineRef.initNew();
		
		//Loading EngineRef
		this.EngineRef.load(); //Loaded!
		System.out.println("Number of WareHouses: " + Warehouse.NUM_OF_WAREHOUSES);
		
		this.init(this.EngineRef, this.GuiRef);
		System.out.println("[DEBUG#ModelController.loadGame] Model-view binds re-created");
		
		System.out.println("[DEBUG#ModelController.loadGame] Timer enabled");
		this.ModelThread = new Thread(Timer.getInstance());
		this.ModelThread.setName("ModelThread");
		System.out.println("[DEBUG#ModelController.loadGame] Timer (ModelThread) created");
		System.out.println("[DEBUG#ModelController.loadGame] Creating ControlThread...");
		this.controller_thread_running = true;
		this.ControlThread = this.createControlThread();
		this.ControlThread.setName("ControlThread");
		System.out.println("[DEBUG#ModelController.loadGame] ControlThread created, starting...");
		this.ControlThread.start();
		
		System.out.println("[DEBUG#ModelController.loadGame] ControlThread started.");
		System.out.println("[DEBUG#ModelController.loadGame] Warming up...");
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timer.getInstance().enable();
		this.startModelThread();
		System.out.println("[DEBUG#ModelController.loadGame] ModelThread started.");
			
	}
	
	/**
	 * @author Demarcsek
	 */
	private void newGame() {
		System.out.println("[DEBUG#ModelController.newGame]");
		if(this.ModelThread != null) {
			this.controller_thread_running = false;
			System.out.println("[DEBUG#ModelController.newGame] Waiting for ContolThread to exit...");
			if(this.ControlThread.isAlive()) {
				try {
					this.ControlThread.join(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("[DEBUG#ModelController.newGame] Ok");
			/*synchronized(Timer.getInstance().Lock) {
				while(true) {
					try {
						Timer.getInstance().Lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				Timer.getInstance().disable();
			}*/
			System.out.println("[DEBUG#ModelController.newGame] Interrupting ModelThread...");
			if(this.ModelThread.isAlive())
				this.ModelThread.interrupt();
			this.ModelThread = null;
			System.out.println("[DEBUG#ModelController.newGame] ModelThread killed.");
		}
		
		TimerObservable T = new TimerObservable(ModelController.TIMER_SECTION_LENGTH, ModelController.TIMER_PRECISION);
		Timer.setInstance(T);
		
	
		this.EngineRef = new Game();
		
		//Timer.getInstance().addSubscriber(this);
		
		System.out.println("[DEBUG#ModelController.newGame] Game object created");
		this.EngineRef.initNew();
		System.out.println("[DEBUG#ModelController.newGame] New game initialized");
		this.init(this.EngineRef, this.GuiRef);
		System.out.println("[DEBUG#ModelController.newGame] Model-view binds re-created");
		
		System.out.println("[DEBUG#ModelController.newGame] Timer enabled");
		this.ModelThread = new Thread(Timer.getInstance());
		this.ModelThread.setName("ModelThread");
		System.out.println("[DEBUG#ModelController.newGame] Timer (ModelThread) created");
		System.out.println("[DEBUG#ModelController.newGame] Creating ControlThread...");
		this.controller_thread_running = true;
		this.ControlThread = this.createControlThread();
		this.ControlThread.setName("ControlThread");
		System.out.println("[DEBUG#ModelController.newGame] ControlThread created, starting...");
		this.ControlThread.start();
		
		System.out.println("[DEBUG#ModelController.newGame] ControlThread started.");
		System.out.println("[DEBUG#ModelController.newGame] Warming up...");
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timer.getInstance().enable();
		this.startModelThread();
		System.out.println("[DEBUG#ModelController.newGame] ModelThread started.");
	}
	
	/**
	 * @author Demarcsek
	 */
	private void startModelThread() {
		if(this.ModelThread != null) {
			this.ModelThread.start();
			System.out.println("[DEBUG#ModelController.newGame] Timer thread started");
		}
	}
	
	
	/**
	 * @author Demarcsek
	 * @param view
	 * @return
	 */
	public synchronized WorldField getModel(WorldFieldView view) {
		return ModelController.Connections.ViewToModel.get(view);
	}
	
	/**
	 * @author Demarcsek
	 * @param model
	 * @return
	 */
	public synchronized WorldFieldView getView(WorldField model) {
		return ModelController.Connections.ModelToView.get(model);
	}
	
	/**
	 * Returns the ModelController object instance
	 * @author Demarcsek
	 * @return The ModelController singleton object instance
	 */
	public static ModelController getInstance() {
		if(ModelController.Instance == null)
			ModelController.Instance = new ModelController();
		
		return ModelController.Instance;
	}
	
	/**
	 * Firing the different Sprays.
	 * @author Szabo
	 * @return void
	 */
	private void fire(Object CommandArgs) {
		
		Object[] args = (Object[]) CommandArgs;
		WorldField field = this.getModel((WorldFieldView)args[1]);
		
		Weapon w = null;
		
		if ( ((MouseEvent) args[0]).getButton() == MouseEvent.BUTTON1 ) { 	//Use the AntKiller!
			w = this.EngineRef.getAntKiller();
		} else { 															//Use the AntOdourKiller!
			w = this.EngineRef.getAntOdourKiller();
		}
		Timer.getInstance().disable();
		try {
			Thread.sleep(1);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		w.triggerAt(field);
		Timer.getInstance().enable();
	}
	
	/**
	 * Generic user command dispatcher
	 * 
	 * Available commands:
	 *  [*] saveGame		Saves the current game
	 *  [*] newGame		Creates a new game
	 *  [*] loadGame		Loads a saved game
	 *  [*] fire			Fires at a WorldField[View] with a Weapon.
	 *  					CommandArgs := <Object>[2], CommandArgs[0] := <MouseEvent>, CommandArgs[1] := <WorldFieldView>
	 *  [*] pauseGame		Pauses the game (disables timer)
	 *  [*] resumeGame	Resumes the game (enables timer)
	 *  [*] killTimer	Kills the Timer
	 * It pops up an error message if the command is not recognized.
	 * @author Demarcsek
	 * @param Command User command identifier string
	 * @param CommandArgs Command arguments
	**/
	public void userCommand(String Command, Object CommandArgs) {
		if("saveGame".equals(Command)) {
			this.saveGame();
		} else if("newGame".equals(Command)) {
			this.newGame();
		} else if("loadGame".equals(Command)) {
			this.loadGame();
		} else if("fire".equals(Command)) {
			this.fire(CommandArgs);
		} else if("pauseGame".equals(Command)) {
			Timer.getInstance().disable();
		} else if("resumeGame".equals(Command)) {
			Timer.getInstance().enable(); 
		} else if("killTimer".equals(Command)) {
			Timer.getInstance().kill();
		} else {
			JOptionPane.showMessageDialog(null,
				    "Invalid user command: " + Command,
				    "Warning (ModelController)",
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Also starts model thread
	 * @author Demarcsek
	 */
	public Thread createControlThread() {
		//if(this.ControlThread != null) return this.ControlThread;
		this.ControlThread = new Thread() {
			public void run() {
				//System.out.println("[DEBUG#ModelController.run] ");
				while(true) {
					if((GuiRef != null) && (EngineRef != null) && (EngineRef.isInitialized())) {
						//System.out.println("[DEBUG#ModelController.run] Waiting for ModelThread to finish...");
						try {
							synchronized(Timer.getInstance().Lock) {
								Timer.getInstance().Lock.wait();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println("[DEBUG#ModelController.run] ModelThread finished");
						if(!controller_thread_running) break;
						//ModelThread = null;
						
						//System.out.println("[DEBUG#ModelController.run] Updating graphics...");
						GuiRef.updateGraphics();
						Timer.getInstance().enable();
						if((Warehouse.isEndOfGame())) {
							System.out.println("!!!EndOfGame");
							Warehouse.reset();
							Warehouse.NUM_OF_WAREHOUSES = 0;
							EngineRef.end();
							
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							GuiRef.updateGraphics();
							GuiRef.updateGraphics();
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							SwingUtilities.invokeLater(new Runnable(){public void run(){ JOptionPane.showMessageDialog(null, "Game over (time elapsed: " + (EngineRef.getTimeElapsed().getMinutes() + "min " + EngineRef.getTimeElapsed().getSeconds()) + "sec)" ); }});
							Timer.getInstance().disable();
							controller_thread_running = false;
						}
					}
					try {
						Thread.sleep(10);
					} catch(InterruptedException e) {
						System.out.println("[DEBUG#ControlThread.run] Async interrupt in Timer thread");
					}
				}
			}
		};
		
		return this.ControlThread;
		
		//System.out.println(GameSerializer.serialize(this.EngineRef));
		
		/*Iterator<Map.Entry<WorldField, WorldFieldView>> it = Connections.ModelToView.entrySet().iterator();
		
		while (it.hasNext()){
			Map.Entry<WorldField, WorldFieldView> entry = it.next();
			WorldField field = entry.getKey();
			WorldFieldView fieldView = entry.getValue();
			
			// clear the stack
			fieldView.clearElements();
			
			// inquire the elements of the field
			Irreplaceable irr = field.getPermanentElement();
			Replaceable re = field.getDynamicElement();
			AntEater ae = field.getAntEater();
			ListIterator<Ant> ants = field.getAntIterator();
			
			// push the elements that are not null to the stack
			if (irr != null){
				fieldView.addElement(irr);
			}
			if (re != null){
				fieldView.addElement(re.getView());
			}
			if (ae != null){
				fieldView.addElement(ae.getView());
			}
			if (ants != null){
				while (ants.hasNext()){
					Ant a = ants.next();
					fieldView.addElement(a.getView());
				}
			}
		}*/
		
		// update the display of the WorldFieldViews
		
		
	}
	
	
	public int getAntKillerLoad() {
		if(this.EngineRef != null)
			return this.EngineRef.getAntKiller().getLoad();
		else
			return -1;
	}
	
	public int getAntOdourKillerLoad() {
		if(this.EngineRef != null)
			return this.EngineRef.getAntOdourKiller().getLoad();
		else
			return -1;
	}
}
