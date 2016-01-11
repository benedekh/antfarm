package org.nerdybeans.antfarm.controller;

import org.nerdybeans.antfarm.model.Game;

/**
* Abstract class for the prototype program 
* @author Demarcsek
* @verison 0.1
*/
public abstract class PrototypeCLI {
	protected Game Context;
	
 	/**
	 * Abstract method selecting the needed command through the given string
	 * @verison 0.1
	 * @param Commandline - runs the properly method
	 */
	public abstract void parseAndRun( String CommandLine ) throws PrototypeCLIException;
	
 	/**
	 * Method to change game context
	 * @verison 0.1
	 * @param newContext
	 */
	public void changeContext(Game newContext) {
		System.out.println("Changing context...");
		Context = newContext;
	}
	
	 /**
	 * Returns the new game context
	 * @author Demarcsek
	 * @verison 0.1
	 * @return Context
	 */
	public Game getContext() {
		return Context;
	}
}
