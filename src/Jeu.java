/* 
 * Class: Jeu
 * Author: Khaled KADRI
 * Purpose: Implements the core logic for generating, validating, and solving Sudoku grids.
 */


import java.util.ArrayList;
import java.util.Random;

public class Jeu {

	// Difficulty flags
    static boolean facile = true, moyen, difficile;

    // Sudoku solution grid
    static int grille[][] = new int[9][9];
    // Sudoku game grid (with some cells removed)
    static int grilleJeu[][] = new int[9][9];

    // List of error positions when validating the user's input
    static ArrayList<int[]> erreur;

    // Temporary variables used in validation
    int xp;
    static int yp;

    // Timer for grid generation/validation
    long t1;

    /**
     * Generate the playable Sudoku grid by removing cells according to difficulty
     */
	static void genererGrilleJeu(){
		// Copy the full solution into the game grid
		for(int i= 0 ; i<9;i++){
			for(int j=0;j<9;j++){
				grilleJeu[i][j]=grille[i][j];
			}
		}
		int x, y, casevide = 0;
		
		// Number of empty cells based on difficulty
		if(facile)casevide=30;
		else if(moyen)casevide=40;
		else if(difficile)casevide=50;
		
		// Remove cells randomly
		for(int p= 0 ; p<casevide; p++){
			do{
				x=generer(9, false);
				y=generer(9, false);
			}while(grilleJeu[x][y]==0);
			grilleJeu[x][y]=0;
		}
		
	}
	
	/**
     * Generate initial numbers in the solution grid
     */
	void genererGrille(int grille[][]){
		int x, y, nb;
		for(int p= 0 ; p<20; p++){
			do{
				x=generer(9, false);
				y=generer(9, false);
			}while(grille[x][y]!=0);
			nb=generer(10, true);
			// Place the number only if it's valid in row, column, and block
			if(absentSurBloc(nb, grille, x, y)
				&&absentSurColonne(nb, grille, y)
					&&absentSurLigne(nb, grille, x)){
				grille[x][y]=nb;
			}
		}
	}	
	
	 /**
     * Check if number k is absent on row i
     */
	boolean absentSurLigne (int k, int grille[][], int i)
	{
	    for (int j=0; j < 9; j++)
	        if (k!=0 && grille[i][j] == k){
	            yp=j;  // Save column of conflict
	        	return false;
	        }
	    return true;
	}

	/**
     * Check if number k is absent on column j
     */
	boolean absentSurColonne (int k, int grille[][], int j)
	{
	    for (int i=0; i < 9; i++)
	        if (k!=0 && grille[i][j] == k){
	            xp=i; // Save row of conflict
	        	return false;
	        }
	    return true;
	}

	/**
     * Check if number k is absent in the 3x3 block containing cell (i,j)
     */
	boolean absentSurBloc (int k, int grille[][], int i, int j)
	{
	    int _i = i-(i%3), _j = j-(j%3);  // or : _i = 3*(i/3), _j = 3*(j/3);
	    for (i=_i; i < _i+3; i++)
	        for (j=_j; j < _j+3; j++)
	            if (k!=0 && grille[i][j] == k){
		            xp=i;yp=j;
		        	return false;
		        }
	    return true;
	}
	
	/**
     * Recursive method to validate a grid by trying all possible numbers
     * Uses backtracking to fill empty cells
     */
	boolean estValide (int grille[][], int position)
	{
	    if (position == 9*9)
	        return true; // Base case: all cells filled

	    int i = position/9, j = position%9;

	    if (grille[i][j] != 0)
	        return estValide(grille, position+1);

	    for (int k=1; k <= 9; k++)
	    {
	        if (absentSurLigne(k,grille,i) && absentSurColonne(k,grille,j) && absentSurBloc(k,grille,i,j))
	        {
	            grille[i][j] = k;

	            if ( estValide (grille, position+1) )
	                return true;
	        }
	    }
	    
	 // Restart grid if validation takes too long
	    if(System.currentTimeMillis()-t1>1000)
	    	init();
	    grille[i][j] = 0;
	    return false;
	}
	
	
	
	/*************************************************/
    // Similar methods with index exclusions (used for checking user's input)
	boolean absentSurLigne2 (int k, int grille[][], int i,int j)
	{
	    for (int j1=0; j1 < 9; j1++)
	        if (j1!=j && grille[i][j1] == k){
	            yp=j1;
	        	return false;
	        }
	    return true;
	}

	boolean absentSurColonne2 (int k, int grille[][], int i, int j)
	{
	    for (int i1=0; i1 < 9; i1++)
	        if (i1!=i && grille[i1][j] == k){
	            xp=i1;
	        	return false;
	        }
	    return true;
	}

	boolean absentSurBloc2 (int k, int grille[][], int i, int j)
	{
		int ii=i,jj=j;
	    int _i = i-(i%3), _j = j-(j%3);  // ou encore : _i = 3*(i/3), _j = 3*(j/3);
	    for (i=_i; i < _i+3; i++)
	        for (j=_j; j < _j+3; j++)
	            if (i!=ii && j!=jj && grille[i][j] == k){
		            xp=i;yp=j;
		        	return false;
		        }
	    return true;
	}
	
	/**
     * Validate the user's grid and populate the error list
     */
	void estValide2 (int grille[][], int position)
	{
		for(int i = 0 ; i < 9; i++){
			for(int j = 0 ; j < 9; j++){
				if (!absentSurLigne2(grille[i][j],grille,i,j) || 
						!absentSurColonne2(grille[i][j],grille,i,j) || 
							!absentSurBloc2(grille[i][j],grille,i,j)){
			    	int[] err = new int[2];
			    	err[0]=i;err[1]=j;
			    	erreur.add(err);
				}
			}
		}
	}
	
	/**
     * Random number generator helper
     * @param x upper bound
     * @param zero whether 0 should be avoided
     */
	static int generer(int x,boolean zero){
        Random random = new Random();
        int nb;
        nb = random.nextInt(x);
        if(nb==0 && zero)
            nb=1;
        return nb;
    }
	
	/**
     * Print the grid to console (for debugging)
     */
	static void affichage(int[][] grille){
		for(int i= 0 ; i<9;i++){
			for(int j=0;j<9;j++){
				System.out.print(grille[i][j]+"  ");
			}
			System.out.println("");
		}
	}
	
	/**
     * Initialize the Sudoku game and solution grids
     */
    void init() {
        // Clear grids
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                grille[i][j] = 0;
                grilleJeu[i][j] = 0;
            }
        }

        // Generate initial numbers
        genererGrille(grille);

        // Record generation start time
        t1 = System.currentTimeMillis();
        boolean incomplete;

        // Ensure the solution grid is valid (no zeros remain)
        do {
            incomplete = false;
            estValide(grille, 0);
            for(int i = 0; i < 9; i++)
                for(int j = 0; j < 9; j++)
                    if(grille[i][j] == 0) incomplete = true;
        } while(incomplete);

        // Generate the playable grid by removing cells
        genererGrilleJeu();
    }

}
