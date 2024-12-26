package com.digitalestate.gestionprofile.controllers;


import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.controllers.response.EnsResponse;
import com.digitalestate.gestionprofile.services.EnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ens")
@CrossOrigin
public class EnsController {
    private final EnsService ensService;


    @GetMapping
     public ResponseEntity<List<EnsResponse>> getEns() {
         return ResponseEntity.ok(ensService.getEns());
    }
    @PostMapping( path = "/{id}/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> UploadEnsFile(@RequestPart("file") MultipartFile file,@PathVariable Long id) throws IOException {
        ensService.saveEnsFile(file,id);
        return ResponseEntity.ok().build();
    }


    @PostMapping()
    public ResponseEntity<EnsResponse> createEns(@RequestBody EnsRequest ensRequest) {
        return ResponseEntity.ok(ensService.createEns(ensRequest));
    }
}
