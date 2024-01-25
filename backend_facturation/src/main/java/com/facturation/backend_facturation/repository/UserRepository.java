package com.facturation.backend_facturation.repository;

import com.facturation.backend_facturation.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User,String> {

    User findUserByUserName(String username);

    boolean existsByUserName(String username);
}
