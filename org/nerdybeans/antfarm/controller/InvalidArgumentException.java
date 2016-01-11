package org.nerdybeans.antfarm.controller;

/**
 * Exception thrown when the Prototype CLI detects invalid
 * command argument given by the user
 * @author Demarcsek
**/
public class InvalidArgumentException extends PrototypeCLIException {
	public InvalidArgumentException(String Message) {
		super(Message);
	}
}
