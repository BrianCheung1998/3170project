import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employee {
    String sql = null;
    String sql_marked = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Scanner reader;
    public void show_available_positions(String employeeID){
        /*
        TO DO: After the employee enteDataBase.rSet his/her Employee_ID, the system shall return all available records.
        Each record should contain the detail of the position including Position_ID,
        Position_Title, Salary, and the detail of the company including Company, Size, Founded.
        */
        try{
          sql = "SELECT P.Position_ID,P.Position_Title,P.Salary,C.Company,C.Size,C.Founded"+
                  "FROM Position_Table P,Employer E,Company C WHERE P.Employer_ID = E.Employer_ID,"+
                  "P.Company = C.Company";

          DataBase.rSet = DataBase.sta.executeQuery(sql);
          System.out.println("Table 6: fa");
          while (DataBase.rSet.next()){
            String id = DataBase.rSet.getString("id");
            String title = DataBase.rSet.getString("title");
            int salary = DataBase.rSet.getInt("salary");
            String company = DataBase.rSet.getString("company");
            int size = DataBase.rSet.getInt("size");
            int founded = DataBase.rSet.getInt("founded");

            System.out.format("%s, %s, %s, %s, %s, %s\n", id, title, salary, company, size, founded);
          }
          DataBase.sta.close();
       }
       catch(Exception e){
         System.err.println("Got an exception! ");
         System.err.println(e.getMessage());
       }

    }

    public void mark_interested_position(String employeeID){
        /*
        TO DO: Show all positions that the employee may be interested,
        then prompt the employee to mark one position as interested, and save this information.
        */
        try{
          sql = "SELECT P.Position_ID,P.Position_Title,P.Salary,C.Company,C.Size,C.Founded"+
                    "FROM Position_Table P,Employer E,Company C,Employment_History H,marked m WHERE P.Employer_ID = E.Employer_ID,"+
                    "P.Company = C.Company, H.Employee_ID = " +employeeID+", H.Position_ID != P.Position_ID,"+
                    "m.Employee_ID = "+"employeeID"+", m.Position_ID != P.Position_ID";
          DataBase.rSet = DataBase.sta.executeQuery(sql);

          while (DataBase.rSet.next()){
            String id = DataBase.rSet.getString("Position_ID");
            String title = DataBase.rSet.getString("Position_Title");
            int salary = DataBase.rSet.getInt("Salary");
            String company = DataBase.rSet.getString("Company");
            int size = DataBase.rSet.getInt("Size");
            int founded = DataBase.rSet.getInt("Founded");

            sql_marked = "INSERT INTO Registration " +
                     "VALUES (id, employeeID, true)";
            DataBase.sta.executeUpdate(sql_marked);
            System.out.format("%s, %s, %s, %s, %s, %s\n", id, title, salary, company, size, founded);
          }
          DataBase.sta.close();
       }
       catch(Exception e){
         System.err.println("Got an exception! ");
         System.err.println(e.getMessage());
       }
    }

    public int check_average_working_time(String employeeID){
        /*
        TO DO: The average working time is defined by the average of the working days
        of the last 3 records (excluding the current position he/she servers
        and return the average working time
        */
        return 0;
    }
}
