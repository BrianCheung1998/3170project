import java.io.*;
import java.util.*;
import java.sql.*;

public class UI{
    /********************************************************
     * State Index Constant List
     ********************************************************/
    private static final int S_identity = 0;
    //menu
    private static final int S_admin    = 1;
    private static final int S_employee = 2;
    private static final int S_employer = 3;
    //admin action
    private static final int S_admin_createTable = 4;
    private static final int S_admin_deleteTable = 5;
    private static final int S_admin_loadData    = 6;
    private static final int S_admin_checkData   = 7;
    //employee action
    private static final int S_employee_showAvailblePosition   = 8;
    private static final int S_employee_markInterestPosition   = 9;
    private static final int S_employee_checkAverageWorkTime   =10;
    //employer action
    private static final int S_employer_postRecruitment = 11;
    private static final int S_employer_checkAndArrange = 12;
    private static final int S_employer_acceptEmployee = 13;
    //Constructor
    static int currentState;
    Scanner kb;
    Administrator admin_do;
    Employee employee_do;
    Employer employer_do;
    public UI(){
        currentState = 0;
        kb = new Scanner(System.in);
        admin_do = new Administrator();
        employee_do = new Employee();
        employer_do = new Employer();

    }
    public static void swap_to(int s){
        currentState = s;
    }

    public void run(){
        while(true){
            switch(currentState){
                case S_identity:
                    System.out.println("Welcome! Who are you?");
                    System.out.println("1. An administrator");
                    System.out.println("2. An employee");
                    System.out.println("3. An employer");
                    System.out.println("4. Exit");
                    do {
                        System.out.println ("Please enter [1-4].");
                        try {
                            int selection = kb.nextInt();
                            kb.nextLine();
                            if (selection == 1){    //An administrator
                                swap_to(S_admin);
                                break;
                            }
                            if (selection == 2){    //An employee
                                swap_to(S_employee);
                                break;
                            }
                            if (selection == 3){    //An employer
                                swap_to(S_employer);
                                break;
                            }
                            if (selection == 4){    //Exit
                                return;
                            }

                        } catch (Exception e) {

                        }
                        System.out.println ("[ERROR] Invlid input");
                    } while(true);
                break;

                case S_admin:
                    System.out.println("Administrator, What would you like to do?");
                    System.out.println("1. Create tables");
                    System.out.println("2. Delete Tables");
                    System.out.println("3. Load data");
                    System.out.println("4. Check data");
                    System.out.println("5. Go back");
                    do {
                        System.out.println ("Please enter [1-5].");
                        try {
                            int selection = kb.nextInt();
                            kb.nextLine();
                            if (selection == 1){    //Create tables ---wrote
                                admin_do.create_table();
                                swap_to(S_admin_createTable);
                                break;
                            }
                            if (selection == 2){    //Delete Tables ---wrote
                                admin_do.delete_table();
                                swap_to(S_admin_deleteTable);
                                break;
                            }
                            if (selection == 3){    //Load data     ---wrote
                                String folder = kb.nextLine();
                                admin_do.load_data(folder);
                                swap_to(S_admin_loadData);
                                break;
                            }
                            if (selection == 4){    //Check data
                                swap_to(S_admin_checkData);
                                break;
                            }
                            if (selection == 5){    //Go Back
                                swap_to(S_identity);
                                break;
                            }
                            
                        } catch (Exception e) {

                        }
                        System.out.println ("[ERROR] Invlid input");
                    } while(true);
                break;

                case S_employee:
                    System.out.println("Empolyee, What would you like to do?");
                    System.out.println("1. Show Available Positions");
                    System.out.println("2. Mark Interested Positions");
                    System.out.println("3. Check Average Working Time");
                    System.out.println("4. Go back");
                    do {

                        System.out.println ("Please enter [1-4].");
                        try {
                            int selection = kb.nextInt();
                            kb.nextLine();
                            if (selection == 1){    //Show Available Positions
                                swap_to(S_employee_showAvailblePosition);
                                break;
                            }
                            if (selection == 2){    //Mark Interested Positions
                                swap_to(S_employee_markInterestPosition);
                                break;
                            }
                            if (selection == 3){    //Check Average Working Time
                                swap_to(S_employee_checkAverageWorkTime);
                                break;
                            }
                            if (selection == 4){    //Go Back
                                swap_to(S_identity);
                                break;
                            }
                            
                        } catch (Exception e) {

                        }
                        System.out.println ("[ERROR] Invlid input");
                    } while(true);
                break;
                    
                case S_employer:
                    System.out.println("Empolyer, What would you like to do?");
                    System.out.println("1. Post Position Recruitment");
                    System.out.println("2. Check employees and arrange an interview");
                    System.out.println("3. Accept an employee");
                    System.out.println("4. Go back");
                    do {
                        System.out.println ("Please enter [1-4].");
                        try {
                            int selection = kb.nextInt();
                            kb.nextLine();
                            if (selection == 1){    //Post Position Recruitment
                                swap_to(S_employer_postRecruitment);
                                break;
                            }
                            if (selection == 2){    //Check employees and arrange an interview
                                swap_to(S_employer_checkAndArrange);
                                break;
                            }
                            if (selection == 3){    //Accept an employee
                                swap_to(S_employer_acceptEmployee);
                                break;
                            }
                            if (selection == 4){    //Go Back
                                swap_to(S_identity);
                                break;
                            }
                            
                        } catch (Exception e) {

                        }
                        System.out.println ("[ERROR] Invlid input");
                    } while(true);
                break;

                case S_admin_createTable:
                    System.out.println("Processing...Done! Tables are created!");
                    /*
                    TO DO: creates all the tables for this system based on the relational schema given.
                    */
                    swap_to(S_admin);
                break;

                case S_admin_deleteTable:
                    System.out.println("Processing...Done! Tables are deleted!");
                    /*
                    TO DO: deletes all existing tables in the system.
                    */
                    swap_to(S_admin);
                break;
                
                case S_admin_loadData:
                    System.out.println("Please enter the folder path.");
                    String fileName = kb.nextLine();
                    /*
                    TO DO: contains all 5 data files, namely employee.csv, company.csv, employer.csv, position.csv, and history.csv. See section 3 for data files specifications.
                    */
                    System.out.println("Processing...Data is loaded!:");

                    swap_to(S_admin);
                break;
            
                case S_admin_checkData:
                    System.out.println("Number of records in each table:");
                    /*
                    TO DO: For each table in the database, display the number of records in it.
                    */

                    swap_to(S_admin);
                break;
            
                case S_employee_showAvailblePosition:
                    /* 
                    A position is available to an employee if the following criteria hold:
                     1. the Status of the position is True(valid);
                     2. the employee is qualified for the position(i.e. the title of the position is included in the skills of the employee);
                     3. the Salary of the position is not less than the Expected_Salary of the employee;
                     4. the Experience of the employee is not less than the required Experience of the position.
                    */ 
                    
                    System.out.println("Please enter your ID.");
                    String Employee_ID = kb.nextLine();
                    System.out.println("Your available positions are:");
                    System.out.println("Position_ID Position_Title, Salary, Company, Size, Founded");
                    /*
                    TO DO: After the employee enters his/her Employee_ID, the system shall return all available records.
                    Each record should contain the detail of the position including Position_ID, Position_Title, Salary, and the detail of the company including Company, Size, Founded.
                    */

                    swap_to(S_admin);
                break;
                
                case S_employee_markInterestPosition:
                    /*
                    An employee may be interested in a position if:
                     1. the position is available to him/her;
                     2. the position is not from any company he/she worked in before(you need to check the Employment History);
                     3. the position has not been marked as interested by the employee before.
                    */
                    System.out.println("Please enter your ID.");
                    Employee_ID = kb.nextLine();
                    System.out.println("Your interest position are:");
                    System.out.println("Position_ID Position_Title, Salary, Company, Size, Founded");
                    /*
                    TO DO: Show all positions that the employee may be interested,
                    then prompt the employee to mark one position as interested, and save this information.
                    */

                    swap_to(S_employee);
                break;

                case S_employee_checkAverageWorkTime:
                    System.out.println("Please enter your ID.");
                    Employee_ID = kb.nextLine();
                    /*
                    TO DO: The average working time is defined by the average of the working days
                    of the last 3 records (excluding the current position he/she servers 
                    */
                    int __time__ = 0;
                    System.out.printf("Your average working time is: %d days",__time__);

                    swap_to(S_employee);
                break;

                case S_employer_postRecruitment:
                    System.out.println("Please enter your ID.");
                    String Employer_ID = kb.nextLine();
                    
                    System.out.println("Please enter the position title.");
                    String Position_Title = kb.nextLine();

                    System.out.println("Please enter an upper bound of salary.");
                    int Salary = kb.nextInt();
                    kb.nextLine();
                    System.out.println("Please enter the required experience(press enter to skip)");
                    String experience_string = kb.nextLine();
                    int experience_int;
                    if (experience_string.isEmpty()) experience_string = "0";// allow skip
                    experience_int = Integer.parseInt(experience_string);
                    
                    /*
                    TO DO: the system should post the position requirement, and display the number of potential employees to the employer. Otherwise return an error message for the employer.
                    A potential employee is an employee that:
                     1. is not working in any company currently;
                     2. meet all the criteria: for criterion 1, the employee's Skills should contain
                        the position title; for criterion 2, the employee's Expected_Salary should not
                        larger than the upper bound of salary; for criterion 3, the employee's Experience
                        should not less than the input experience (no input means experience=0) 
                    */
                    int __num__ = 0;
                    System.out.printf("%d potential empolyees are found. The position recruitment is posted.\n",__num__);
                    swap_to(S_employee);
                break;

                case S_employer_checkAndArrange:
                    System.out.println("Please enter your ID.");
                    Employer_ID = kb.nextLine();
                    kb.nextLine();

                    System.out.println("The id of position recruitment posted by you are:");
                    /*List of position */
                    System.out.println("Please pick one position id.");
                    String Position_ID = kb.nextLine();
                    kb.nextLine();

                    System.out.println("The employees who mark interested in this position recruitment are:");
                    System.out.println("Employee_ID, Name, Expected_Salary, Experience, Skills");
                    /*List of employees */
                    System.out.println("Please pick one employee by Employee_ID.");
                    Employee_ID = kb.nextLine();
                    kb.nextLine();
                    // do something?
                    System.out.println("An IMMEDIATE interview has done.");

                    swap_to(S_employee);
                break;

                case S_employer_acceptEmployee:
                    System.out.println("Please enter your ID.");
                    Employer_ID = kb.nextLine();
                    kb.nextLine();

                    System.out.println("Please enter the Employee_ID you want to hire.");
                    Employee_ID = kb.nextLine();
                    kb.nextLine();

                    System.out.println("An Employment History record is created, details are:");
                    System.out.println("Employee_ID, Company, Position_ID, Start, End");
                    /* List of person history */

                    swap_to(S_employee);
                break;
                default:

            }
        }

    }

    public static void main(String[] args){
        UI program = new UI();
        program.run();

    }
}