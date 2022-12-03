package com.backend.repositories;

import com.backend.entities.Pet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetRepo extends MongoRepository<Pet, String> {

}