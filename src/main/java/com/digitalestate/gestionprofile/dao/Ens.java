package com.digitalestate.gestionprofile.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ens {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="name_ens")
    private String nameEns;
    @Column(name="name_contact")
    private String nameContact;
    private String poste;
    private String email;
    private String phone;
    private String image;
}
