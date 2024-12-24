package com.digitalestate.gestionprofile.controllers.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class EnsResponse {
    private Long id;
    private String nameEns;
    private String nameContact;
    private String poste;
    private String email;
    private String phone;
    private String image;
}
