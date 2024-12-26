package com.digitalestate.gestionprofile.controllers;

import com.digitalestate.gestionprofile.controllers.request.FreelanceRequest;
import com.digitalestate.gestionprofile.controllers.response.FreelanceResponse;

import com.digitalestate.gestionprofile.exception.ResourceNotAllowedException;
import com.digitalestate.gestionprofile.services.FreelanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/freelance")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class FreelanceController {
    private final FreelanceService freelanceService;

    @GetMapping()
    public ResponseEntity<List<FreelanceResponse>> getFreelance() {
        return ResponseEntity.ok(freelanceService.getFreelances());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FreelanceResponse> getFreelanceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(freelanceService.getFreelanceById(id));
    }


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
    @DeleteMapping()
    public ResponseEntity<Void> deleteEns(@RequestParam("id")Long id) {
        freelanceService.deleteFreelance(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<FreelanceResponse>> getFreelancePaginate(@RequestParam("size") int size, @RequestParam("page") int page) {
        return ResponseEntity.ok(freelanceService.getFreelancePaginate(page,size));
    }

    @PutMapping(value = "/{id}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateFreelance(
            @PathVariable("id")Long id,
            @RequestPart("resume") MultipartFile resume,
            @RequestPart("image") MultipartFile image,
            @RequestPart("freelanceRequest") FreelanceRequest freelanceRequest) throws IOException{
        freelanceService.updateFreelance(id,resume,image,freelanceRequest);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchFreelance(@PathVariable("id")Long id,@RequestBody FreelanceRequest freelanceRequest){
        freelanceService.patchFreelance(id,freelanceRequest);
        return ResponseEntity.ok().build();
    }


}
