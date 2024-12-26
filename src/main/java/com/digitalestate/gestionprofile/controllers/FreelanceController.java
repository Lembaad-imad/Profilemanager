package com.digitalestate.gestionprofile.controllers;

import com.digitalestate.gestionprofile.controllers.request.FreelanceRequest;
import com.digitalestate.gestionprofile.controllers.response.FreelanceResponse;

import com.digitalestate.gestionprofile.exception.ResourceNotAllowedException;
import com.digitalestate.gestionprofile.services.FreelanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/freelance")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class FreelanceController {
    private final FreelanceService freelanceService;

    @PostMapping
    public ResponseEntity<FreelanceResponse> createFreelance(@RequestBody FreelanceRequest freelance) {
        return ResponseEntity.ok(freelanceService.createFreelance(freelance));
    }


    @PostMapping(path = "/{id}/cv")
    public ResponseEntity<Void> uploadCv(@RequestPart("cv") MultipartFile file,@PathVariable("id") Long id) throws IOException {
        if (file == null || !file.getContentType().equals("application/pdf")) {
            throw new ResourceNotAllowedException("Only PDF files are allowed.");
        }
        freelanceService.uploadCv(file,id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/image")
    public ResponseEntity<Void> uploadImage(@RequestPart("image") MultipartFile file,@PathVariable("id") Long id) throws IOException {
        if (file == null || !file.getContentType().startsWith("image/")) {
            throw new ResourceNotAllowedException("Only images files are allowed.");
        }
        freelanceService.uploadImage(file,id);
        return ResponseEntity.ok().build();
    }


}
