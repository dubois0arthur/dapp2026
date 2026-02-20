package hotel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookingManager extends UnicastRemoteObject implements IBookingManager{

	private Room[] rooms;

	public BookingManager() throws RemoteException {
		this.rooms = initializeRooms();
	}

	public Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		Room room = null;
		for(Room i : rooms)
		{
			if(i.getRoomNumber().equals(roomNumber)) {
				room = i;
				break;
			}
		}
		for (BookingDetail bd : room.getBookings()) {
			if (date.equals(bd.getDate())) {
				return false;
			}
		}
		return true;
	}

	public void addBooking(BookingDetail bookingDetail) {
		int roomnr = bookingDetail.getRoomNumber();
		LocalDate date = bookingDetail.getDate();
		if (!isRoomAvailable(roomnr, date)) {
			throw new IllegalStateException(
					"Room " + roomnr + " is not available on " + date
			);
		}

		for (Room r : rooms) {
			if (r.getRoomNumber().equals(roomnr)) {
				r.getBookings().add(bookingDetail);
				return;
			}
		}
	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
		Set<Integer> available = new HashSet<>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);

		for (Room r : roomIterator) {
			if (isRoomAvailable(r.getRoomNumber(), date)) {
				available.add(r.getRoomNumber());
			}
		}
		return available;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
