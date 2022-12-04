package com.backend.repositories;

import com.backend.entities.ShopItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopItemRepo extends MongoRepository<ShopItem, String> {

}
