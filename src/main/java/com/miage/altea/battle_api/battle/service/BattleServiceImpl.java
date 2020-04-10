package com.miage.altea.battle_api.battle.service;

import com.miage.altea.battle_api.battle.bo.Battle;
import com.miage.altea.battle_api.battle.bo.BattlePokemon;
import com.miage.altea.battle_api.battle.bo.BattleTrainer;
import com.miage.altea.battle_api.battle.exception.BattleNotFoundException;
import com.miage.altea.battle_api.battle.exception.NoPokemonAliveException;
import com.miage.altea.battle_api.battle.exception.TrainerNotFoundException;
import com.miage.altea.battle_api.battle.exception.WrongTurnException;
import com.miage.altea.battle_api.pokemonTypes.service.PokemonTypeService;
import com.miage.altea.battle_api.trainers.bo.Trainer;
import com.miage.altea.battle_api.trainers.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BattleServiceImpl implements BattleService{

    private PokemonTypeService pokemonTypeService;

    private TrainerService trainerService;

    private HashMap<UUID, Battle> saved_battles;

    @Autowired
    public BattleServiceImpl(TrainerService trainerService, PokemonTypeService pokemonTypeService){
        this.pokemonTypeService = pokemonTypeService;
        this.trainerService = trainerService;
        this.saved_battles = new HashMap<>();
    }

    @Override
    public Battle createBattle(String dresseur1, String dresseur2) throws TrainerNotFoundException {
        Trainer trainer = trainerService.trainerByName(dresseur1);
        if(trainer == null)
            throw new TrainerNotFoundException("Trainer "+dresseur1+" doesnt exist");
        Trainer opponent = trainerService.trainerByName(dresseur2);
        if(opponent == null)
            throw new TrainerNotFoundException("Trainer "+dresseur2+" doesnt exist");

        List<BattlePokemon> battlePokemonList_trainer = trainer.getTeam().stream().map(team ->
                BattlePokemonFactory.createBattlePokemon(pokemonTypeService.getPokemonType(team.getPokemonTypeId()),team.getLevel())
        ).sorted(Comparator.comparingInt(BattlePokemon::getSpeed)).collect(Collectors.toList());

        List<BattlePokemon> battlePokemonList_opponent = opponent.getTeam().stream().map(team ->
                BattlePokemonFactory.createBattlePokemon(pokemonTypeService.getPokemonType(team.getPokemonTypeId()),team.getLevel())
        ).sorted(Comparator.comparingInt(BattlePokemon::getSpeed)).collect(Collectors.toList());

        Battle battle = new Battle(
                UUID.randomUUID(),
                new BattleTrainer(
                        trainer.getName(),
                        battlePokemonList_trainer.get(0).getSpeed() >= battlePokemonList_opponent.get(0).getSpeed(),
                        battlePokemonList_trainer
                ),
                new BattleTrainer(
                        opponent.getName(),
                        battlePokemonList_opponent.get(0).getSpeed() >= battlePokemonList_trainer.get(0).getSpeed(),
                        battlePokemonList_opponent
                )
        );

        saved_battles.put(battle.getUuid(), battle);
        return battle;
    }

    @Override
    public List<Battle> listBattle() {
        return new ArrayList<>(saved_battles.values());
    }

    @Override
    public Battle findBattle(UUID uuid) throws BattleNotFoundException {
        if(!saved_battles.keySet().contains(uuid))
            throw new BattleNotFoundException("The battle "+uuid+" doesnt exist");
        return saved_battles.get(uuid);
    }

    @Override
    public Battle attack(UUID uuid, String trainerName) throws BattleNotFoundException, NoPokemonAliveException, WrongTurnException {
        Battle battle = this.findBattle(uuid);
        if(battle.getTrainer().getTeam().stream().noneMatch(BattlePokemon::isAlive))
            throw new NoPokemonAliveException("The trainer "+battle.getTrainer().getName()+" can't fight, all his pokemons are KO");
        if(battle.getOpponent().getTeam().stream().noneMatch(BattlePokemon::isAlive))
            throw new NoPokemonAliveException("The trainer "+battle.getOpponent().getName()+" can't fight, all his pokemons are KO");

        BattleTrainer attacker = (battle.getTrainer().getName().equals(trainerName)) ? battle.getTrainer() : battle.getOpponent();
        BattleTrainer opponent = (attacker.getName().equals(battle.getTrainer().getName())) ? battle.getOpponent() : battle.getTrainer();

        if(!attacker.isNextTurn())
            throw new WrongTurnException(attacker.getName()+" can't attack for the moment, it's not his turn");

        battle.attack(attacker, opponent);
        return battle;
    }

}
