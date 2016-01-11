package org.nerdybeans.antfarm.controller;

/**
 * Exception thrown when the Prototype CLI detects that the user
 * have not specified sufficient arguments for a given command
 * @author Demarcsek
**/
public class MissingArgumentException extends PrototypeCLIException {
	public MissingArgumentException(String Message) {
		super(Message);
		
	}
}
