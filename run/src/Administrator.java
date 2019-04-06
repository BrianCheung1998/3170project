import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Administrator{
    String sql_employee = null;
    String sql_company  = null;
    String sql_employer = null;
    String sql_position = null;
    String sql_employmentHistory = null;
    String sql_marked = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Scanner reader;

    /**
     * Table Structure reference: phase 1 - solution
     */
    public void create_table(){
        //TABLE 1: Employee - employee.csv
        sql_employee = "CREATE TABLE IF NOT EXISTS Employee("+
        "Employee_ID char(6) NOT NULL,"+
        "Name char(30) NOT NULL,"+
        "Expected_Salary int NOT NULL,"+
        "Experience int NOT NULL,"+
        "Skills char(50) NOT NULL,"+
        "PRIMARY KEY(Employee_ID)"+")"
        ;
        //TABLE 2: Company – company.csv
        sql_company = "CREATE TABLE IF NOT EXISTS Company("+
        "Company char(30) NOT NULL,"+
        "Size int NOT NULL,"+
        "Founded int NOT NULL,"+
        "PRIMARY KEY(Company)"+")"
        ;
        //TABLE 3: Employer – employer.csv
        sql_employer = "CREATE TABLE IF NOT EXISTS Employer("+
        "Employer_ID char(6) NOT NULL,"+
        "Name char(30) NOT NULL,"+
        "Company char(30) NOT NULL,"+
        "PRIMARY KEY(Employer_ID),"+
        "FOREIGN KEY(Company) REFERENCES Company(Company)"+")"
        ;
        //TABLE 4: Position – position.csv
        sql_position = "CREATE TABLE IF NOT EXISTS Position_Table("+
        "Position_ID char(6) NOT NULL,"+
        "Position_Title char(30) NOT NULL,"+
        "Salary Integer NOT NULL,"+
        "Experience Integer NOT NULL,"+
        "Employer_ID char(6) NOT NULL,"+
        "Status boolean NOT NULL,"+
        "PRIMARY KEY(Position_ID),"+
        "FOREIGN KEY(Employer_ID) REFERENCES Employer(Employer_ID)"+")"
        ;
        //System.out.println()
        // TABLE 5: Employment History – history.csv
        sql_employmentHistory = "CREATE TABLE IF NOT EXISTS Employment_History("+
        "Employee_ID char(6) NOT NULL,"+
        "Position_ID char(6) NOT NULL,"+
        "Start date NOT NULL,"+
        "End date,"+
        "PRIMARY KEY(Position_ID),"+
        "FOREIGN KEY(Employee_ID) REFERENCES Employee(Employee_ID)"+")"
        ;

        sql_marked = "CREATE TABLE IF NOT EXISTS marked("+
        "Position_ID char(6) NOT NULL,"+
        "Employee_ID char(6) NOT NULL,"+
        "Status boolean NOT NULL,"+
        "PRIMARY KEY(Position_ID, Employee_ID),"+
        "FOREIGN KEY(Position_ID) REFERENCES Position_Table(Position_ID),"+
        "FOREIGN KEY(Employee_ID) REFERENCES Employee(Employee_ID)"+")"
        ;

        try{
            DataBase.sta = DataBase.con.createStatement();
            DataBase.sta.executeUpdate(sql_employee);
            DataBase.sta.executeUpdate(sql_company);
            DataBase.sta.executeUpdate(sql_employer);
            DataBase.sta.executeUpdate(sql_position);
            DataBase.sta.executeUpdate(sql_employmentHistory);
            DataBase.sta.executeUpdate(sql_marked);

        }catch(SQLException e){
            System.out.println("Error in Create Table!");
            System.out.print("[Error]:");
            System.out.println(e);
        }

    }

    public void delete_table(){
        sql_employee = "DROP TABLE IF EXISTS Employee";
        sql_company  = "DROP TABLE IF EXISTS Company";
        sql_employer = "DROP TABLE IF EXISTS Employer";
        sql_position = "DROP TABLE IF EXISTS Position_Table";
        sql_employmentHistory = "DROP TABLE IF EXISTS Employment_History";
        sql_marked = "DROP TABLE IF EXISTS marked";

        try{
            DataBase.sta = DataBase.con.createStatement();

            DataBase.sta.executeUpdate(sql_marked);
            DataBase.sta.executeUpdate(sql_employmentHistory);
            DataBase.sta.executeUpdate(sql_position);
            DataBase.sta.executeUpdate(sql_employer);
            DataBase.sta.executeUpdate(sql_company);
            DataBase.sta.executeUpdate(sql_employee);
          
        }catch(SQLException e){
            System.out.println("Error in Delete Table!");
            System.out.print("[Error]:");
            System.out.println(e);
        }

    }

    public void load_data(String folderPath){/*
        sql_employee = "load data local infile './"+folderPath+"/employee.csv' "+
        "into table Employee "+
        "fields terminated by ',' lines terminated by '\\n'"+
        "(Employee_ID, Name, Experience, Expected_Salary, Skills)";

        sql_company = "load data local infile './"+folderPath+"/company.csv' "+
        "into table Company "+
        "fields terminated by ',' lines terminated by '\\n'"+
        "(Company, Size, Founded)";

        sql_employer = "load data local infile './"+folderPath+"/employer.csv' "+
        "into table Employer "+
        "fields terminated by ',' lines terminated by '\\n'"+
        "(Employer_ID, Name, Company)";

        sql_position = "load data local infile './"+folderPath+"/position.csv' "+
        "into table Position_Table"+
        "fields terminated by ',' lines terminated by '\\n'"+
        "(Position_ID, Position_Title, Salary, Experience, Status, Employer_ID)";

        sql_employmentHistory = "load data local infile './"+folderPath+"/history.csv' "+
        "into table Employment_History "+
        "fields terminated by ',' lines terminated by '\\n'"+
        "(Position_ID, Employee_ID, Start, End)";
        */

        sql_employee = "load data local infile '"+folderPath+"/employee.csv' "+
        "into table Employee "+
        "fields terminated by ',' enclosed by '\"' lines terminated by '\\n'";

        sql_company = "load data local infile '"+folderPath+"/company.csv' "+
        "into table Company "+
        "fields terminated by ',' enclosed by '\"' lines terminated by '\\n'";

        sql_employer = "load data local infile '"+folderPath+"/employer.csv' "+
        "into table Employer "+
        "fields terminated by ',' enclosed by '\"' lines terminated by '\\n'";

        sql_position = "load data local infile '"+folderPath+"/position.csv' "+
        "into table Position_Table "+
        "fields terminated by ',' enclosed by '\"' lines terminated by '\\n'";

        sql_employmentHistory = "load data local infile '"+folderPath+"/history.csv' "+
        "into table Employment_History "+
        "fields terminated by ',' enclosed by '\"' lines terminated by '\\n'";


        try{
            DataBase.sta = DataBase.con.createStatement();
            DataBase.sta.executeUpdate(sql_employee);
            DataBase.sta.executeUpdate(sql_company);
            DataBase.sta.executeUpdate(sql_employer);
            DataBase.sta.executeUpdate(sql_position);
            DataBase.sta.executeUpdate(sql_employmentHistory);
          //  DataBase.sta.executeUpdate(sql_marked);
        }catch(SQLException e){
            System.out.println("Error in Load Table!");
            System.out.print("[Error]:");
            System.out.println(e);
        }
    }

    public void check_data(){
      sql_employee = "SELECT COUNT(*) AS total FROM Employee";
      sql_company  = "SELECT COUNT(*) AS total FROM Company";
      sql_employer = "SELECT COUNT(*) AS total FROM Employer";
      sql_position = "SELECT COUNT(*) AS total FROM Position_Table";
      sql_employmentHistory = "SELECT COUNT(*) AS total FROM Employment_History";
      sql_marked = "SELECT COUNT(*) AS total FROM marked";

      try{
          DataBase.sta = DataBase.con.createStatement();

          DataBase.rSet = DataBase.sta.executeQuery(sql_employee);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("Employee: %s\n",count1);
          }

          DataBase.rSet = DataBase.sta.executeQuery(sql_company);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("Company 2: %s\n",count1);
          }

          DataBase.rSet = DataBase.sta.executeQuery(sql_employer);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("Employer: %s\n",count1);
          }

          DataBase.rSet = DataBase.sta.executeQuery(sql_position);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("Position: %s\n",count1);
          }

          DataBase.rSet = DataBase.sta.executeQuery(sql_employmentHistory);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("Employment_History: %s\n",count1);
          }


          DataBase.rSet = DataBase.sta.executeQuery(sql_marked);
          while (DataBase.rSet.next()){
            int count1 = DataBase.rSet.getInt("total");
            System.out.format("marked: %s\n",count1);
          }

      }catch(SQLException e){
          System.out.println("Error in Load Table!");
          System.out.print("[Error]:");
          System.out.println(e);
      }


    }



}
