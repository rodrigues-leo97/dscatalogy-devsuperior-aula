package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

@UserUpdateValid // annotation criada por conta -> serve para validar se o email inserido no banco já existe na base
public class UserUpdateDTO extends UserDTO{
    private static final long serialVersionUID = 1L;

}
