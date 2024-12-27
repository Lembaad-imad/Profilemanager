package com.digitalestate.gestionprofile.controllers;


import com.digitalestate.gestionprofile.controllers.request.EnsRequest;
import com.digitalestate.gestionprofile.controllers.response.EnsResponse;
import com.digitalestate.gestionprofile.services.EnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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


    @GetMapping()
     public ResponseEntity<List<EnsResponse>> getEns() {
        return ResponseEntity.ok(ensService.getEns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnsResponse> getEnsById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ensService.getEnsById(id));
    }


    @GetMapping("/page")
    public ResponseEntity<Page<EnsResponse>> getEnsPaginate(@RequestParam("size") int size, @RequestParam("page") int page) {
        return ResponseEntity.ok(ensService.getEnsPaginate(page,size));
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


    @DeleteMapping()
    public ResponseEntity<Void> deleteEns(@RequestParam("id")Long id) {
        ensService.deleteEns(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value= "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateEns(
            @PathVariable("id") Long id,
            @RequestPart("image") MultipartFile image,
            @RequestParam("ensRequest") String ensRequestBase64)
            throws Exception
    {
        ensService.updateEns(id,image,ensRequestBase64);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchEns(@PathVariable("id") Long id,@RequestBody EnsRequest ensRequest) {
        ensService.patchEns(id,ensRequest);
        return ResponseEntity.ok().build();
    }
}
