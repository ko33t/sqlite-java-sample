package com.scilm_k;

import java.io.Serializable;

/**
 * ゲームの点数と、ユーザの名前を保持するクラス
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
