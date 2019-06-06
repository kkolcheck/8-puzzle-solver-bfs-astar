// Driver.java
// DESC: My implementation of an 8-puzzle solver using BFS and A* 
// NAME: Kevin Kolcheck
// DATE: 02/03/2019

public class Driver {

	public static void main(String[] args) {
		/* Create 3 random puzzles and solve with BFS and A* 
		 * Output stats to console */

		for (int i = 0; i < 3; i++) {
			Board puzzle = new Board();
			Board puzzle2 = new Board();
			puzzle.generatePuzzle();
			puzzle2.setState(puzzle.getStateDeepCopy());

			System.out.println("SEARCH #" + (i+1) + ":");
			puzzle.printBoardState();
			Board goalState = puzzle.breadthFirstSearch();
			System.out.println("BFS depth: " + goalState.getDepth());
			Board goalState2 = puzzle2.aStar();
			System.out.println("A* depth: " + goalState2.getDepth());
			System.out.println("");
		}//end for

		/*REMOVE COMMENTS IF YOU WANT TO SEE PATHS
			 System.out.println("SEARCH COMPLETE. PRINTING PATHS.");
			 System.out.println("BFS SEARCH: #" + (i+1));
			 goalState.printPath();
			 System.out.println("A* SEARCH: #"+ (i+1));
			 goalState2.printPath();
			 System.out.println("PATH DISPLAYED.");		
		*/
		
	}// end main
}//end Driver
