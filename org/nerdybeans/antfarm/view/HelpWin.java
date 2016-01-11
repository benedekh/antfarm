package org.nerdybeans.antfarm.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.BoxLayout;

import org.nerdybeans.antfarm.view.viewelements.*;

/**
 * 
 * The Help window of the game. It gives a little description of the game and the rules. After, it
 * displays the possible graphical elements of the map and finally some legal notes and names are
 * written on it.
 * 
 * @author David, Demarcsek
 * @version 1.0
 **/
public class HelpWin extends JDialog {
	/**
	 * The content Panel, that holds the content of the window.
	 */
	private JPanel contentPane;

	/**
	 * Create the frame.
	 * 
	 * @author Demarcsek, David
	 * @param Frame the owner frame, which means it has a parent window that creates it.
	 */
	public HelpWin(Frame owner) {
		super(owner, "Help", true);
		//setBounds(new Rectangle(0, 0, 800, 400));
		//this.setPreferredSize(new Dimension(800, 600));
		//setSize(800, 600);
		//setTitle("Help");
		setResizable(false);
		setLocation(220, 60); 		// starting position of the window on the screen
		//this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		String helpStr = new String(
				"About the game\r\n\r\n" +
				"This is a single player antfarm game. The user has to save the foods stored in the three " +
				"warehouses. The ants are moving continuously from the anthill to the warehouses to eat food" +
				" from them. One ant can eat one unit food. There are 10 units in each warehouse. \r\n\r\n"  +
				
				"Moreover, each ant and food has its own odour. Ants are also able to follow other ant's odour " +
				"and to smell the odour of the food stored in the warehouses.\r\n\r\n"+
				
				"Two utilities, the antkiller and the antodourkiller are available for the user. " +
				"The former can be used 10 times clicking with the left mouse button, " +
				"the latter can be used 15 times clicking with the right mouse button. The antkiller kills all "+
				"ants in 3 radius around the clicked field. The antodourkiller removes the odour from the " +
				"fields in 2 radius around the clicked field.\r\n\r\n" +
				
				"The game is over when all food are eaten by the ants."
		);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		
		
		JTextArea lblNewLabel = new JTextArea(helpStr);
		lblNewLabel.setBackground(UIManager.getColor("Button.background"));
		lblNewLabel.setEditable(false);
		lblNewLabel.setLineWrap(true);
		lblNewLabel.setWrapStyleWord(true);
		
		//lblNewLabel.setRows(30);
		//lblNewLabel.setColumns(60);
		
		JScrollPane scrollPane = new JScrollPane(lblNewLabel);
		scrollPane.setSize(new Dimension(600, 600));
		scrollPane.setPreferredSize(new Dimension(600, 250));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		panel_1.add(scrollPane);
		
		
		JPanel panel_2 = new JPanel();
		
		panel_2.setLayout(new BorderLayout(5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("Ant");
		//lblNewLabel_1.setIconTextGap(5);
		lblNewLabel_1.setIcon( new ImageIcon( new AntView().getSprite() ) );
		lblNewLabel_1.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_1.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_2 = new JLabel("Ant hill");
		//lblNewLabel_2.setIconTextGap(5);
		lblNewLabel_2.setIcon( new ImageIcon( new AntHillView().getSprite() ) );
		lblNewLabel_2.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_2.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_3 = new JLabel("Ant eater");
		//lblNewLabel_3.setIconTextGap(5);
		lblNewLabel_3.setIcon( new ImageIcon( new AntEaterView().getSprite() ) );
		lblNewLabel_3.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_3.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_4 = new JLabel("Ant lion");
		//lblNewLabel_4.setIconTextGap(5);
		lblNewLabel_4.setIcon( new ImageIcon( new AntLionView().getSprite() ) );
		lblNewLabel_4.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_4.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_5 = new JLabel("Pebble");
		//lblNewLabel_5.setIconTextGap(5);
		lblNewLabel_5.setIcon( new ImageIcon( new PebbleView().getSprite() ) );
		lblNewLabel_5.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_5.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_6 = new JLabel("Puddle");
		//lblNewLabel_6.setIconTextGap(5);
		lblNewLabel_6.setIcon( new ImageIcon( new PuddleView().getSprite() ) );
		lblNewLabel_6.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_6.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel lblNewLabel_7 = new JLabel("Warehouse");
		//lblNewLabel_7.setIconTextGap(5);
		lblNewLabel_7.setIcon( new ImageIcon( new WarehouseView().getSprite() ) );
		lblNewLabel_7.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_7.setHorizontalTextPosition(JLabel.CENTER);
		
		
		JLabel lblNewLabel_8 = new JLabel("Field");
		//lblNewLabel_8.setIconTextGap(5);
		lblNewLabel_8.setIcon( new ImageIcon( new FoodOdourView().getSprite() ) );
		lblNewLabel_8.setVerticalTextPosition(JLabel.BOTTOM);
        lblNewLabel_8.setHorizontalTextPosition(JLabel.CENTER);
		
		JPanel panel = new JPanel();
		
		//panel.setLayout(new GridLayout(1, 8, 10, 10));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		panel.add(lblNewLabel_1);
		panel.add(lblNewLabel_2);
		panel.add(lblNewLabel_3);
		panel.add(lblNewLabel_4);
		panel.add(lblNewLabel_5);
		panel.add(lblNewLabel_6);
		panel.add(lblNewLabel_7);
		panel.add(lblNewLabel_8);
		
		panel_2.add(panel);
		
		
		contentPane.add(panel_2);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel lblNewLabel_9 = new JLabel("<html>"  +
				"AntFarm&#169 2013 NerdyBeans&#174 \r\nDevteam: Bendek Szab&#243, Benedek Horv&#225th," +
				" Gergely D&#225vid, Gy&#246rgy Demarcsek</html>");
		
		panel_3.add(lblNewLabel_9);
		
		this.contentPane.validate();
		this.pack();
	}

}
