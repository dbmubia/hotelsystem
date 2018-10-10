package hotel.checkout;

import java.text.SimpleDateFormat;
import java.util.List;

import hotel.booking.BookingCTL;
import hotel.checkin.CheckinCTL;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Booking;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.ServiceCharge;
import hotel.utils.IOUtils;

public class CheckoutCTL {

	private enum State {ROOM, ACCEPT, CREDIT, CANCELLED, COMPLETED };
	
	private Hotel hotel;
	private State state;
	private CheckoutUI checkoutUI;
	private double total;
	private int roomId;
        
        private CreditCardType type;
        private int number;
        private int ccv;
        
	public CheckoutCTL(Hotel hotel) {
		this.hotel = hotel;
		this.checkoutUI = new CheckoutUI(this);
	}

	
	public void run() {
		IOUtils.trace("CheckoutCTL: run");
		state = State.ROOM;
		checkoutUI.run();
	}

	
	public void roomIdEntered(int roomId) {
		if (state != State.ROOM) {
			String mesg = String.format("CheckoutCTL: roomIdEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		this.roomId = roomId;
		BookingCTL booking = new BookingCTL(hotel);
                CheckinCTL checkin=new CheckinCTL(hotel);
					
		StringBuilder sb = new StringBuilder();
                String mesg = sb.toString();
                
		sb.append(String.format("Confirmation number for room: %d, is: ", 
				roomId));
				
		checkoutUI.displayMessage(mesg);
		state = State.ACCEPT;
		checkoutUI.setState(CheckoutUI.State.ACCEPT);	
		
	}


	public void chargesAccepted(boolean accepted) {
		if (state != State.ACCEPT) {
			String mesg = String.format("CheckoutCTL: roomIdEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
		if (!accepted) {
			checkoutUI.displayMessage("Charges not accepted");
			cancel();
		}
		else {
			checkoutUI.displayMessage("Charges accepted");
			state = State.CREDIT;
			checkoutUI.setState(CheckoutUI.State.CREDIT);
		}		
	}

	
	public void creditDetailsEntered(CreditCardType type, int number, int ccv) {
		if (state != State.CREDIT) {
			String mesg = String.format("BookingCTL: creditDetailsEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
                
                this.type=type;
                this.number=number;
                this.ccv=ccv;
                
                checkoutUI.displayCreditDetails(type,number,ccv);
                state=State.COMPLETED;
                checkoutUI.setState(CheckoutUI.State.COMPLETED);
	
	}


	public void cancel() {
		checkoutUI.displayMessage("Checking out cancelled");
		state = State.CANCELLED;
		checkoutUI.setState(CheckoutUI.State.CANCELLED);
	}
	
	
	public void completed() {
		checkoutUI.displayMessage("Checking out completed");
	}



}
