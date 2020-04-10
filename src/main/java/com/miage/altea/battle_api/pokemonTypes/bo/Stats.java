package com.miage.altea.battle_api.pokemonTypes.bo;

public class Stats {
    private int speed;

    private int defense;

    private int attack;

    private int hp;

    public Stats(){}

    public Stats(int speed, int defense, int attack, int hp) {
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.hp = hp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
