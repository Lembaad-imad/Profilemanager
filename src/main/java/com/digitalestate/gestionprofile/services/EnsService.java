package com.digitalestate.gestionprofile.services;

import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.controllers.request.FreelanceRequest;
import com.digitalestate.gestionprofile.controllers.response.EnsResponse;
import com.digitalestate.gestionprofile.dao.Ens;
import com.digitalestate.gestionprofile.exception.ApiNotFoundException;
import com.digitalestate.gestionprofile.exception.ResourceAlreadyExistsException;
import com.digitalestate.gestionprofile.repositories.EnsRepository;
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
public class EnsService {
    private final EnsRepository ensRepository;
    @Value("${conf.profileManager}")
    private String confFolder ;
    private final String ensFolder = "ens";
    private final ObjectMapper objectMapper;

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
        saveFile(id,file);
        ens.setImage(file.getOriginalFilename());
        ensRepository.save(ens);
    }


    public void saveFile(Long id,MultipartFile file) throws IOException{
        String imagePath = confFolder + File.separator + ensFolder + File.separator + id ;
        Path path = Paths.get(imagePath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        FileUtils.saveFile(file , imagePath);
    }

    private EnsResponse toEnsResponse(Ens ens){
        EnsResponse ensResponse =  EnsResponse.builder()
                .id(ens.getId())
                .nameEns(ens.getNameEns())
                .nameContact(ens.getNameContact())
                .poste(ens.getPoste())
                .email(ens.getEmail())
                .phone(ens.getPhone())
                .image(null)
                .build();

        if(ens.getImage()!=null){
            String imagePath = confFolder + File.separator + ensFolder + File.separator + ens.getId()+File.separator+ens.getImage();
            File file = Paths.get(imagePath).toFile();
            byte[] imageByte = FileUtils.convertFileToByteArray(file);
            ensResponse.setImage(imageByte);
            ensResponse.setImageType(ens.getImage().substring(ens.getImage().lastIndexOf(".")+1));
        }
        return ensResponse;
    }


    public Page<EnsResponse> getEnsPaginate(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return ensRepository.findAll(pageable).map(this::toEnsResponse);
    }

    public void deleteEns(Long id) {
        Ens ens = ensRepository.findById(id).orElseThrow(()->new ApiNotFoundException("ens is not found with id " + id));
        ensRepository.delete(ens);
    }

    public void updateEns(Long id,MultipartFile image ,String ensRequestBase64) throws Exception {
        Ens ens = ensRepository.findById(id).orElseThrow(()->new ApiNotFoundException("ens is not found with id " + id));
        EnsRequest ensRequest = base64ToEnsRequest(ensRequestBase64);
        ens.setNameEns(ensRequest.getNameEns());
        ens.setNameContact(ensRequest.getNameContact());
        ens.setPoste(ensRequest.getPoste());
        ens.setEmail(ensRequest.getEmail());
        ens.setPhone(ensRequest.getPhone());
        if(image!=null){
            saveFile(id,image);
            ens.setImage(image.getOriginalFilename());
        }
        ensRepository.save(ens);
    }

    public void patchEns(Long id, EnsRequest ensRequest) {
        Ens ens = ensRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Ens is not found with id " + id));

        // Update only the provided fields
        if (ensRequest.getNameEns() != null) {
            ens.setNameEns(ensRequest.getNameEns());
        }
        if (ensRequest.getNameContact() != null) {
            ens.setNameContact(ensRequest.getNameContact());
        }
        if (ensRequest.getPoste() != null) {
            ens.setPoste(ensRequest.getPoste());
        }
        if (ensRequest.getEmail() != null) {
            ens.setEmail(ensRequest.getEmail());
        }
        if (ensRequest.getPhone() != null) {
            ens.setPhone(ensRequest.getPhone());
        }
        // Save updated entity
        ensRepository.save(ens);
    }

    public EnsResponse getEnsById(Long id) {
        Ens ens = ensRepository.findById(id).orElseThrow(()->new ApiNotFoundException("ens is not found with id " + id));
        return toEnsResponse(ens);
    }

    public EnsRequest base64ToEnsRequest(String base64) throws Exception {
        // Decode Base64 to JSON string
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String jsonString = new String(decodedBytes);
        log.info("received json is "+jsonString);
        // Convert JSON string to FreelanceRequest object
        return objectMapper.readValue(jsonString, EnsRequest.class);
    }

}
