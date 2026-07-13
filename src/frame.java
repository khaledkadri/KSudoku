/*                                     
 * Author : KHALED KADRI   

 * LinkedIn : https://www.linkedin.com/in/khaled-kadri/ *
 * MIT License - Copyright (c) 2024 Khaled Kadri
 */


import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class frame {

	// Main frame for the Sudoku game
    private JFrame frmKsudoku;

    // 9x9 grid of text fields for user input
    private JTextField tf[][] = new JTextField[9][9];

    // Current grid values (what the player has entered)
    static int grille_ac[][] = new int[9][9];

    // Timer variables
    private double t1, t2;
    boolean stop = false;         // Controls the chronometer
    boolean first_time = true;    // Checks if the game is started for the first time
    boolean b_aide = false;       // Indicates if "help" mode is active
    JLabel tpsjoueur;             // Label to display the timer

    // Sudoku game logic object
    Jeu jeu;

    // Colors for highlighting errors or fixed cells
    Color red = new Color(243, 146, 146);
    Color vert = new Color(235, 235, 235);

    JLabel label; // Label for the chronometer
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					frame window = new frame();
					window.frmKsudoku.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application and initialise the game logic.
	 */
	public frame() {
		initialize();
		jeu = new Jeu();
	}

	/**
     * Initialize the contents of the frame (GUI components).
     */
	
	private void initialize() {
		
		frmKsudoku = new JFrame();
		frmKsudoku.getContentPane().setBackground(new Color(210, 180, 140));
		frmKsudoku.setTitle("Sudoku");
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("icones/icone_sudoku.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Set frame properties
		frmKsudoku.setIconImage(img);
		frmKsudoku.setBounds(100, 100, 795, 583);
		frmKsudoku.setResizable(false);
		frmKsudoku.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKsudoku.getContentPane().setLayout(null);
		frmKsudoku.setLocationRelativeTo(null);
		
		 // Separator for layout
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.DARK_GRAY);
		separator.setBounds(563, 0, 13, 564);
		frmKsudoku.getContentPane().add(separator);
		
		// Panel for difficulty selection (Easy, Medium, Hard)
		JPanel panel = new JPanel();
		//panel.setBackground(new Color(217, 194, 160));
		panel.setOpaque(false);
		
		TitledBorder border = new TitledBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		border.setTitle("Level");
		border.setTitleJustification( TitledBorder.CENTER );
		border.setTitleFont(new Font("Lucida HandWriting", Font.BOLD, 16));
		panel.setBorder(border);
		panel.setBounds(596, 93, 165, 118);
		frmKsudoku.getContentPane().add(panel);
		panel.setLayout(null);
		
		 // Radio buttons for difficulty levels
        final JRadioButton rdbtnFacile = new JRadioButton("Easy");
        rdbtnFacile.setOpaque(false);
        final JRadioButton rdbtnMoyen = new JRadioButton("Medium");
        rdbtnMoyen.setOpaque(false);
        final JRadioButton rdbtnDifficile = new JRadioButton("Hard");
        rdbtnDifficile.setOpaque(false);
		
        // Easy difficulty selected
		rdbtnFacile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnFacile.setSelected(true);
				rdbtnMoyen.setSelected(false);
				rdbtnDifficile.setSelected(false);
				jeu.facile=true;
				jeu.moyen=false;
				jeu.difficile=false;
			}
		});
		rdbtnFacile.setFont(new Font("Berlin Sans FB", Font.PLAIN, 18));
		rdbtnFacile.setBounds(45, 23, 96, 25);
		panel.add(rdbtnFacile);

		
		// Medium difficulty selected
		rdbtnMoyen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFacile.setSelected(false);
				rdbtnMoyen.setSelected(true);
				rdbtnDifficile.setSelected(false);
				jeu.facile=false;
				jeu.moyen=true;
				jeu.difficile=false;
			}
		});
		rdbtnMoyen.setFont(new Font("Berlin Sans FB", Font.PLAIN, 18));
		rdbtnMoyen.setBounds(45, 53, 96, 25);
		panel.add(rdbtnMoyen);
		
		// Hard difficulty selected
		rdbtnDifficile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnFacile.setSelected(false);
				rdbtnMoyen.setSelected(false);
				rdbtnDifficile.setSelected(true);
				jeu.facile=false;
				jeu.moyen=false;
				jeu.difficile=true;
			}
		});
		rdbtnDifficile.setFont(new Font("Berlin Sans FB", Font.PLAIN, 18));
		rdbtnDifficile.setBounds(45, 87, 96, 18);
		panel.add(rdbtnDifficile);
		
		rdbtnFacile.setSelected(true);// Default difficulty
		
		rdbtnMoyen.setSelected(false);
		rdbtnDifficile.setSelected(false);
		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(Color.DARK_GRAY);
		separator_1.setBounds(596, 224, 165, 2);
		frmKsudoku.getContentPane().add(separator_1);
		
		// "New Puzzle" button
		JButton btnCommencer = new JButton("New puzzle");
		btnCommencer.setBackground(new Color(238, 232, 170));
		btnCommencer.setOpaque(true);
		
		btnCommencer.setFont(new Font("Berlin Sans FB", Font.PLAIN, 19));
		btnCommencer.setBounds(612, 239, 131, 34);
		frmKsudoku.getContentPane().add(btnCommencer);
		
		JPanel panel_pr = new JPanel();
		panel_pr.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_pr.setForeground(Color.BLACK);
		panel_pr.setBounds(12, 13, 525, 525);
		frmKsudoku.getContentPane().add(panel_pr);
		panel_pr.setLayout(null);

		
		/*********************************************************/
		JPanel[] pan = new JPanel[9];
		for(int i = 0 ; i < 9; i++){
			pan[i]= new JPanel();
			pan[i].setBorder(new LineBorder(Color.DARK_GRAY, 3));
			//panel_pr.add(pan[i]);
			pan[i].setLayout(new GridLayout(3, 3, 0, 0));
			panel_pr.setLayout(new GridLayout(9, 9, 0, 0));
		}
		
		// Initialize the 9x9 Sudoku grid of JTextFields
		for(int i = 0; i < 9; i++){
		    for(int j = 0; j < 9; j++){
		        // Create a new JTextField for each cell
		        tf[i][j] = new JTextField();
		        textfield(tf[i][j]); // Apply custom formatting (font, alignment, border)
		        pan[i].add(tf[i][j]); // Add to sub-panel (3x3 blocks)
		        panel_pr.add(tf[i][j]); // Add to main panel

		        final int ii = i;
		        final int jj = j;

		        // Add KeyListener to handle user input in the cell
		        tf[i][j].addKeyListener(new KeyListener() {

		            @Override
		            public void keyTyped(KeyEvent arg0) {
		                // Not used here
		            }

		            @Override
		            public void keyReleased(KeyEvent arg0) {
		                // Check if the pressed key is a number 1-9, delete, or backspace
		                if(arg0.getKeyCode() == KeyEvent.VK_NUMPAD1 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD2 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD3 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD4 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD5 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD6 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD7 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD8 ||
		                   arg0.getKeyCode() == KeyEvent.VK_NUMPAD9 ||
		                   arg0.getKeyCode() == KeyEvent.VK_DELETE ||
		                   arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

		                    String s_nb = tf[ii][jj].getText();
		                    if(!s_nb.isEmpty()){
		                        int nb = Integer.parseInt(s_nb);

		                        // Check for conflicts in the column
		                        if(!jeu.absentSurColonne(nb, grille_ac, jj)){
		                            for(int col = 0; col < 9; col++){
		                                tf[col][jj].setBackground(red);
		                            }
		                            tf[ii][jj].setBackground(red);
		                        }

		                        // Check for conflicts in the row
		                        if(!jeu.absentSurLigne(nb, grille_ac, ii)){
		                            for(int col = 0; col < 9; col++){
		                                tf[ii][col].setBackground(red);
		                            }
		                            tf[ii][jj].setBackground(red);
		                        }

		                        // Check for conflicts in the 3x3 block
		                        if(!jeu.absentSurBloc(nb, grille_ac, ii, jj)){
		                            int _i = ii - (ii % 3), _j = jj - (jj % 3);
		                            for(int ii1 = _i; ii1 < _i + 3; ii1++)
		                                for(int jj1 = _j; jj1 < _j + 3; jj1++)
		                                    tf[ii1][jj1].setBackground(red);
		                            tf[ii][jj].setBackground(red);
		                        } else {
		                            // No conflict, update active grid and set background to white
		                            grille_ac[ii][jj] = nb;
		                            tf[ii][jj].setBackground(Color.WHITE);
		                        }
		                    } else {
		                        // If the cell is empty, restore background colors
		                        for(int col = 0; col < 9; col++){
		                            tf[col][jj].setBackground(jeu.grilleJeu[col][jj]==0 ? Color.WHITE : vert);
		                            tf[ii][col].setBackground(jeu.grilleJeu[ii][col]==0 ? Color.WHITE : vert);
		                        }

		                        int _i = ii - (ii % 3), _j = jj - (jj % 3);
		                        for(int ii1 = _i; ii1 < _i + 3; ii1++)
		                            for(int jj1 = _j; jj1 < _j + 3; jj1++)
		                                tf[ii1][jj1].setBackground(jeu.grilleJeu[ii1][jj1]==0 ? Color.WHITE : vert);
		                    }

		                    // If delete/backspace, clear value from active grid
		                    if(arg0.getKeyCode() == KeyEvent.VK_DELETE || arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE){
		                        grille_ac[ii][jj] = 0;
		                        tf[ii][jj].setBackground(Color.WHITE);
		                    }
		                }
		            }

		            @Override
		            public void keyPressed(KeyEvent arg0) {
		                // Not used here
		            }
		        });
		    }
		}

		// Add thicker borders to separate 3x3 blocks visually
		for(int i = 0; i < 9; i++){
		    for(int j = 0; j < 9; j++){
		        if(i == 2 || i == 5){
		            Border oldBorder = tf[i][j].getBorder();
		            Border redBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
		            tf[i][j].setBorder(BorderFactory.createCompoundBorder(redBorder, oldBorder));
		        }
		        if(j == 2 || j == 5){
		            Border oldBorder = tf[i][j].getBorder();
		            Border redBorder = BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK);
		            tf[i][j].setBorder(BorderFactory.createCompoundBorder(redBorder, oldBorder));
		        }
		    }
		}
		
		
		JPanel panel_88 = new JPanel();
		panel_88.setOpaque(false);
		TitledBorder border2 = new TitledBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		border2.setTitle("Time");
		border2.setTitleJustification( TitledBorder.CENTER );
		border2.setTitleFont(new Font("Lucida HandWriting", Font.BOLD, 14));
		panel_88.setBorder(border2);
		panel_88.setBounds(612, 482, 131, 56);
		frmKsudoku.getContentPane().add(panel_88);
		
		
		
		panel_88.setLayout(null);
		final Chrono chrono = new Chrono(panel_88);
		
		label = new JLabel("00:00");
		label.setFont(new Font("Courier", Font.BOLD, 22));
		label.setBounds(35, 18, 72, 35);
		panel_88.add(label);
		
		stop=true;
		
		// Action listener to start the game
		btnCommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jeu.erreur = new ArrayList<int[]>();
			
				if(stop){
					// Reset chronometer
					chrono.diffMinutes=0;
					chrono.diffSecondes=0;
				}
				
				// Clear all text fields
				for(int i = 0 ; i < 9; i++)
					for(int j = 0 ; j < 9; j++){
						tf[i][j].setBackground(Color.WHITE);
						tf[i][j].setText("");
						tf[i][j].setEditable(true);
						grille_ac[i][j]=0;
					}
				
				// Initialize Sudoku grid
				jeu.init();
				
				// Populate the grid with initial numbers
				for(int i = 0 ; i < 9; i++){
					for(int j = 0 ; j < 9; j++)
						{
							if(jeu.grilleJeu[i][j]!=0){
								tf[i][j].setText(Integer.toString(jeu.grilleJeu[i][j]));
								grille_ac[i][j]=jeu.grilleJeu[i][j];
								tf[i][j].setBackground(vert);
								tf[i][j].setEditable(false);
							}
							/*if(i>2 && i<6 && j!=3&& j!=4&& j!=5){
								tf[i][j].setBackground(new Color(183,206,179));
								tf[j][i].setBackground(new Color(183,206,179));
							}*/
						}
				}
				stop=false;
				t2=0.0;
				t1=System.currentTimeMillis();
				if(first_time){
					chrono.start();// Start chronometer
					first_time=false;
				}
			}
		});
		
		
		
		JButton btnRsoudre = new JButton("Solve");
		btnRsoudre.setBackground(new Color(238, 232, 170));
		// Action listener for the "Solve" button
		btnRsoudre.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Fill the Sudoku grid with the solution from the 'jeu' object
		        for(int i = 0; i < 9; i++){
		            for(int j = 0; j < 9; j++){
		                if(jeu.grilleJeu[i][j] == 0) {
		                    // Empty cells are set to white background
		                    tf[i][j].setBackground(Color.WHITE);
		                } else {
		                    // Pre-filled cells from the solution are set to light green
		                    tf[i][j].setBackground(vert);
		                }
		                // Set the cell value to the solution value
		                tf[i][j].setText(Integer.toString(jeu.grille[i][j]));
		                // Update the active grid
		                grille_ac[i][j] = jeu.grille[i][j];
		            }
		        }
		    }
		});

		btnRsoudre.setFont(new Font("Berlin Sans FB", Font.PLAIN, 19));
		btnRsoudre.setBounds(612, 285, 131, 34);
		frmKsudoku.getContentPane().add(btnRsoudre);
		
		
		// Action listener for the "I finished" button
		JButton btnTerminer = new JButton("I finished");
		btnTerminer.setBackground(new Color(238, 232, 170));
		btnTerminer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Reset the list of errors
		        jeu.erreur = new ArrayList<int[]>();

		        // Validate the current Sudoku grid
		        jeu.estValide2(grille_ac, 0);

		        // If there are no errors, the puzzle is correctly solved
		        if(jeu.erreur.isEmpty()){
		            stop = true; // Stop the chrono

		            // Prepare a label to show the completion message
		            JLabel customFontText = new JLabel();
		            customFontText.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
		            customFontText.setHorizontalAlignment(SwingConstants.CENTER);

		            // Format the time with leading zeros if needed
		            String sec = "", min = "";
		            if (chrono.diffSecondes < 10 && chrono.diffMinutes < 10){
		                sec = "0" + chrono.diffSecondes;
		                min = "0" + chrono.diffMinutes;
		            } else if(chrono.diffSecondes < 10){
		                sec = "0" + chrono.diffSecondes;
		                min = "" + chrono.diffMinutes;
		            } else if(chrono.diffMinutes < 10){
		                sec = "" + chrono.diffSecondes;
		                min = "0" + chrono.diffMinutes;
		            }

		            // Set the congratulatory message
		            customFontText.setText("<html><div style=\"text-align: center;\">" +
		                                   "Congratulations! You solved this Sudoku puzzle in <br>" +
		                                   min + ":" + sec + "</div></html>");

		            // Show the message in a dialog
		            JOptionPane.showMessageDialog(null, customFontText, "Solved!", JOptionPane.PLAIN_MESSAGE);
		        }

		        // Highlight any remaining incorrect cells in red
		        if(Jeu.erreur != null)
		            for(int x = 0; x < jeu.erreur.size(); x++){
		                tf[jeu.erreur.get(x)[0]][jeu.erreur.get(x)[1]].setBackground(red);
		            }
		    }
		});

		btnTerminer.setFont(new Font("Berlin Sans FB", Font.PLAIN, 19));
		btnTerminer.setBounds(612, 332, 131, 34);
		frmKsudoku.getContentPane().add(btnTerminer);
		
		
		// Help button
		final JButton btn_aide = new JButton("");
		btn_aide.setBackground(new Color(238, 232, 170));
		try {
			img = ImageIO.read(this.getClass().getResource("icones/solve2.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		btn_aide.setIcon(new ImageIcon(img));
		btn_aide.setToolTipText("Help");
		
		final MyFocusListener myFocusListener[][] = new MyFocusListener[9][9];
		for(int i = 0 ; i < 9; i++)
			for(int j = 0 ; j < 9; j++){
				myFocusListener[i][j] = new MyFocusListener();
				myFocusListener[i][j].addij(i,j);
			}
		
		
		// Action listener for the "Help" button (btn_aide)
		btn_aide.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		        // If help mode is already active, deactivate it
		        if(b_aide){
		            b_aide = false;
		            // Remove the focus listeners from all text fields
		            for(int i = 0 ; i < 9; i++)
		                for(int j = 0 ; j < 9; j++)
		                    tf[i][j].removeFocusListener(myFocusListener[i][j]);

		            // Set the button icon back to the default "solve2" icon
		            BufferedImage img = null;
		            try {
		                img = ImageIO.read(this.getClass().getResource("icones/solve2.png"));
		            } catch (IOException e1) {
		                e1.printStackTrace();
		            }
		            btn_aide.setIcon(new ImageIcon(img));
		        }
		        // If help mode is not active, activate it
		        else{
		            b_aide = true;

		            // Set the button icon to the "solve" icon
		            BufferedImage img = null;
		            try {
		                img = ImageIO.read(this.getClass().getResource("icones/solve.png"));
		            } catch (IOException e1) {
		                e1.printStackTrace();
		            }
		            btn_aide.setIcon(new ImageIcon(img));

		            // Add focus listeners to all text fields to show the correct number when focused
		            for(int i = 0 ; i < 9; i++){
		                for(int j = 0 ; j < 9; j++){
		                    final int ii = i;
		                    final int jj = j;
		                    tf[ii][jj].addFocusListener(myFocusListener[ii][jj]);
		                }
		            }
		        }
		    }
		});
		
		btn_aide.setBounds(648, 378, 59, 43);
		frmKsudoku.getContentPane().add(btn_aide);
		
		JPanel contentPane = new JPanel();
		contentPane.setBounds(0, 0, 10, 10);
		
		JLabel lblSudoku = new JLabel("  Sudoku");
		InputStream is = frame.class.getResourceAsStream("icones/Parchment.ttf");
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(75f).deriveFont(Font.BOLD);
			lblSudoku.setFont(font);
			//lblSudoku.setFont(new Font("Parchment", Font.BOLD, 75));
		} catch (FontFormatException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		lblSudoku.setBounds(577, 10, 194, 75);
		frmKsudoku.getContentPane().add(lblSudoku);
		
		//JButton btnNewButton = new JButton("\u00C0 propos");
		
		/*
		 * Create a new frame/window for the "About" dialog
		 */
		JButton btnNewButton = new JButton("About");
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFrame frame= new JFrame("About");
                frame.setVisible(true);
                frame.setSize(new Dimension(300,220));
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //frame.setBackground(new Color(215, 183, 136));
                try {
					frame.setIconImage(ImageIO.read(this.getClass().getResource("icones/icone_sudoku.jpg")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Font font = new Font("Courier new", Font.PLAIN, 16);
                
                JPanel p1 = new JPanel();
                p1.setBorder(new MatteBorder(5, 5, 5, 5, new Color(215, 183, 136)));
                               
                String s = "<html>";
                s+=("<br/>Author:<br/>");
                s+=("Khaled KADRI<br/>");
                s+=("<br/>Join me on LinkedIn:<br/>");
                s+=("<font color='blue'>Click here</font><br/>");
                
                final JTextPane textPane = new JTextPane();
                textPane.setContentType("text/html");
                String link = "<a href=\"https://www.linkedin.com/in/khaled-kadri/\" style=\"color: #0077B5; text-decoration: none; font-weight: bold;\">LinkedIn</a><br>"
                		+ "<a href=\"https://github.com/khaledkadri\" style=\"color: #0077B5; text-decoration: none; font-weight: bold;\">GitHub</a>";
                textPane.setText("<html><body style=\"font-family: Arial, sans-serif; font-size: 14px; color: #333;\">" +
                        "<br><b>Author: Khaled KADRI</b><br>" +
                        "<p>Connect with me on :</p>" +
                        link +
                        "</body></html>");
                textPane.setEditable(false);
                textPane.setBackground(new Color(182,206,179));
                textPane.setFont(font);
                textPane.setOpaque(false);
                textPane.setCursor(new Cursor(Cursor.HAND_CURSOR));

                textPane.addHyperlinkListener(new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent e) {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            try {
                                Desktop.getDesktop().browse(URI.create(e.getURL().toString()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                                
                p1.add(textPane,BorderLayout.CENTER);
                frame.getContentPane().add(p1);
                frame.setResizable(false);
			}
		});
		btnNewButton.setFont(new Font("Berlin Sans FB", Font.PLAIN, 16));
		btnNewButton.setBackground(new Color(238, 232, 170));
		btnNewButton.setBounds(630, 434, 97, 25);
		frmKsudoku.getContentPane().add(btnNewButton);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(Color.DARK_GRAY);
		separator_2.setBounds(596, 468, 165, 2);
		frmKsudoku.getContentPane().add(separator_2);
		//frmKsudoku.getContentPane().add(contentPane);
		//frmKsudoku.setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("");
		try {
			img = ImageIO.read(this.getClass().getResource("icones/old_paper.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(0, 0, 791, 551);
		frmKsudoku.getContentPane().add(lblNewLabel);
		
		frmKsudoku.getContentPane().repaint();
		frmKsudoku.getContentPane().validate();
		/*=========================================================*/
		
		
	}

	/**
	 * Custom FocusListener for Sudoku text fields.
	 * When a text field gains focus, this listener fills it 
	 * with the correct number from the Sudoku solution grid.
	 */
	class MyFocusListener implements FocusListener {

	    // Coordinates of the text field in the 9x9 grid
	    int ii, jj;

	    /**
	     * Assigns the coordinates of the text field.
	     * @param i Row index
	     * @param j Column index
	     */
	    void addij(int i, int j) {
	        ii = i;
	        jj = j;
	    }

	    /**
	     * Called when the text field gains focus.
	     * Fills the text field with the correct value from the solution grid
	     * and updates the current active grid.
	     */
	    @Override
	    public void focusGained(FocusEvent e) {
	        tf[ii][jj].setText(Integer.toString(jeu.grille[ii][jj]));
	        grille_ac[ii][jj] = jeu.grille[ii][jj];
	    }

	    /**
	     * Called when the text field loses focus.
	     * Currently does nothing.
	     */
	    @Override
	    public void focusLost(FocusEvent e) {
	        // No action needed when focus is lost
	    }
	}
    
    /**
     * Helper method to set properties of a JTextField in the grid.
     */
	void textfield(JTextField tf)
	{
		
		tf.setFont(new Font("Tahoma", Font.PLAIN, 38));
		tf.setHorizontalAlignment(SwingConstants.CENTER);
		tf.setColumns(10);
		Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
		tf.setBorder(border);

		
	}
	
	/**
     * Chronometer class to measure elapsed time.
     */
	public class Chrono extends Thread{
	  	  int diffSecondes;
	  	  int diffMinutes;
	  	  int diffHeures;
	  	  public Chrono(JPanel p){
	  		  label = new JLabel("");
	  		  label.setFont(new Font("Courier", Font.BOLD, 22));
	  		  //tpsjoueur.setText(""+0+" : "+0+" : "+0);
	  		  label.setText("00:00");
	  		  //p.add(tpsjoueur);
	  	  }
	  	  public void run(){
	  		  while(true){
	  			try {
					this.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  			  if(!stop){
	  				t2 +=System.currentTimeMillis()-t1;
	      			int duree = (int)t2 / 1000;
	      			diffSecondes = duree%60;
	      			if(diffSecondes>=60)
	      				diffSecondes=0;
	      			diffMinutes = duree / (60);       
	      			if(diffMinutes>=60)
	      				diffMinutes=0;
	      			diffHeures = duree / (60 * 60);
	      			
	      			// Update label with elapsed time
	      			if (diffSecondes<10 && diffMinutes<10)
	      				label.setText("0"+diffMinutes+":0"+diffSecondes);
	      			else if(diffSecondes<10)
	      				label.setText(diffMinutes+":0"+diffSecondes);
	      			else if(diffMinutes<10)
	      				label.setText("0"+diffMinutes+":"+diffSecondes);
	      			 
	      			t1 = System.currentTimeMillis();
	  		  }}
	  	  }
	    }
}
