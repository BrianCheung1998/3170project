import java.io.*;
import java.util.*;
import java.text.*;

public class Employee {
    String sql = null;
    String sql_marked = null;
    ConnectToMySQL DataBase = new ConnectToMySQL();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Scanner kb = new Scanner(System.in);;

    public void show_available_positions(String employeeID){
        /*
        TO DO: After the employee enteDataBase.rSet his/her Employee_ID, the system shall return all available records.
        Each record should contain the detail of the position including Position_ID,
        Position_Title, Salary, and the detail of the company including Company, Size, Founded.
        */
        try{
            // get the skill set of the employee
            sql = "SELECT E.Skills FROM Employee E WHERE E.Employee_ID=\'" + employeeID + "\'";
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);
            DataBase.rSet.next();
            String skills = DataBase.rSet.getString("skills");
            String[] Skill_Set = skills.split(";");
            int Skill_Set_Size = Skill_Set.length;
            DataBase.sta.close();

          sql = "SELECT P.Position_ID, P.Position_Title, P.Salary, C.Company, C.Size, C.Founded "+
                  "FROM Position_Table P, Employer E, Employee E2, Company C WHERE P.Employer_ID = E.Employer_ID and "+
                  "E.Company = C.Company and P.Status = true AND P.Salary >= E2.Expected_Salary AND "+
                  "P.Experience <= E2.Experience and E2.Employee_ID =\'" + employeeID + "\'";
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
            // check is it in skill set
              for(int foo = 0; foo < Skill_Set_Size; foo++){
                  if(Skill_Set[foo].equals(title)){
                      System.out.format("%-12s %-15s %-7s %-8s %-5s %s\n", id, title, salary, company, size, founded);
                      break;
                  }
              }

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
            // get the skill set of the employee
            sql = "SELECT E.Skills FROM Employee E WHERE E.Employee_ID=\'" + employeeID + "\'";
            DataBase.sta = DataBase.con.createStatement();
            DataBase.rSet = DataBase.sta.executeQuery(sql);
            DataBase.rSet.next();
            String skills = DataBase.rSet.getString("skills");
            String[] Skill_Set = skills.split(";");
            int Skill_Set_Size = Skill_Set.length;
            DataBase.sta.close();

          DataBase.sta = DataBase.con.createStatement();

          sql = "SELECT P.Position_ID, P.Position_Title, P.Salary, C.Company, C.Size, C.Founded "+
                    "FROM Position_Table P, Employer E, Employee E2, Company C "+
                    "where P.Employer_ID = E.Employer_ID and E.Company = C.Company and "+
                    "P.Status = true and P.Salary >= E2.Expected_Salary and P.Experience <= E2.Experience and "+
                    "E2.Employee_ID =\'" + employeeID + "\' and "+
                        "E.Company NOT IN( select EH.Company "+
                        "from Employment_History EH, Position_Table P "+
                        "where P.Position_ID = EH.Position_ID and EH.Employee_ID = \'"+employeeID+"\')" +
                        "and P.Position_ID NOT IN (select m.Position_ID from marked m where m.Employee_ID = \'"+employeeID+"\')" ;

                    String[] id = new String[10000];
                    String[] title = new String[10000];
                    String[] salary = new String[10000];
                    String[] company = new String[10000];
                    String[] size = new String[10000];
                    String[] founded = new String[10000];
                    int i = 0;
          DataBase.rSet = DataBase.sta.executeQuery(sql);

          while (DataBase.rSet.next()){
            id[i] = DataBase.rSet.getString("Position_ID");
            title[i] = DataBase.rSet.getString("Position_Title");
            salary[i] = DataBase.rSet.getString("Salary");
            company[i] = DataBase.rSet.getString("Company");
            size[i] = DataBase.rSet.getString("Size");
            founded[i] = DataBase.rSet.getString("Founded");
            // check is it in skill set
            for(int foo = 0; foo < Skill_Set_Size; foo++){
                if(Skill_Set[foo].equals(title[i])){
                    System.out.format("%-12s %-15s %-7s %-8s %-5s %s\n", id[i], title[i], salary[i], company[i], size[i], founded[i]);
                    break;
                }
            }
            i = i + 1;
          }
          DataBase.sta.close();
          System.out.println("Please enter your interest position are:");
          String Interested_ID = kb.nextLine();
          for(int j = 0; j < id.length; j++){
              if(id[j].equals(Interested_ID))
              {
                  sql_marked = "INSERT INTO marked (Position_ID,Employee_ID,Status) " +
                           "VALUES ('"+id[j]+"', '"+employeeID+"' , FALSE)";
                  DataBase.sta = DataBase.con.createStatement();
                  DataBase.sta.executeUpdate(sql_marked);
                  DataBase.sta.close();
                  System.out.format("You have successfully marked %s with your %s.\n", id[j], employeeID);
                  break;
              }
          }
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
              "where Employee_ID = \'"+employeeID+"\' order by End DESC LIMIT 3  ) t order by End ASC";
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
