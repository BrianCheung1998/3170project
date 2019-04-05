import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    String sql = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    public void post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
        int Temp_Position_ID = 0;
        int Suitable_Count = 0;
        //setup the sql query
        String Get_SQL =
                "SELECT E.Skills, E.Expected_Salary, E.Experience"+
                "FROM Employment_History EH, Employee E"+
                "WHERE (EH.Employee_ID = E.Employee_ID and EH.End IS NOT NULL)"+ // not working for any company currently
                " or E.Employee_ID NOT IN("+
                    "SELECT EH2.Employee_ID"+
                    "FROM Employment_History EH2)"+
                ")";

        String Set_SQL =
                String.format("INSERT INTO Position (Position_ID, Position_Title, Salary, Experience, Employer_ID, Status)" +
                        "VALUE (" + String.format("%010d", Temp_Position_ID) + Position_Title + String.valueOf(Salary) +
                        String.valueOf(Experience) + Employer_ID + "TRUE");

        try{ //check if there is any suitable employee
            DataBase.rSet = DataBase.sta.executeQuery(Get_SQL);
            while(DataBase.rSet.next()){
                String skills = DataBase.rSet.getString("skills");
                int expected_salary = DataBase.rSet.getInt("expected_salary");
                int experience = DataBase.rSet.getInt("experience");
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
        }
        catch (Exception e){
            System.err.println("Error occur when getting data for Position Recruitment");
            System.err.println(e.getMessage());
        }
        // if there is no record meet the requirement, return error
        if(Suitable_Count <= 0){
            System.out.println("No potential employee found, The position recruitment have not been posted");
        }
        else{
            System.out.print(String.valueOf(Suitable_Count) + " potential employees are found.");
            try{

            }
            catch(Exception e2){

            }
        }




    }
    public void check_employees_and_arrange_an_interview(String Employer_ID, String Position_ID, String Employee_ID){

    }
    public void accept_an_employee(String Employer_ID, String Employee_ID){
        /* List of person history */
    }
}
