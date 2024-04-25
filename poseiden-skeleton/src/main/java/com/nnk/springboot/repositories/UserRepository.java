package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Integer> {

    public DBUser findByUsername(String username);

}
