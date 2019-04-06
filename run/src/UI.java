import javax.swing.text.Position;
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
                                swap_to(S_admin_createTable);
                                break;
                            }
                            if (selection == 2){    //Delete Tables ---wrote
                                swap_to(S_admin_deleteTable);
                                break;
                            }
                            if (selection == 3){    //Load data     ---wrote
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
                    admin_do.create_table();
                    System.out.println("Processing...Done! Tables are created!");
                    swap_to(S_admin);
                break;

                case S_admin_deleteTable:
                    admin_do.delete_table();
                    System.out.println("Processing...Done! Tables are deleted!");
                    swap_to(S_admin);
                break;

                case S_admin_loadData:
                    System.out.println("Please enter the folder path.");
                    String folderPath = kb.nextLine(); //Enter the folder path
                    admin_do.load_data(folderPath);

                    System.out.println("Processing...Data is loaded!:");

                    swap_to(S_admin);
                break;

                case S_admin_checkData:
                    System.out.println("Number of records in each table:");
                    admin_do.check_data();
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

                    employee_do.show_available_positions(Employee_ID);

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
                    employee_do.mark_interested_position(Employee_ID);

                    swap_to(S_employee);
                break;

                case S_employee_checkAverageWorkTime:
                    System.out.println("Please enter your ID.");
                    Employee_ID = kb.nextLine();
                    int __time__ = employee_do.check_average_working_time(Employee_ID);
                    if (__time__ > 0){
                      System.out.printf("Your average working time is: %d days\n",__time__);
                    }
                    else {System.out.printf("Less than 3 records\n");}  

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

                    int __num__ = employer_do.post_position_recruitment(Employer_ID, Position_Title, Salary, experience_int);

                    /*
                    TO DO: the system should post the position requirement, and display the number of potential employees to the employer. Otherwise return an error message for the employer.
                    A potential employee is an employee that:
                     1. is not working in any company currently;
                     2. meet all the criteria: for criterion 1, the employee's Skills should contain
                        the position title; for criterion 2, the employee's Expected_Salary should not
                        larger than the upper bound of salary; for criterion 3, the employee's Experience
                        should not less than the input experience (no input means experience=0)
                    */
                    if(__num__ > 0){
                        System.out.printf("%d potential empolyees are found. The position recruitment is posted.\n",__num__);
                    }
                    else
                    {
                        System.out.println("No potential employee found, The position recruitment have not been posted");
                    }
                    swap_to(S_employer);
                break;

                case S_employer_checkAndArrange:
                    System.out.println("Please enter your ID.");
                    Employer_ID = kb.nextLine();
                    kb.nextLine();
                    // Get the list of position
                    employer_do.find_position_posted(Employer_ID);


                    System.out.println("Please pick one position id.");
                    String Position_ID = kb.nextLine();
                    kb.nextLine();
                    employer_do.find_interest_employee(Position_ID);

                    System.out.println("Please pick one employee by Employee_ID.");
                    Employee_ID = kb.nextLine();
                    kb.nextLine();
                    employer_do.arrange_interview(Employee_ID, Position_ID);

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

                    employer_do.accept_an_employee(Employer_ID, Employee_ID);

                    swap_to(S_employer);
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
