package assignment2;

public class Warehouse{

	protected Shelf[] storage;
	protected int nbShelves;
	public Box toShip;
	public UrgentBox toShipUrgently;
	static String problem = "problem encountered while performing the operation";
	static String noProblem = "operation was successfully carried out";

	public Warehouse(int n, int[] heights, int[] lengths){
		this.nbShelves = n;
		this.storage = new Shelf[n];
		for (int i = 0; i < n; i++){
			this.storage[i]= new Shelf(heights[i], lengths[i]);
		}
		this.toShip = null;
		this.toShipUrgently = null;
	}

	public String printShipping(){
		Box b = toShip;
		String result = "not urgent : ";
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n" + "should be already gone : ";
		b = toShipUrgently;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}

	public String print(){
		String result = "";
		for (int i = 0; i < nbShelves; i++){
			result += i + "-th shelf " + storage[i].print();
		}
		return result;
	}

	public void clear(){
		toShip = null;
		toShipUrgently = null;
		for (int i = 0; i < nbShelves ; i++){
			storage[i].clear();
		}
	}

	/**
	 * initiate the merge sort algorithm
	 */
	public void sort(){
		mergeSort(0, nbShelves -1);
	}

	/**
	 * performs the induction step of the merge sort algorithm
	 * @param start
	 * @param end
	 */
	protected void mergeSort(int start, int end){
		//ADD YOUR CODE HERE


		if (start<end) {
			int  mid = (start + end )/2;


			mergeSort(start, mid);
			mergeSort(mid +1,end);

			merge(start, mid, end);

		}

	}

	/**
	 * performs the merge part of the merge sort algorithm
	 * @param start
	 * @param mid
	 * @param end
	 */
	protected void merge(int start, int mid, int end){
		//ADD YOUR CODE HERE

		int lengthL = mid - start+1;
		int lengthR = end - mid;
		Shelf[] L= new Shelf[lengthL];
		Shelf[] R = new Shelf[lengthR];

		for ( int i = 0; i<lengthL; i++) {

			L[i]= storage[start + i];


		}

		for ( int j=0; j<lengthR; j++) {

			R[j]= storage[mid +j+1];

		}

		int i =0;
		int j=0;
		int k=start;

		while (i < lengthL && j < lengthR ) {
			if (L[i].height <= R[j].height) {
				storage[k]= L[i];
				i++;
				k++;

			}
			else {
				storage[k]=R[j];
				j++;
				k++;


			}	
		}
		if(j<lengthR) {
			for ( ; j<lengthR ; j++, k++) {
				storage[k]= R[j];

			}
		}
		else {
			for ( ; i<lengthL ; i++, k++) {

				storage[k]= L[i];

			}

		}

	}


	/**
	 * Adds a box is the smallest possible shelf where there is room available.
	 * Here we assume that there is at least one shelf (i.e. nbShelves >0)
	 * @param b
	 * @return problem or noProblem
	 */
	public String addBox (Box b){
		this.sort();
		int i=0;
		boolean added=false;

		for(i=0;added==false && i<this.nbShelves;i++) {
			if(this.storage[i].height -b.height>=0 && this.storage[i].availableLength - b.length >= 0){
				this.storage[i].addBox(b);
				added=true;
			}

		}

		if (added=true) {
			return noProblem;
		}
		else {return problem;}
	}

	/**
	 * Adds a box to its corresponding shipping list and updates all the fields
	 * @param b
	 * @return problem or noProblem
	 */
	public String addToShip (Box b){
		//ADD YOUR CODE HERE

		boolean typeOfBox = b instanceof UrgentBox;
		if( b ==null) {
			return problem;
		}


		if (typeOfBox == true) {//if box is an urgentbox
			if (toShipUrgently != null) {
				toShipUrgently.previous = b;
				b.next = toShipUrgently;
				b.previous = null;
			}
			toShipUrgently = (UrgentBox) b;

		}


		else {
			if (toShip != null) {//if box is a normal one
				toShip.previous = b;
				b.next = toShip;
				b.previous = null;

			}
			toShip =  b;

		}


		return noProblem;
	}

	/**
	 * Find a box with the identifier (if it exists)
	 * Remove the box from its corresponding shelf
	 * Add it to its corresponding shipping list
	 * @param identifier
	 * @return problem or noProblem
	 */
	public String shipBox (String identifier){
		//ADD YOUR CODE HERE


		String check ="";
		boolean found=false;
		int i=0;

		while ( i<nbShelves) { //to go through all the shelfs
			Box clientBox = storage[i].removeBox(identifier);

			if(clientBox == null) {//if box not found in the particular shelf increment to the next one


				i++;

			}
			else {//if box found then add it to ship
				found = true;

				check = addToShip( clientBox);


				break;

			}
		}
		if (found == true && check.equals(noProblem)) {//if the box was found and correctly added to the ship
			return noProblem;
		}
		else {
			return problem;
		}
	}

	/**
	 * if there is a better shelf for the box, moves the box to the optimal shelf
	 * If there are none, do not do anything
	 * @param b
	 * @param position
	 */
	public void moveOneBox (Box b, int position){
		//ADD YOUR CODE HERE
		int i=0;
		while( i<nbShelves) {
			if ((storage[i].height < storage[position].height) && (storage[i].height >= b.height)&& (storage[i].availableLength >= b.length)) {//If there is a Shelf with a smaller height where the Box would fit t, move the
				// move the Box to the end of that new Shelf
				storage[position].removeBox(b.id);

				storage[i].addBox(b);

				break;

			}
			else {
				i++;
			}
		}
	}

	/**
	 * reorganize the entire warehouse : start with smaller shelves and first box on each shelf
	 */
	public void reorganize (){
		//ADD YOUR CODE HERE

		for (int j=0; j<nbShelves;j++) {
			System.out.println(storage[j].print());
		}
		System.out.println();

		Box nextBox;
		Box currentBox;
		for (int j=0; j<nbShelves;j++) {//to go through all the shelfs

			currentBox = storage[j].firstBox;

			if (currentBox ==null ) {
				continue;


			}

			while(currentBox != null) {//if box exist

				nextBox = currentBox.next;
				moveOneBox(currentBox, j);
				currentBox= nextBox;


			}
			System.out.println(j);
			for (int i=0; i<nbShelves;i++) {
				System.out.println(storage[i].print());
			}
			System.out.println();

		}


	}
}