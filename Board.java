// Board.java

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board implements Comparable<Board> {
	protected
	Board parent;
	Board nChild;
	Board sChild;
	Board eChild;
	Board wChild;
	int bSize;
	int depth;
	int pathCost;
	int[] state;


/*****************************/
/********CONSTRUCTORS*********/
/*****************************/	

	public Board(){
		setParent(null);
		setnChild(null);
		setsChild(null);
		seteChild(null);
		setwChild(null);
		setbSize(9);
		setDepth(0);
		setState(null);
		setPathCost(0);
		
	}//end constructor
	
/*****************************/
/*****SETTERS AND GETTERS*****/
/*****************************/
	public void setParent(Board parent){
		this.parent = parent;
	}//end setParent
	
	public Board getParent(){
		return (this.parent);
	}//end getNext
	
	public void setnChild(Board child) {
		this.nChild = child;
	}//end setnChild
	
	public Board getnChild() {
		return (this.nChild);
	}//end getnChild
	
	public void setsChild(Board child) {
		this.sChild = child;
	}//end setsChild
	
	public Board getsChild() {
		return (this.sChild);
	}//end getsChild
	
	public void seteChild(Board child) {
		this.eChild = child;
	}//end seteChild
	
	public Board geteChild() {
		return (this.eChild);
	}//end geteChild
	
	public void setwChild(Board child) {
		this.wChild = child;
	}//end setwChild
	
	public Board getwChild() {
		return (this.wChild);
	}//end getwChild
	
	public void setbSize(int size) {
		this.bSize = size;
	}//end setbSize
	
	public int getbSize() {
		return (this.bSize);
	}//end getbSize
	
	public void setDepth(int depth) {
		this.depth = depth;
	}//end setbSize
	
	public int getDepth() {
		return (this.depth);
	}//end getbSize
	
	public void setState(int[] state){
		this.state = state;
	}//end setState
	
	public int[] getState(){
		return (this.state);
	}//end getState
	
	public int[] getStateDeepCopy() {
		int[] puzzle = new int[this.getbSize()];
		System.arraycopy(this.getState(), 0, puzzle, 0, this.getbSize());	//deep copy
		return (puzzle);
	}//end getStateDeepCopy
	
	//Default. Tile move cost = 1 + # of misplaced tiles
	public void setPathCost() {
		this.pathCost = (1 + this.getDepth() +  this.getMisplaced());
	}//end setPathCost
	
	//Manually set pathCost
	public void setPathCost(int cost) {
		this.pathCost = cost;
	}//end setPathCost
	
	public int getPathCost() {
		return (this.pathCost);
	}//end getPathCost
	
	//Convert the 9 numbers board state into a unique ID to identify board state
	public String getID(){
		int[] puzzle = this.getState();
		String id = "";
		for (int i = 0; i < 9; i++) { id += puzzle[i]; }				
		return (id);
	}//end getID
	
	//Get position of blank space, represented by value 0.
	public int getPos(){
		int pos = -1;
		int[] puzzle = this.getState();
		for (int i = 0; i < 9; i++) {
			if (puzzle[i] == 0) {
				pos = i;
				break;
			}
		}
		return (pos);
	}//end getPos
	
	//Get number of misplaced tiles for heuristics function
	public int getMisplaced() {
		int misplaced = 0;
		int[] puzzle = this.getState();
		for (int i = 0; i < 9; i++) {
			if (puzzle[i] != i) { misplaced++; }
		}
		return misplaced;
	}//end getMisplaced
	
/*****************************/
/********PRINT METHODS********/
/*****************************/
	
	public void printBoardState() {
		int[] puzzle = this.getState();
		int[] row = { 0,0,0 };
		for (int i = 0; i < 9; i++) {
			row[i % 3] = puzzle[i];
			if ((i+1) % 3 == 0) {
				System.out.println(row[0] + " " + row[1] + " " + row[2]);	
			}
		}
		System.out.println("");
	}//end printBoardState
	
	public void printPath() {
		Deque<Board> path = new LinkedList<Board>();
		Board curBoard = this;
		path.addLast(curBoard);
		while (curBoard.getParent() != null) {
			curBoard = curBoard.getParent();
			path.addLast(curBoard);
		}
		int count = 1;
		while(!path.isEmpty()){
			System.out.println(count + ".");
			path.peekLast().printBoardState();
			path.removeLast();
			count++;
		}
	}//end printPath	
	
/*****************************/
/**********COMPARABLE*********/
/*****************************/	

	//For priority queue. Compare path costs.
	public int compareTo(Board other) {
		if (this.distEquals(other)) { 
			return (0); 
		} else if (this.getPathCost() > other.getPathCost()) { 
			return (1); 
		} else { 
			return (-1); 
		}
	}//end compareTo
	
/*****************************/
/*******BOOLEAN CHECKS********/
/*****************************/
	
	public boolean distEquals(Board other){
		return (this.getPathCost() == other.getPathCost());
	}//end distEquals
	
	//If the board has even inversions, it is solvable.
	public boolean isPuzzleSolvable() {
		int invCount = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = i+1; j < 9; j++) {
				if (this.state[i] != 0 && this.state[j] != 0 && this.state[i] > this.state[j]) { 
					invCount++; 
				}
			}
		}
		return (invCount%2 == 0);
	}//end isPuzzleSolvable
	
	public boolean isGoalState() {
		int[] goalState = {
				0,1,2,
				3,4,5,
				6,7,8
			};
		
		for (int i = 0; i < 9; i++) {
			if (this.state[i] != goalState[i]) { return (false); }
		}
		
		return true;
	}//end isGoalState
	
	/*Take the position of blank space and what direction it is trying to move: n, s, e, w.
	 * This is how the positions are labeled:
	 *			(0) (1) (2)
	 *			(3) (4) (5)
	 *			(6) (7) (8)
	 */
	public boolean isMoveInBounds(char direction) {
		switch (direction) {
			case 'n':
				if (this.getPos() == 0 || this.getPos() == 1 || this.getPos() == 2) { return (false); }
				else { return (true); }
			case 's':
				if (this.getPos() == 6 || this.getPos() == 7 || this.getPos() == 8) { return (false); }
				else { return (true); }
			case 'e':
				if (this.getPos() == 2 || this.getPos() == 5 || this.getPos() == 8) { return (false); }
				else { return (true); }
			case 'w':
				if (this.getPos() == 0 || this.getPos() == 3 || this.getPos() == 6) { return (false); }
				else { return (true); }
			default:
				return (false);
		}
	}//end isMoveInBounds

/*****************************/
/*****GENERATIVE METHODS******/
/*****************************/	
	
	//Creates and randomizes a new puzzle for this board
	public void generatePuzzle() {
		int[] puzzle = {0,1,2,3,4,5,6,7,8};
		this.setState(puzzle);
		do { shuffleState(); } while (isPuzzleSolvable() == false);
	}//end generatePuzzle;
	
	public void shuffleState() {
		int[] newPuzzle = getState();
		Random rnd = ThreadLocalRandom.current();
		
		//randomly choose an index and swap values
		for(int i = newPuzzle.length-1; i > 0; i--) {
			int j = rnd.nextInt(i+1);
			int temp = newPuzzle[j];
			newPuzzle[j] = newPuzzle[i];
			newPuzzle[i] = temp;
		}
		setState(newPuzzle);
	}//end shuffleState

	public void createChild(int dist) {
		Board parent = this;
		Board child = new Board();
		int[] puzzle = parent.getStateDeepCopy();
		int pos = parent.getPos();
		int cDepth = (parent.getDepth() + 1);
	
		//swap positions
		puzzle[pos] = puzzle[pos + dist];
		puzzle[pos + dist] = 0;
		
		child.setParent(parent);
		child.setDepth(cDepth);
		child.setState(puzzle);
		child.setPathCost();
		
		//add child to parent
		if (dist == -3) { parent.setnChild(child); }
		else if (dist == 3) { parent.setsChild(child); }
		else if (dist == 1) { parent.seteChild(child); }
		else if (dist == -1) { parent.setwChild(child); }
		else { System.out.println("Incorrect distance sent to child."); }
	} //end createChild
	
	public void expandBoard() {
		if (isMoveInBounds('n')) {
			createChild(-3);
		}
		if (isMoveInBounds('s')) { 
			createChild(3);
		}
		if (isMoveInBounds('e')) { 
			createChild(1);
		}
		if (isMoveInBounds('w')) { 
			createChild(-1);
		}	
	}//end expandBoard

/*****************************/
/********SEARCH METHODS*******/
/*****************************/
	
	//followed book algorithm, pg. 82
	public Board breadthFirstSearch() {
		//Keep known but unexplored board states in a queue
		Deque<Board> frontier = new LinkedList<Board>();
		
		//Store string IDs in HashSet for quick look up
		LinkedHashSet<String> frontierID = new LinkedHashSet<String>();
		HashSet<String> exploredID = new HashSet<String>();
		String xID = "";
		
		Board curBoard = this;
		if (curBoard.isGoalState()) { return (curBoard); }	//check goal

		frontier.addLast(curBoard);
		frontierID.add(curBoard.getID());
		int count = 0;
		do {
			count++;
			
			//get new board state, update frontier/explored lists
			curBoard = frontier.peekFirst();
			frontier.removeFirst();
			xID = frontierID.iterator().next();
	        frontierID.remove(xID);
			exploredID.add(curBoard.getID());
			
			//expand current board by creating any possible children
			curBoard.expandBoard();
			
			//Create a queue of all the new child nodes
			Deque<Board> childQueue = new LinkedList<Board>();
			if (curBoard.getnChild() != null) { childQueue.addLast(curBoard.getnChild()); };
			if (curBoard.getsChild() != null) { childQueue.addLast(curBoard.getsChild()); };
			if (curBoard.geteChild() != null) { childQueue.addLast(curBoard.geteChild()); };
			if (curBoard.getwChild() != null) { childQueue.addLast(curBoard.getwChild()); };
			
			//Check that each child is unique, if so check goal state. If not the goal, push onto frontier queue.
			Iterator<Board> iterator = childQueue.iterator(); 
	        while (iterator.hasNext()) {
	        	Board child = iterator.next();
				if (!frontierID.contains(child.getID()) && !exploredID.contains(child.getID())) {
					if (child.isGoalState()) { 
						System.out.println("BFS explored: " + count + " board states.");
						return (child); 
					} else {
						frontier.addLast(child);
						frontierID.add(child.getID());
					}//end if
				}//end if
	        }//end while
		} while (!frontier.isEmpty());
		//Failed to find a solution. This should not happen as each puzzle is checked if solvable, exit with error 
		System.out.println("Error occured, exiting");
		System.exit(-1);
		return (this);
	}//end breadthFirstSearch
	
	//algorithm like uniform-cost search, using tiles misplaced as heuristic. See book pgs. 84,94,95
	public Board aStar() {
		//Keep known but unexplored board states in a priority queue, by path cost
		PriorityQueue<Board> frontier = new PriorityQueue<Board>();
		
		//Store string IDs in separate HashSet for quick look up
		HashSet<String> frontierID = new HashSet<String>();
		HashSet<String> exploredID = new HashSet<String>();		
		
		//Add board to tracking structures
		Board curBoard = this;
		curBoard.setPathCost(0); //start node has a path cost of 0
		frontier.add(curBoard);
		frontierID.add(curBoard.getID());
		int count = 0;
		do {
			count++;
			//get new board state and remove from pQueue
			curBoard = frontier.poll();
	        frontierID.remove(curBoard.getID());
	        
	        if (curBoard.isGoalState()) { 
	        	System.out.println("A* explored: " + count + " board states.");
	        	return (curBoard); 
	        }
	        
			exploredID.add(curBoard.getID());	//add to explored
			
			//expand current board by creating any possible children
			curBoard.expandBoard();
			
			//Create a queue of all the new child nodes
			Deque<Board> childQueue = new LinkedList<Board>();
			if (curBoard.getnChild() != null) { childQueue.addLast(curBoard.getnChild()); };
			if (curBoard.getsChild() != null) { childQueue.addLast(curBoard.getsChild()); };
			if (curBoard.geteChild() != null) { childQueue.addLast(curBoard.geteChild()); };
			if (curBoard.getwChild() != null) { childQueue.addLast(curBoard.getwChild()); };
			
			//Check that each child is unique. If unique, push onto frontier queue.
			Iterator<Board> iterator = childQueue.iterator(); 
	        while (iterator.hasNext()) {
	        	Board child = iterator.next();
	        	if (!frontierID.contains(child.getID()) && !exploredID.contains(child.getID())) {
	        		frontier.add(child);
	        		frontierID.add(child.getID());
	        	}
	        }//end while
	        
		} while (!frontier.isEmpty());
		//Failed to find a solution. This should not happen as each puzzle is checked to be solvable, exit with error 
		System.out.println("Error occured, exiting");
		System.exit(-1);
		return (this);
	}//end aStar
	
}//end Board class
