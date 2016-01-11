/**
 * 
 */
package org.nerdybeans.antfarm.view;

import javax.swing.*;

import org.nerdybeans.antfarm.controller.ModelController;
import org.nerdybeans.antfarm.model.GameWorld;
import org.nerdybeans.antfarm.model.WorldField;
import org.nerdybeans.antfarm.model.modelelements.Ant;
import org.nerdybeans.antfarm.model.modelelements.AntEater;
import org.nerdybeans.antfarm.model.modelelements.FoodOdour;
import org.nerdybeans.antfarm.model.modelelements.Warehouse;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Starting window of the GUI
 * @author David, Demarcsek, Horvath
**/
public class MainWindow extends JFrame implements ActionListener {
	private JButton btnNewGame;  // new game button
	private JButton btnLoadGame; // load game button
	private JButton btnHelp;	 // help button
	private JButton btnSaveExit; //save & exit button
	
	private JLabel lblStatusMsg; // bottom status message label
	private JPanel centerPanel;  // center panel with the map
	
	private WorldFieldView[][] MapView; // map
	
	private boolean game_created = false; //is the game already created?
	
	/**
	 * Save the game when the window is closing.
	 * @author Horvath
	 **/
	private class WindowCloser extends WindowAdapter {
		
		/**
		 * Save the current state of the game and close the window.
		 * @author Horvath
		 * @param WindowEvent the parameters of the Event
		 */
		@Override
		public void windowClosed(WindowEvent e) {
			// windowClosed() -> dispose() callback
			ModelController.getInstance().userCommand("saveGame", null);
			ModelController.getInstance().userCommand("killTimer", null);
			System.gc(); System.exit(0);
		}
		
		@Override
		public void windowIconified(WindowEvent e) {
			if(!game_created) return;
			JOptionPane.showMessageDialog(e.getWindow(), "Game paused");
			ModelController.getInstance().userCommand("pauseGame", null);
		}
		
		@Override
		public void windowDeiconified(WindowEvent e) {
			if(!game_created) return;
			JOptionPane.showMessageDialog(e.getWindow(), "Game resumed");
			ModelController.getInstance().userCommand("resumeGame", null);
		}
	}
	
	/**
	 * Default constructor of the window
	 * @author David, Horvath
	**/
	public MainWindow() {
		
		this.showSplashScreen();
		
		this.addWindowListener(new WindowCloser());			// save the game when the window is closing
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); 	// clicking on the red X closes the app
		this.setTitle("AntFarm 2013 by NerdyBeans"); 		// title of the window
		this.setResizable(false); 							// the window is not resizable
		this.setLocation(200, 10); 							// starting position of the window on the screen
		
		initializeComponents(); // Shows the main page
		
		
	}
	
	static void renderSplashFrame(Graphics2D g) {
    }
	
	/**
	 * Method for the splashscreen
	 * @author David, Demarcsek
	 **/
	public void showSplashScreen() {
		
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null) { // if it doesnt exist
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        renderSplashFrame(g);
        for(int i=0; i<100; i++) {
            
            splash.update();
            try {
                Thread.sleep(36); //length of the animation is 3.6 sec
            }
            catch(InterruptedException e) {
            }
        }
        splash.close();
        this.setVisible(true); // now the main window is visible
	}
	
	/**
	 * Sets up the components
	 * @author David
	 */
	public void initializeComponents() {
		
		this.getContentPane().removeAll(); 		// deletes the content
		this.setLayout(new BorderLayout()); 		// sets the layout

		// the upper bar, which contains the buttons
		JPanel menuBar = new JPanel(); 
		menuBar.setLayout(new FlowLayout()); // however, flowlayout is the default

		// the buttons in the upper bar (mentioned above)
		btnNewGame = new JButton("New Game");
		btnLoadGame = new JButton("Load Game");
		btnHelp = new JButton("Help");
		btnSaveExit = new JButton("Save & Exit");
		
		btnNewGame.addActionListener(this);
		btnLoadGame.addActionListener(this);
		btnHelp.addActionListener(this);
		btnSaveExit.addActionListener(this);
	
		
		// adding buttons to the upper bar
		menuBar.add(btnNewGame);
		menuBar.add(btnLoadGame);
		menuBar.add(btnHelp);
		menuBar.add(btnSaveExit);		
		
		// the center panel with a beautiful picture :)
		centerPanel = new JPanel(); // contains only a picture in the main menu 
		centerPanel.setLayout( new FlowLayout(FlowLayout.LEADING, 65, 65) );
		centerPanel.add(new JLabel("")); //we didnt want it to be empty
		
		// the bottom bar, the status bar, shows messages etc.
		JPanel statusBar = new JPanel();
		lblStatusMsg = new JLabel("<html>Welcome! Good luck & have fun! NerdyBeans&#174</html>");
		statusBar.add(lblStatusMsg); //add this message to the bottom status bar

		// Adds JPanels to the BorderLayout
		this.add(menuBar, BorderLayout.NORTH); // upper part
		this.add(centerPanel, BorderLayout.CENTER); // center
		this.add(statusBar, BorderLayout.SOUTH); // bottom part
	
		this.getContentPane().validate(); // validating
		this.pack(); // proper sizes
	}
	
	
	/**
	 * Shows the game page with the map
	 * @author David, Demarcsek
	 */
	public void initializeGamePage()
	{	
		// changes between MainPage and Gamepage
		this.getContentPane().remove(centerPanel);
		btnLoadGame.setEnabled(true);
		btnHelp.setEnabled(true);
		
		// Adds the grid to the center part 
		JPanel gridPanel = new JPanel(); 
		
		int h = org.nerdybeans.antfarm.model.GameWorld.SIZE_H; //map horizontal size
		int w = org.nerdybeans.antfarm.model.GameWorld.SIZE_W; //map vertical size
		this.MapView = new WorldFieldView[h][w];

		GridBagLayout MapLayout = new GridBagLayout(); // layout for the map
		GridBagConstraints ml_c = new GridBagConstraints();
		
		
		ml_c.fill = GridBagConstraints.HORIZONTAL | GridBagConstraints.VERTICAL;
		
		gridPanel.setLayout(MapLayout);
		
		for (int i = 0; i < h; i++) // makes map
		{
			for(int j = 0; j < w; ++j) {
				this.MapView[i][j] = new WorldFieldView();
				this.MapView[i][j].setPreferredSize(new Dimension(0,0)); //size of field is 0,0 now,
																		//cause of Windows AERO glitch
				ml_c.gridy = i;
				ml_c.gridx = j;
				
				ml_c.insets = new Insets(0,0,0,0);
				ml_c.weightx = 0.05;
			
				
					if(((i-1) % 2) == 0) {
						ml_c.gridx++;
					}

				gridPanel.add(this.MapView[i][j], ml_c);
			}
		}
		

		this.add(gridPanel, BorderLayout.CENTER); // center
		
		this.getContentPane().validate();
		this.getContentPane().repaint();
		this.pack();
		
	}
	
	/**
	 * @author Horvath
	 */
	public WorldFieldView[][] getFields(){
		return this.MapView;
	}
	
	
	private WorldFieldView WorkUnit;
	/**
	 * Updates the graphics
	 * @author Demarcsek, Szabo
	 */
	public void updateGraphics() {
		this.setStatusText(
				"AntKiller load: " + ModelController.getInstance().getAntKillerLoad() 
			+   " AntOdourKiller load:" + ModelController.getInstance().getAntOdourKillerLoad() 
			+   " Warehouses remaining: " + Warehouse.NUM_OF_WAREHOUSES	 
		);
		//System.out.println("[DEBUG#MainWindow.updateGraphics] Redrawing...");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					for(int i = 0; i < GameWorld.SIZE_H; ++i) {
						for(int j = 0; j < GameWorld.SIZE_W; ++j) {
							//System.out.println("[DEBUG#MainWindow.updateGraphics] Rebuilding view stack...");
							WorldFieldView fieldView = MapView[i][j];
							WorldField field = ModelController.getInstance().getModel(fieldView);
							fieldView.clearElements();
							
							fieldView.addElement(FoodOdour.View);
							
							if(field.getPermanentElement() != null) {
								fieldView.addElement(field.getPermanentElement().getView());
							} else if(field.getDynamicElement() != null) {
								fieldView.addElement(field.getDynamicElement().getView());
							}
								
								
							if(field.getAntIterator().hasNext()) {
								fieldView.addElement(Ant.View);
							}
								
							if(field.getAntEater() != null) {
								fieldView.addElement(AntEater.View);
							}
							
							WorkUnit = MapView[i][j];
							
							
							WorkUnit.repaint();
							
						}
					}
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("[DEBUG#MainWindow.updateGraphics] Done");
	}
	
	/**
	 * Returns the text of the statusbar
	 * @author Horvath
	 * @return
	 */
	private String getStatusText() {
		return this.lblStatusMsg.toString();
	}
	
	/**
	 * Sets the text of the statusbar
	 * @author Horvath
	 * @param message
	 */
	private void setStatusText(String message){
		this.lblStatusMsg.setText(message);
	}
	
	/**
	 * Common handler of actionevents.
	 * 
	 * @author Demarcsek, Horvath (Save&Exit), David (Help), Szabo
	**/
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object emitterComponent = evt.getSource();
		if(emitterComponent == this.btnNewGame) {
			
			int h = org.nerdybeans.antfarm.model.GameWorld.SIZE_H; //map size horizontal
			int w = org.nerdybeans.antfarm.model.GameWorld.SIZE_W; //map size vertical
			for (int i = 0; i < h; i++) // sets the size of a field, only now cause of AERO glitch...
			{
				for(int j = 0; j < w; ++j) {
					this.MapView[i][j].setPreferredSize(new Dimension((714/(w+2)), (605)/(h+2)));
				}
			}
			this.getContentPane().validate();
			this.getContentPane().repaint();
			this.pack();
			
			game_created = true;
			ModelController.getInstance().userCommand("newGame", null);
			this.setStatusText("(Re)starting game...");
		} else if(emitterComponent == this.btnLoadGame) {
			System.out.println("[DEBUG#btnLoadGame actionPerformed]");
			
			int h = org.nerdybeans.antfarm.model.GameWorld.SIZE_H;  //map size horizontal
			int w = org.nerdybeans.antfarm.model.GameWorld.SIZE_W; //map size vertical
			for (int i = 0; i < h; i++) // makes map
			{
				for(int j = 0; j < w; ++j) { // sets the size of a field, only now cause of AERO glitch...
					this.MapView[i][j].setPreferredSize(new Dimension((714/(w+2)), (605)/(h+2)));
				}
			}
			this.getContentPane().validate();
			this.getContentPane().repaint();
			this.pack();
			
			game_created = true;
			ModelController.getInstance().userCommand("loadGame", null);
			this.setStatusText("Loading game...");
		} else if(emitterComponent == this.btnHelp) {
			System.out.println("[DEBUG#btnHelp actionPerformed]");
			//HelpWindow helpWin = new HelpWindow(); //displays help in new window			
			new HelpWin(this).setVisible(true);
		} else if(emitterComponent == this.btnSaveExit) {
			System.out.println("[DEBUG#btnSaveExit actionPerformed]");
			// save the game & close the window
			this.dispose();
		} else {
			// Do nothing...
		}
	}
	
	/**
	 * Entry point of the program
	 * @author David, Demarcsek
	 * @param args
	 */
	public static void main(String[] args) {
		// Gui App Entry Point (...)
		try {
			SwingUtilities.invokeAndWait( 
				new Runnable() {
					public void run() {
						MainWindow Win = new MainWindow();
						
						Win.initializeGamePage();
						
						ModelController.getInstance().init(new org.nerdybeans.antfarm.model.Game(), Win);
					}
				}
			);
			
			//ModelController.getInstance().createControlThread().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
