package hotel.checkin;

import java.text.SimpleDateFormat;
import java.util.Date;

import hotel.exceptions.CancelException;
import hotel.exceptions.NullInputException;
import hotel.utils.IOUtils;

public class CheckinUI {

	public static enum State {ENTERING,CHECKING, CONFIRMING, CANCELLED, COMPLETED};
	
	private CheckinCTL checkInCTL;
	private State state;
	

	public CheckinUI(CheckinCTL checkInCTL) {
		this.checkInCTL = checkInCTL;
		this.state = State.ENTERING;
	}

	
	public void run() {
		IOUtils.trace("CheckinUI: run");
		int confirmationNumber = 0;
		
		boolean completed = false;		
		while (!completed) {
			try {
				switch (state) {
                                    
				case ENTERING:
                                        RoomDetails roomDetails=enterRoomDetails();
                                        checkInCTL.roomDetailsEntered(roomDetails.roomNumber, roomDetails.confirmationNumber);
                                        break;

				case CHECKING:
					confirmationNumber = enterConfirmationNumber();
					checkInCTL.confirmationNumberEntered(confirmationNumber);
					break;
					
				case CONFIRMING:
					boolean confirmed = enterCheckinConfirmation();
					checkInCTL.checkInConfirmed(confirmed);
					break;
					
				case CANCELLED:
					completed = true;
					break;
					
				case COMPLETED:
					IOUtils.input("Hit <enter> to continue");
					checkInCTL.completed();
					completed = true;
					break;
					
				default:
					String mesg = String.format("CheckInUI: run : unknown state : %s", state);
					IOUtils.outputln(mesg);
				}
			}
			catch (CancelException e) {
				checkInCTL.cancel();
			}
		}
	}

	
	private boolean enterCheckinConfirmation() {
		IOUtils.trace("checkInUI: enterCheckinConfirmation");
		
		boolean confirmed = false;
		try {
			confirmed = IOUtils.getBooleanYesNoAnswer("Confirm checkin? ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("BookingUI: User cancelled at confirming checkin");
			throw new CancelException();
		}
		return confirmed;
	}


	private int enterConfirmationNumber() {
		IOUtils.trace("checkInUI: enterConfirmationNumber");
		
		int number = 0;
		try {
			number = IOUtils.getValidPositiveInt("Enter confirmation number you want to check: ");
		}
		catch (NullInputException e) {
			IOUtils.outputln("BookingUI: User cancelled at input confirmation number");
			throw new CancelException();
		}
		return number;
	}

        public RoomDetails enterRoomDetails(){
            IOUtils.trace("BookingUI: enterRoomDetails");
		int roomNumber = 0;
		int confirmationNumber = 0;
		IOUtils.outputln("\nEnter room details");
		
		boolean completed = false;
		while (!completed) {			
						
			//enter room number
			try {
				roomNumber = IOUtils.getValidPositiveInt("Enter room number: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User reset at input room number");
				continue;
			}	
			//enter confirmation number
			try {
				confirmationNumber = IOUtils.getValidPositiveInt("Enter confirmation number: ");
			}
			catch (NullInputException e) {
				IOUtils.outputln("BookingUI: User reset at input confirmation number");
				continue;
			}	
			completed = true;
		}		
		RoomDetails roomDetails = new RoomDetails(roomNumber, confirmationNumber);
		return roomDetails;
        }  

	public void displayCheckinMessage(String roomDescription, int roomId, 
			Date arrivalDate, int stayLength, String guestName,
			String cardVendor, int cardNumber, 
			long confirmationNumber) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		
		String message = String.format(
				"\n%s %d is booked from %s for %d nights by %s.\n" +
		        "%s Credit card number: %d\n" +
		        "Confirmation Number : %d",
		        roomDescription, roomId, 
		        format.format(arrivalDate), stayLength, 
		        guestName,
		        cardVendor, cardNumber, 
		        confirmationNumber);
		
		IOUtils.outputln(message);
	}	
		
        public void displayRoomDetails(int roomNumber,int confirmationNumber){
            String outputStr = String.format(
				"\nThe room number is %d The confirmation number is %d",
				roomNumber,confirmationNumber);
				
		IOUtils.outputln(outputStr);
        }
	
        public void displayConfirmationNumber(int confirmationNumber){
            String outputStr = String.format(
				"\nThe confirmation number checked is %d",
				confirmationNumber);
				
		IOUtils.outputln(outputStr);
        }
        
        public void displayConfirmed(){
            String outputStr = String.format(
				"\nConfirmed!");
				
		IOUtils.outputln(outputStr);
        }
        
	public void displayMessage(String message) {
		IOUtils.outputln(message);
	}
	
	public void setState(State state) {
		this.state = state;
	}

class RoomDetails{
            int roomNumber;
            int confirmationNumber;
            
            public RoomDetails(int roomNumber,int confirmationNumber){
                this.roomNumber=roomNumber;
                this.confirmationNumber=confirmationNumber;
                
            }
        }

}
