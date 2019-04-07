import javax.swing.text.Position;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Employer {
    String sql = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    ConnectToMySQL DataBase2 = new ConnectToMySQL();

    public int post_position_recruitment(String Employer_ID, String Position_Title, int Salary, int Experience){
        String Temp_Position_ID = "";
        int Suitable_Count = 0;
        //setup the sql query
        sql =
                "SELECT DISTINCT E.Employee_ID, E.Skills, E.Expected_Salary, E.Experience "+
                "FROM Employment_History EH, Employee E "+
                "WHERE " +
                // employee's expected salary is no larger than the upper-bound of the salary
                // employee's experience no less than input experience
                "E.Expected_Salary<=" + Salary +
                " and E.Experience>=" + Experience +
                " and ((EH.Employee_ID = E.Employee_ID and EH.End IS NOT NULL)"+ // not working for any company currently
                " or E.Employee_ID NOT IN("+
                    "SELECT EH2.Employee_ID "+
                    "FROM Employment_History EH2))";

        try{ //check if there is any suitable employee
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

            while(DataBase.rSet.next()){
                String employee_id = DataBase.rSet.getString("employee_id");
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
        if(Suitable_Count > 0){
            try{
                for(int x = 0; x < 999; x++){
                    DataBase2.sta = DataBase2.con.createStatement();
                    Temp_Position_ID = String.format("pid%03d", x);
                    String Find_pid =
                            "SELECT count(*) FROM Position_Table WHERE Position_ID=\'" + Temp_Position_ID + "\'";
                    DataBase2.rSet = DataBase2.sta.executeQuery(Find_pid);
                    DataBase2.rSet.next();
                    int duplicated_ID_count = DataBase2.rSet.getInt(1);
                    DataBase2.sta.close();
                    if (duplicated_ID_count == 0){
                        System.out.println("No duplicated position ID for" + Temp_Position_ID);
                        break;
                    }
                    if(duplicated_ID_count > 0 && x == 999){
                        return -1;
                    }
                }
                String Set_SQL =
                        "INSERT INTO Position_Table (Position_ID, Position_Title, Salary, Experience, Employer_ID, Status)" +
                                "VALUE (\'" + Temp_Position_ID + "\', \'" + Position_Title +  "\', \'" + String.valueOf(Salary) +
                                "\', \'" + String.valueOf(Experience) +  "\', \'" + Employer_ID +  "\', " + "TRUE"+")";
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
    public int find_position_posted(String Employer_ID){
        int NumberOfPosition = 0;
        System.out.println("Find position posted");
        sql =
            "SELECT P.Position_ID "+
            "From Position_Table P "+
            "WHERE P.Employer_ID=\'" + Employer_ID + "\'";

        try{ // get the position posted by this employer
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);

            System.out.println("The id of position recruitment posted by you are:");
            while(DataBase.rSet.next()){
                NumberOfPosition++;
                String position_ID = DataBase.rSet.getString("position_ID");
                System.out.println(position_ID);
            }
            DataBase.sta.close();
        }
        catch(Exception e){
            System.err.println("Error occur when getting data for Position Recruitment");
            System.err.println(e.getMessage());
        }
        return NumberOfPosition;
    }

    public int find_interest_employee(String Position_ID){
        int NumberOfInterestEmployee = 0;
        System.out.println("Find interest employee");
        //get who is interest in the position
        sql =
        "SELECT M.Employee_ID, E.Name, E.Expected_Salary, E.Experience, E.Skills "+
        "FROM marked M, Employee E "+
        "WHERE M.Employee_ID = E.Employee_ID and " +
        "M.Position_ID=\'" + Position_ID + "\'";

        try{
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);
            System.out.println("The employees who mark interested in this position recruitment are:");
            System.out.println("Employee_ID, Name, Expected_Salary, Experience, Skills");

            while(DataBase.rSet.next()){
                NumberOfInterestEmployee++;
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
        return NumberOfInterestEmployee;
    }

    public void arrange_interview(String Employee_ID, String Position_ID){

        System.out.println("arrange interview");
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
                "SELECT M.Position_ID, E.Company " +
                "FROM marked M, Position_Table P, Employer E " +
                "WHERE E.Employer_ID = P.Employer_ID and P.Status=TRUE and M.Status=TRUE and " +
                "M.Position_ID = P.Position_ID and M.Employee_ID=\'"
                + Employee_ID + "\' and P.Employer_ID=\'" + Employer_ID + "\'";

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
                System.out.println("The position is not available or no record match.");
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
