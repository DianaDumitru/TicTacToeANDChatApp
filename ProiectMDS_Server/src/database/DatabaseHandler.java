package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

	// JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    
    //  Database attributes
    private String USER = "root";
    private String PASS = "root";
    private static final String DB_NAME = "ADDGames_DB";
    
    Connection conn;
    
    private static DatabaseHandler instance;
    
    private DatabaseHandler(String USER, String PASS) {
        this.USER = USER;
        this.PASS = PASS;
        
        if (createDatabase() == true){
            createTables();
        }
    }

    
    public static DatabaseHandler getInstance(String USER, String PASS) {
        if(instance == null){
            instance = new DatabaseHandler(USER,PASS);
        }
        return instance;
    }


    /**
     * Register JDBC driver and open a connection
     */
    private void preliminaries() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException se) {
            // Handle errors for JDBC   
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Create the database
     *
     * @return true if the database has been created, otherwise return false
     */
    private boolean createDatabase() {
        preliminaries();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);

        }  catch (SQLException sqlException) {
            //if the database already exist
            if (sqlException.getErrorCode() == 1007) {
                closeConnection();
                return false;
            } else {
                closeConnection();
                sqlException.printStackTrace();
                return false;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                closeConnection();
                return false;
            }
        }
        closeConnection();
        return true;
    }
    private void createTables() {
        preliminaries();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            String sqlQuery = "USE "+ DB_NAME;
            stmt.executeUpdate(sqlQuery);
            
            //create PLAYER
            sqlQuery = "CREATE TABLE PLAYER "
                    + "(username VARCHAR(255) not NULL, "
                    + " password VARCHAR(255) not NULL, "
                    + " rank INTEGER, "
                    + " PRIMARY KEY ( username ))";
            stmt.executeUpdate(sqlQuery);

            // create GAME
            sqlQuery = "CREATE TABLE GAME "
                    + "(id_game INTEGER not NULL, "
                    + " name VARCHAR(255), "
                    + " description VARCHAR(255) not NULL, "
                    + " PRIMARY KEY ( id_game )) ";
            stmt.executeUpdate(sqlQuery);

            // create GAME_HISTORY
            sqlQuery = "CREATE TABLE GAME_HISTORY "
                    + "(id_match INTEGER not NULL, "
            		+ " id_game INTEGER not NULL, "
                    + " winner VARCHAR(255) not NULL,"
                    + " play_date DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + " PRIMARY KEY ( id_match ), "
                    + " FOREIGN KEY (id_game) REFERENCES "
                    + DB_NAME + ".GAME(id_game) "
                    + " ON DELETE CASCADE ON UPDATE CASCADE, "
                    + " FOREIGN KEY (winner) REFERENCES "
                    + DB_NAME + ".PLAYER(username) "
                    + " ON DELETE CASCADE ON UPDATE CASCADE) ";
            stmt.executeUpdate(sqlQuery);
            
        } catch (SQLException ex) {
            closeConnection();
            ex.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                closeConnection();
            }
        }
        closeConnection();
    }
    
}
