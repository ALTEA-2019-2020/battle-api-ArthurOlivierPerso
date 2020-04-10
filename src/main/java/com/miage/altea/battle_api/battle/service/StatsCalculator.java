package com.miage.altea.battle_api.battle.service;

import com.miage.altea.battle_api.battle.bo.BattlePokemon;

public class StatsCalculator {
    public StatsCalculator(){

    }
    public static int calculateStat(int base, int level){
        return 5 + (base * level / 50);
    }

    public static int calculateHp(int base, int level){
        return 10 + level + (base * level / 50);
    }

    public static int calculateDamage(BattlePokemon attackerPokemon, BattlePokemon opponentPokemon) {
        return (((2*attackerPokemon.getLevel())/5) + 2 * (attackerPokemon.getAttack()/opponentPokemon.getDefense())) + 2;
    }
}
