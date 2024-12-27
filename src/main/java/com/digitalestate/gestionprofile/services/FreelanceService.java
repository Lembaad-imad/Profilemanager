package com.digitalestate.gestionprofile.services;

import com.digitalestate.gestionprofile.controllers.request.FreelanceRequest;
import com.digitalestate.gestionprofile.controllers.response.FreelanceResponse;
import com.digitalestate.gestionprofile.dao.Freelance;
import com.digitalestate.gestionprofile.exception.ApiNotFoundException;
import com.digitalestate.gestionprofile.exception.ResourceAlreadyExistsException;
import com.digitalestate.gestionprofile.repositories.FreelanceRepository;
import com.digitalestate.gestionprofile.utils.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class FreelanceService {
    private final FreelanceRepository freelanceRepository;
    @Value("${conf.profileManager}")
    private String confFolder ;
    private final String freelanceFolder = "freelance";
    private final ObjectMapper objectMapper;


    public FreelanceResponse createFreelance(FreelanceRequest freelanceRequest) {
        Optional<Freelance> freelance = freelanceRepository.findFreelanceByEmailIgnoreCase(freelanceRequest.getEmail());
        if(freelance.isPresent()) {
            throw new ResourceAlreadyExistsException("Freelance already exists");
        }
        String competencesString = String.join(",", freelanceRequest.getCompetences());
        Freelance freelanceToSave = Freelance.builder()
                .name(freelanceRequest.getName())
                .intitule(freelanceRequest.getIntitule())
                .phone(freelanceRequest.getPhone())
                .email(freelanceRequest.getEmail())
                .competences(competencesString)
                .build();
        Freelance savedFreelance = freelanceRepository.save(freelanceToSave);
        log.info("ens created with  name ");
        return toFreelanceResponse(savedFreelance);
    }


    public void uploadCv(MultipartFile file,Long id) throws IOException {
        // Handle the resume upload
        saveFreelanceFile(file,id);

    }

    public void uploadImage(MultipartFile file,Long id) throws IOException {
        // Handle  the image upload
        saveFreelanceFile(file,id);
    }
    public void deleteFreelance(Long id) {
        Freelance freelance = freelanceRepository.findById(id).orElseThrow(()->new ApiNotFoundException("freelancer is not found with id " + id));
        freelanceRepository.delete(freelance);
    }



    private FreelanceResponse toFreelanceResponse(Freelance freelance) {
        FreelanceResponse freelanceResponse =  FreelanceResponse.builder()
                .id(freelance.getId())
                .name(freelance.getName())
                .resume(null)
                .intitule(freelance.getIntitule())
                .phone(freelance.getPhone())
                .email(freelance.getEmail())
                .competences(freelance.getCompetences())
                .image(null)
                .build();

        if(freelance.getImage() != null) {
            String imagePath = confFolder + File.separator + freelanceFolder + File.separator + freelance.getId()+File.separator+freelance.getImage() ;
            File imageFile = Paths.get(imagePath).toFile();
            byte[] imageByte = FileUtils.convertFileToByteArray(imageFile);
            freelanceResponse.setImage(imageByte);
            freelanceResponse.setImageType(freelance.getImage().substring(freelance.getImage().lastIndexOf(".")+1));
        }
        if(freelance.getResume() != null) {
            String resumePath = confFolder + File.separator + freelanceFolder + File.separator + freelance.getId()+File.separator+freelance.getResume() ;
            File resumeFile = Paths.get(resumePath).toFile();
            byte[] resumeByte = FileUtils.convertFileToByteArray(resumeFile);
            freelanceResponse.setResume(resumeByte);
        }
        return freelanceResponse;
    }



    public void saveFreelanceFile(MultipartFile file , Long id ) throws IOException {
        Freelance freelance = freelanceRepository.findById(id).orElseThrow(() -> new ApiNotFoundException("freelance  is not found with id " + id));
        saveFile(freelance.getId(),file);
        freelance.setImage(file.getOriginalFilename());
        freelanceRepository.save(freelance);
    }

    public void saveFile(Long id,MultipartFile file) throws IOException{
        String imagePath = confFolder + File.separator + freelanceFolder + File.separator + id ;
        Path path = Paths.get(imagePath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        FileUtils.saveFile(file , imagePath);
    }


    public Page<FreelanceResponse> getFreelancePaginate(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return freelanceRepository.findAll(pageable).map(this::toFreelanceResponse);
    }

    public List<FreelanceResponse> getFreelances() {
        return freelanceRepository.findAll().stream().map(this::toFreelanceResponse).collect(Collectors.toList());
    }

    public void updateFreelance(Long id, MultipartFile resume, MultipartFile image, String freelanceRequestBase64) throws Exception {
        FreelanceRequest freelanceRequest = base64ToFreelanceRequest(freelanceRequestBase64);
        Freelance freelance = freelanceRepository.findById(id).orElseThrow(()->new ApiNotFoundException("freelance is not found with id " + id));
        freelance.setName(freelanceRequest.getName());
        freelance.setIntitule(freelanceRequest.getIntitule());
        freelance.setPhone(freelanceRequest.getPhone());
        freelance.setEmail(freelanceRequest.getEmail());
        freelance.setCompetences(String.join(",", freelanceRequest.getCompetences()));
        if(resume!=null){
            saveFile(id,resume);
            freelance.setResume(resume.getOriginalFilename());
        }if(image!=null){
            saveFile(id,image);
            freelance.setImage(image.getOriginalFilename());
        }
        freelanceRepository.save(freelance);
    }

    public void patchFreelance(Long id, FreelanceRequest freelanceRequest) {
        Freelance freelance = freelanceRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Freelance is not found with id " + id));

        // Update only the provided fields
        if (freelanceRequest.getName() != null) {
            freelance.setName(freelanceRequest.getName());
        }
        if (freelanceRequest.getIntitule() != null) {
            freelance.setIntitule(freelanceRequest.getIntitule());
        }
        if (freelanceRequest.getPhone() != null) {
            freelance.setPhone(freelanceRequest.getPhone());
        }
        if (freelanceRequest.getEmail() != null) {
            freelance.setEmail(freelanceRequest.getEmail());
        }
        if (freelanceRequest.getCompetences() != null) {
            freelance.setCompetences(String.join(",", freelanceRequest.getCompetences()));
        }

        // Save updated entity
        freelanceRepository.save(freelance);
    }


    public FreelanceRequest base64ToFreelanceRequest(String base64) throws Exception {
        // Decode Base64 to JSON string
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String jsonString = new String(decodedBytes);
        log.info("received json is "+jsonString);
        // Convert JSON string to FreelanceRequest object
        return objectMapper.readValue(jsonString, FreelanceRequest.class);
    }


    public FreelanceResponse getFreelanceById(Long id) {
        Freelance freelance = freelanceRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Freelance is not found with id " + id));
        return toFreelanceResponse(freelance);
    }
}
