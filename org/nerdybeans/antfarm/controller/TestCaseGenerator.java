package org.nerdybeans.antfarm.controller;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.nerdybeans.antfarm.model.*;
import org.nerdybeans.antfarm.auxiliary.GameSerializer;

/**
 * Our test-case map generator class before prototype phase  
 * @author Demarcsek
 * @verison 0.1
 */
public class TestCaseGenerator {

 	/**
	 * Static method which generates a new Game context
	 * @author Demarcsek
	 * @return Game
	 */
	private static Game generateState() {
		Game StateToConvert = new Game();
		// Set up state here!
		
		// Manipulate the object as you wish...
		// Of course, the methods you use here on StateToConvert must be
		// implemented in the concerned classes! These basic methods
		// that manipulate the data structures are quite straightforward to implement
		
		return StateToConvert;
	}
	
	/**
	 * Starting point, main method
	 * @author Demarcsek
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(" -= Game Serializer Tool v0.1 =- "); //welcome messages
		System.out.println("=================================");
		System.out.println("Generating state object...");
		
		Game ObjectToWrite = generateState();
		
		System.out.println("Transforming to output language...");
		
		StringBuffer Serialized = GameSerializer.serialize(ObjectToWrite); //serializes object
		
		System.out.println("Writing file...");
		
		String FileName = String.valueOf(ObjectToWrite.hashCode()) + ".txt";
		StringTokenizer LineTokenizer = new StringTokenizer(Serialized.toString(), GameSerializer.LINE_SEPARATOR);
		
		Serialized = null;
		
	
		
		try {
			FileWriter Handle = new FileWriter( new File(FileName) ); //BANNER FILE
			BufferedWriter Channel = new BufferedWriter(Handle);
			Channel.write( "# org.nerdybeans.antfarm.model.Game state object data" );
			Channel.newLine();
			DateFormat DTFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Channel.write( "# Object [" + ObjectToWrite.toString() + "] encoded on " + (DTFormat.format(Calendar.getInstance().getTime())) + " with Game Serializer Tool" );
			Channel.newLine();
			while(LineTokenizer.hasMoreTokens()) { 
				String Line = LineTokenizer.nextToken(); //Returns next token
				Channel.write(Line);
				Channel.newLine();
			}
			Channel.close();
		} catch(IOException e) { //catching exception -> I/o error
			System.err.println("I/O error occured!");
		}
		
		System.out.println("Output file: " + FileName);
		System.out.println("Done.");
	}
}
