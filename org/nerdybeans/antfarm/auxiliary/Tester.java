/**
 * 
 */
package org.nerdybeans.antfarm.auxiliary;

import org.nerdybeans.antfarm.controller.PrototypeCLI;
import org.nerdybeans.antfarm.controller.PrototypeCLIException;
import org.nerdybeans.antfarm.model.Game;

/**
 * Simple Test conductor class that is capable of executing test cases
 * and evaluating results.
 * 
 * @author Demarcsek
 * @version 0.1
 * 
 * @see TestCase
 * 
**/
public class Tester {
	/**
	 * Context of the test (full state space)
	**/
	private Game Context;
	
	/**
	 * A reference to the PrototypeCLI (master) instance to which the Tester is attached
	**/
	PrototypeCLI Parent;
	
	/**
	 * Default constructor
	 * @param Parent A reference to a Prototype CLI Framework object that owns this object
	 * 
	 * @see Prototype
	**/
	public Tester(PrototypeCLI Parent) {
		this.Parent = Parent;
	}
	
	/**
	 * Runs a test over the specified test case.
	 * 
	 * To minimize overheads, runTest also evaluates the test, so you can
	 * store its result and use it later on.
	 * 
	 * @return true if the test was successfull, false otherwise
	 * 
	 * @param Case The test case to test
	**/
	public boolean runTest(TestCase Case) throws PrototypeCLIException {
		this.Context = Case.getInitialState();
		Game Original = Parent.getContext();		// Preserve current state
		
		Parent.changeContext(this.Context);
		
		// Please note:
		// In test mode, the algorithm of odour volatilization is not covered
		// so it is NOT tested
		
		
		try {
			for(String cmd : Case.getCommands()) {
				Parent.parseAndRun(cmd);
			}
		} catch(PrototypeCLIException e) {
			throw e;
		}
		
		this.Context = Parent.getContext();
		boolean RetValue = false;
		//if(this.Context.equals(Case.getExpectedState())) {
		if(GameSerializer.serialize(this.Context).toString().equals(GameSerializer.serialize(Case.getExpectedState()).toString())) {
			RetValue = true;
		} else {
			/// DEBUG data on failure
			System.err.println("[DEBUG] TestFailure occured");
			System.err.println("[DEBUG] -------------------");
			System.err.println("[DEBUG] Context: ");
			String Init = GameSerializer.serialize(this.Context).toString();
			System.err.println(Init);
			System.err.println("[DEBUG] Expected: ");
			String Ex = GameSerializer.serialize(Case.getExpectedState()).toString();
			System.err.println(Ex);
			int Len = (Ex.length() > Init.length() ? Init.length() : Ex.length());
			for(int i = 0; i < Len; ++i)
				if(Init.charAt(i) != Ex.charAt(i)) {
					System.err.println("[DEBUG] Difference at character " + i);
					break;
				}
			if(Ex.length() != Init.length()) {
				System.err.println("[DEBUG] Difference at character " + (Len + 1));
			}
			System.err.println("[DEBUG] -------------------");
		}
		Parent.changeContext(Original);
		return RetValue;
	}
	
	/**
	 * Sets the parent (or owner)
	 * @param newParent The new PrototypeCLI instance that owns the Tester
	**/
	public void setParent(PrototypeCLI newParent) {
		this.Parent = newParent;
	}
}
