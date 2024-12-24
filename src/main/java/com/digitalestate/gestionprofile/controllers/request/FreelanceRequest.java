package com.digitalestate.gestionprofile.controllers.request;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FreelanceRequest {
    private String name;
    private String resume;
    private String intitule;
    private String listcompetences;
    private String email;
    private String phone;
    private String image;
}
