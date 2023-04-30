package uz.pdp.projectimtihon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.service.SaveCSVFileService;

import java.io.File;


@RequestMapping("/api/csv")
@RestController
@RequiredArgsConstructor
public class SaveCSVFileController {
    private final SaveCSVFileService saveCSVFileService;

    @PostMapping("/{userId}")
    public ApiResponse<String> SaveCSVFile(@RequestParam("file") MultipartFile file, @PathVariable Integer userId){
        return saveCSVFileService.SaveCSVFile(file,userId);
    }
}
