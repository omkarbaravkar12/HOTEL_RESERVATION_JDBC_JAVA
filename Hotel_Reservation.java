import java.util.Scanner;
import java.sql.*;

public class Hotel_Reservation {

    private static final String url = "jdbc:postgresql://localhost/jdbcproject";
    private static final String user = "postgres";
    private static final String pass = "root1234";

    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
        try 
        {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ie) 
        {
            ie.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) 
        {
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("============= HOTEL MANAGEMENT SYSTEM ==============");
                System.out.println("1. Reserve Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) 
                {
                    case 1:
                        reserveroom(conn, sc);
                        break;
                    case 2:
                        viewReservation(conn);
                        break;
                    case 3:
                        getRoomNumber(conn, sc);
                        break;
                    case 4:
                        updateReservation(conn, sc);
                        break;
                    case 5:
                        deleteReservation(conn, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } 
        catch (SQLException | InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    private static void reserveroom(Connection conn, Scanner sc) 
    {
        try 
        {
            sc.nextLine(); 
            System.out.print("Enter Guest Name: ");
            String guestName = sc.nextLine();

            System.out.print("Enter Room Number: ");
            int roomNumber = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Guest Phone Number: ");
            String contactNumber = sc.nextLine();

            String sql = "INSERT INTO reservation (guest_name, room_number, contact_number) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) 
            {
                ps.setString(1, guestName);
                ps.setInt(2, roomNumber);
                ps.setString(3, contactNumber);

                int row = ps.executeUpdate();
                if (row > 0) 
                {
                    System.out.println("Reservation Successful!");
                } 
                else 
                {
                    System.out.println("Reservation Failed!");
                }
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection conn) 
    {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_time FROM reservation";
        try (Statement st = conn.createStatement(); 
        ResultSet rs = st.executeQuery(sql)) 
        {
            System.out.println("Current Reservations:");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.printf("| %-14s | %-15s | %-13s | %-20s | %-19s |\n", 
                              "Reservation ID", "Guest", "Room Number", "Contact Number", "Reservation Time");
            System.out.println("-------------------------------------------------------------------------------------------");

            while (rs.next()) 
            {
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int room = rs.getInt("room_number");
                String phone = rs.getString("contact_number");
                String time = rs.getTimestamp("reservation_time").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n", id, name, room, phone, time);
            }
            System.out.println("-------------------------------------------------------------------------------------------");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static void getRoomNumber(Connection conn, Scanner sc) 
    {
        try 
        {
            System.out.print("Enter Reservation Id: ");
            int reservationId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Guest Name: ");
            String guestName = sc.nextLine();

            String sql = "SELECT room_number FROM reservation WHERE reservation_id = ? AND guest_name = ?";
         
            try (PreparedStatement ps = conn.prepareStatement(sql)) 
            {
                ps.setInt(1, reservationId);
                ps.setString(2, guestName);

                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        int room_number = rs.getInt("room_number");
                        System.out.println("Room Number for Reservation ID " + reservationId +
                                           " and Guest " + guestName + " is: " + room_number);
                    } 
                    else 
                    {
                        System.out.println("Reservation not found for given ID and Guest Name!");
                    }
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection conn, Scanner sc) 
    {
        try 
        {
            System.out.print("Enter Reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine();

            if (!reservationExists(conn, reservationId)) 
            {
                System.out.println("Reservation not found for given ID!");
                return;
            }

            System.out.print("Enter New Guest Name: ");
            String newGuestName = sc.nextLine();

            System.out.print("Enter New Room Number: ");
            int newRoomNumber = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Contact Number: ");
            String newContactNumber = sc.nextLine();

            String sql = "UPDATE reservation SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) 
            {
                ps.setString(1, newGuestName);
                ps.setInt(2, newRoomNumber);
                ps.setString(3, newContactNumber);
                ps.setInt(4, reservationId);

                int row = ps.executeUpdate();
                if (row > 0) 
                {
                    System.out.println("Reservation updated Successfully!");
                } 
                else 
                {
                    System.out.println("Reservation update failed!");
                }
            }
        } 
        catch 
        (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection conn, Scanner sc) 
    {
        try 
        {
            System.out.print("Enter Reservation Id to Delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(conn, reservationId)) 
            {
                System.out.println("Reservation not found for the given ID!");
                return;
            }

            String sql = "DELETE FROM reservation WHERE reservation_id = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) 
            {
                ps.setInt(1, reservationId);

                int row = ps.executeUpdate();
                if (row > 0) 
                {
                    System.out.println("Reservation deleted Successfully!");
                } 
                else 
                {
                    System.out.println("Failed to delete Reservation!");
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public static boolean reservationExists(Connection conn, int reservationId) 
    {
        
        String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) 
            {
                return rs.next();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException 
    {
        System.out.println("Exiting System!");
        for (int i = 5; i > 0; i--) 
        {
            System.out.println(".");
            Thread.sleep(500);
        }
        System.out.println();
        System.out.println("Dhanyavad bhau Hotel Reservation System use kelaya badal!");
    }
}
