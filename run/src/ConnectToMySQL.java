import java.sql.*;
import java.io.*;
import java.util.*;


public class ConnectToMySQL{

    public Connection con = null;
    public Statement sta = null;
    public ResultSet rSet = null;
    public PreparedStatement p_sta = null;


    public ConnectToMySQL() {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db26";
        String dbUsername = "Group26";
        String dbPassword = "group26";


        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress,dbUsername,dbPassword);
            System.out.println("Connected database successfully.");
        }catch(ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!");
            System.exit(0);
        }catch(SQLException e){
            System.out.println(e);
        }
    }
}
