/**
 * 
 */
package org.nerdybeans.antfarm.auxiliary;

import java.util.ListIterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.nerdybeans.antfarm.model.*;
import org.nerdybeans.antfarm.model.modelelements.Ant;
import org.nerdybeans.antfarm.model.modelelements.AntEater;
import org.nerdybeans.antfarm.model.modelelements.AntHill;
import org.nerdybeans.antfarm.model.modelelements.AntLion;
import org.nerdybeans.antfarm.model.modelelements.Pebble;
import org.nerdybeans.antfarm.model.modelelements.Puddle;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;


/**
 * Serializes Game objects. The serialized output complies with the 
 * specified 'output language'.
 * @author Demarcsek
 * @version 0.2
**/
public class GameSerializer {
	/// Separator string for field attribs
	public static final String ATOMIC_SEPARATOR = ",";
	/// Separator string for fields
	public static final String FIELD_SEPARATOR = "|";
	/// Line separator string
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	/// Error message
	private static String UnserializeErrorMsg;
	
	/**
	 * @return The latest error message concerning unserialization. null if no error occured
	 */
	public static String getLastErrorMsg() {
		return GameSerializer.UnserializeErrorMsg;
	}
	
	/**
	 * Serializes the given Game object (text format)
	 * @param Target The Game state to serialize
	 * @return The serialized object (as a StringBuffer) 
	 */
	public static StringBuffer serialize(Game Target) {
		// TODO: Revise language and complete serialization + overload toString()s in model
		Game WorkUnit = Target;
		GameWorld World = WorkUnit.getMap();
		WorldField[][] MapData = World.getMap();
		StringBuffer SerializedData = new StringBuffer();
		
		for( int i = 0; i < MapData.length; ++i ) {
			String Line = "";
			for( int j = 0; j < MapData[0].length; ++j ) {
				WorldField CurrField = MapData[i][j];
				Line += CurrField.getFoodOdour();
				Line += GameSerializer.ATOMIC_SEPARATOR;
				Line += CurrField.getAntOdour();
				Line += GameSerializer.ATOMIC_SEPARATOR;
				Line += CurrField.getPoison();
				Line += GameSerializer.ATOMIC_SEPARATOR;
				
				// Ant 				# Of Ants
				// AntEater			# Of Consumed Ants
				// Warehouse			Amount of food stored
				// Empty field		0
				int lastNum = 0;
				
				ListIterator<Ant> Temp = CurrField.getAntIterator();
				Replaceable Dynamic;
				Irreplaceable Static; 
				
				if((Static = CurrField.getPermanentElement()) != null) {
					Line += Static.toString();
					int Index = World.getExitPoints().indexOf(Static);
					if(Index >= 0) {		// Warehouse
						lastNum = World.getExitPoints().get(Index).getFoodAmount();
					} else {
						lastNum = 1;
					}
				} else if((Dynamic = CurrField.getDynamicElement()) != null) {
					Line += Dynamic.toString();
					lastNum = 1;
				} else if(CurrField.getAntEater() != null) {
					Line += CurrField.getAntEater().toString();
					lastNum = CurrField.getAntEater().getConsumed();
				} else if(Temp.hasNext()) {
					Line += "A";
					while(Temp.hasNext()) {
						lastNum++;
						Temp.next(); //DG - forgotten part
					}
				} else {
					Line += CurrField.toString();
				}
				Line += GameSerializer.ATOMIC_SEPARATOR; //DG - forgotten part
				Line += Integer.toString(lastNum); //DG - forgotten part
				Line += GameSerializer.FIELD_SEPARATOR;
			}
			
			SerializedData.append(Line);
			SerializedData.append(GameSerializer.LINE_SEPARATOR);
		}
		
		SerializedData.append(Target.getAntKiller().getLoad() + GameSerializer.ATOMIC_SEPARATOR + Target.getAntOdourKiller().getLoad() );
		
		return SerializedData;
	}
	
	/**
	 * Creates a new Game object in the memory by unserializing
	 * a text-formatted (serialized) Game object. If any error occurs during
	 * the process, UnserializeErrorMsg gets set, so it contains the latest error message.
	 * @param Target The serialized Game object (as a StringBuffer)
	 * @return A reference to the converted Game object
	 */
	public static Game unserialize(StringBuffer Target) {
		//System.out.println("--- RAW TARGET ---");
		//System.out.print(Target.toString());
		//System.out.println("------------------");
		GameSerializer.UnserializeErrorMsg = null;
		//System.out.println("[DEBUG#GameSerializer.unserialize] Line separator used: " + GameSerializer.LINE_SEPARATOR);
		Game Unserialized = new Game();
		GameWorld World = Unserialized.getMap();
		WorldField[][] Matrix = World.getMap();
		Scanner LineTokenizer = new Scanner(Target.toString());
		//System.out.println("[DEBUG#GameSerializer.unserialize] LineTokenizer.delimeter(): " + LineTokenizer.delimiter());
		int NumOfRows = 0;
		int NumOfCols = 0;
		boolean WeaponsLoaded = false;
		int NumOfWeapons = 0;
		
		while(LineTokenizer.hasNextLine()) {
			String Line = LineTokenizer.nextLine();
			//System.out.println("[DEBUG#GameSerializer.unserialize] Line: '" + Line + "'");
			StringTokenizer FieldTokenizer = new StringTokenizer(Line, GameSerializer.FIELD_SEPARATOR);
			NumOfCols = 0;
			/// Don't count last lines containing weapon loads
			//System.out.println("[DEBUG#GameSerializer.unserialize] FieldTokenizer.countTokens(): " + FieldTokenizer.countTokens());
			if(FieldTokenizer.countTokens() <= 1) {	/// Try load weapons here...quite tricky :)
				StringTokenizer WeaponFieldTokenizer = new StringTokenizer(Line, GameSerializer.ATOMIC_SEPARATOR);
				
				while(WeaponFieldTokenizer.hasMoreTokens()) {
					String Load = WeaponFieldTokenizer.nextToken();
					int LoadValue = 0;
					try {
						LoadValue = Integer.parseInt(Load);
					} catch(NumberFormatException NaN) {
						GameSerializer.UnserializeErrorMsg = "Weapon load must be integer";
						return null;
					}
					NumOfWeapons++;
					if(NumOfWeapons == 1) {
						Unserialized.setAntKiller(new AntKiller(LoadValue));
					} else if(NumOfWeapons == 2) {
						Unserialized.setAntOdourKiller(new AntOdourKiller(LoadValue));
						WeaponsLoaded = true;
					}
				}
			}
			
			if(WeaponsLoaded) {
				//System.out.println("[DEBUG#GameSerializer.unserialize] Weapon loads: " + Unserialized.getAntKiller().getLoad() + "," + Unserialized.getAntOdourKiller().getLoad());
				break;
			}
			else if(!WeaponsLoaded && (NumOfWeapons >= 1)) {
				GameSerializer.UnserializeErrorMsg = "Two weapon loads must be given";
				return null;
			}
			
			
			while(FieldTokenizer.hasMoreTokens()) {
				//System.out.println("[DEBUG#GameSerializer.unserialize] Reading WorldField (" + NumOfRows + "," + NumOfCols + ")");
				String Field = FieldTokenizer.nextToken();
				//System.out.println(" [DEBUG#GameSerializer.unserialize] Data Read: '" + Field + "'");
				StringTokenizer AtomicTokenizer = new StringTokenizer(Field, GameSerializer.ATOMIC_SEPARATOR);
				int NumOfAttributes = 0;
				String Kind = "";
				
				while(AtomicTokenizer.hasMoreTokens()) {
					String Attribute = AtomicTokenizer.nextToken();
					//System.out.println("  [DEBUG#GameSerializer.unserialize] Parsing attribute: " + Attribute);
					switch(NumOfAttributes) {
					case 0:		/// Food odour
						try {
							int FoodOdour = Integer.parseInt(Attribute);
							Matrix[NumOfRows][NumOfCols].setFoodOdour(FoodOdour);
						} catch(NumberFormatException NaN) {
							GameSerializer.UnserializeErrorMsg = "Invalid FoodOdour value for WorldField(" + NumOfRows + "," + NumOfCols + ")";
							return null;
						}
						break;
					case 1: 		/// Ant odour
						try {
							int AntOdour = Integer.parseInt(Attribute);
							Matrix[NumOfRows][NumOfCols].setAntOdour(AntOdour);
						} catch(NumberFormatException NaN) {
							GameSerializer.UnserializeErrorMsg = "Invalid AntOdour for WorldField(" + NumOfRows + "," + NumOfCols + ")";
							return null;
						}
						break;
					case 2:		/// Poison level
						try {
							int PoisonLevel = Integer.parseInt(Attribute);
							Matrix[NumOfRows][NumOfCols].setPoison(PoisonLevel);
						} catch(NumberFormatException NaN) {
							GameSerializer.UnserializeErrorMsg = "Invalid Poison for WorldField(" + NumOfRows + "," + NumOfCols + ")";
							return null;
						}
						break;
					case 3: 		/// Kind
						Kind = Attribute;
						
						if(Kind.equals("A")) {
							Matrix[NumOfRows][NumOfCols].addAnt(
								new Ant()
							);
						} else if(Kind.equals("Ae")) {
							
							Matrix[NumOfRows][NumOfCols].addAntEater(
									new AntEater()
							);
						} else if(Kind.equals("L")) {
							Matrix[NumOfRows][NumOfCols].setPermanentElement(new AntLion());
							Matrix[NumOfRows][NumOfCols].setPassable(true);
						} else if(Kind.equals("H")) {
							Matrix[NumOfRows][NumOfCols].setPermanentElement(new AntHill());
							Matrix[NumOfRows][NumOfCols].setPassable(false);
							if(World.getEntryPoint() == null)
								World.setEntryPoint((AntHill)Matrix[NumOfRows][NumOfCols].getPermanentElement());
							else
								GameSerializer.UnserializeErrorMsg = "Only one AntHill instance permitted per map. Ignoring AntHill at WorldField(" + NumOfRows + "," + NumOfCols + ")";
							
						} else if(Kind.equals("Pe")) {
							Matrix[NumOfRows][NumOfCols].setDynamicElement(new Pebble());
						} else if(Kind.equals("Pu")) {
							Matrix[NumOfRows][NumOfCols].setPermanentElement(new Puddle());
							Matrix[NumOfRows][NumOfCols].setPassable(false);
						} else if(Kind.equals("W")) {
							System.out.println("Warehouse at " + NumOfRows + "," + NumOfCols);
							Matrix[NumOfRows][NumOfCols].setPermanentElement(new Warehouse());
							Matrix[NumOfRows][NumOfCols].setPassable(true);
							World.getExitPoints().add((Warehouse)Matrix[NumOfRows][NumOfCols].getPermanentElement());
						} else if(Kind.equals("x")) {
							// just in case...
							Matrix[NumOfRows][NumOfCols].setPermanentElement(null);
							Matrix[NumOfRows][NumOfCols].setDynamicElement(null);
							Matrix[NumOfRows][NumOfCols].setPassable(true);
						} else {
							GameSerializer.UnserializeErrorMsg = "Invalid element: '" + Kind + "'";
							return null;
						}
						break;
					case 4:
						int LastNum = 0;
						try {
							LastNum = Integer.parseInt(Attribute);
						} catch(NumberFormatException NaN) {
							GameSerializer.UnserializeErrorMsg = "Field attribute must be an integer at WorldField(" + NumOfRows + "," + NumOfCols + ")";
							return null;
						}
						if(Kind.equals("A")) {
							for(int i = 0; i < LastNum - 1; ++i)		//one already added, so we only need LastNum -1
								Matrix[NumOfRows][NumOfCols].addAnt(
									new Ant()
								);
						} else if(Kind.equals("Ae")) {
							Matrix[NumOfRows][NumOfCols].getAntEater().setConsumed(LastNum);
						} else if(Kind.equals("L")) {
							if(LastNum != 1) {
								GameSerializer.UnserializeErrorMsg = "More than one AntLion objects on WorldField(" + NumOfRows + "," + NumOfCols + ")";
								return null;
							}
						} else if(Kind.equals("W")) {
							((Warehouse)Matrix[NumOfRows][NumOfCols].getPermanentElement()).setFoodAmount(LastNum);
							
						} else if(Kind.equals("x")) {
							if(LastNum != 0) {
								GameSerializer.UnserializeErrorMsg = "Expected '0' for argument 4 at WorldField(" + NumOfRows + "," + NumOfCols + ")";
							}
						}
						break;
					}
					NumOfAttributes++;
				}
				if(NumOfAttributes < 5) {
					GameSerializer.UnserializeErrorMsg = "Missing attributes for WorldField(" + NumOfRows + "," + NumOfCols + ")";
					return null;
				}
				NumOfCols++;
			}
			if(NumOfCols != GameWorld.SIZE_W) {
				GameSerializer.UnserializeErrorMsg = "Invalid map width: " + NumOfCols;
				return null;
			}
			NumOfRows++;
		}
		
		if(NumOfRows != GameWorld.SIZE_H) {
			GameSerializer.UnserializeErrorMsg = "Invalid map height: " + NumOfRows;
			return null;
		}
		
		return Unserialized;
	}
}
