package org.nerdybeans.antfarm.auxiliary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.nerdybeans.antfarm.model.Game;

/**
 * The class represents a test case in the testing framework for 
 * the AntFarm Prototype. A test case basically consists of a specified initial
 * game state on which a given sequence of instructions should be run and the expected
 * final state to which the state that issue at the end of executing the commands.
 * If the expected state is identical to that state ('current state' or 'context'),
 * the test instance is considered SUCCESSFULL. However, if there's any difference
 * between the expected and the context states, the test is UNSUCCESSFULL, so there
 * may be an issue in the implementation of a set of features that should be fixed
 * later.
 * 
 * @author Demarcsek
 * @version 0.1
 * 
**/
public class TestCase {
	/**
	 * Each test case file should begin with the following string. It is used for
	 * validating test case files 
	**/
	public static final String FILE_BANNER = "# org.nerdybeans.antfarm.auxiliary.TestCase";
	
	/**
	 * Initial state
	**/
	private Game Initial;
	/**
	 * Expected state
	**/
	private Game Expected;
	/**
	 * Series of commands to run by the Prototype framework
	**/
	private ArrayList<String> Commands;
	/**
	 * The identifier of the test (should be used as a name for the test file)
	**/
	private String Name;
	
	/**
	 * Constructor that creates a test case object named NameId with
	 * an empty list of commands and uninitialized "input" and "expected output" (aka Initial and Expected)
	 * @param NameId The desired name of the new TestCase
	 */
	public TestCase(String NameId) {
		this.Name = NameId;
		this.Commands = new ArrayList<String>();
	}
	
	/**
	 * Returns the initial game state (input)
	 * @return Variable 'Initial'
	 */
	public Game getInitialState() {
		return this.Initial;
	}
	
	/**
	 * Returns the expected game state (expected output)
	 * @return Variable 'Expected'
	 */
	public Game getExpectedState() {
		return this.Expected;
	}
	
	/**
	 * Returns the list of test commands
	 * @return An list of test commands (containing String-s)
	**/
	public ArrayList<String> getCommands() {
		return this.Commands;
	}
	
	/**
	 * Returns the unique name of the test case. Please note that 
	 * the corresponding filename would be getName() + ".txt"
	 * @return Name (ID) of the test case
	 */
	public String getName() {
		return this.Name;
	}
	
	/**
	 * Sets the initial game state variable 
	 * @param newExpected The initial state (Game object)
	**/
	public void setInitialState(Game newInitial) {
		assert newInitial != null;
		this.Initial = newInitial;
	}
	
	/**
	 * Sets the expected game state variable 
	 * @param newExpected The expected state (Game object)
	**/
	public void setExpectedState(Game newExpected) {
		assert newExpected != null;
		this.Expected = newExpected;
	}
	
	/**
	 * Sets the list of the desired test framework commands
	 * @param newCommands The list of commands
	 * @see addCommand
	 */
	public void setCommands(ArrayList<String> newCommands) {
		assert newCommands != null;
		this.Commands = newCommands;
	}
	
	/**
	 * Adds a command to be executed when the test is being run
	 * @param Cmd A command to add
	**/
	public void addCommand(String Cmd) {
		this.Commands.add(Cmd);
	}
	
	/**
	 * Convert a TestCase to string. It may be used for creating test files using
	 * TestCase objects
	 * 
	 * Note that it uses the platform's own line endings (UNIX - \n, WINDOWS - \r\n) and appends
	 * some basic information to the output including the timestamp of the serialization and the name
	 * of the test case. The order of stored information is: 
	 *  [1] Initial State 
	 *  [2] Expected State
	 *  [3] Framework Commands
	 *  
	 * @author Demarcsek
	 * @verison 0.2
	 * 
	 * @see GameSerializer
	**/
	@Override
	public String toString() {
		StringBuffer RetBuff = new StringBuffer();
		String LineEnding = GameSerializer.LINE_SEPARATOR;
		DateFormat DTFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		RetBuff.append(FILE_BANNER + " test case created at " + DTFormat.format(Calendar.getInstance().getTime()));
		RetBuff.append(LineEnding);
		RetBuff.append("# TestCase ID: " + this.Name);
		RetBuff.append(LineEnding);
		RetBuff.append(GameSerializer.serialize(Initial));
		RetBuff.append(LineEnding); RetBuff.append(LineEnding);
		RetBuff.append(GameSerializer.serialize(Expected));
		RetBuff.append(LineEnding); RetBuff.append(LineEnding);
		for(String cmd : this.Commands) {
			RetBuff.append(cmd); RetBuff.append(LineEnding);
		}
		return RetBuff.toString();
	}
	
}
