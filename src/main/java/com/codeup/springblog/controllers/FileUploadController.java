// NOT USING ANYMORE


package com.codeup.springblog.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    @Value("${file-upload-path}")
    private String uploadPath;

    @PostMapping("/fileupload")
    public void uploadFile(@RequestParam(name = "file")MultipartFile uploadedFile, Model model) {
        String fileName = uploadedFile.getOriginalFilename();
        String filePath = Paths.get(uploadPath, fileName).toString();
        File destinationFile = new File(filePath);
        try {
            uploadedFile.transferTo(destinationFile);
            model.addAttribute("message", "File uploaded successfully");
        } catch(IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Something went wrong: " + e);
        }
    }
}
