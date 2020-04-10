package com.miage.altea.battle_api.battle.bo;

import com.miage.altea.battle_api.battle.service.StatsCalculator;

import java.util.UUID;

public class Battle {
    private UUID uuid;
    private BattleTrainer trainer;
    private BattleTrainer opponent;

    public Battle(UUID uuid, BattleTrainer trainer, BattleTrainer opponent) {
        this.uuid = uuid;
        this.trainer = trainer;
        this.opponent = opponent;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public BattleTrainer getTrainer() {
        return trainer;
    }

    public void setTrainer(BattleTrainer trainer) {
        this.trainer = trainer;
    }

    public BattleTrainer getOpponent() {
        return opponent;
    }

    public void setOpponent(BattleTrainer opponent) {
        this.opponent = opponent;
    }

    public void attack(BattleTrainer attacker, BattleTrainer opponent){
        BattlePokemon attackerPokemon = attacker.getTeam().stream().filter(BattlePokemon::isAlive).findFirst().get();
        BattlePokemon opponentPokemon = opponent.getTeam().stream().filter(BattlePokemon::isAlive).findFirst().get();
        int damage = StatsCalculator.calculateDamage(attackerPokemon, opponentPokemon);
        opponentPokemon.setHp(opponentPokemon.getHp() - damage);
        if(opponentPokemon.getHp() <= 0){
            opponentPokemon.setHp(0);
            opponentPokemon.setAlive(false);
        }
        this.trainer.setNextTurn(!this.trainer.isNextTurn());
        this.opponent.setNextTurn(!this.opponent.isNextTurn());
    }
}
