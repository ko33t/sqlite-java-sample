package com.scilm_k;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite3をつないで色々してみるクラス
 */
public class DatabaseConnection {
    private static final String CONNECTION_NAME = "jdbc:sqlite:sampletest.sqlite3";
    private static DatabaseConnection databaseConnection;

    /**
     * ドライバー読み込み、テーブル作成
     *
     * @throws ClassNotFoundException 　ライブラリ読み込みに失敗している時スロー
     */
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

    /**
     * テーブル作成
     */
    private void createTable() {
        executeSQL("CREATE TABLE IF NOT EXISTS \"tbl_user\"(\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"name\" TEXT NOT NULL)",
                "CREATE TABLE IF NOT EXISTS \"tbl_game\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, \"user_id\" text NOT NULL, \"point\" integer NOT NULL)");
    }

    /**
     * update系のSQL実行メソッド
     *
     * @param sql 実行するSQL
     */
    private synchronized void executeSQL(String... sql) {
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
                // 何もしない
            }
        }
    }

    /**
     * 新しいユーザを作成
     *
     * @param name 　ユーザ名
     * @return ユーザID
     */
    public long insertNewUser(String name) {
        String statement = String.format("INSERT INTO tbl_user(\"name\") values(\"%s\")", name);
        executeSQL(statement);

        return getLastInsertedId();
    }

    /**
     * ユーザ削除
     *
     * @param id 削除するユーザのID
     */
    public void deleteUser(long id) {
        String statement = String.format("DELETE FROM tbl_user WHERE id = %d", id);
        executeSQL(statement);
    }

    /**
     * ゲームのデータ削除
     *
     * @param id 削除するユーザのID
     */
    public void deleteGameData(long id) {
        String statement = String.format("DELETE FROM tbl_game WHERE user_id = %d", id);
        executeSQL(statement);
    }

    /**
     * ゲームのポイント追加
     *
     * @param id    ゲームプレイヤーのユーザID
     * @param point ポイント
     */
    public void insertNewPoint(long id, int point) {
        String statement = String.format("INSERT INTO tbl_game(\"user_id\", \"point\") values(%d, %d)", id, point);
//        System.out.println(statement);
        executeSQL(statement);
    }


    /**
     * ポイントでソートしたデータの取得
     *
     * @param limit 取得する最大件数
     * @return データ
     */
    public List<Game> selectSortData(int limit) {
        List<Game> games = new ArrayList<Game>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String sql = String.format("SELECT tbl_user.name, point FROM tbl_game LEFT JOIN tbl_user ON tbl_game.user_id = tbl_user.id  ORDER BY point DESC LIMIT %d", limit);

            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                String name = rs.getString("name");
                int point = rs.getInt("point");

                games.add(new Game(name, point));
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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

    /**
     * 最後にtbl_userに挿入した行のID
     *
     * @return ID
     */
    private long getLastInsertedId() {
        long id = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_NAME);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String sql = String.format("SELECT MAX(id) FROM tbl_user");

            ResultSet rs = statement.executeQuery(sql);


            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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


}
