import java.util.Scanner;
import java.sql.*;


public class Hotel_Reservation {

    private static final String url = "jdbc:postgresql://localhost/jdbc";
    private static final String user = "postgres";
    private static final String pass = "root1234";

    public static void main(String[] args) throws ClassNotFoundException,SQLException {
         try
         {
            Class.forName("org.postgresql.Driver");
         }
         catch(ClassNotFoundException ie)
         {
            ie.printStackTrace();
         }

         try
         {
            Connection conn = DriverManager.getConnection(url, user, pass);
            while(true)
            {
            System.out.println();
            System.out.println("=============HOTEL MANAGEMENT SYSTEM==============");
            Scanner sc = new Scanner(System.in);
            System.out.println("1.Reserve Room");
            System.out.println("2.View Reservation");
            System.out.println("3.Get Room Number");
            System.out.println("4.Update Reservation");
            System.out.println("5.Delete Reservation");
            System.out.println("0.Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:reserveroom(conn,sc);
                    break;

                case 2:viewReservation(conn);
                    break;

                case 3:getRoomNumber(conn,sc);
                    break;
                
                case 4:updateReservation(conn,sc);
                    break;

                case 5:deleteReservation(conn,sc);
                    break;

                case 0: 
                    exit(); 
                    sc.close();
                    return; 

                default: System.out.println("Inavlid choice!");
                    
            }
        }
    }
            catch(SQLException i)
            {
                System.out.println(i.getMessage());
            }
            catch(InterruptedException i)
            {
                throw new RuntimeException(i);
            }
         }

         private static void reserveroom(Connection conn,Scanner sc)
         {
            try
            {
            System.out.println("Enter Guest Name: ");
            String guestName = sc.nextLine();
            sc.nextLine();

            System.out.println("Enter Room Number: ");
            int roomNumber = sc.nextInt();

            System.out.println("Enter Guest Phone Number: ");
            String contactNumber = sc.next();

            String sql = "INSERT INTO reservation VALUES(guest_name , room_number, contact_number) " +"VALUES('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";
            
            try(Statement st = conn.createStatement())
            {
             int row = st.executeUpdate(sql);

             if(row > 0)
             {
                System.out.println("Reservation Succesfull!");
             }
             else
             {
                System.out.println("Reservation Failed!");
             }
            }
         }
         catch(SQLException e)
         {
            e.printStackTrace();
         }


    }

    private static void viewReservation(Connection conn) throws SQLException
    {
        String sql = "SELECT reservation_id, guest_name, room_number,contact_number, reservation_time FROM reservation";

        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("Current Reservations: ");
            System.out.println("----------------+------------+----------------+----------------+---------------------+");
            System.out.println("|Reservation id | Guest      | Room Number    | Contact Number | Reservation Time    |");
            System.out.println("----------------+------------+----------------+----------------+---------------------+");

            while (rs.next()) 
            {
                int ReservationId = rs.getInt("reservation_id");
                String GuestName = rs.getString("guest_name");
                int RoomNumber = rs.getInt("room_number");
                String ContactNumber =rs.getString("contact_number");
                String ReservationTime = rs.getTimestamp("reservation_time").toString();
                
                //format table GPT
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",ReservationId,GuestName,RoomNumber,ContactNumber,ReservationTime);
            }   

                System.out.println("----------------+------------+----------------+----------------+---------------------+");

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    } 


    private static void getRoomNumber(Connection conn,Scanner sc)
    {
        try
        {
        System.out.println("Enter Reservation Id:");
        int ReservationId = sc.nextInt();

        System.out.println("Enter Guest Name: ");
        String guestName = sc.next();

        String sql = "SELECT room_number FROM reservation" +
                     "WHERE reservation_id = " + ReservationId +
                     "AND guest_name = '" +guestName+ "'";

        try(Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql))
            {

            if(rs.next())
            {
                int room_number = rs.getInt("room_number");
                System.out.println("Room Number for Reservation ID "+ReservationId +"and Guest" +guestName+"is:"+room_number);
            }
            else
            {
                System.out.println("Reservation not found for given Guest ID and Guest Name!");
            }
        }
    }
    catch(SQLException i)
    {
        i.printStackTrace();
    }
}


    private static void updateReservation(Connection conn,Scanner sc)
    {
        try{
        System.out.println("Enter Reservation ID to update: ");
        int ReservationId = sc.nextInt();
        sc.nextLine();

        if(!reservationExists( conn,ReservationId))
        {
            System.out.println("Reservation not found for given ID:");
            return;
        }

        System.out.println("Enter New Guest Name: ");
        String newGuestName = sc.nextLine();

        System.out.println("Enter New Room Number: ");
        int newRoomNumber = sc.nextInt();

        System.out.println("Enter new contact number: ");
        String newContactNumber = sc.next();
            
        String sql = "UPDATE reservtion SET guest_name = '" +newGuestName + "',"+"room_number = "+ newRoomNumber+","+"contact_number = '"+newContactNumber+"'"+"WHERE reservation_id = "+ ReservationId;
        
        try(Statement st = conn.createStatement())
        {
            int row = st.executeUpdate(sql);

            if(row > 0)
            {
                System.out.println("Reservation updated Succesfully!");
            }
            else
            {
                System.out.println("Reservation update failed!");
            }

        }
    }catch(SQLException i)
    {
        i.printStackTrace();
    }
}

    private static void deleteReservation(Connection conn,Scanner sc)
    {
        try
        {
            System.out.println("Enter Reservation Id to Delete Reservation:");
            int ReservationId = sc.nextInt();

            if(!reservationExists(conn,ReservationId))
            {
                System.out.println("Reservation not found for the given ID!");
            }
            
            String sql = "DELETE FROM reservation WHERE reservation_id = "+ReservationId;

            try(Statement st = conn.createStatement())
            {
                int row = st.executeUpdate(sql);

                if(row > 0)
                {
                    System.out.println("Reseravtion done Succesfully!");  
                }
                else
                {
                    System.out.println("Failed to delete Reservation!");
                }
            }

        }
        catch(SQLException i)
        {
            i.printStackTrace();
        }
    }

    public static boolean reservationExists(Connection conn,int ReservationId)
    {
        String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = "+ReservationId;


        try(Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql))
        {
           return rs.next();

        }
        
        catch(SQLException se)
        {
            se.printStackTrace();
            return false;
        }
        
    }

    public static void exit() throws InterruptedException
    {
        System.out.println("EXiting System!");
        int i = 5;

        while(i!=0)
        {
            System.out.println(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Dhanyavad bhau Hotel Reservation System use kelaya badal!");    
    }


}
    

