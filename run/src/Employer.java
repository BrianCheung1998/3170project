import javax.swing.text.Position;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    String sql = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();

    public int post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
        int Temp_Position_ID = 0;
        int Suitable_Count = 0;
        //setup the sql query
        sql =
                "SELECT E.Skills, E.Expected_Salary, E.Experience "+
                "FROM Employment_History EH, Employee E "+
                "WHERE (EH.Employee_ID = E.Employee_ID and EH.End IS NOT NULL)"+ // not working for any company currently
                " or E.Employee_ID NOT IN("+
                    "SELECT EH2.Employee_ID "+
                    "FROM Employment_History EH2)"+
                // employee's expected salary is no larger than the upper-bound of the salary
                // employee's experience no less than input experience
                " and E.Expected_Salary <= " + Salary +
                " and E.Experience >= " + String.valueOf(Experience);

        String Set_SQL =
                "INSERT INTO Position_Table (Position_ID, Position_Title, Salary, Experience, Employer_ID, Status)" +
                "VALUE (" + String.format("%010d", Temp_Position_ID) + ", " + Position_Title +  ", " + String.valueOf(Salary) +
                ", " + String.valueOf(Experience) +  ", " + Employer_ID +  ", " + "TRUE"+")";

        try{ //check if there is any suitable employee
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

            //DataBase.rSet = DataBase.sta.executeQuery(sql);
            while(DataBase.rSet.next()){
                String skills = DataBase.rSet.getString("skills");
                int expected_salary = DataBase.rSet.getInt("expected_salary");
                int experience = DataBase.rSet.getInt("experience");
                String[] Skill_Set = skills.split(";");
                int Skill_Set_Size = Skill_Set.length;
                Boolean Have_Skill = Boolean.FALSE;
                for(int i = 0; i < Skill_Set_Size; i++){
                    if(Skill_Set[i].equals(Position_Title)){
                        Have_Skill = Boolean.TRUE;
                        break;
                    }
                }
                if(Have_Skill){ // skills should contain the position title
                    Suitable_Count++;
                }
            }
            DataBase.sta.close();
        }
        catch (Exception e){
            System.err.println("Error occur when getting data for Position Recruitment");
            System.err.println(e.getMessage());
        }
        // if there is no record meet the requirement, return error
        if(Suitable_Count == 0){
            try{
                DataBase.sta = DataBase.con.createStatement();
                DataBase.sta.executeUpdate(Set_SQL);
                DataBase.sta.close();
            }
            catch(Exception e2){
                System.err.println("Error occur when positing Position Recruitment");
                System.err.println(e2.getMessage());
            }
        }
        return Suitable_Count;
    }
    public String[] find_position_posted(String Employer_ID){
        String[] Position_List = new String[10];
        Position_List[0] = "Nothing";
        // get the position posted by this employer
        String Pos_Query =
                "SELECT P.Position_ID"+
                "From Position_Table P"+
                "WHERE P.Employer_ID=" + Employer_ID;




        return Position_List;
    }

    public String[][] find_interest_employee(String Position_ID){
        //get who is interest in the position
        String Employee_Query =
                "SELECT M.Employee_ID, E.Name, E.Expected_Salary, E.Experience, E.Skills"+
                        "FROM marked M, Employee E"+
                        "WHERE M.Employee_ID = E.Employee_ID and " +
                        "M.Position_ID=" + Position_ID;

        String[][] Employee_Information = new String[10][5];

        return Employee_Information;
    }

    public void arrange_interview(String Employee_ID, String Position_ID){
        //arrange an immediate interview
        String Interview_Query =
                "UPDATE marked SET Status = TRUE" +
                " WHERE Position_ID=" + Position_ID +
                " Employee_ID=" + Employee_ID;
        /*String Interview_Query =
                "UPDATE Position_Table"+
                        "SET Status = TRUE"+
                        "WHERE Position_ID=" + Position_ID;*/
    }

    public void accept_an_employee(String Employer_ID, String Employee_ID){
        /* List of person history */

        // check if the position is marked by the employee and posted by the employer
        String Get_Position =
                "SELECT M.Position_ID FROM marked M, Position_Table P " +
                "WHERE M.Position_ID = P.Position_ID and M.Employee_ID="
                + Employee_ID + " and P.Employer_ID=" + Employer_ID;
        // if suitable, create employment history record

        // if suitable, changes the status of the "marked" record to be invalid

        // if not suitable, show error message
    }
}
