package utils;

import java.sql.*;

public class DatabaseConnection {
    Connection c = null;
    Statement stmt = null;

    public DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:justmeet.db");
            c.setAutoCommit(false);
            createTables();
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void closeConnection() {
        try{
            c.close();
        }catch( Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createTables() {
        try{
            stmt = c.createStatement();
            //Create user table
            String sql = "CREATE TABLE IF NOT EXISTS users"+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username VARCHAR(255) NOT NULL UNIQUE," +
                    "name VARCHAR(255) NOT NULL," +
                    "surname VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "moderatore INT DEFAULT 0);";
            stmt.executeUpdate(sql);
            //Create event table
            sql = "CREATE TABLE IF NOT EXISTS events"+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "creatorId INT NOT NULL," +
                    "eventType INT NOT NULL," +
                    "title VARCHAR(255) NOT NULL," +
                    "description VARCHAR(255) NOT NULL," +
                    "dateTime VARCHAR(255) NOT NULL," +
                    "nPartecipantiMax INT NOT NULL," +
                    "nPartecipanti INT DEFAULT 0," +
                    "active INT DEFAULT 1, " +
                    "FOREIGN KEY(creatorId) REFERENCES users(id)," +
                    "FOREIGN KEY(eventType) REFERENCES event_type(id));";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS user_event"+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INT NOT NULL," +
                    "eventId INT NOT NULL," +
                    "signup VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY(userId) REFERENCES users(id)," +
                    "FOREIGN KEY(eventId) REFERENCES events(id));";
            stmt.executeUpdate(sql);
            stmt.close();
            sql = "CREATE TABLE IF NOT EXISTS user_event_rating"+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INT NOT NULL," +
                    "eventId INT NOT NULL," +
                    "rating FLOAT," +
                    "FOREIGN KEY(userId) REFERENCES users(id)," +
                    "FOREIGN KEY(eventId) REFERENCES events(id));";
            stmt.executeUpdate(sql);
            stmt.close();
            sql = "CREATE TABLE IF NOT EXISTS event_type" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "event_type VARCHAR(255) NOT NULL);";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        }catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
