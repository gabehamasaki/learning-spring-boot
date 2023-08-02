package com.gabriel.sales.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.gabriel.sales.models.UserModel;


public interface UserRepository extends JpaRepository<UserModel, UUID> {
  UserDetails findByLogin(String login);
}
