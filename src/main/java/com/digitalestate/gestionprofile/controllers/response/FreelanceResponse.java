package com.digitalestate.gestionprofile.controllers.response;


import jakarta.annotation.Nullable;
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
    @Nullable
    private byte[] resume;
    private String intitule;
    private String competences;
    private String email;
    private String phone;
    @Nullable
    private byte[] image;
}
