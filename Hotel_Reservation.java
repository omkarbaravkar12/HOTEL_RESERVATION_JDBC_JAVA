import java.util.Scanner;
import java.sql.*;
import java.util.*;

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
                    break;
            }
            }
            catch(SQLException i)
            {
                i.printStackTrace();    
            }
         }


    }
    
}
