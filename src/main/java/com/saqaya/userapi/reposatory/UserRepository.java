package com.saqaya.userapi.reposatory;

import com.saqaya.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
