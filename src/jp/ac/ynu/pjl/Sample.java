package jp.ac.ynu.pjl;

import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: kosasa
 * Date: 2013/06/20
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class Sample {
    public static void main(String[] args) {
        try {
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            long id = databaseConnection.insertNewUser("テスト5");
            for (int i = 0; i < 2; ++i) {
                Random random = new Random();
                int point = random.nextInt(100);
                databaseConnection.insertNewPoint(id, point);
            }

            List<Game> gameList = databaseConnection.selectSortData(30);
            System.out.println(gameList);

//            databaseConnection.deleteGameData(id);
//            databaseConnection.deleteUser(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
