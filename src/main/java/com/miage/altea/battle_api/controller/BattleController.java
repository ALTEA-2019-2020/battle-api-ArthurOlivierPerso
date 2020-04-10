package com.miage.altea.battle_api.controller;

import com.miage.altea.battle_api.battle.bo.Battle;
import com.miage.altea.battle_api.battle.bo.BattlePokemon;
import com.miage.altea.battle_api.battle.bo.BattleTrainer;
import com.miage.altea.battle_api.battle.exception.BattleNotFoundException;
import com.miage.altea.battle_api.battle.exception.NoPokemonAliveException;
import com.miage.altea.battle_api.battle.exception.TrainerNotFoundException;
import com.miage.altea.battle_api.battle.exception.WrongTurnException;
import com.miage.altea.battle_api.battle.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/battles")
public class BattleController {
    private BattleService battleService;

    @Autowired
    public BattleController(BattleService battleService){
        this.battleService = battleService;
    }

    @PostMapping
    public ResponseEntity createBattle(@RequestParam String dresseur1, @RequestParam String dresseur2) {
        try{
            return ResponseEntity.ok(battleService.createBattle(dresseur1, dresseur2).getUuid());

        }
        catch(TrainerNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Battle>> listBattle(){
        return ResponseEntity.ok(battleService.listBattle());
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity findBattle(@PathVariable UUID uuid) {
        try {
            return ResponseEntity.ok(battleService.findBattle(uuid));
        } catch (BattleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/{uuid}/{trainerName}/attack")
    public ResponseEntity attack(@PathVariable UUID uuid, @PathVariable String trainerName) {
        try {
            return ResponseEntity.ok(battleService.attack(uuid, trainerName));
        }
        catch(BattleNotFoundException | NoPokemonAliveException | WrongTurnException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
