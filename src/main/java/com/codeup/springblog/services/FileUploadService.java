package com.codeup.springblog.services;

import com.codeup.springbootexercises.models.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

// Service for uploading files
@Service("uploadService")
public class FileUploadService {

    // Local variable for filepath for uploading files
    // Saved in application.properties so the value will not be uploaded to github
    @Value("${file-upload-path}")
    private String uploadPath;

    // Takes a MultipartFile and a post, changes the filename, uploads the file, and updates post with filename
    public void uploadFile(MultipartFile uploadedFile, Post post, Model model) {
        // Sets new filename with the post id in front of uploaded filename
        String fileName = post.getId().toString().concat(uploadedFile.getOriginalFilename());
        String filePath = Paths.get(uploadPath, fileName).toString();
        File destinationFile = new File(filePath);

        // Tries to upload file, and adds model attribute based on success or failure
        // If successful also sets filename to post
        try {
            uploadedFile.transferTo(destinationFile);
            post.setFilename(fileName);
            model.addAttribute("message", "File uploaded successfully");
        } catch(IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Something went wrong: " + e);
        }
    }
}
