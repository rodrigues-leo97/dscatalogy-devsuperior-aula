package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;

@UserInsertValid // annotation criada por conta -> serve para validar se o email inserido no banco já existe na base
public class UserInsertDTO extends UserDTO{
    private static final long serialVersionUID = 1L;

    //trabalhando com herança para manipular o PASSWORD

    private String password;

    public UserInsertDTO() {
        super(); //para garantir que se tiver alguma lógica no construtor vazio da SUPERCLASSE ele pegue a lógica de lá também
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
