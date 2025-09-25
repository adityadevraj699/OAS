package com.myproject.OAS.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        String resourceType;

        // Decide resource type
        if(extension.matches("jpg|jpeg|png|gif|mp4|mov")) {
            resourceType = "auto";  // Images / Videos
        } else {
            resourceType = "raw";   // PDF / DOCX / PPT / ZIP
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "use_filename", true,
                        "unique_filename", true
                ));

        String secureUrl = uploadResult.get("secure_url").toString();

        // Raw files ke liye inline view flag
        if(resourceType.equals("raw") && extension.matches("pdf|docx|ppt|pptx")) {
            secureUrl = secureUrl.replace("/upload/", "/upload/fl_attachment:false/");
        }

        return secureUrl;
    }
}
