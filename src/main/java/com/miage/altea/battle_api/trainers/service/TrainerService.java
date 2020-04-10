package com.miage.altea.battle_api.trainers.service;

import com.miage.altea.battle_api.battle.exception.TrainerNotFoundException;
import com.miage.altea.battle_api.trainers.bo.Trainer;

public interface TrainerService {
    Trainer trainerByName(String name) throws TrainerNotFoundException;

}
