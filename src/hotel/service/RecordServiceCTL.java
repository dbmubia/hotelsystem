package hotel.service;

import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;
import hotel.utils.IOUtils;

public class RecordServiceCTL {
	
	private static enum State {ROOM, SERVICE, CHARGE, CANCELLED, COMPLETED};
	
	private Hotel hotel;
	private RecordServiceUI recordServiceUI;
	private State state;
	
	private Booking booking;
	private int roomNumber;
        
        private ServiceType serviceType;
        private double cost;

	public RecordServiceCTL(Hotel hotel) {
		this.recordServiceUI = new RecordServiceUI(this);
		state = State.ROOM;
		this.hotel = hotel;
	}

	
	public void run() {		
		IOUtils.trace("PayForServiceCTL: run");
		recordServiceUI.run();
	}


	public void roomNumberEntered(int roomNumber) {
		if (state != State.ROOM) {
			String mesg = String.format("PayForServiceCTL: roomNumberEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
                
		this.roomNumber = roomNumber;
                
		state = State.SERVICE;
		recordServiceUI.setState(RecordServiceUI.State.SERVICE);
		
	}
	
	
	public void serviceDetailsEntered(ServiceType serviceType, double cost) {
		if (state != State.SERVICE) {
			String mesg = String.format("PayForServiceCTL: roomNumberEntered : bad state : %s", state);
			throw new RuntimeException(mesg);
		}
                
                this.serviceType=serviceType;
                this.cost=cost;
                
                state = State.COMPLETED;
		recordServiceUI.setState(RecordServiceUI.State.COMPLETED);
	}


	public void cancel() {
		recordServiceUI.displayMessage("Pay for service cancelled");
		state = State.CANCELLED;
		recordServiceUI.setState(RecordServiceUI.State.CANCELLED);
	}


	public void completed() {
		recordServiceUI.displayMessage("Pay for service completed");
	}


	

}
