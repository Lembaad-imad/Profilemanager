package com.digitalestate.gestionprofile.services;

import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.dao.Ens;
import com.digitalestate.gestionprofile.exception.ResourceAlreadyExistsException;
import com.digitalestate.gestionprofile.repositories.EnsRepository;
import com.digitalestate.gestionprofile.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class EnsService {
    private final EnsRepository ensRepository;
    @Value("${conf.profileManager}")
    private String confFolder ;

    public void createEns(MultipartFile file) throws IOException {
//        Optional<Ens> ens = ensRepository.findByEmailIgnoreCase(ensRequest.getEmail());
//        if(ens.isPresent()){
//            throw new ResourceAlreadyExistsException("Ens already exists");
//        }
//
//
//        Ens ensToSave = Ens.builder()
//                .nameEns(ensRequest.getNameEns())
//                .nameContact(ensRequest.getNameContact())
//                .poste(ensRequest.getPoste())
//                .email(ensRequest.getEmail())
//                .phone(ensRequest.getPhone())
//                .build();
//        Ens savedEns = ensRepository.save(ensToSave);
        saveEnsFile(file);
        log.info("ens created with  name ");
    }

    public void saveEnsFile(MultipartFile file ) throws IOException {
        String imagePath = confFolder + File.separator + "ens" + File.separator ;
        Path path = Paths.get(imagePath + File.separator + file.getOriginalFilename());
        FileUtils.saveFile(file , imagePath);
    }
}
