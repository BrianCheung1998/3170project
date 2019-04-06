import java.io.*;
import java.util.*;
//import java.sql.*;
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
                  "P.Company = C.Company, P.Status = true";
          DataBase.sta = DataBase.con.createStatement();
          DataBase.rSet = DataBase.sta.executeQuery(sql);
          //System.out.println("Table 6: fa");
          while (DataBase.rSet.next()){
            String id = DataBase.rSet.getString("Position_ID");
            String title = DataBase.rSet.getString("Position_Title");
            int salary = DataBase.rSet.getInt("Salary");
            String company = DataBase.rSet.getString("Company");
            int size = DataBase.rSet.getInt("Size");
            int founded = DataBase.rSet.getInt("Founded");

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

          DataBase.sta = DataBase.con.createStatement();
          sql = "SELECT P.Position_ID,P.Position_Title,P.Salary,C.Company,C.Size,C.Founded"+
                    "FROM Position_Table P,Employer E,Company C,Employment_History H,marked m WHERE P.Employer_ID = E.Employer_ID,"+
                    "P.Company = C.Company,P.Status = true, H.Employee_ID = "+employeeID+", H.Position_ID != P.Position_ID,"+
                    "m.Employee_ID = "+"employeeID"+", m.Position_ID != P.Position_ID";
          DataBase.rSet = DataBase.sta.executeQuery(sql);

          while (DataBase.rSet.next()){
            String id = DataBase.rSet.getString("Position_ID");
            String title = DataBase.rSet.getString("Position_Title");
            int salary = DataBase.rSet.getInt("Salary");
            String company = DataBase.rSet.getString("Company");
            int size = DataBase.rSet.getInt("Size");
            int founded = DataBase.rSet.getInt("Founded");

            sql_marked = "INSERT INTO Registration (Position_ID,Employee_ID,Status)" +
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
        sql = "select * from ( select * from Employment_History "+
              "where Employee_ID = '"+employeeID+"' order by End DESC LIMIT 3  ) t order by End ASC";
        String sql_employmentHistory = "SELECT COUNT(*) AS total FROM Employment_History where Employee_ID = '"+employeeID+"'";
        int count1 = 0;
        long temp = 0;
        try {
              DataBase.sta = DataBase.con.createStatement();
              DataBase.rSet = DataBase.sta.executeQuery(sql_employmentHistory);
              while (DataBase.rSet.next()){
                count1 = DataBase.rSet.getInt("total");
              }
              if (count1 >= 4){
                DataBase.rSet = DataBase.sta.executeQuery(sql);

                while (DataBase.rSet.next()){
                  long count = 0;
                  Date Start = dateFormat.parse(DataBase.rSet.getString("Start"));
                  Date  End = dateFormat.parse(DataBase.rSet.getString("End"));
                  count = End.getTime() - Start.getTime();
                  count = count / (1000*60*60*24);
                  temp = temp+count;
                }
                DataBase.sta.close();
                temp = temp / 3;
                int temp1 = (int)temp;
                return temp1;
             }
             else {return 0;}

           }
           catch(Exception e){
             System.err.println("Got an exception! ");
             System.err.println(e.getMessage());
           }











        return 0;
    }
}
