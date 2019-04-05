import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db26";
    static String dbUsername = "Group26";
    static String dbPassword = "group26";
    public static Connection con = null;

    public static Statement stmt = null;
    public static ResultSet resultSet = null;
    String sql = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();

    public int post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
        int Temp_Position_ID = 0;
        int Suitable_Count = 0;
        try{
            Connection con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }
        catch(Exception Connection_e){
            System.out.println("Connect Fail");
        }
        //setup the sql query
        sql =
                "SELECT E.Skills, E.Expected_Salary, E.Experience"+
                "FROM Employment_History EH, Employee E"+
                "WHERE (EH.Employee_ID = E.Employee_ID and EH.End IS NOT NULL)"+ // not working for any company currently
                " or E.Employee_ID NOT IN("+
                    "SELECT EH2.Employee_ID"+
                    "FROM Employment_History EH2)"+
                ")";

        String Set_SQL =
                "INSERT INTO Position_Table (Position_ID, Position_Title, Salary, Experience, Employer_ID, Status)" +
                "VALUE (" + String.format("%010d", Temp_Position_ID) + Position_Title + String.valueOf(Salary) +
                String.valueOf(Experience) + Employer_ID + "TRUE"+")";

        try{ //check if there is any suitable employee
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            //DataBase.rSet = DataBase.sta.executeQuery(sql);
            while(resultSet.next()){
                String skills = resultSet.getString("skills");
                int expected_salary = resultSet.getInt("expected_salary");
                int experience = resultSet.getInt("experience");
                String[] Skill_Set = skills.split(";");
                int Skill_Set_Size = Skill_Set.length;
                Boolean Have_Skill = Boolean.FALSE;
                // employee's expected salary is no larger than the upper-bound of the salary
                // employee's experience no less than input experience
                if(experience >= Experience && expected_salary <= Salary){
                    for(int i = 0; i < Skill_Set_Size; i++){
                        if(Skill_Set[i].equals(Position_Title)){
                            Have_Skill = Boolean.TRUE;
                            break;
                        }
                    }
                }
                if(Have_Skill){ // skills should contain the position title
                    Suitable_Count++;
                }
            }
            stmt.close();
        }
        catch (Exception e){
            System.err.println("Error occur when getting data for Position Recruitment");
            System.err.println(e.getMessage());
        }
        // if there is no record meet the requirement, return error
        if(Suitable_Count > 0){
            try{
                DataBase.sta.executeUpdate(Set_SQL);
            }
            catch(Exception e2){
                System.err.println("Error occur when positing Position Recruitment");
                System.err.println(e2.getMessage());
            }
        }
        return Suitable_Count;
    }
    public void check_employees_and_arrange_an_interview(String Employer_ID, String Position_ID, String Employee_ID){

    }
    public void accept_an_employee(String Employer_ID, String Employee_ID){
        /* List of person history */
    }
}
