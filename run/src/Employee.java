import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employee {
    String sql_employee = null;
    String sql_company  = null;
    String sql_employer = null;
    String sql_position = null;
    String sql_employmentHistory = null;
    String sql_marked = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Scanner reader;

    public void show_available_positions(String employeeID){
        /*
        TO DO: After the employee enters his/her Employee_ID, the system shall return all available records.
        Each record should contain the detail of the position including Position_ID, Position_Title, Salary, and the detail of the company including Company, Size, Founded.
        */
    }

    public void mark_interested_position(String employeeID){
        /*
        TO DO: Show all positions that the employee may be interested,
        then prompt the employee to mark one position as interested, and save this information.
        */
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
