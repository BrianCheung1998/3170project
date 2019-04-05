import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    String sql = null;
    String outer_sql = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    public void post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
        int Temp_Position_ID = 0;
        //setup the sql query
        String Get_SQL =
                "SELECT E.Skills, E.Expected_Salary, E.Experience"+
                "FROM Employment_History EH, Employee E"+
                "WHERE (EH.Employee_ID = E.Employee_ID and EH.End IS NOT NULL)"+
                " or E.Employee_ID NOT IN("+
                    "SELECT EH2.Employee_ID"+
                    "FROM Employment_History EH2)"+
                ")";

        String Set_SQL =
                String.format("INSERT INTO Position (Position_ID, Position_Title, Salary, Experience, Employer_ID, Status)" +
                        "VALUE (" + String.format("%010d", Temp_Position_ID) + Position_Title + String.valueOf(Salary) +
                        String.valueOf(Experience) + Employer_ID + "TRUE");

        // if there is no record meet the requirement, return error


        // not working for any company currently
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
