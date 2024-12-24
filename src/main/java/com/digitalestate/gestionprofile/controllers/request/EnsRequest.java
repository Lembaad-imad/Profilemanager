package com.digitalestate.gestionprofile.controllers.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EnsRequest {
    private String nameEns;
    private String nameContact;
    private String poste;
    private String email;
    private String phone;
}
