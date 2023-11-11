package com.saqaya.userapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;


    @Setter
    @Getter
    @RequiredArgsConstructor
    public class UserResponseDTO {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private boolean marketingConsent;


        public UserResponseDTO userToDTO(User user)
        {
            if(user!=null) {
                this.setId(user.getId());
                this.setFirstName(user.getFirstName());
                this.setLastName(user.getLastName());
                this.setEmail(user.getEmail());
                this.setMarketingConsent(user.isMarketingConsent());
            }
            return this;
        }
}
