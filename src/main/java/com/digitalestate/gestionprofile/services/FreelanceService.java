package com.digitalestate.gestionprofile.services;

import com.digitalestate.gestionprofile.controllers.request.FreelanceRequest;
import com.digitalestate.gestionprofile.controllers.response.FreelanceResponse;
import com.digitalestate.gestionprofile.dao.Freelance;
import com.digitalestate.gestionprofile.exception.ApiNotFoundException;
import com.digitalestate.gestionprofile.exception.ResourceAlreadyExistsException;
import com.digitalestate.gestionprofile.repositories.FreelanceRepository;
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
public class FreelanceService {
    private final FreelanceRepository freelanceRepository;
    @Value("${conf.profileManager}")
    private String confFolder ;
    public FreelanceResponse createFreelance(FreelanceRequest freelanceRequest) {
        Optional<Freelance> freelance = freelanceRepository.findFreelanceByEmailIgnoreCase(freelanceRequest.getEmail());
        if(freelance.isPresent()) {
            throw new ResourceAlreadyExistsException("Freelance already exists");
        }
        Freelance freelanceToSave = Freelance.builder()
                .name(freelanceRequest.getName())
                .intitule(freelanceRequest.getIntitule())
                .phone(freelanceRequest.getPhone())
                .email(freelanceRequest.getEmail())
                .competences(freelanceRequest.getCompetences())
                .build();
        Freelance savedFreelance = freelanceRepository.save(freelanceToSave);
        log.info("ens created with  name ");
        return toFreelanceResponse(savedFreelance);
    }


    public void uploadCv(MultipartFile file,Long id) throws IOException {
            // Handle the resume upload
        ;saveFreelanceFile(file,id);

    }

    public void uploadImage(MultipartFile file,Long id) throws IOException {
        // Handle  the image upload
        saveFreelanceFile(file,id);
    }


    private FreelanceResponse toFreelanceResponse(Freelance freelance) {
        return FreelanceResponse.builder()
                .id(freelance.getId())
                .name(freelance.getName())
                .intitule(freelance.getIntitule())
                .phone(freelance.getPhone())
                .email(freelance.getEmail())
                .competences(freelance.getCompetences())
                .build();
    }


    public void saveFreelanceFile(MultipartFile file , Long id ) throws IOException {
        Freelance freelance = freelanceRepository.findById(id).orElseThrow(() -> new ApiNotFoundException("freelance  is not found with id " + id));
        String freelanceFolder = "freelance";
        String imagePath = confFolder + File.separator + freelanceFolder + File.separator + freelance.getId() ;
        Path path = Paths.get(imagePath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        FileUtils.saveFile(file , imagePath);
        freelance.setImage(file.getOriginalFilename());
        freelanceRepository.save(freelance);
    }



}
