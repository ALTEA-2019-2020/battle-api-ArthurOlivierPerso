package com.miage.altea.battle_api.pokemonTypes.service;

import com.miage.altea.battle_api.pokemonTypes.bo.PokemonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class PokemonTypeServiceImpl implements PokemonTypeService {
    private String url;
    private RestTemplate template;

    @Autowired
    @Qualifier("restTemplate")
    public void setRestTemplate(RestTemplate restTemplate) {
        template = restTemplate;
    }

    @Value("${pokemonType.service.url}")
    void setPokemonTypeServiceUrl(String pokemonServiceUrl) {
        url= pokemonServiceUrl;
    }

    @Override
    public List<PokemonType> listPokemonsTypes() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, String.valueOf(LocaleContextHolder.getLocale()));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PokemonType[]> response = this.template.exchange(this.url + "/pokemon-types/", HttpMethod.GET, entity, PokemonType[].class);
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public PokemonType getPokemonType(int id) {
        return this.template.getForObject(url +"/pokemon-types/{id}", PokemonType.class, id);
    }
}
