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
                "VALUE (\'" + String.format("%010d", Temp_Position_ID) + "\', \'" + Position_Title +  "\', \'" + String.valueOf(Salary) +
                "\', \'" + String.valueOf(Experience) +  "\', \'" + Employer_ID +  "\', " + "TRUE"+")";

        try{ //check if there is any suitable employee
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

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
    public void find_position_posted(String Employer_ID){
        sql =
            "SELECT P.Position_ID"+
            "From Position_Table P"+
            "WHERE P.Employer_ID=" + Employer_ID;

        try{ // get the position posted by this employer
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

            System.out.println("The id of position recruitment posted by you are:");
            while(DataBase.rSet.next()){
                String position_ID = DataBase.rSet.getString("position_ID");
                System.out.println(position_ID);
            }
            DataBase.sta.close();
        }
        catch(Exception e){
            System.err.println("Error occur when getting data for Position Recruitment");
            System.err.println(e.getMessage());
        }
    }

    public void find_interest_employee(String Position_ID){
        //get who is interest in the position
        sql =
        "SELECT M.Employee_ID, E.Name, E.Expected_Salary, E.Experience, E.Skills "+
        "FROM marked M, Employee E "+
        "WHERE M.Employee_ID = E.Employee_ID and " +
        "M.Position_ID=" + Position_ID;

        try{
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);
            System.out.println("The employees who mark interested in this position recruitment are:");
            System.out.println("Employee_ID, Name, Expected_Salary, Experience, Skills");

            while(DataBase.rSet.next()){
                String employee_ID = DataBase.rSet.getString("employee_ID");
                String name = DataBase.rSet.getString("name");
                int expected_salary = DataBase.rSet.getInt("salary");
                int experience = DataBase.rSet.getInt("experience");
                String skills = DataBase.rSet.getString("skills");
                //print the employee information
                System.out.println(employee_ID + ", " + name + ", " + expected_salary + ", " + experience + ", " + skills);

            }
        }
        catch(Exception e){
            System.err.println("Error occur when getting employees information");
            System.err.println(e.getMessage());
        }

    }

    public void arrange_interview(String Employee_ID, String Position_ID){

        sql =
            "UPDATE marked SET Status = TRUE" +
            " WHERE Position_ID=" + Position_ID +
            " Employee_ID=" + Employee_ID;
        try{//arrange an immediate interview
            DataBase.sta = DataBase.con.createStatement();
            DataBase.sta.executeUpdate(sql);
            System.out.println("An IMMEDIATE interview has done.");
        }
        catch(Exception e){
            System.err.println("Error occur when updating the position status");
            System.err.println(e.getMessage());
        }
    }

    public void accept_an_employee(String Employer_ID, String Employee_ID){
        /* List of person history */
        String position_id;
        String company;
        try{// check if the position is marked by the employee and posted by the employer
            sql=
                "SELECT M.Position_ID, E.Company" +
                "FROM marked M, Position_Table P, Employer E" +
                "WHERE E.Employer_ID = P.Employer_ID and P.Status=TRUE and M.Status=TRUE and " +
                "M.Position_ID = P.Position_ID and M.Employee_ID="
                + Employee_ID + " and P.Employer_ID=" + Employer_ID;

            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

            if(DataBase.rSet.next()){
                position_id = DataBase.rSet.getString("position_id");
                company = DataBase.rSet.getString("company");
                System.out.println("An Employment History record is created, details are:");
                System.out.println("Employee_ID, Company, Position_ID, Start, End");
                System.out.println(Employee_ID + ", " + company + ", " + position_id + ", " + "2019-01-01" + ", " + "NULL");
                sql ="INSERT INTO Employment_History Value(\'" + Employee_ID + "\', \'" + company + "\', \'" + position_id + "\', \'" + "2019-01-01" + "\', " + "NULL)";
                DataBase.sta.close();
                DataBase.sta = DataBase.con.createStatement();
                DataBase.sta.executeUpdate(sql);
            }
            else{
                System.out.println("The position is not available or no record match");
            }
            DataBase.sta.close();
        }
        catch(Exception e){
            System.err.println("Error occur when getting interview record or posting posting new job history");
            System.err.println(e.getMessage());
        }
        // if suitable, create employment history record

        // if suitable, changes the status of the "Position" record to be invalid

        // if not suitable, show error message
    }
}
