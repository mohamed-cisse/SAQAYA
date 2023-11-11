package com.saqaya.userapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UserCreateCredentialsDTO {

    private  String id;
    private  String accessToken;

    public UserCreateCredentialsDTO userToDTO(User user)
    {
        if(user!=null) {
            this.setId(user.getId());
            this.setAccessToken(user.getAccessToken());
        }
        return this;
    }

}
