import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    String sql = null;
    String sql_marked = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    public void post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
    // if there is no record meet the requirement, return error
    sql = "";    // not working for any company currently
        // skills should contain the position title
        // employee's expected salary is no larger than the upper-bound of the salary
        // employee's experience no less than input experience
    }
    public void check_employees_and_arrange_an_interview(String Employer_ID, String Position_ID, String Employee_ID){

    }
    public void accept_an_employee(String Employer_ID, String Employee_ID){
        /* List of person history */
    }
}
