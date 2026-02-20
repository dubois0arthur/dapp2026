package hotel;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BookingServer {
    public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
        BookingManager bookingManager = new BookingManager();
        Registry registry = LocateRegistry.createRegistry(6666);
        registry.bind("booking_server", bookingManager);
        System.out.println("RMI Server started");
    }
}
