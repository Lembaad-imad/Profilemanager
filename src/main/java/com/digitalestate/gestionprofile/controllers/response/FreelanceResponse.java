package com.digitalestate.gestionprofile.controllers.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class FreelanceResponse {
    private Long id;
    private String name;
    private String resume;
    private String intitule;
    private String listcompetences;
    private String email;
    private String phone;
    private String image;
}
