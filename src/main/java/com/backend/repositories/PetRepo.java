package com.backend.repositories;

import com.backend.entities.Pet;
import com.backend.entities.ShopItem;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;

public interface PetRepo extends MongoRepository<Pet, String> {

}