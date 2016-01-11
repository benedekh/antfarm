package org.nerdybeans.antfarm.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.nerdybeans.antfarm.auxiliary.*;
import org.nerdybeans.antfarm.model.*;
import org.nerdybeans.antfarm.model.modelelements.Ant;
import org.nerdybeans.antfarm.model.modelelements.AntEater;
import org.nerdybeans.antfarm.model.modelelements.AntHill;
import org.nerdybeans.antfarm.model.modelelements.AntLion;
import org.nerdybeans.antfarm.model.modelelements.Pebble;
import org.nerdybeans.antfarm.model.modelelements.Puddle;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;

/**
 * MAIN CLASS OF PROTOTYPE
 * 
 * The main tasks of this class:
 * 1, Interpretation of the user commands
 * 2, Command manipulation performed by through game functions 
 * @author David, Demarcsek
 * @verison 0.1
**/
public class Prototype extends PrototypeCLI {
	/// Default section length for the timer
	public static final int TIMER_SECTION_LENGTH = 1000;
	
	/**
	 * An array of accepted user commands
	**/
	private static final String[] ValidCmds = new String[] {
		"set", "init", "save", "load", "show", "dump", "leave",
		"run", "test", "eval", "edit", "map", "fire", "help", "quit"
	};
	
	/**
	 * Prompt string
	 */
	private static final String PROMPT = "> ";
	
	/**
	 * Prototype environment variables 
	**/
	private HashMap< String, String > EnvVars;
	
	/**
	 * Current Timer instance that iterates the game algorithm
	**/
	private Timer GameConductor;
	
	/**
	 * Latest command word from the user
	**/
	private String lastCmd;
	
	/**
	 * Latest command line from the user
	**/
	private String lastCmdLine;
	
	/**
	 * A list of the latest command arguments
	**/
	private Queue<String> lastParams;
	
	/**
	 * A reference to a Tester object to use
	**/
	private Tester TestConductor;
	
	//private TimerConnector EngineInterface;
	
	/**
	 * The result of the latest test run
	**/
	private boolean TestResult;
	
	
	/**
	 * Indicates if the CLI should run or not 
	**/
	private boolean IsRunning;
	
	/**
	 * Current test case to examine, edit or run
	**/
	private TestCase TestUnit;
	
	/**
	 *  User mode type
	 *  Currently, there are 2 different user modes:
	 *   'Command': execute commands
	 *   'EditTest': edit a test case (the typed command lines are considered as commands for the test case)
	**/
	private enum Mode {
		Command,
		EditTest
	};
	
	/**
	 * The default user mode is 'command execute' 
	**/
	private Mode UserMode = Mode.Command;
	
	/**
	 * # Of game iterations run since the last init
	**/
	private int GlobalIterations;
	
	/**
	 * Default constructor
	 * 
	 * Prints welcome text, initializes local variables, sets environment variables to
	 * their default values.
	 * 
	 * @author Demarcsek
	**/
	public Prototype() {
		this.EnvVars = new HashMap<String, String>();
		this.IsRunning = true;
		this.GlobalIterations = 0;
		String S =		//Very nice welcome image! Hope u like it! :)
				"  NerdyBeans   AntFarm " + GameSerializer.LINE_SEPARATOR +
				"       \\/      \\/" + GameSerializer.LINE_SEPARATOR +
				" ___  _@@       @@_  ___ " + GameSerializer.LINE_SEPARATOR +
				"(___)(_)         (_)(___) " + GameSerializer.LINE_SEPARATOR +
				"//|| || Prototype || ||\\\\ ";
		System.out.println(S);
		System.out.println("----------------------------");
		System.out.println("Initializing...");
		S = null;
		try {
			this.parseAndRun("set def"); //sets the values to default
		} catch(PrototypeCLIException e) { //catching exception
			System.err.println("*** CRITICAL ERROR ***"); //haha, not so good
			System.err.println(e.getMessage());
		}
		System.out.println("Done.");
	}

	/**
	 * Attempts to parse a command line and run the specified command.
	 * 
	 * In case of any problem in connection with parsing or running,
	 * it thwos a PrototypeCLIException instance. Executing a command
	 * involves calling a private c[Command] method that implements
	 * the required functionality. (Note that these calls are synchronous,
	 * no background jobs.) The method only accepts a valid command.
	 * Valid commands are located in Prototype.ValidCmds.
	 * 
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	 * @see ValidCmds
	**/
	public void parseAndRun( String CommandLine ) throws PrototypeCLIException {
		assert CommandLine != null;
		
		if("".equals(CommandLine))	// ENTERs are legal...:)
			return;
		
		StringTokenizer Tokenizer = new StringTokenizer(CommandLine, " "); 
		if( Tokenizer.hasMoreTokens() ) { //gives the nex token
			String CommandWord = Tokenizer.nextToken();
			
			boolean IsValid = false;
			for(String valid : Prototype.ValidCmds) { //iterating through the list of valid commands
				if(valid.equals(CommandWord)) {	
					IsValid = true;
					break;
				}
			}
			
			if(!IsValid) //when the command is invalid
				throw new InvalidCmdException( "Unrecognized command (NOTE: command parsing is case-sensitive)" );
			
			this.lastCmd = CommandWord;
			
			
			this.lastParams = new LinkedList<String>();
			
			while(Tokenizer.hasMoreElements()) {
				this.lastParams.add(Tokenizer.nextToken());
			}
			
			try { //switch case for the availabe commands
				if(CommandWord.equals("init")) {
					cInit(); //calling cInit method
				} else if(CommandWord.equals("set")) {
					cSet(); //calling cSet method
				} else if(CommandWord.equals("save")) {
					cSave(); //calling cSave method
				} else if(CommandWord.equals("load")) {
					cLoad(); //calling cLoad method
				} else if(CommandWord.equals("show")) {
					cShow(); //calling cShow method
				} else if(CommandWord.equals("dump")) {
					cDump(); //calling cDump method
				} else if(CommandWord.equals("leave")) {
					cLeave(); //calling cLeave method
				} else if(CommandWord.equals("run")) {
					cRun(); //calling cRun method
				} else if(CommandWord.equals("test")) {
					cTest(); //calling cTest method
				} else if(CommandWord.equals("eval")) {
					cEval(); //calling cEval method
				} else if(CommandWord.equals("edit")) {
					cEdit(); //calling cEdit method
				} else if(CommandWord.equals("map")) {
					cMap(); //calling cMap method
				} else if(CommandWord.equals("fire")) {
					cFire(); //calling cFire method
				} else if(CommandWord.equals("help")) {
					cHelp(); //calling cHelp method
				} else if(CommandWord.equals("quit")) {
					cQuit(); //calling cQuit method
				} else {
					System.err.println("*** CRITICAL ERROR ***");
				}
			} catch(IOException IOe) {
				System.err.println("I/O Error: " + IOe.getMessage());
			}
		} else {
			throw new InvalidCmdException( "Invalid command format" );
		}
	}
	
	/**
	 * Runs the command line interface until exit condition is satisfied (the user executes the quit command)
	 * 'Run' means:
	 *  1) Print prompt
	 *  2) Read user input
	 *  3) Parse and run command
	 *  4) Print error message if needed
	 *  5) Repeat from 1) if IsRunning == true, stop otherwise
	 * 
	 * @author Demarcsek
	 * @throws IOException
	**/
	public void runCLI() throws IOException {
		
		BufferedReader CLIReader = new BufferedReader( new InputStreamReader( System.in ) );
		
		while(IsRunning) {
			if((this.TestUnit != null) && (this.UserMode == Mode.EditTest)) {
				if(! ("".equals(this.TestUnit.getName())))
					System.out.print("(" + this.TestUnit.getName() + ") ");
			} else if(this.Context != null) {
				System.out.print("(Game" + this.Context.hashCode() + ") ");
			}
			System.out.print(Prototype.PROMPT);
			String CL = CLIReader.readLine();
			this.lastCmdLine = CL;
			try {
				//if(this.UserMode == Mode.Command) 
					this.parseAndRun(CL);
				/*else if(this.UserMode == Mode.EditTest) {
					assert this.TestUnit != null;
					if(this.lastCmd.startsWith("edit test end"))
						this.UserMode = Mode.Command;
					else
						this.TestUnit.addCommand(CL);
				}*/
			} catch(PrototypeCLIException Error) {
				System.err.println(" ! " + Error.getMessage());
			}
		}
		
		CLIReader.close();
	}
	
	/**
	 * The set command can be used to adjust the values of 
	 * environment variables. Environment variables effect
	 * essential behaviours of the engine and/or the test 
	 * framework. 
	 * 
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cSet() throws PrototypeCLIException {
		if(this.UserMode == Mode.EditTest) {
			this.TestUnit.addCommand(this.lastCmdLine);
			return;
		}
		if(lastParams.size() == 0)
			throw new InvalidArgumentException("Missing arguments");
		
		ArrayList<String> Modified = new ArrayList<String>();
		
		String lastKey = "";
		String lastValue = "";
		if(lastParams.element().equals("def")) {
			Modified.add("weapon_antkiller_load");
			Modified.add("weapon_antodourkiller_load");
			Modified.add("ant_next_move_dir");
			Modified.add("anteater_next_move_dir");
			Modified.add("ant_respawn_field");
			Modified.add("anteater_respawn_field");
			Modified.add("dump_period");
			
			EnvVars.put("weapon_antkiller_load", "10");				// done
			EnvVars.put("weapon_antodourkiller_load", "15");			// done
			EnvVars.put("ant_next_move_dir", "rand");				// done
			EnvVars.put("anteater_next_move_dir", "rand");			// done
			// may be used...
			EnvVars.put("ant_respown_field", "rand");
			EnvVars.put("anteater_respawn_field", "rand");
			
			// -------------- Never used shit ----------------
			//  (date: 2013/04/22)
			//EnvVars.put("warehouse1_food", "10");					// why???
			//EnvVars.put("warehouse2_food", "10");
			//EnvVars.put("warehouse3_food", "10");
			// -----------------------------------------------
			
			EnvVars.put("dump_period", "10");
			System.out.println("Variables set to default");
		} else {
			
			if(lastParams.size() >= 2) { //we need at least 2 parameters
				lastKey = lastParams.poll(); //key
				lastValue = lastParams.poll(); //value
				if(lastKey.equals("") || lastValue.equals("")) {
					throw new InvalidArgumentException( "Empty string???" );
				}
				if(!EnvVars.containsKey(lastKey)) {
					System.out.println("Key does not exist!");
					return;
				}
				Modified.add(lastKey);
				EnvVars.put(lastKey, lastValue);
				System.out.println( lastKey + " -> " + lastValue );
				// TODO Continue this shit...
			} else {
				throw new MissingArgumentException( "Missing argument(s). Two arguments required: key, value" );
			}
		}
		// TODO Refresh model here according to environment changes
		if(this.Context == null)
			return;
		
		try {
			for(String key : Modified) {
				lastKey = key;
				if(key.equals("weapon_antkiller_load")) //modifing the load of the antkiller
					this.Context.getAntKiller().setLoad(Integer.parseInt(EnvVars.get(key)));
				else if(key.equals("weapon_antodourkiller_load")) //modifing the load of the antodourkiller
					this.Context.getAntOdourKiller().setLoad(Integer.parseInt(EnvVars.get(key)));
				else if(key.equals("ant_next_move_dir")) { //modifing the dir
					int dir = Integer.parseInt(EnvVars.get(key));
					for(int i = 0; i < GameWorld.SIZE_H; ++i) {
						for(int j = 0; j < GameWorld.SIZE_W; ++j) {
							ListIterator<Ant> Ants = this.Context.getMap().getMap()[i][j].getAntIterator();
							while(Ants.hasNext()) {	//getting ants for modifing
								Ant CurrEnt = Ants.next(); // :)
								CurrEnt.setNextMove(
									dir
								);
								// Looks naughty?? Duck me, right?
							}
						}
					}
				} else if(key.equals("anteater_next_move_dir")) {
					int dir = Integer.parseInt(EnvVars.get(key));
					for(int i = 0; i < GameWorld.SIZE_H; ++i) {
						for(int j = 0; j < GameWorld.SIZE_W; ++j) {
							AntEater AntEaterObj = this.Context.getMap().getMap()[i][j].getAntEater();
							if(AntEaterObj != null) //giving dir to an ant
								AntEaterObj.setNextMove(
									dir
								);
						}
					}
				}
			}
		} catch (NumberFormatException NaN) { //we need number, integer!
			if(!EnvVars.get(lastKey).equals("rand"))
				throw new InvalidArgumentException( "Integer expected: " + lastKey );
		}
	}
	
	/**
	 * Initialises a new game context
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cInit() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) { //this command is not available in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		this.GlobalIterations = 0;
		
		this.GameConductor = Timer.getInstance();
		this.GameConductor.reset();
		
		this.Context = new Game();
		System.out.println("Game object created");
		//this.GameConductor = new TimerObservable(TIMER_SECTION_LENGTH);
		
		//this.EngineInterface = new TimerObservableConnector(this.GameConductor, this.Context);
		//this.EngineInterface.connect();
		System.out.println("Timer connected");
		//this.Context.initNew();
		System.out.println("Ready.");
	}
	
	/**
	 * Saves the current context to a text file
	 * @author David
	 * @throws PrototypeCLIException
	**/
	private void cSave() throws PrototypeCLIException  {
		
		if(this.UserMode == Mode.EditTest) { //can't use "save" command in test mode!
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}		
		if(Context != null){ //Need a context to save...
			if(lastParams.size() >= 1) {
				String Name = lastParams.poll(); //returns the given parameter after "save"
				
				StringBuffer SerializedData = new StringBuffer();
				SerializedData.append(Game.FILE_BANNER+GameSerializer.LINE_SEPARATOR);//(cLoad() needs it)
				SerializedData.append(GameSerializer.serialize(Context)); //serializing Context				
						
				try {
					System.out.println("Saving context to file..."); //saving... :D
					BufferedWriter out = new BufferedWriter(new FileWriter(Name+".txt"));
					String outText = SerializedData.toString();
					out.write(outText); //writing to file
					System.out.println("File saved: " + Name + ".txt");
					out.close();		//closing bufferedwriter			  
				}
				catch (IOException e){
					e.printStackTrace();
				}
				
			} else { //not enough parameter
				throw new InvalidArgumentException( "Syntax: save [name]" );
			}
		}else{
			System.out.println("Context is not initialized yet.");
		}	
	}
	
	/**
	 * Loads context or test case from file
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cLoad() throws PrototypeCLIException, IOException  {
		if(this.UserMode == Mode.EditTest) { //this command is not available in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		if(this.lastParams.size() >= 1) {
			String LoadableName = this.lastParams.poll();
			File LoadableFile = new File(LoadableName + ".txt");
			if(LoadableFile.exists()) {
				BufferedReader FileChannel = new BufferedReader(
					new FileReader( LoadableFile )
				);
				String FirstLine = FileChannel.readLine();
				StringBuffer Lines = new StringBuffer();
				String Line = "";
				while((Line = FileChannel.readLine()) != null) {
					if(Line.startsWith("#"))		// # = comment
						continue;
					Lines.append(Line + GameSerializer.LINE_SEPARATOR);
				}	
				Line = null;
				FileChannel.close();
				if(FirstLine.startsWith(Game.FILE_BANNER)) {
					System.out.println("Loading Game object...");
					this.GameConductor = Timer.getInstance();
					this.GameConductor.reset();
					Game LocalGameObj = GameSerializer.unserialize(Lines);
					if(LocalGameObj == null) {
						System.out.println("Invalid file format");
						System.out.println("Autorun init...");
						cInit();
						throw new InvalidCmdException(GameSerializer.getLastErrorMsg());
					} else {
						
						
						this.changeContext(LocalGameObj);
						
						System.out.println("Done.");
					}
				} else if(FirstLine.startsWith(TestCase.FILE_BANNER)) {
					System.out.println("Loading TestCase object...");
					// TODO Write test case file load
					Scanner LineScanner = new Scanner( Lines.toString() );
					int ObjectId = 0; // 0 => Initial
									  // 1 => Expected
									  // 2 => Commands
					String InitialGameStateStr = "";
					String ExpectedGameStateStr = "";
					ArrayList<String> FrameworkCommands = new ArrayList<String>();
					// Please note that Scanner.nextLine() does not return line separators
					while(LineScanner.hasNextLine()) {
						Line = LineScanner.nextLine();
						if(Line.startsWith("#"))	// comment again
							continue;
						
						if( "".equals(Line) ) {
							ObjectId++;
						} else if(ObjectId == 0) { //setting seperators
							InitialGameStateStr += (Line + GameSerializer.LINE_SEPARATOR); 
						} else if(ObjectId == 1) {
							ExpectedGameStateStr += (Line + GameSerializer.LINE_SEPARATOR);
						} else if(ObjectId == 2) {
							FrameworkCommands.add(Line);
						}
					}
					System.out.println("Loading: Initial ...");
					this.GameConductor = Timer.getInstance();
					this.GameConductor.reset();
					Game InitialGameStateObj = GameSerializer.unserialize(new StringBuffer(InitialGameStateStr));
					if(InitialGameStateObj == null) { //unserialising
						System.out.println("Autorun init..."); //running
						cInit();
						throw new PrototypeCLIException("Error loading (test) Game object: " + GameSerializer.getLastErrorMsg() );
					}
					System.out.println("Loading: Expected ...");
					
					//this.GameConductor.reset();
					Timer.getInstance().disable(); //disabling timer
					Game ExpectedGameStateObj = GameSerializer.unserialize(new StringBuffer(ExpectedGameStateStr));
					Timer.getInstance().enable(); //enabling timer
					if(ExpectedGameStateObj == null)
						throw new PrototypeCLIException("Error loading (test) Game object: " + GameSerializer.getLastErrorMsg() );
					System.out.println(FrameworkCommands.size() + " command(s) loaded");
					this.TestUnit = new TestCase(LoadableName);
					this.TestUnit.setInitialState(InitialGameStateObj);
					this.TestUnit.setExpectedState(ExpectedGameStateObj);
					this.TestUnit.setCommands(FrameworkCommands);
					
					if( this.TestConductor == null )
						this.TestConductor = new Tester(this);
					
					
					//this.EngineInterface = new SimpleTimerGameConnector();
					
					
					System.out.println("Done.");
					//throw new PrototypeCLIException("Feature not implemented yet.");
				} else {
					System.out.println("Invalid file format");
				}
				if(GameSerializer.getLastErrorMsg() != null)
					System.out.println(" (Warning: " + GameSerializer.getLastErrorMsg() + ")");
			} else {
				throw new InvalidArgumentException( "No such file: " + LoadableFile.getPath() );
			}
		} else {
			throw new MissingArgumentException( "Missing argument: name" );
		}
		
		
	}
	
	/**
	 * Shows the variables and their values or 
	 * Prints the current number of iteration
	 * 
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cShow() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) { //this command is not available in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		//if(lastParams.size() >= 1) {
			String What = lastParams.poll();
			if(What == null)
				What = "";
			
			if(What.equals("vars")) {
				for(String key : EnvVars.keySet()) {
					System.out.println( key + " -> " + EnvVars.get(key) );
				}
			} else if(What.equals("iter")) {
				if(this.GameConductor != null) {
					System.out.println("Iterations run since the last init: " + this.GlobalIterations);
					System.out.println("Timer [ObjID=" + this.GameConductor.hashCode() + "]");
					System.out.println(" curent_tick = " + this.GameConductor.getTick());
				} else {
					System.out.println("Timer component is not connected.");
				}
			} else {
				// Easter egg :)
				System.out.println("AntFarm Prototype");
				System.out.print("Engine (Game) Instance (Context): ");
				if(this.Context != null) 
					System.out.println("loaded (" + this.Context.hashCode() + ")");
				else
					System.out.println("not loaded yet");
				System.out.print("Timer Instance (GameConductor): ");
				if(this.GameConductor != null)
					System.out.println("loaded (" + this.GameConductor.hashCode() + ")");
				else
					System.out.println("not loaded yet");
				System.out.print("Timer-Game-Connector Instance (EngineInterface): ");
				/*if(this.EngineInterface != null) {
					System.out.print("loaded (" + this.EngineInterface.hashCode() );
					if(this.EngineInterface.getGame() != null)
						System.out.print(" G=" + this.EngineInterface.getGame().hashCode());
					else
						System.out.print(" G=null");
					if(this.EngineInterface.getTimer() != null)
						System.out.print(" T=" + this.EngineInterface.getTimer().hashCode());
					else
						System.out.print(" T=null");
					System.out.println(")");
				} else {
					System.out.println("not loaded yet");
				}*/
				//throw new InvalidArgumentException( "No such data" );
			}
		//} else {
		//	throw new InvalidArgumentException( "Syntax: show [vars|iter]" );
		//}
	}
	
	/**
	 * Prints the current context to the screen, with a beautiful, readable map
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	 * @version 0.2
	**/
	private void cDump() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) { //this command is not avaibable in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		if( this.Context != null ) { //need a running context!
			StringBuffer Output = GameSerializer.serialize(this.Context);
			if( Output != null ) { //need an output
				System.out.println("Dump of " + this.Context.toString() + ":");
				for(char c : Output.toString().toCharArray()) {
					if(c == GameSerializer.LINE_SEPARATOR.charAt(0))
						System.out.println();
					else System.out.print(c);
				}
				System.out.println();
				System.out.println("End of dump");
				System.out.println();
			} else {
				System.out.println("Serialization failed.");
			}
		} else {
			System.out.println("Context is not initialized yet.");
		}
	}
	
	/**
	 * Leaves the current context
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cLeave() throws PrototypeCLIException, IOException  {
		if(TestUnit != null) {
			if(this.UserMode == Mode.EditTest) { //this command is available in edit test mode
				// TODO Save test file
				System.out.println("Saving test file..."); //saving before leaving
				BufferedWriter Channel = new BufferedWriter( new FileWriter( this.TestUnit.getName() + ".txt") );
				Channel.write(this.TestUnit.toString());
				Channel.newLine();
				Channel.close();
				System.out.println("File saved: " + this.TestUnit.getName() + ".txt");
				this.UserMode = Mode.Command;
			}
			// Leave context
			System.out.println("Unloading TestUnit...");
			this.TestUnit = null;
		}
		else if(this.Context != null) {
			System.out.println("Unloading Context...");
			this.Context = null;
		}	
		
		//this.EngineInterface = null;
		this.GameConductor = null;
	}
	
	/**
	 * Runs the given iterations of the game
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cRun() throws PrototypeCLIException, IOException  {
		if(this.UserMode == Mode.EditTest) {
			this.TestUnit.addCommand(this.lastCmdLine);
			return;
		}
		if( this.lastParams.size() >= 1 ) { //need at least 1 parameter -> how many iterations
			if((this.GameConductor != null) && (this.Context != null) /*&& (this.EngineInterface != null)*/) {
				int NumOfIterations = 0;
				try {
					NumOfIterations = Integer.valueOf( this.lastParams.poll() );
				} catch(NumberFormatException NotNumber) { //need an integer
					throw new InvalidArgumentException("Integer expected");
				}
				int DumpPeriod = Integer.MAX_VALUE-1;
				try {
					DumpPeriod = Integer.valueOf(EnvVars.get("dump_period"));
				} catch(NullPointerException NoValue) {
					System.out.println("Environment variable 'dump_period' is not set. (Critical error??) ");
					System.out.println("Assuming dump_period = infinite");
				} catch(NumberFormatException NotNumber) { //need a number
					System.out.println("dump_period = NaN");
					System.out.println("Assuming dump_period = infinite");
				}

				System.out.println("Executing " + NumOfIterations + " iterations...");
				while(NumOfIterations > 0) {
					this.GameConductor.tick();
					NumOfIterations--;
					this.GlobalIterations++;
					
					if(DumpPeriod > 0)
						if((NumOfIterations % DumpPeriod) == 0) {
							System.out.println("Object dump (iteration=" + NumOfIterations + ", tick=" + this.GameConductor.getTick() + ")");
							this.cDump();
							System.out.println("(Hit ENTER)"); //yea yea, hit hit hit hit!
							System.in.read();
						}
				}
				System.out.println("Done.");
			} else {
				System.out.println("Uninitialized or inconsistent state. Try run init");
			}
		} else {
			throw new MissingArgumentException("Missing argument: iter");
		}
	}
	
	/**
	 * Runs the loaded test case
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cTest() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) { //this command is not available in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		if(this.TestConductor == null) { //need a test framework initialized first
			System.out.println("Test Framework not initialized yet...try to load a test case");
			return;
		}
		if(this.TestUnit != null) { //or a testunit
			//this.EngineInterface.connect(this.GameConductor, this.TestUnit.getInitialState());
			TestResult = TestConductor.runTest(this.TestUnit);
			System.out.println("Done.");
		} else {
			System.out.println("No selected test case. Tip: load a test case using load");
		}
	}
	
	/**
	 * Evaluates the latest runned test and prints the results
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cEval() throws PrototypeCLIException {
		if(this.UserMode == Mode.EditTest) { //this command is not available in edit test mode
			throw new InvalidCmdException("Command not available in 'edit test' mode");
		}
		if(this.TestUnit != null) { //RESULT?
			System.out.println("Result: " + (TestResult ? "success" : "failure"));
		} else {
			System.out.println("No test result available");
		}
	}
	
	/**
	 * Edits (or creates) test case
	 * @author Demarcsek
	 * @throws PrototypeCLIException
	**/
	private void cEdit() throws PrototypeCLIException, IOException  {
		if(this.lastParams.size() >= 4) { //we need at least 4 parameters
			String What = lastParams.poll();
			if(What.equals("test")) { //first parameter
				if(this.UserMode == Mode.EditTest) {
					new InvalidCmdException("Command not available in 'edit test' mode");
				} else {
					
					
					String InitialStateName = lastParams.poll(); //2nd -> initial state
					String ExpectedStateName = lastParams.poll(); //3rd -> end state
					String TestCaseName = lastParams.poll(); //4th -> name				- 
					
					Game Original = this.Context;
					
					System.out.println("Loading initial state...");
					
					this.lastParams.add(InitialStateName);
					cLoad();
					Game InitialState = this.Context;
					
					System.out.println("Loading expected state...");
					lastParams.add(ExpectedStateName);

					cLoad();


					Game ExpectedState = this.Context;
					
					this.Context = Original;
					
					System.out.println("Creating TestCase object...");
					this.TestUnit = new TestCase(TestCaseName);
					this.TestUnit.setInitialState(InitialState);
					this.TestUnit.setExpectedState(ExpectedState);
					
					
					
					this.UserMode = Mode.EditTest;
					System.out.println("Done. Waiting for commands");
					
				}
			} /*else if(What.equals("end")) {
				if(this.UserMode == Mode.EditTest) {
					System.out.println("Saving test file...");
					BufferedWriter Channel = new BufferedWriter( new FileWriter( this.TestUnit.getName() + ".txt") );
					Channel.write(this.TestUnit.toString());
					Channel.newLine();
					Channel.close();
					System.out.println("File saved: " + this.TestUnit.getName());
					this.UserMode = Mode.Command;
				} else {
					System.out.println("Nothing to end. :)");
				}
			}*/
		} else { //not enough arguments
			throw new MissingArgumentException("Missing arguments.");
		}
	}
	
	/**
	 * Gives commands to the user to edit the map manually
	 * @author David
	 * @throws PrototypeCLIException
	 */
	private void cMap() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) {
			this.TestUnit.addCommand(this.lastCmdLine);
			return;
		}
		if(Context != null)
		{
			if(lastParams.size() >= 1) {
				String What = lastParams.poll();
			
				if(What.equals("generate")) {
					Context.getMap().doGenesis();	//generates random world 
					
				} else if(What.equals("remove")) {
					int x=-1, y=-1;
					
					try{
						x = Integer.valueOf(lastParams.poll()); //to get the x coordinate
						y = Integer.valueOf(lastParams.poll()); //to get the y coordinate
					}catch(NumberFormatException NotNumber)
					{	//when user types not an integer...
						throw new InvalidArgumentException("[x] [y] -> Integer expected");
					}
					
					if(0<=x && x<20 && 0<=y && y<20){	// removes the top element from the field
						
						ListIterator<Ant> AntIt = Context.getMap().getMap()[x][y].getAntIterator();
						while(AntIt.hasNext()){ //removing ants
							AntIt.next();
							AntIt.remove();
						}

						Context.getMap().getMap()[x][y].setPermanentElement(null); // removes permelements
						if(!Context.getMap().getExitPoints().isEmpty())			// safety check
							Context.getMap().getExitPoints().remove(Context.getMap().getExitPoints().size()-1);
						Context.getMap().getMap()[x][y].removeAntEater(); // removes anteater						
						Context.getMap().getMap()[x][y].setDynamicElement(null); // removes dynelements					
						Context.getMap().getMap()[x][y].setAntOdour(0); //sets antodour to 0
						Context.getMap().getMap()[x][y].setFoodOdour(0); //sets foododour to 0
						Context.getMap().getMap()[x][y].setPoison(0); //sets poison to 0
					}
					else{
						throw new InvalidArgumentException("[x] and [y] should be between 0 and 19!");
					}
					
				} else if(What.equals("add")) {
					
					String element = lastParams.poll(); //available addings
					String[] all = {"ant","antlion","anteater","anthill","warehouse","puddle","pebble","antodour","foododour","poison"};
					ArrayList<String> elements = new ArrayList<String>();
					for(int i=0;i<all.length;i++)
						elements.add(all[i]);
					
					int x=-1, y=-1;		
					int o = -1;
					try{
						x = Integer.valueOf(lastParams.poll()); //to get the x coordinate
						y = Integer.valueOf(lastParams.poll()); //to get the y coordinate
						if(lastParams.size() != 0) 
							o = Integer.valueOf(lastParams.poll());
					}catch(NumberFormatException NotNumber)
					{	//when user types not an integer...
						throw new InvalidArgumentException("Integer expected");
					}
					//0->19, 0->19
					if(0<=x && x<GameWorld.SIZE_H && 0<=y && y<GameWorld.SIZE_W){
						
						switch(elements.indexOf(element)){ //which element to add
						case 0:
							Context.getMap().getMap()[x][y].addAnt(new Ant());
							break;
						case 1:
							Context.getMap().getMap()[x][y].setPermanentElement(new AntLion());
							break;
						case 2:
							Context.getMap().getMap()[x][y].addAntEater(new AntEater());
							break;
						case 3:
							Context.getMap().getMap()[x][y].setPermanentElement(new AntHill());
							break;
						case 4:
							Warehouse wh = new Warehouse();
							Context.getMap().getMap()[x][y].setPermanentElement(wh); //add to worldfield
							Context.getMap().getExitPoints().add(wh); //need to add to exitpoint list, too!
							break;
						case 5:
							Context.getMap().getMap()[x][y].setPermanentElement(new Puddle());
							break;
						case 6:
							Context.getMap().getMap()[x][y].setDynamicElement(new Pebble());
							break;
						case 7:
							Context.getMap().getMap()[x][y].setAntOdour(o);
							break;
						case 8:
							Context.getMap().getMap()[x][y].setFoodOdour(o);
							break;
						case 9:
							Context.getMap().getMap()[x][y].setPoison(o);
							break;
						default:
							throw new InvalidArgumentException( "No such element" );						
						}
					}
					else{
						throw new InvalidArgumentException("[x] and [y] should be between 0 and 19!");
					}
					
				}
				else {
					throw new InvalidArgumentException( "No such data" );
				}
			} else { //syntax info
				throw new InvalidArgumentException( "Syntax:" + GameSerializer.LINE_SEPARATOR +
						"	    map add [ant|antlion|anteater|anthill|+" +
											"warehouse|puddle|pebble] [x] [y] or" + GameSerializer.LINE_SEPARATOR +
						"	    map remove [x] [y] or\n" +
						"	    map generate" );
			}
		}else{ //need context to add elements
			System.out.println("Context is not initialized yet.");
		}	
	}
	
	/**
	 * Use of antkiller or antodourkiller
	 * 
	 * @author David
	 * @throws PrototypeCLIException
	 */
	private void cFire() throws PrototypeCLIException  {
		if(this.UserMode == Mode.EditTest) {	//user can use "fire" command in "test edit" mode!
			this.TestUnit.addCommand(this.lastCmdLine);
			return;
		}
		if(lastParams.size() >= 3) {
			String What = lastParams.poll();
			int x=-1, y=-1;
			
			try{
				x = Integer.valueOf(lastParams.poll()); //to get the x coordinate
				y = Integer.valueOf(lastParams.poll()); //to get the y coordinate
			}catch(NumberFormatException NotNumber)
			{	//when user types not an integer...
				throw new InvalidArgumentException("[x] [y] -> Integer expected");
			}
			if(What.equals("antkiller")) {
				
				if(0<=x && x<GameWorld.SIZE_H && 0<=y && y<GameWorld.SIZE_W){	//triggers antkiller at the given coordinates			
					this.Context.getAntKiller().triggerAt(this.Context.getMap().getMap()[x][y]);			
				}
				else{
					throw new InvalidArgumentException("[x] and [y] should be between 0 and 19!");
				}
				
			} else if(What.equals("antodourkiller")) {
				
				if(0<=x && x<GameWorld.SIZE_H && 0<=y && y<GameWorld.SIZE_W){ //triggers antodourkiller at the given coordinates				
					this.Context.getAntOdourKiller().triggerAt(this.Context.getMap().getMap()[x][y]);			
				}
				else{
					throw new InvalidArgumentException("[x] and [y] should be between 0 and 19!");
				}
				
			} else {
				throw new InvalidArgumentException( "No such data" );
			}
		} else {
			throw new InvalidArgumentException( "Syntax: fire [antkiller|antodourkiller] [x] [y]" );
		}
		
	}
	
	/**
	 * Prints the available commands
	 * 
	 * @author David
	 * @throws PrototypeCLIException
	 */
	private void cHelp() throws PrototypeCLIException  {
		System.out.println(" Available commands:"); //command name, and than a short description
		System.out.println(
		"set [variable] [value]							- Sets a variable to a value\n" +
		"set def									- Sets everything to default value\n"+
		"show vars								- Shows the variables and their values\n"+ 
		"show iter								- Prints the current number of iterations\n" +
		"save [name]								- Saves current game context to file\n" +
		"load [name]								- Loads  context or test case from file\n" + 
		"init									- Sets up the initial state\n"+ 
		"dump									- Prints the current context to the screen\n" + 
		"leave									- Leaves context\n" + 
		"run [n]									- Runs n iterations of the game\n" + 
		"test									- Runs the loaded test case\n" + 
		"edit test [initial state] [end state] [name]				- Edits (or creates) test case\n" +
		"eval									- Evaluates the latest runned test and prints the results\n" + 
		"map add [ant|antlion|anteater|anthill|warehouse|puddle|pebble|antodour|foododour|poison] [x] [y]  - Places element to the given coordinates\n" +
		"map remove [x] [y] 							- Removes element from the given coordinates\n" +
		"map generate								- Generates random map\n" + 
		"fire antkiller [x] [y]							- Uses the antkiller to the given coordinates\n" +
		"fire antodourkiller [x] [y]						- Uses the antodourkiller to the given coordinates\n" + 
		"help									- Here you are\n" + 
		"quit									- Quits the prototype program\n");
	}
	
	/**
	 * Stops the prototype program
	 * 
	 * @author David
	 * @throws PrototypeCLIException
	 */
	private void cQuit() throws PrototypeCLIException, IOException  {
		cLeave();
		System.out.println("Exiting...");
		this.IsRunning = false;
	}
	
	
	/**
	 * Starting point of the prototype program
	 * 
	 * @author David
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//  We choose to use this timer: TimerObservable
			System.out.println("Nerdybeans AntFarm Prototype");
			System.out.println("----------------------------");
			System.out.println("Initializing...");
			System.out.println("Using timer: TimerObservable [section_length=" + Prototype.TIMER_SECTION_LENGTH + "]");
			
			
			TimerObservable tmp = new TimerObservable(Prototype.TIMER_SECTION_LENGTH);
			tmp.setPrecision(10);
			Timer.setInstance(tmp);
			
			new Prototype().runCLI();
		} catch (IOException IOError) {
			System.err.println("I/O Error occured. Message: " + IOError.getMessage());
			IOError.printStackTrace();
		}
	}
}
