package com.miage.altea.battle_api.battle.service;

import com.miage.altea.battle_api.battle.bo.Battle;
import com.miage.altea.battle_api.battle.exception.BattleNotFoundException;
import com.miage.altea.battle_api.battle.exception.NoPokemonAliveException;
import com.miage.altea.battle_api.battle.exception.TrainerNotFoundException;
import com.miage.altea.battle_api.battle.exception.WrongTurnException;

import java.util.List;
import java.util.UUID;

public interface BattleService {
    Battle createBattle(String dresseur1, String dresseur2) throws TrainerNotFoundException;
    List<Battle> listBattle();
    Battle findBattle(UUID uuid) throws BattleNotFoundException;
    Battle attack(UUID uuid, String trainerName) throws BattleNotFoundException, NoPokemonAliveException, WrongTurnException;
}
