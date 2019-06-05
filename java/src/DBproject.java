/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

import java.time.LocalDate;
import java.lang.Object;
import java.time.format.DateTimeFormatter;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Plane");
				System.out.println("2. Add Pilot");
				System.out.println("3. Add Flight");
				System.out.println("4. Add Technician");
				System.out.println("5. Book Flight");
				System.out.println("6. List number of available seats for a given flight.");
				System.out.println("7. List total number of repairs per plane in descending order");
				System.out.println("8. List total number of repairs per year in ascending order");
				System.out.println("9. Find total number of passengers with a given status");
				System.out.println("10. < EXIT");
				
				switch (readChoice()){
					case 1: AddPlane(esql); break;
					case 2: AddPilot(esql); break;
					case 3: AddFlight(esql); break;
					case 4: AddTechnician(esql); break;
					case 5: BookFlight(esql); break;
					case 6: ListNumberOfAvailableSeats(esql); break;
					case 7: ListsTotalNumberOfRepairsPerPlane(esql); break;
					case 8: ListTotalNumberOfRepairsPerYear(esql); break;
					case 9: FindPassengersCountWithStatus(esql); break;
					case 10: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddPlane(DBproject esql) {//1
		int pID;
		String model;
		String make;
		int age;
		int numSeats;
		String query;

		//Need to query user for each of these values

		//pId

		while (true)
		{
			System.out.print("Please input Plane ID: ");
			try 
			{
				pID = Integer.parseInt(in.readLine());
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//model

		while (true)
		{
			System.out.print("Please input Plane Model: ");
			try
			{
				model = in.readLine();
				if (model.length() <= 0 || model.length() > 64) 
				{
					throw new RuntimeException("Your input is invalid! Plane model cannot be empty, and cannot exceed 64 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//make

		while (true)
		{
			System.out.print("Please input Plane Make: ");
			try
			{
				make = in.readLine();
				if (make.length() <= 0 || make.length() > 32)
				{
					throw new RuntimeException("Your input is valid! Plane Make cannot be empty, and cannot exceed 32 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//age
		//*INCLUDE IN ASSUMPTIONS: Plane age cannot be zero or greater than 30 (31+)

		while (true)
		{
			System.out.print("Please input Plane Age: ");
			try
			{
				age = Integer.parseInt(in.readLine());
				if (age <= 0)
				{
					throw new RuntimeException("Your input is invalid! Plane age cannot be zero, and cannot be negative!");
				}
				else if (age > 30)
				{
					throw new RuntimeException("Your input is invalid! Planes older than 30 are retired!");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//numSeats
		//*INCLUDE IN ASSUMPTIONS: Num seats cannot be zero, negative, or greater than 853

		while (true)
		{
			System.out.print("Please input Number of Seats on the Plane: ");
			try
			{
				numSeats = Integer.parseInt(in.readLine());
				if (numSeats <= 0)
				{
					System.out.println("Your input is invalid! Number of seats cannot be 0 or negative");
				}
				else if (numSeats > 853)
				{
					System.out.println("Your input is invalid! The maximum capacity of any plane is 853");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//query

		try
		{
			query = "INSERT INTO Plane (ID, make, model, age, seats) VALUES (" + pID + ", \'" + make + "\', \'" + model + "\', " + age + ", " + numSeats + ");";
			esql.executeUpdate(query);
		}
		catch (Exception e)
		{
			System.err.println("Query failed: " + e.getMessage());
		}
	}

	public static void AddPilot(DBproject esql) {//2
		int pID;
		String fullname;
		String nationality;
		String query;

		while (true)
		{
			System.out.print("Please input Pilot ID Number: ");
			try
			{
				pID = Integer.parseInt(in.readLine());
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is : " + e.getMessage());
				continue;
			}
		}

		//fullname

		while (true)
		{
			System.out.print("Please input Pilot fullname: ");
			try
			{
				fullname = in.readLine();
				if (fullname.length() <= 0)
				{
					throw new RuntimeException("Your input is invalid! Pilot fullname cannot be zero or negative");
				}
				else if (fullname.length() > 128)
				{
					throw new RuntimeException("Your input is invalid! Pilot fullname cannot exceed 128 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//nationality

		while (true)
		{
			System.out.print("Please input Pilot Nationality: ");
			try
			{
				nationality = in.readLine();
				if (nationality.length() <= 0)
				{
					throw new RuntimeException("Your input is invalid! Pilot Nationality cannot be zero or neagtive");
				}
				else if (nationality.length() > 24)
				{
					throw new RuntimeException("Your input is invalid! Pilot Nationality cannot exceed 24 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		//query

		try
		{
			query = "INSERT INTO Pilot (id, fullname, nationality) VALUES (" + pID + ", \'" + fullname + "\', \'" + nationality + "\');";
			esql.executeUpdate(query);
		}
		catch (Exception e)
		{
			System.err.println("Query failed: " + e.getMessage());
		}
	}

public static void AddFlight(DBproject esql) {//3
		// Given a pilot, plane and flight, adds a flight in the DB
		int fnum;
		int cost;
		int num_sold;
		int num_stops;
		String actual_departure_date;
		String actual_arrival_date;
		String arrival_airport;
		String departure_airport;
		String query;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		while (true)
		{
			System.out.print("Please input Flight number: ");
			try
			{
				fnum = Integer.parseInt(in.readLine());
				break;			
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}		
		}
		//add fnum
		
		while (true)
		{
			System.out.print("Please input the cost: ");
			try
			{
				cost = Integer.parseInt(in.readLine());
				if (cost <= 0)
				{
					throw new RuntimeException("Your input is invalid! Flight cost cannot be 0 or negative");
				}
				break;			
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}		
		}
		//add cost

		while (true)
		{
			System.out.print("Please input the seats sold: ");
			try
			{
				num_sold = Integer.parseInt(in.readLine());
				if (num_sold < 0)
				{
					throw new RuntimeException("Your input is invalid! Num seats sold cannot be zero or negative");
				}
				break;			
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}		
		}
		//add num_sold

		while (true)
		{
			System.out.print("Please input number of stops: ");
			try
			{
				num_stops = Integer.parseInt(in.readLine());
				if (num_stops < 0)
				{
					throw new RuntimeException("Your inputis invalid! Num stops cannot be negative");
				}
				break;			
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}		
		}
		//add num_stops

		while (true)
		{
			System.out.print("Please input actual departure date: ");
			try
			{
				actual_departure_date = in.readLine();
				LocalDate localadd = LocalDate.parse(actual_departure_date, format);
				break;			
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}		
		}
		//add actual_departure_date
		
		while (true)
		{
			System.out.print("Please input actual arrival date: ");
			try
			{
				actual_arrival_date = in.readLine();
				LocalDate localaad = LocalDate.parse(actual_arrival_date, format);
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}		
		}
		//add actual_arrival_date

		while (true)
		{
			System.out.print("Please input arrival_airport code: ");
			try
			{
				arrival_airport = in.readLine();
				if (arrival_airport.length() <= 0)
				{
					throw new RuntimeException("Your input is invalid! the airport code cannot be empty or negative");
				}
				else if (arrival_airport.length() > 5)
				{
					throw new RuntimeException("Your input is invalid! the airport code cannot exceed 5 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}
		}
		//add arrival_airport code		
		
		while (true)
		{
			System.out.print("Please input departure airport code: ");
			try
			{
				departure_airport = in.readLine();
				if (departure_airport.length() <= 0)
				{
					throw new RuntimeException("Your input is invalid! the airport code cannot be empty or negative");
				}
				else if (departure_airport.length() > 5)
				{
					throw new RuntimeException("Your input is invalid! the airport code cannot exceed 5 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e);
				continue;
			}
		}
		//add departure_airport code

		try
		{
			query = "INSERT INTO Flight (fnum, cost, num_sold, num_stops, actual_departure_date, actual_arrival_date, arrival_airport, departure_airport) VALUES (" + fnum + ", \'" + cost + "\', \'" + num_sold + "\', \'" + num_stops + "\', \'" + actual_departure_date + "\', \'" + actual_arrival_date + "\', \'" +  arrival_airport + "\', \'" + departure_airport + "\');";

			esql.executeUpdate(query);
		}
		catch (Exception e)
		{
			System.err.println("Query failed: " + e.getMessage());
		}
	}

	public static void AddTechnician(DBproject esql) {//4
		int tID;
		String fullname;
		String query;

		while (true)
		{
			System.out.print("Please input Technician ID Number: " );
			try
			{
				tID = Integer.parseInt(in.readLine());
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

		while (true)
		{
			System.out.print("Please input Technician fullname: ");
			try
			{
				fullname = in.readLine();
				if (fullname.length() <= 0)
				{
					throw new RuntimeException("Your input is invalid! Technician name cannot be empty or negative");
				}
				else if (fullname.length() > 128)
				{
					throw new RuntimeException("Your input is invalid! Technician name cannot exceed 128 characters");
				}
				break;
			}
			catch (Exception e)
			{
				System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
				continue;
			}
		}

			try
			{
				query = "INSERT INTO Technician (id, full_name) VALUES (" + tID + ", \'" + fullname + "\');";
				esql.executeUpdate(query);
			}
			catch (Exception e)
			{
				System.err.println("Query failed: " + e.getMessage());
			}

	}

	public static void BookFlight(DBproject esql) {//5
		// Given a customer and a flight that he/she wants to book, add a reservation to the DB
		int customerID;
	        int flightNumber;
        	String userInput;
	        String query;
        	int reservationNum;
        	String reservationStatus = "";
        while (true)
        {
            System.out.print("Please input Customer ID: ");
            try
            {
                customerID = Integer.parseInt(in.readLine());
                break;
            }
            catch (Exception e)
            {
                System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                continue;
            }
        }
        while (true)
        {
            System.out.print("Please input Flight Number: ");
            try
            {
                flightNumber = Integer.parseInt(in.readLine());
                break;
            }
            catch (Exception e)
            {
                System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                continue;
            }
        }
        try
        {
            query = "SELECT status\nFROM Reservation\nWHERE cid = " + customerID + " AND fid = " + flightNumber + ";";
            if(esql.executeQueryAndPrintResult(query) == 0) //reservation doesn't exist
            {
                while (true)
                {
                    System.out.println("Your reservation is not in our database. Would you like to book one? (y/n): ");
                    try
                    {
                        userInput = in.readLine();
                        if (userInput.equals("y"))
                        {
                            while (true)
                            {
                                System.out.print("Please input Reservation Number: ");
                                try
                                {
                                    reservationNum = Integer.parseInt(in.readLine());
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                                }
                            }
                            while (true)
                            {
                                System.out.print("Please input Reservation Status(W/R/C): ");
                                try
                                {
                                    reservationStatus = in.readLine();
                                    if(!reservationStatus.equals("W") && !reservationStatus.equals("R") && !reservationStatus.equals("C")) {
                                        throw new RuntimeException("Your input is invalid! Status can only be W, R, or C");
                                    }
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Your input is invalid! Your exception is " + e.getMessage());
                                }
                                try
                                {
                                    query = "INSERT INTO Reservation (rnum, cid, fid, status) VALUES (" + reservationNum + ", " + customerID + ", " + flightNumber + ", \'" + reservationStatus + "\');";
                                    esql.executeUpdate(query);
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Your input is invalid! Your exception is " + e.getMessage());
                                }
                            }
                        }
                        else if (!userInput.equals("n"))
                        {
                            throw new RuntimeException("Your input is invalid! Please input (y/n) next time");
                        }
                        break;
                    }
                    catch (Exception e)
                    {
                        System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                        continue;
                    }
                }
            }
                else
                {
                    while (true)
                    {
                        try
                        {
                            System.out.println("We found your reservation! Would you like to update it? (y/n)");
                            userInput = in.readLine();
                            if (userInput.equals("y"))
                            {
                                while (true)
                                {
                                    System.out.print("Please input new Reservation Status: " );
                                    try
                                    {
                                        reservationStatus = in.readLine();
                                        if(!reservationStatus.equals("W") && !reservationStatus.equals("R") && !reservationStatus.equals("C")) {
                                            throw new RuntimeException("Your input is invalid! Status can only be W, R, or C");
                                        }
                                        break;
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println("Your input is invalid! Your exception is " + e.getMessage());
                                        continue;
                                    }
                                }
                                try
                                {
                                    query = "UPDATE Reservation SET status = \'" + reservationStatus + "\' WHERE cid = " + customerID + " AND fid = " + flightNumber + ";";
                                    esql.executeUpdate(query);
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                                }
                            }
                            else if (!userInput.equals("n"))
                            {
                                throw new RuntimeException("Your input is invalid! Please input (y/n) next time");
                            }
                            break;
                        }
                        catch (Exception e)
                        {
                            System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
                            continue;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
            }
	}
	public static void ListNumberOfAvailableSeats(DBproject esql) {//6
		// For flight number, find the number of availalbe seats (i.e. total plane capacity minus num_sold from flight)	
		//without indexing 8.844ms 
		//with indexing 10.179ms
		String query;
		
		try
		{
			query = "SELECT T1.fnum, T1.seats-T2.num_sold FROM (SELECT F1.fnum, P1.seats FROM Flight F1, FlightInfo FI1, Plane P1 WHERE FI1.fiid = F1.fnum AND FI1.plane_id = P1.id) as T1, (SELECT F2.fnum, F2.num_sold FROM Flight F2 GROUP BY F2.fnum) as T2 WHERE T1.fnum = T2.fnum";
			esql.executeQueryAndPrintResult(query);
		}

		catch (Exception e)
		{
			System.err.println("Query failed: " + e.getMessage());
		}
	}

	public static void ListsTotalNumberOfRepairsPerPlane(DBproject esql) {//7
		// Count number of repairs per planes and list them in descending order
		//without indexing 1.377ms
		//with indexing 1.347ms
		String query;
		
		try
		{
			query = "SELECT P.id, count(R.rid)\nFROM Plane P, repairs R WHERE P.id = R.plane_id GROUP BY P.id ORDER BY count DESC";
			esql.executeQueryAndPrintResult(query);
		}
		catch (Exception e)
		{
			System.err.println("Query failed: " + e);
		}
	}

	public static void ListTotalNumberOfRepairsPerYear(DBproject esql) {//8
		// Count repairs per year and list them in ascending order
		//without indexing 1.134ms
		//with indexing 1.053ms
		String query;

		try
		{
			query = "SELECT EXTRACT (year FROM R.repair_date) as \"Year\", count(R.rid)\nFROM repairs R\nGROUP BY \"Year\"\nORDER BY count ASC;";
			esql.executeQueryAndPrintResult(query);
		}
		catch (Exception e)
		{
			System.err.println("Query failed: " + e);
		}
	}
	
	public static void FindPassengersCountWithStatus(DBproject esql) {//9
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
		//with indexing  2.343ms
		//without indexing 4.438ms
	    String status;
	    int flightNum;
	    String query;

	    while (true)
	    {
		System.out.println("Please input Passenger Status (W, C, R): ");
		try
		{
		    status = in.readLine();
		    if (!status.equals("W") && !status.equals("C") && !status.equals("R"))
		    {
			throw new RuntimeException("Your input is invalid! Please input W, C, or R");
		    }
		    break;
		}
		catch (Exception e)
		{
		    System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
		    continue;
		}
	    }
	    

	    while (true)
	    {
		System.out.println("Please input flight number:  ");
		try
		{
		    flightNum = Integer.parseInt(in.readLine());
		    break;
		}
		catch (Exception e)
		{
		    System.out.println("Your input is invalid! Your exception is: " + e.getMessage());
		    continue;
		}
	    }
	    
	    while (true)
	    {
		try 
		{
		    query = "SELECT COUNT(*)\nFROM Reservation\nWHERE fid = " + flightNum + " AND status = \'" + status + "\';";
		    esql.executeQueryAndPrintResult(query);
		    break;
		}
		catch (Exception e)
		{
		    System.out.println("Your input is invalid! Your exception is " + e.getMessage());
		}
	    }
	}
}
