package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.TrainService;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        System.out.println("Running IRCTC Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("there is something wrong ...");
            return;
        }
        Train trainSelectedForBooking = new Train();
        while(option!=7){
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();
            //Train trainSelectedForBooking = new Train();
            switch(option) {
                case 1:
                    System.out.println("Enter the username to signUp :");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to SignUp : ");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp),new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);
                    break;
                case 2:
                    System.out.println("Enter the username to Login :");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to Login : ");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin,passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException e) {
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetching your Bookings :");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Search Trains :\n");
                    System.out.println("Enter the source Point :");
                    String source = scanner.next();
                    System.out.println("Enter the Destination Point :");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source,dest);
                    int index = 1;
                    for(Train t : trains) {
                        System.out.println(index+ " Train Id : " + t.getTrainId());
                        for(Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("Station " + entry.getKey() + " time: " + entry.getValue());
                        }
                    }
                    System.out.println("Select a train by typing 1,2,3 ....");
                    trainSelectedForBooking = trains.get(scanner.nextInt());
                    break;
                case 5:
                    System.out.println("Select a seat out of these seats :");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for(List<Integer> row : seats) {
                        for(Integer val : row) {
                            System.out.println(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column :");
                    System.out.println("Enter the row :");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column :");
                    int column = scanner.nextInt();
                    System.out.println("Booking your Seat :");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, column);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;
                case 6:
                    System.out.println("Cancel the Ticket : \n");
                    System.out.println("Enter the  ticket ID to cancel : ");
                    String ticketId = scanner.next();
                    userBookingService.cancelBooking(ticketId);
                    break;
                default:
                    //throw new IllegalStateException("Unexpected value: " + option);
                    break;
            }
        }
    }
}