package com.gabriel.sales.dtos;

import com.gabriel.sales.models.UserRoles;

public record RegisterRecordDto(String login, String password, UserRoles role) {
  
}
