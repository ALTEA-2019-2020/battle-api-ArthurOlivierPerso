package com.miage.altea.battle_api.battle.service;

import com.miage.altea.battle_api.battle.bo.BattlePokemon;
import com.miage.altea.battle_api.pokemonTypes.bo.PokemonType;

public class BattlePokemonFactory {

    public BattlePokemonFactory(){}

    public static BattlePokemon createBattlePokemon(PokemonType pokemonType, int level){
        int hp = StatsCalculator.calculateHp(pokemonType.getStats().getHp(), level);
        int attack = StatsCalculator.calculateStat(pokemonType.getStats().getAttack(), level);
        int defense = StatsCalculator.calculateStat(pokemonType.getStats().getDefense(), level);
        int speed = StatsCalculator.calculateStat(pokemonType.getStats().getSpeed(), level);

        return new BattlePokemon(hp, hp, attack, defense, speed, level, true, pokemonType);
    }
}
