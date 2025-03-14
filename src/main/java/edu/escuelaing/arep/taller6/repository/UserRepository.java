package edu.escuelaing.arep.taller6.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.escuelaing.arep.taller6.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    
}
