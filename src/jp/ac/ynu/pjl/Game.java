package jp.ac.ynu.pjl;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: kosasa
 * Date: 2013/06/20
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class Game implements Serializable{
    private int point;
    private String name;

    public Game(String name, int point){
        this.name = name;
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[name:" + name + " " + "point:" + point + "]";
    }
}
