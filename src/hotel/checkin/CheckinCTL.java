package hotel.checkin;

import hotel.credit.CreditCard;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.utils.IOUtils;

public class CheckinCTL {
	
	private enum State {ENTERING,CHECKING, CONFIRMING, CANCELLED, COMPLETED };
	
	private Hotel hotel;
	private CheckinUI checkInUI;
	private State state;
	private Booking booking = null;
	private int confirmationNumber;
        private int roomNumber; 
        private boolean confirmed;

	public CheckinCTL(Hotel hotel) {
		this.hotel = hotel;
		this.state = State.ENTERING;
		this.checkInUI = new CheckinUI(this);
	}

	
	public void run() {
		IOUtils.trace("BookingCTL: run");
		checkInUI.run();
	}

        public int getConfirmationNumber(){
            return confirmationNumber;
        }
        
        public int getRoomNumber(){
            return roomNumber;
        }
        
	public void roomDetailsEntered(int roomNumber,int confirmationNumber){
            if (state != State.ENTERING) {
			String mesg = String.format("CheckinCTL: roomDetailsEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
               
            this.roomNumber=roomNumber;
            this.confirmationNumber=confirmationNumber;
            
            checkInUI.displayRoomDetails(roomNumber,confirmationNumber);
            state=State.CHECKING;
            checkInUI.setState(CheckinUI.State.CHECKING);
        }
        
        public void confirmationNumberEntered(int confirmationNumber){
            if (state != State.CHECKING) {
			String mesg = String.format("CheckinCTL: confirmationNumberEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
              
            this.confirmationNumber=confirmationNumber;
            
            checkInUI.displayConfirmationNumber(confirmationNumber);
            state=State.CONFIRMING;
            checkInUI.setState(CheckinUI.State.CONFIRMING);
        }
           
        public void checkInConfirmed(boolean confirmed){
            if (state != State.CONFIRMING) {
			String mesg = String.format("CheckinCTL: confirmed : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
              
            this.confirmed=confirmed;
            
            checkInUI.displayConfirmed();
            state=State.COMPLETED;
            checkInUI.setState(CheckinUI.State.COMPLETED);
        }
        
	public void cancel() {
		checkInUI.displayMessage("Checking in cancelled");
		state = State.CANCELLED;
		checkInUI.setState(CheckinUI.State.CANCELLED);
	}
	
	
	public void completed() {
            IOUtils.trace("CheckinCTL: completed");
		checkInUI.displayMessage("Checking in completed");
	}

}
