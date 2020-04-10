package com.miage.altea.battle_api.trainers.service;

import com.miage.altea.battle_api.battle.exception.TrainerNotFoundException;
import com.miage.altea.battle_api.pokemonTypes.bo.PokemonType;
import com.miage.altea.battle_api.pokemonTypes.service.PokemonTypeService;
import com.miage.altea.battle_api.trainers.bo.Team;
import com.miage.altea.battle_api.trainers.bo.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TrainerServiceImpl implements TrainerService {
    private String url;
    private RestTemplate template;

    PokemonTypeService pokemonTypeService;

    @Autowired
    public void setPokemonTypeService(PokemonTypeService pokemonTypeService){
        this.pokemonTypeService = pokemonTypeService;
    }

    @Autowired
    @Qualifier("trainerApiRestTemplate")
    void setRestTemplate(RestTemplate restTemplate) {
        template = restTemplate;
    }


    @Value("${trainer.service.url}")
    void setTrainerServiceUrl(String trainerServiceUrl) {
        url= trainerServiceUrl;
    }

    @Override
    public Trainer trainerByName(String name) throws TrainerNotFoundException {
        Trainer trainer = template.getForObject(url+"/trainers/"+name, Trainer.class);
        if(trainer == null || trainer.getName() == null)
            throw new TrainerNotFoundException("This trainer doesnt exist: "+name);
        List<PokemonType> pokemonTypes = pokemonTypeService.listPokemonsTypes();

        for(Team team: Objects.requireNonNull(trainer).getTeam()){
            List<PokemonType> list = (trainer.getPokemonTypes() == null) ? new ArrayList<>() : trainer.getPokemonTypes();
            PokemonType pokemonType = pokemonTypes.stream().filter(pType ->pType.getId()==team.getPokemonTypeId()).findFirst().get();
            list.add(pokemonType);
            trainer.setPokemonTypes(list);
        }
        return trainer;
    }
}
