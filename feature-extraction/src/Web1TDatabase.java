/**
 * Web1TDatabase.java
 * 
 * Last Updated: 11/19/2017
 * 
 * Connects to a database storing the Google Web1T bigrams, trigrams, 4-grams,
 * and 5-grams and their frequencies. Column names should be as follows:
 * Table Name: 2gms
 * Columns: ngram_id (bigint(20), null:no, key:primary, default:null, auto_increment)
 * 			w1 (varchar(200), null:yes, default:null)
 * 			w2 (varchar(200), null:yes, default:null)
 * 			frequency (bigint(20), null:yes, default:0)
 * 
 * Table Name: 3gms
 * Columns: ngram_id (bigint(20), null:no, key:primary, default:null, auto_increment)
 * 			w1 (varchar(200), null:yes, default:null)
 * 			w2 (varchar(200), null:yes, default:null)
 * 			w3 (varchar(200), null:yes, default:null)
 * 			frequency (bigint(20), null:yes, default:0)
 * 
 * Table Name: 4gms
 * Columns: ngram_id (bigint(20), null:no, key:primary, default:null, auto_increment)
 * 			w1 (varchar(200), null:yes, default:null)
 * 			w2 (varchar(200), null:yes, default:null)
 * 			w3 (varchar(200), null:yes, default:null)
 * 			w4 (varchar(200), null:yes, default:null)
 * 			frequency (bigint(20), null:yes, default:0)
 * 
 * Table Name: 5gms
 * Columns: ngram_id (bigint(20), null:no, key:primary, default:null, auto_increment)
 * 			w1 (varchar(200), null:yes, default:null)
 * 			w2 (varchar(200), null:yes, default:null)
 * 			w3 (varchar(200), null:yes, default:null)
 * 			w4 (varchar(200), null:yes, default:null)
 * 			w5 (varchar(200), null:yes, default:null)
 * 			frequency (bigint(20), null:yes, default:0)
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Web1TDatabase {

	private static Web1TDatabase instance = new Web1TDatabase();
	public static final String URL = "jdbc:mysql://localhost/web1t_full"; // Change this info according to the computer being used.
	public static final String USER = "ngram_user"; // Change this info according to the computer being used.
	public static final String PASSWORD = "web1t"; // Change this info according to the computer being used.
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private Connection connection;
	
	private Connection createConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD); // Connects to the database.
		}
		catch (SQLException e) {
			System.out.println("Error: Unable to connect to the database!");
		}
		return connection;
	}
	
	public Connection getConnection() {
		connection = instance.createConnection();
		return connection;
	}
	
	public Web1TDatabase() {
		try {
			Class.forName(DRIVER_CLASS); // Loads the MySQL Java Driver class.
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Load the necessary parts of the data, unmodified, into a table really quickly.
	public void quickLoad(String gms, String file_name) {
		try {
			Statement statement = connection.createStatement();
			
			// Process the Web1T data.
			String folderPath = "H:/Google Web1T/OriginalData/data/" + gms + "/processed/"; // Change according to the computer being used.
			String query = "load data local infile '" + folderPath + file_name + "' into table " + gms + " (w1, w2, w3, w4, w5, frequency);";  // This is for if you are loading 5-grams; if you're loading trigrams, for example, remove the columns for w4 and w5.
			System.out.println(query);
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAllData(String gms) {
		// Process the Web1T data.
		String folderPath = "H:/Google Web1T/OriginalData/data/" + gms + "/processed"; // Change according to the computer being used.
		File folder = new File(folderPath);
		
		// Sort the file names numerically.
		CompareStrings compareStrings = new CompareStrings();
		ArrayList<String> fileNames = new ArrayList<String>();
		for(final File fileEntry : folder.listFiles()) {
			fileNames.add(fileEntry.getName());
		}
		Collections.sort(fileNames, compareStrings);
		ArrayList<File> files = new ArrayList<File>();
		for(String fileName : fileNames) {
			files.add(new File(folderPath + "/" + fileName));
		}

		for (final File fileEntry : files) {
				quickLoad(gms, fileEntry.getName());
		}
	}

	public static void main(String[] args) {
		Web1TDatabase web1TDatabase = new Web1TDatabase();
		web1TDatabase.getConnection();
		
		// Example for building the dataset (do not uncomment unless you want
		// to build this dataset! This is a lengthy process that will take a
		// lot of storage space):
		//web1TDatabase.loadAllData("5gms");
	}

}
