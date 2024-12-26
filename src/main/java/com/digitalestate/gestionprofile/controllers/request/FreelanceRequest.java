package com.digitalestate.gestionprofile.controllers.request;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FreelanceRequest {
    private String name;
    private String intitule;
    private List<String> competences;
    private String email;
    private String phone;
}
