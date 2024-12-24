package com.digitalestate.gestionprofile.controllers.request;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FreelanceRequest {
    private String name;
    private String intitule;
    private String competences;
    private String email;
    private String phone;

}
