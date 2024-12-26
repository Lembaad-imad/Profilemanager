package com.digitalestate.gestionprofile.services;

import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.controllers.response.EnsResponse;
import com.digitalestate.gestionprofile.dao.Ens;
import com.digitalestate.gestionprofile.exception.ApiNotFoundException;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EnsService {
    private final EnsRepository ensRepository;
    @Value("${conf.profileManager}")
    private String confFolder ;

    public List<EnsResponse> getEns(){
        List<EnsResponse> ens = ensRepository.findAll().stream().map(this::toEnsResponse).collect(Collectors.toList());
        return ens;
    }


    public EnsResponse createEns(EnsRequest ensRequest){
        Optional<Ens> ens = ensRepository.findByEmailIgnoreCase(ensRequest.getEmail());
        if(ens.isPresent()){
            throw new ResourceAlreadyExistsException("Ens already exists");
        }


        Ens ensToSave = Ens.builder()
                .nameEns(ensRequest.getNameEns())
                .nameContact(ensRequest.getNameContact())
                .poste(ensRequest.getPoste())
                .email(ensRequest.getEmail())
                .phone(ensRequest.getPhone())
                .build();
        Ens savedEns =  ensRepository.save(ensToSave);
        log.info("ens created with  name ");
        return toEnsResponse(savedEns);
    }

    public void saveEnsFile(MultipartFile file , Long id ) throws IOException {
        Ens ens = ensRepository.findById(id).orElseThrow(() -> new ApiNotFoundException("ens is not found with id " + id));
        String ensFolder = "ens";
        String imagePath = confFolder + File.separator + ensFolder + File.separator + ens.getId() ;
        Path path = Paths.get(imagePath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        FileUtils.saveFile(file , imagePath);
        ens.setImage(file.getOriginalFilename());
        ensRepository.save(ens);
    }


    private EnsResponse toEnsResponse(Ens ens){
        return EnsResponse.builder()
                .id(ens.getId())
                .nameEns(ens.getNameEns())
                .nameContact(ens.getNameContact())
                .poste(ens.getPoste())
                .email(ens.getEmail())
                .phone(ens.getPhone())
                .build();
    }
}
