import java.sql.*;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;

public class SqliteDemo {

    public static Connection con = null;
    public static int maxId = 0;
    
    
    

    public static void connect() {
    	String dir = System.getProperty("user.dir") + "\\notes.db";
        try {
            if(con == null){
                String url = "jdbc:sqlite:"+dir;
                con = DriverManager.getConnection(url);
                System.out.println("===> Connection to SQLite has been established successfully.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createFile() throws IOException {
    	// let's create the db file, and the database inside
    	String dir = System.getProperty("user.dir") + "\\notes.db";
    	
    	String path = dir;
    	File file = new File(path);
    	if (file.createNewFile()) {
    		System.out.println(path +" File Created");
    	} else {
    		System.out.println("File " + path + " already exists");
    	}
    }
    
    public static void createTable() {
    	try {
    		connect();
    		Statement state = con.createStatement();
    		String sql = "CREATE TABLE Notes(id int primary key, title text not null, date text not null)";
    		state.execute(sql);
    		System.out.println("Table has been created successfully.");
    		close();
    	} catch (SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    
    
    public static void close() {
        try {
            if (con != null) {
                con.close();
                con = null;
                System.out.println("===> Connection to SQLite has been closed successfully.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
    }

    public static void addNewNote(String title, String date) throws SQLException {
        try {
            connect();
            Statement state = con.createStatement();
            String sql = "INSERT INTO Notes VALUES("+(maxId + 1)+",'"+title+"', '"+date+"')";
            state.execute(sql);
            state.close();
            close();
            System.out.println("The note has been added successfully!");
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }


    }

    public static void showAllNotes(){
        try{
            connect();
            Statement state = con.createStatement();
            String sql = "SELECT * FROM Notes";
            ResultSet resultSet = state.executeQuery(sql);

            while (resultSet.next()){
                System.out.println( resultSet.getInt("id") + "\t" +
                                    resultSet.getString("title") + "\t" +
                                    resultSet.getString("date")
                );
            }
            close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void removeAll(){
        //TODO: this function will remove all the data stored in the sql database
        try{
            connect();
            Statement state = con.createStatement();
            String sql = "DELETE FROM Notes";
            state.executeQuery(sql);
            close();

        }
        catch (SQLException ex){
            System.out.println((ex.getMessage()));
        }

    }

    public static void removeNote(int id){
        //TODO: this function will remove the note with the specified id from the database
    	try {
    		connect();
    		Statement state = con.createStatement();
    		String sql = "DELETE FROM Notes where Notes.id="+id+"";
    		state.executeQuery(sql);
    		close();
    		
    	}catch(SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    }

    public static void updateNote(int id, String title, String date){
        //TODO: this function will update the note with the specified id
    	try {
    		connect();
    		Statement state = con.createStatement();
    		String sql = "UPDATE Notes set title='"+title+"', date='"+date+"' where id="+id+"";
    		state.executeQuery(sql);
    		close();
    		
    	}catch(SQLException ex) {
    		System.out.println(ex.getMessage());
    	}
    }


    public static void maxId(){
        try{
            connect();
            Statement state = con.createStatement();
            String sql = "SELECT max(id) FROM Notes";
            ResultSet resultSet = state.executeQuery(sql);
            while (resultSet.next()){
                maxId = resultSet.getInt(1);
                System.out.println("Notes count : " + maxId);
            }
            close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    

    public static void main(String[] args) throws SQLException, IOException {
    	createFile();
    	createTable();
    	
        Scanner input = new Scanner(System.in);
        String title;
        String date;
        int id;
        int choice;
        boolean running = true;
        
        
        

        System.out.println("======== NOTE KEEPER alpha ======");
        System.out.println("Welcome to note keeper , a program to store your notes created by Amine and Imane Elmouradi!");
        do {
            maxId();
            System.out.println("Select your choice:");
            System.out.println("1- Show all notes.");
            System.out.println("2- Add new note.");
            System.out.println("3- Update an existing note.");
            System.out.println("4- Remove an existing note.");
            System.out.println("5- Delete All your notes. (BE CAREFUL)");
            System.out.println("6- To exit the program.");
            System.out.print("Your choice: ");
            choice = input.nextInt();
            input.nextLine();
            switch (choice){
                case 1:
                    System.out.println("===== Your notes =====");
                    showAllNotes();
                    input.nextLine();
                    break;
                case 2:
                    System.out.println("=== Adding new note ===");
                    System.out.print("Title: ");
                    title = input.nextLine();
                    System.out.print("Date: ");
                    date = input.nextLine();
                    addNewNote(title,date);
                    break;
                case 3:
                    //TODO: update a note
                	System.out.println("=== Updating a note ===");
                	System.out.println("Id: ");
                	id = input.nextInt();
                	input.nextLine();
                	System.out.print("Title: ");
                	title = input.nextLine();
                    System.out.print("Date: ");
                    date = input.nextLine();
                    updateNote(id, title, date);
                    break;
                case 4:
                    //TODO: remove a note
                	System.out.println("=== Removing a note ===");
                	System.out.println("Id: ");
                	id = input.nextInt();
                	removeNote(id);                	
                    break;
                case 5:
                    //TODO: remove all notes
                	System.out.println("=== Removing all notes ===");
                	removeAll();
                    break;
                case 6:
                    running = false;
                    break;

                default:
                        System.out.println("Please select a correct number!");
            }
        } while (running);

        System.out.println("Thanks for using our program, see you soon!");

    }
}