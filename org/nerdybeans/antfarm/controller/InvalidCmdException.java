package org.nerdybeans.antfarm.controller;

/**
 * Exception thrown when the Prototype CLI detects invalid
 * command given by the user
 * @author Demarcsek
**/
public class InvalidCmdException extends PrototypeCLIException {
	public InvalidCmdException(String Message) {
		super(Message);
	}
}
