package com.digitalestate.gestionprofile.controllers;


import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.services.EnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ens")
@CrossOrigin
public class EnsController {
    private final EnsService ensService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createEns(@RequestPart("file") MultipartFile file) throws IOException {
        ensService.createEns(file);
        return ResponseEntity.ok().build();
    }
}
