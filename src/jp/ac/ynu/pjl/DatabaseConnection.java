package jp.ac.ynu.pjl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kosasa
 * Date: 2013/06/20
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseConnection {
    private static final String CONNECTION_NAME = "jdbc:sqlite:sampletest.sqlite3";
    private static DatabaseConnection databaseConnection;

    private DatabaseConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        createTable();

    }

    public static DatabaseConnection getInstance() throws ClassNotFoundException {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }

        return databaseConnection;
    }

    private void createTable() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"tbl_user\"(\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"name\" TEXT NOT NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"tbl_game\" (\"user_id\" text NOT NULL, \"point\" integer NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    private synchronized void executeSQL(String... sql) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            for (String s : sql) {
                statement.execute(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {

            }
        }
    }


    private long getLastInsertedId(){
        long id = 0;

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String sql = String.format("SELECT MAX(id) FROM tbl_user");

            ResultSet rs = statement.executeQuery(sql);


            if(rs.next()){
                id = rs.getInt(1);
            }

            rs.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public long insertNewUser(String name) {
        String statement = String.format("INSERT INTO tbl_user(\"name\") values(\"%s\")", name);
        executeSQL(statement);

        return getLastInsertedId();
    }

    public void deleteUser(long id){
        String statement = String.format("DELETE FROM tbl_user WHERE id = %d", id);
        executeSQL(statement);
    }

    public void deleteGameData(long id){
        String statement = String.format("DELETE FROM tbl_game WHERE user_id = %d", id);
        executeSQL(statement);
    }


    public void insertNewPoint(long id, int point) {
        String statement = String.format("INSERT INTO tbl_game(\"user_id\", \"point\") values(%d, %d)", id, point);
        System.out.println(statement);
        executeSQL(statement);
    }

    public List<Game> selectSortData(int limit){
        List<Game> games = new ArrayList<Game>();

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String sql = String.format("SELECT tbl_user.name, point FROM tbl_game LEFT JOIN tbl_user ON tbl_game.user_id = tbl_user.id  ORDER BY point DESC LIMIT %d", limit);

            ResultSet rs = statement.executeQuery(sql);


            while(rs.next()){
                String name = rs.getString("name");
                int point = rs.getInt("point");

                games.add(new Game(name, point));
            }

            rs.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return games;
    }




}
