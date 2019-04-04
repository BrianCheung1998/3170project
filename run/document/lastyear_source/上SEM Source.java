import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Properties;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Main {

    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
    public static String dbUsername = "Group--";
    public static String dbPassword = "CSCI3170";
    public static String sql = null;
    public static Connection con = null;
    public static Statement stmt = null;
    public static ResultSet resultSet = null;
    public static PreparedStatement pstmt = null;


    public static void system() throws SQLException{
        
    
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting...");    
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            System.out.println("Connected database successfully.");
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        
        Scanner system = new Scanner(System.in);
        int system_input;
        while(true){
            displayMenu("");
            system_input = system.nextInt();
            while(system_input > 4 || system_input < 1){
                System.out.println("[ERROR] Invalid input");
                System.out.println("Please enter [1-4]");
                system_input = system.nextInt();
            }
            while(system_input == 4){
                con.close();
                //stmt.close();
                System.exit(0);
            }
            if(system_input == 1){
                administrator();
            }else if(system_input == 2){
                passenger();
            }else if(system_input == 3){
                driver();
            }
        }
    }
    
    public static void displayMenu(String s){
        if(s.equals("A")){
            System.out.println("Administrator, what would you like to do?");
            System.out.println("1. Create tables");
            System.out.println("2. Delete tables");
            System.out.println("3. Load data");
            System.out.println("4. Check data");
            System.out.println("5. Go back");
            System.out.println("Please enter [1-5]");
        }else if(s.equals("P")){
            System.out.println("Passenger, what would you like to do?");
            System.out.println("1. Request a ride");
            System.out.println("2. Check trip records");
            System.out.println("3. Rate a trip");
            System.out.println("4. Go back");
            System.out.println("Please enter [1-4]");
        }else if(s.equals("D")) {
            System.out.println("Driver, what would you like to do?");
            System.out.println("1. Take a ride");
            System.out.println("2. Finish a trip");
            System.out.println("3. Check driver rating");
            System.out.println("4. Go back");
            System.out.println("Please enter [1-4]");
        }else{
            System.out.println("Welcome! Who are you?");
            System.out.println("1. An administrator");
            System.out.println("2. A passenger");
            System.out.println("3. A driver");
            System.out.println("4. None of the above");
            System.out.println("Please enter [1-4].");
        }
    }
    
    public static void administrator(){
        Scanner admin = new Scanner(System.in);
        int admin_input;
        int number_p = 0;
        int number_d = 0;
        int number_v = 0;
        int number_t = 0;
        String folder_path;
        String sql_d = null;
        String sql_v = null;
        String sql_p = null;
        String sql_t = null;
        String sql_r = null;

        while(true){
            displayMenu("A");
            admin_input = admin.nextInt();
            while(admin_input > 5){
                System.out.println("[ERROR] Ivalid input");
                System.out.println("Please enter [1-5]");
                admin_input = admin.nextInt();
            }
            if(admin_input == 1){
                System.out.print("Processing...");


                sql_d = "CREATE TABLE IF NOT EXISTS drivers("+
                "id int NOT NULL,"+
                "name char(30) NOT NULL,"+ 
                "vehicle_id char(6) NOT NULL,"+
                "PRIMARY KEY(id),"+
                "FOREIGN KEY(vehicle_id) REFERENCES vehicles(id))";

                sql_v = "CREATE TABLE IF NOT EXISTS vehicles("+
                "id char(6) NOT NULL,"+
                "model char(30) NOT NULL,"+
                "model_year int,"+
                "seats int,"+
                "PRIMARY KEY(id))";

                sql_p ="CREATE TABLE IF NOT EXISTS passengers("+
                "id INT NOT NULL,"+
                "name CHAR(30) NOT NULL,"+
                "PRIMARY KEY (id))";
                

               sql_t = "CREATE TABLE IF NOT EXISTS trips("+
                "id int NOT NULL,"+
                "driver_id int,"+
                "passenger_id int,"+
                "start datetime,"+
                "end datetime,"+
                "fee int,"+
                "rating int,"+
                "PRIMARY KEY(id),"+
                "FOREIGN KEY(driver_id) REFERENCES drivers(id),"+
                "FOREIGN KEY(passenger_id) REFERENCES passengers(id))";

                sql_r ="CREATE TABLE IF NOT EXISTS requests("+
                "id int NOT NULL,"+
                "passenger_id int,"+
                "model_year int,"+
                "model char(30),"+
                "passengers int,"+
                "taken boolean,"+
                "PRIMARY KEY(id))";


                
                try{
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql_p);
                    stmt.executeUpdate(sql_v);                  
                    stmt.executeUpdate(sql_d);                
                    stmt.executeUpdate(sql_t);
                    stmt.executeUpdate(sql_r);
                    System.out.println("Done! Tables are created!");
                }catch(SQLException e){
                    System.out.println("Error in Create Table!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }

            }else if(admin_input == 2){
                sql_t = "DROP TABLE IF EXISTS trips";

                sql_d = "DROP TABLE IF EXISTS drivers";

                sql_v = "DROP TABLE IF EXISTS vehicles";

                sql_p = "DROP TABLE IF EXISTS passengers";

                sql_r = "DROP TABLE IF EXISTS requests";

                 try{
                    stmt = con.createStatement();
                     stmt.executeUpdate(sql_t);
                    stmt.executeUpdate(sql_r);
                    stmt.executeUpdate(sql_p);           
                    stmt.executeUpdate(sql_d);
                    stmt.executeUpdate(sql_v);
                    System.out.println("Done! Tables are deleted!");
                }catch(SQLException e){
                    System.out.println("Error in Delete Table!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
            }else if(admin_input == 3){
                System.out.println("Please enter the folder path.");
                admin.nextLine();
                folder_path = admin.nextLine();
                System.out.print("Processing...");

                sql_p = "load data local infile './"+
                folder_path+
                "/passengers.csv' "+
                "into table passengers "+
                "fields terminated by ',' lines terminated by '\\n' (id,name)";


                sql_v = "load data local infile './"+
                folder_path+
                "/vehicles.csv' "+
                "into table vehicles "+
                "fields terminated by ',' lines terminated by '\\n' (id,model,model_year,seats)";

                sql_d = "load data local infile './"+
                folder_path+
                "/drivers.csv' "+
                "into table drivers "+
                "fields terminated by ',' lines terminated by '\\n' (id,name,vehicle_id)";

                sql_t = "load data local infile './"+
                folder_path+
                "/trips.csv' "+
                "into table trips "+
                "fields terminated by ',' lines terminated by '\\n' (id,driver_id,passenger_id,start,end,fee,rating)";

                 try{
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql_p);
                    stmt.executeUpdate(sql_v);
                    stmt.executeUpdate(sql_d);
                    stmt.executeUpdate(sql_t);
                    System.out.println("Data is loaded!");
                }catch(SQLException e){
                    if(e.getErrorCode() == 1146){
                        System.out.println("[Error] There are no tables in the database!");
                        continue;
                    }else if (e.getErrorCode() == 0){
                        System.out.println("[Error]File not found. Please check the folder path again.");
                        continue;
                    }
                    System.out.println("Error in Load Table!");
                    System.out.print("[Error ");
                    System.out.print(e.getErrorCode());
                    System.out.print("](");
                    System.out.print(e.getSQLState());
                    System.out.print("): ");
                    System.out.println(e);
                    
                }
            }else if(admin_input == 4){
                try{
                    stmt = con.createStatement();
                    sql_d = "SELECT count(*) FROM drivers;";
                    resultSet = stmt.executeQuery(sql_d);

                    if(!resultSet.isBeforeFirst()){
                        System.out.println("No records found.");
                    }else{
                        System.out.println("Number of records in each table:");
                        System.out.print("Table1: ");
                        if(resultSet.next())
                            System.out.println(resultSet.getInt("count(*)"));
                    }

                    sql_v = "SELECT count(*) FROM vehicles;";
                    resultSet = stmt.executeQuery(sql_v);

                    if(!resultSet.isBeforeFirst()){
                        System.out.println("No records found.");
                    }else{
                        System.out.print("Table2: ");
                        if(resultSet.next())
                            System.out.println(resultSet.getInt("count(*)"));
                    }

                    sql_p = "SELECT count(*) FROM passengers;";
                    resultSet = stmt.executeQuery(sql_p);

                    if(!resultSet.isBeforeFirst()){
                        System.out.println("No records found.");
                    }else{
                        System.out.print("Table3: ");
                        if(resultSet.next())
                            System.out.println(resultSet.getInt("count(*)"));
                    }

                    sql_t = "SELECT count(*) FROM trips;";
                    resultSet = stmt.executeQuery(sql_t);

                    if(!resultSet.isBeforeFirst()){
                        System.out.println("No records found.");
                    }else{
                        System.out.print("Table4: ");
                        if(resultSet.next())
                            System.out.println(resultSet.getInt("count(*)"));
                    }

                    stmt.close();
                    resultSet.close();
                    
                }catch(SQLException e){
                    if(e.getErrorCode() == 1146)
                        System.out.println("[Error] There are no tables in the database!");
                }
            }else if(admin_input == 5)
                    break;
        }
    }
    
     public static void passenger(){
        Scanner passenger = new Scanner(System.in);
        int passenger_input;
        int passenger_ID;
        int passenger_number;
        int passenger_taken = 0;
        int model_year;
        int trip_ID;
        int passenger_rating;
        int id_p = 0;
        int id_t = 0;
        int num_veh_suit = 0;
        String sql_p = null;
        String model;
        String model_year_temp;
        String start = null;
        String end = null;
        Date start_final = null;
        Date end_final = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int[] tem;
        tem = new int[4];
        while(true){
            displayMenu("P");
            passenger_input = passenger.nextInt();
           
            while(passenger_input > 4 || passenger_input < 1){
                System.out.println("[ERROR] Ivalid input");
                System.out.println("Please enter [1-4]");
                passenger_input = passenger.nextInt();
            }
            
            if(passenger_input == 1){
                System.out.println("Please enter your ID.");
                passenger_ID = passenger.nextInt();
                while(passenger_ID <= 0){
                    System.out.println("Invalid ID. Please enter again");
                    passenger_ID = passenger.nextInt();
                }
                System.out.println("Please enter the number of passengers.");
                passenger_number = passenger.nextInt();
                while(passenger_number <= 0){
                    System.out.println("Invalid number. Please enter again");
                    passenger_number = passenger.nextInt();
                }
                System.out.println("Please enter the earlist model year. (Press enter to skip)");
                passenger.nextLine();
                model_year_temp = passenger.nextLine();
                if(model_year_temp.equals(""))
                    model_year = 2010;
                else{
                    model_year = Integer.parseInt(model_year_temp);
                    if(model_year <= 0){
                        model_year = 2010;
                    }
                }
                System.out.println("Please enter the model. (Press enter to skip)");
                model = passenger.nextLine();
                
                sql_p = "SELECT MAX(id) AS id FROM passengers;";  //Error Handling. Check if passenger id is invalid
                try{
                    stmt = con.createStatement();
                    ResultSet rs_p = stmt.executeQuery(sql_p);
                    rs_p.next();
                    id_p = rs_p.getInt("id");
                }
                catch(SQLException e){
                    System.out.println("Error in executing SQL statement!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                if(passenger_ID > id_p){
                    System.out.println("[ERROR] Passenger not found.");
                    continue;
                }
                sql_p = "SELECT COUNT(*) AS passenger_taken FROM requests R WHERE R.passenger_id = "+passenger_ID+" AND R.taken = 0;";  //Error Handling. Check if passenger id is invalid
                try{
                     stmt = con.createStatement();
                     ResultSet rs_p = stmt.executeQuery(sql_p);
                     rs_p.next();
                     passenger_taken = rs_p.getInt("passenger_taken");
                }
                catch(SQLException e){
                    System.out.println("Error in executing SQL statement!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                if(passenger_taken >= 1){
                    System.out.println("[ERROR] There is Open Request.");
                    continue;
                }
                
                sql_p = "SELECT MAX(id) AS id FROM requests;";
                try{
                     pstmt = con.prepareStatement(sql_p);
                     ResultSet rs_p = pstmt.executeQuery(sql_p);
                     rs_p.next();
                     id_p = rs_p.getInt("id");
                }
                catch(SQLException e){
                    System.out.println("Error in executing SQL statement!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                id_p = id_p + 1;
                
                sql_p = "INSERT INTO requests(id, passenger_id, model_year, model, passengers, taken) VALUES ("+id_p+", "+passenger_ID+", "+model_year+", '"+model+"', "+passenger_number+", 0);";
                try{
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql_p);
                }
                catch(SQLException e){
                    System.out.println("Error in placing request!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                int unfinish_num = 0;
                sql_p = "SELECT count(*) FROM trips where end IS NULL;";
                try{
                    stmt = con.createStatement();
                    ResultSet rs_p = stmt.executeQuery(sql_p);
                    rs_p.next();
                    unfinish_num = rs_p.getInt("count(*)");
                }
                catch(SQLException e){
                    System.out.println("Error in executing SQL statement!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                
                if(model.equals("")){
                    if(unfinish_num == 0){
                        sql_p = "SELECT COUNT(*) AS vehicle_suitable "+
                        "FROM vehicles WHERE seats >= "+passenger_number+
                        " AND model_year >= "+model_year+";";
                    }else{
                        sql_p = "SELECT COUNT(*) AS vehicle_suitable "+
                        "FROM vehicles V,(SELECT vehicle_id FROM drivers,(SELECT driver_id FROM trips where end IS NULL) as temp1 "+
                        "WHERE id <> temp1.driver_id) as temp2 WHERE V.seats >= "+passenger_number+" AND V.model_year >= "+model_year+" "+
                        "AND V.id = temp2.vehicle_id;";
                    }
                }else{
                    if(unfinish_num == 0){
                        sql_p = "SELECT COUNT(*) AS vehicle_suitable "+
                        "FROM vehicles WHERE seats >= "+passenger_number+
                        " AND model_year >= "+model_year+" AND model LIKE '%" + model + "%';";
                    }else{
                        sql_p = "SELECT COUNT(*) AS vehicle_suitable "+
                        "FROM vehicles V,(SELECT vehicle_id FROM drivers,(SELECT driver_id FROM trips where end IS NULL) as temp1 "+
                        "WHERE id <> temp1.driver_id) as temp2 WHERE V.seats >= "+passenger_number+" AND V.model_year >= "+model_year+" "+
                        "AND V.model LIKE '%" + model + "%' AND V.id = temp2.vehicle_id;";
                    }
                } 
                try{
                    stmt = con.createStatement();
                    ResultSet rs_p = stmt.executeQuery(sql_p);
                    rs_p.next();
                    num_veh_suit = rs_p.getInt("vehicle_suitable");
                }
                catch(SQLException e){
                    System.out.println("Error in executing SQL statement!");
                    System.out.print("[Error]:");
                    System.out.println(e);
                }
                System.out.println("Your request is placed. "+num_veh_suit+" drivers are able to take the request.");
                continue;
            }else if(passenger_input == 2){
                    System.out.println("Please enter your ID.");
                    passenger_ID = passenger.nextInt();
                    while(passenger_ID <= 0){
                        System.out.println("Invalid ID. Please enter again");
                        passenger_ID = passenger.nextInt();
                    }
                    System.out.println("Please enter the start date.");
                    passenger.nextLine();
                    start = passenger.nextLine();
                    try {
                        start_final = dateFormat.parse(start);
                    } catch(Exception e) { 
                      System.out.println("Time start error") ;
                    }
                    System.out.println("Please enter the end date.");                
                    end = passenger.nextLine();
                    try {
                        end_final = dateFormat.parse(end);
                    } catch(Exception e) { 
                      System.out.println("Time end error") ;
                    }
                    sql_p = "SELECT MAX(id) AS id FROM passengers;";  //Error Handling. Check if passenger id is invalid
                    try{
                        stmt = con.createStatement();
                        ResultSet rs_p = stmt.executeQuery(sql_p);
                        rs_p.next();
                        id_p = rs_p.getInt("id");
                    }
                    catch(SQLException e){
                        System.out.println("Error in executing SQL statement!");
                        System.out.print("[Error]:");
                        System.out.println(e);
                    }
                    if(passenger_ID > id_p){
                        System.out.println("[ERROR] Passenger not found.");
                        continue;
                    }
                    System.out.println("Trip, ID, Driver Name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");
                    sql_p = "SELECT T.id AS id, D.name AS name, D.vehicle_id AS vehicle_id, V.model AS model, T.start AS start, T.end AS end, T.fee AS fee, T.rating AS rating FROM trips T, drivers D, vehicles V WHERE V.id = D.vehicle_id AND D.id = T.driver_id AND T.passenger_id = "+passenger_ID+" AND T.start BETWEEN '"+dateFormat.format(start_final)+"' AND '"+dateFormat.format(end_final)+"' ORDER BY start DESC ;";
                    try{
                        stmt = con.createStatement();
 //                       System.out.println("Before loop");
                        ResultSet rs_p = stmt.executeQuery(sql_p);
                        while(rs_p.next()){
 //                           System.out.println("Entered loop");
                            int T_id = rs_p.getInt("id");
                            String D_name = rs_p.getString("name");
                            String vehicle_id = rs_p.getString("vehicle_id");
                            model = rs_p.getString("model");                          
                            Date start_d = rs_p.getDate("start");
                            Time start_t = rs_p.getTime("start");
                            Date end_d = rs_p.getDate("end");
                            Time end_t = rs_p.getTime("end");
                            String start_record = start_d+" "+start_t;
                            String end_record = start_d+" "+end_t;
                            int fee = rs_p.getInt("fee");
                            int rating = rs_p.getInt("rating");                            
                            System.out.println(T_id+", "+D_name+", "+vehicle_id+", "+model+", "+start_record+", "+end_record+", "+fee+", "+rating);
                        }                     
                    }
                    catch(SQLException e){
                        System.out.println("Error in executing SQL statement!");
                        System.out.print("[Error]:");
                        System.out.println(e);
                    }                                                        
                    continue;
                }else if(passenger_input == 3){
                    System.out.println("Please enter your ID.");
                    passenger_ID = passenger.nextInt();
                    while(passenger_ID <=0){
                        System.out.println("Invalid ID. Please enter again");
                        passenger_ID = passenger.nextInt();
                    }
                    System.out.println("Please enter the trip ID.");
                    trip_ID = passenger.nextInt();
                    while(trip_ID <=0){
                        System.out.println("Invalid ID. Please enter again");
                        trip_ID = passenger.nextInt();
                    }
                    System.out.println("Please enter the rating");
                    passenger_rating = passenger.nextInt();                    
                    if(passenger_rating > 5 || passenger_rating < 0){
                        while(passenger_rating > 5 || passenger_rating < 0){
                            System.out.println("Invalid rating.");
                            System.out.println("Please enter [1-5]");
                            passenger_rating = passenger.nextInt();
                        }                        
                    }
                    sql_p = "SELECT MAX(id) AS id FROM passengers;";  //Error Handling. Check if passenger id is invalid
                    try{
                        stmt = con.createStatement();
                        ResultSet rs_p = stmt.executeQuery(sql_p);
                        rs_p.next();
                        id_p = rs_p.getInt("id");
                    }
                    catch(SQLException e){
                        System.out.println("Error in executing SQL statement!");
                        System.out.print("[Error]:");
                        System.out.println(e);
                    }
                    if(passenger_ID > id_p){
                        System.out.println("[ERROR] Passenger not found.");
                        continue;
                    }
                    sql_p = "SELECT MAX(id) AS id FROM trips;";  //Error Handling. Check if trip id is invalid
                    try{
                        stmt = con.createStatement();
                        ResultSet rs_p = stmt.executeQuery(sql_p);
                        rs_p.next();
                        id_t = rs_p.getInt("id");
                    }
                    catch(SQLException e){
                        System.out.println("Error in executing SQL statement!");
                        System.out.print("[Error]:");
                        System.out.println(e);
                    }
                    if(trip_ID > id_t){
                        System.out.println("[ERROR] Trip not found.");
                        continue;
                    }
                    
                    System.out.println("Trip ID, Driver name, Vehicle ID, Vehicle Model, Start, End, Fee, Rating");
                    sql_p = "SELECT end AS end FROM trips WHERE id = "+trip_ID+";";
                    try{
                     pstmt = con.prepareStatement(sql_p);
                     ResultSet rs_p = pstmt.executeQuery(sql_p);
                     rs_p.next();
                     end = rs_p.getString("end");
                    }
                    catch(SQLException e){
                        System.out.println("Error in executing SQL statement!");
                        System.out.print("[Error]1:");
                        System.out.println(e);
                    }
                    if(end != null){
                        sql_p = "UPDATE trips T SET T.rating = "+passenger_rating+" WHERE T.passenger_id = "+passenger_ID+" AND T.id = "+trip_ID+";";
                        try{
                            stmt = con.createStatement();
                            stmt.executeUpdate(sql_p);
                        }
                        catch(SQLException e){
                            System.out.println("Error in executing SQL statement!");
                            System.out.print("[Error]2:");
                            System.out.println(e);
                        }
                        sql_p = "SELECT T.id AS id, D.name AS name, D.vehicle_id AS vehicle_id, V.model AS model, T.start AS start, T.end AS end, T.fee AS fee, T.rating AS rating FROM trips T, drivers D, vehicles V WHERE V.id = D.vehicle_id AND D.id = T.driver_id AND T.id = "+trip_ID+";";
                        try{
                            pstmt = con.prepareStatement(sql_p);
                            ResultSet rs_p = pstmt.executeQuery(sql_p);
                            rs_p.next();
                            int T_id = rs_p.getInt("id");
                            String D_name = rs_p.getString("name");
                            String vehicle_id = rs_p.getString("vehicle_id");
                            model = rs_p.getString("model");
                            Format date_formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Format time_formatter = new SimpleDateFormat("HH:mm:ss");
                            Date start_date = rs_p.getDate("start");
                            String s_date = date_formatter.format(start_date);
                            Time start_time = rs_p.getTime("start");
                            String s_time = time_formatter.format(start_time);
                            String start_record = s_date +" "+ s_time;
                            Date end_date = rs_p.getDate("end");
                            String e_date = date_formatter.format(end_date);
                            Time end_time = rs_p.getTime("end");                           
                            String e_time = time_formatter.format(end_time);
                            String end_record = e_date +" "+ e_time;
                            int fee = rs_p.getInt("fee");
                            passenger_rating = rs_p.getInt("rating");                           
                            System.out.println(T_id+", "+D_name+", "+vehicle_id+", "+model+", "+start_record+", "+end_record+", "+fee+", "+passenger_rating);                
                        }
                        catch(SQLException e){
                            System.out.println("Error in executing SQL statement!");
                            System.out.print("[Error]3:");
                            System.out.println(e);
                        }
                    }
                    continue;
                }else if(passenger_input == 4){
                    break;
                }
        }
    } 
    
    public static void driver(){
        Scanner driver = new Scanner(System.in);
        int driver_input;
        int driver_ID;
        int request_ID = 0;
        int latest_trip_id = 0;
        double rating = 0;
        int pass_id = 0;
        int trip_fee;
        int numOfDriver;
        int numOfUnfin;
        String pass_name;
        char  yes_no;
        int r_id[] = new int[100];
        int r_index=0;
        Date start_date,end_date;
        Time start_time,end_time;

        while(true){
            displayMenu("D");
            driver_input = driver.nextInt();
            while(driver_input > 4 || driver_input < 1){
                System.out.println("[ERROR] Ivalid input");
                System.out.println("Please enter [1-4]");
                driver_input = driver.nextInt();
            }
          
           if(driver_input == 1){
            System.out.println("Please enter your ID.");
            driver_ID = driver.nextInt();

            try{
                stmt = con.createStatement();
                sql = "SELECT count(*) FROM drivers;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                numOfDriver = resultSet.getInt("count(*)");
                if(driver_ID <= 0 || driver_ID > numOfDriver){
                    System.out.println("[Error]Driver not found.");
                    continue;
                }
                sql = "SELECT count(*) FROM trips WHERE trips.driver_id = "+driver_ID+" AND trips.end IS NULL;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                numOfUnfin = resultSet.getInt("count(*)");
                if(numOfUnfin != 0){
                    System.out.println("[Error]You have not finished another trip.");
                    continue;
                }

                sql = "SELECT R.id, P.name, R.passengers "+ 
                "FROM requests R, passengers P, (SELECT V.seats, V.model_year, V.model "+ 
                "FROM vehicles V,(SELECT vehicle_id FROM drivers WHERE drivers.id = "+ new Integer(driver_ID).toString() + ") as temp1 "+
                "WHERE V.id = vehicle_id) as temp2 "+
                "WHERE R.passenger_id = P.id "+
                "AND R.taken = 0 "+
                "AND temp2.seats >= R.passengers "+
                "AND temp2.model_year >= R.model_year "+
                "AND temp2.model LIKE CONCAT('%',R.model,'%');";
                resultSet = stmt.executeQuery(sql);
                if(!resultSet.isBeforeFirst()){
                    System.out.println("There is no suitable request.");
                    continue;
                }else{
                    System.out.println("Request ID, Passenger name, Passengers");
                    r_index=0;
                    while(resultSet.next()){
                        r_id[r_index] = resultSet.getInt("id");
                        System.out.print(r_id[r_index] + ", ");
                        System.out.print(resultSet.getString(2) + ", ");
                        System.out.println(resultSet.getInt("passengers"));
                        r_index++;
                    }
                }
                resultSet = stmt.executeQuery(sql);
                boolean notEnd = true;
                int i;
                do{
                    System.out.println("Please enter the request ID");
                    request_ID = driver.nextInt();
                    for (i = 0; i<r_index; i++){
                        if(r_id[i] == request_ID){
                            notEnd = false;
                        }
                    }
                    if(notEnd){
                        System.out.println("Invalid request ID.");
                    }
                }while(notEnd);

                sql = "UPDATE requests SET taken = 1 WHERE id = "+ request_ID +";";
                stmt.executeUpdate(sql);

                sql = "SELECT MAX(id) FROM trips;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                latest_trip_id = resultSet.getInt("MAX(id)") + 1;

                sql = "SELECT P.id,P.name FROM passengers P, (SELECT passenger_id FROM requests WHERE requests.id = "+ 
                request_ID +")as temp1 "+
                "WHERE P.id = temp1.passenger_id;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                pass_id = resultSet.getInt("id");
                pass_name = resultSet.getString(2);

                sql = "SELECT now() AS start;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                start_date = resultSet.getDate("start");
                start_time = resultSet.getTime("start");

                sql = "INSERT INTO trips(id,driver_id,passenger_id,start) "+
                "VALUES ("+latest_trip_id+","+driver_ID+","+pass_id+","+"'"+start_date+" "+start_time+"');";
                stmt.executeUpdate(sql);

                System.out.println("Trip ID, Passenger name, Start");
                System.out.println(latest_trip_id+", "+pass_name+", "+start_date+" "+start_time);

                stmt.close();
                resultSet.close();
            }catch(SQLException e){
                System.out.println("Error!");
                System.out.print("[Error ");
                System.out.print(e.getErrorCode());
                System.out.print("](");
                System.out.print(e.getSQLState());
                System.out.print("): ");
                System.out.println(e);                
            }
                
           }else if(driver_input == 2){
            long time_S=0,time_E=0,time_D=0;
            System.out.println("Please enter your ID.");
            driver_ID = driver.nextInt();
            try{
                stmt = con.createStatement();
                sql = "SELECT count(*) FROM drivers;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                numOfDriver = resultSet.getInt("count(*)");
                if(driver_ID <= 0 || driver_ID > numOfDriver){
                    System.out.println("[Error]Driver not found.");
                    continue;
                }
                sql = "SELECT T.id,T.passenger_id,T.start FROM trips T "+
                "WHERE T.driver_id = "+driver_ID+" "+
                "AND T.end is NULL;";
                resultSet = stmt.executeQuery(sql);
                if(!resultSet.isBeforeFirst()){
                    System.out.println("There is no unfinished trip.");
                    continue;
                }
                System.out.println("Trip ID, Passenger ID, Start");
                resultSet.next();
                latest_trip_id = resultSet.getInt("id");
                System.out.print(latest_trip_id + ", ");
                pass_id = resultSet.getInt("passenger_id");
                System.out.print(pass_id + ", ");
                start_date = resultSet.getDate("start");
                start_time = resultSet.getTime("start");
                time_S = start_time.getTime();
                System.out.println(start_date + " " + start_time);
                
                
                System.out.println("Do you wish to finsih the trip? [y/n]");
                yes_no = driver.next().charAt(0);
                if(yes_no == 'y'){
                    sql = "SELECT now() AS end;";
                    resultSet = stmt.executeQuery(sql);
                    resultSet.next();
                    end_date = resultSet.getDate("end");
                    end_time = resultSet.getTime("end");
                    time_E = end_time.getTime();
                    time_D = time_E-time_S;
                    long diffMinutes = (time_D / (60 * 1000) % 60);
                    long diffHours = (time_D / (60 * 60 * 1000) % 24);
                    trip_fee = (int)(diffHours * 60 + diffMinutes);

                    sql = "UPDATE trips SET end = '"+end_date+" "+end_time+"',"+
                    "fee = "+trip_fee+",rating = 0 "+
                    "WHERE id = "+ latest_trip_id +";";
                    stmt.executeUpdate(sql);

                    sql = "SELECT P.name FROM passengers P "+
                    "WHERE P.id = "+pass_id+";";
                    resultSet = stmt.executeQuery(sql);
                    resultSet.next();
                    pass_name = resultSet.getString(1);

                    System.out.println("Trip ID, Passenger name, Start, End, Fee");
                    System.out.println(latest_trip_id+", "+pass_name+", "+start_date+" "+start_time+" "+end_date+" "+end_time+", "+trip_fee);
                }else if(yes_no == 'n'){
                    System.out.println("You have canceled to finsih the trip.");
                }else{
                    System.out.println("[Error]Invalid input.");
                }
                stmt.close();
                resultSet.close();
            }catch(SQLException e){
                System.out.println("Error!");
                System.out.print("[Error ");
                System.out.print(e.getErrorCode());
                System.out.print("](");
                System.out.print(e.getSQLState());
                System.out.print("): ");
                System.out.println(e);
            }

           }else if(driver_input == 3){
            int no_of_rating =0;
            System.out.println("Please enter your ID.");
            driver_ID = driver.nextInt();
            try{
                stmt = con.createStatement();
                sql = "SELECT count(*) FROM drivers;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                numOfDriver = resultSet.getInt("count(*)");
                if(driver_ID <= 0 || driver_ID > numOfDriver){
                    System.out.println("[Error]Driver not found.");
                    continue;
                }
                sql = "SELECT count(*) FROM trips WHERE driver_id = "+driver_ID+" AND rating IS NOT NULL;";
                resultSet = stmt.executeQuery(sql);
                resultSet.next();
                no_of_rating = resultSet.getInt("count(*)");
                
                if(no_of_rating > 4){
                    sql = "SELECT AVG(rating) FROM (SELECT * FROM trips WHERE driver_id = "+driver_ID+
                    " AND rating > 0 ORDER BY id DESC LIMIT 5) as T;";
                    resultSet = stmt.executeQuery(sql);
                    resultSet.next();
                    rating = resultSet.getDouble("AVG(rating)");
                }else{
                    System.out.println("NOT_YET_DETERMINED");
                    continue;           
                }
                System.out.println("Your driver rating is "+String.format("%.1f", rating)+".");
                

                stmt.close();
                resultSet.close();
            }catch(SQLException e){
                System.out.println("Error!");
                System.out.print("[Error ");
                System.out.print(e.getErrorCode());
                System.out.print("](");
                System.out.print(e.getSQLState());
                System.out.print("): ");
                System.out.println(e);
            }
           }else if(driver_input == 4){ 
               return;
           }
        }
    }
    
    public static void main(String[] args) throws SQLException{
        system();
    }
}
