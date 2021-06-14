package com.thekeval.goalgame.Model;

public class PlayerModel {
    public String name;
    public String password;
    public int highestScore;

    public PlayerModel(String _name, String _password, int _highestScore) {
        name = _name;
        password = _password;
        highestScore = _highestScore;
    }
}
