import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {


    //    MySQL connection part - commented (no need to use it as project works on SQLite

//    private static final String USERNAME = "dbuser";
//    private static final String CONN = "jdbc:mysql://localhost/login";
//    private static final String SQCONN = "jdbc:sqlite:schoolsystem.sqlite";

    private static final String SQCONN = "jdbc:sqlite:minesweeper.sqlite";

    public static Connection getConnection() throws Exception {


        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SQCONN);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
