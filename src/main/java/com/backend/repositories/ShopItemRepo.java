package com.backend.repositories;

import com.backend.entities.ShopItem;

import com.backend.entities.ShopItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;

public interface ShopItemRepo extends MongoRepository<ShopItem, String> {

}
