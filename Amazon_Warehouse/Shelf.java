package assignment2;


public class Shelf {

	protected int height;
	protected int availableLength;
	protected int totalLength;
	protected Box firstBox;
	protected Box lastBox;

	public Shelf(int height, int totalLength){
		this.height = height;
		this.availableLength = totalLength;
		this.totalLength = totalLength;
		this.firstBox = null;
		this.lastBox = null;
	}

	protected void clear(){
		availableLength = totalLength;
		firstBox = null;
		lastBox = null;
	}

	public String print(){
		String result = "( " + height + " - " + availableLength + " ) : ";
		Box b = firstBox;
		while(b != null){
			result += b.id + ", ";
			b = b.next;
		}
		result += "\n";
		return result;
	}

	/**
	 * Adds a box on the shelf. Here we assume that the box fits in height and length on the shelf.
	 * @param b
	 */
	public void addBox(Box b){//can we add from the beginning too?
		//ADD YOUR CODE HERE


		if(lastBox==null) {//queue is empty : we can  check it
			firstBox = b;
			lastBox = b;
			this.availableLength -= b.length;

		}else   //when the queue is not empty 
		{


			lastBox.next = b;   //previous  of lastBox points now   to newBox
			b.previous = lastBox;  //next of newbox points now to lastBox
			lastBox = b;//lastBox is now moved to newBox 
			b.next= null;

			//  the new name of newBox is lastBox because when inserted in 
			//the back newBox becomes the first element of the queue
			this.availableLength -= b.length;
		}
	}

	/**
	 * If the box with the identifier is on the shelf, remove the box from the shelf and return that box.
	 * If not, do not do anything to the Shelf and return null.
	 * @param identifier
	 * @return
	 */
	public Box removeBox(String identifier){

		Box checkingStep = this.firstBox; 

		Box temporaryBox = this.firstBox; 

		while (checkingStep != null) { 

			if (checkingStep.id.equals(identifier) == true) { 

				if (checkingStep == this.firstBox && checkingStep == this.lastBox) { // one box in list

					this.firstBox = null;
					this.lastBox = null;
					this.availableLength = this.availableLength + checkingStep.length;
					break;

				} else if (checkingStep == this.firstBox) { // first box in list

					this.firstBox = checkingStep.next;
					this.firstBox.previous = null;
					checkingStep.next = null;
					this.availableLength = this.availableLength + checkingStep.length;
					break;

				} else if (checkingStep.next == null) { //last box in list

					this.lastBox = checkingStep.previous;
					this.lastBox.next = null;
					checkingStep.previous = null;
					this.availableLength = this.availableLength + checkingStep.length;
					break;

				} else { //middle box in list

					temporaryBox = checkingStep.previous;
					temporaryBox.next = checkingStep.next;

					temporaryBox = checkingStep.next;
					temporaryBox.previous = checkingStep.previous;

					checkingStep.next = null;
					checkingStep.previous = null;

					this.availableLength = this.availableLength + checkingStep.length;

					break;

				}
			}

			checkingStep = checkingStep.next; // to verify next box in shelf

		}

		return checkingStep; 

	}
}


