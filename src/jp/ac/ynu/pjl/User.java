package jp.ac.ynu.pjl;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: kosasa
 * Date: 2013/06/20
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class User implements Serializable {
    private String name;
    private int point;

    public User(String name, int point){
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
