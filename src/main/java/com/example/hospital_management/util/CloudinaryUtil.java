package com.example.hospital_management.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryUtil {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "hospital_avatars",
                            "resource_type", "image",
                            "use_filename", true,
                            "unique_filename", true
                    )
            );

            String uploadedUrl = (String) uploadResult.get("secure_url");

            // Apply transformation by building the URL manually
            String transformedUrl = buildTransformedUrl(uploadedUrl);

            System.out.println("Original URL: " + uploadedUrl);
            System.out.println("Transformed URL: " + transformedUrl);

            return transformedUrl;

        } catch (Exception e) {
            System.err.println("Cloudinary upload error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error uploading image to Cloudinary: " + e.getMessage());
        }
    }

    private String buildTransformedUrl(String originalUrl) {
        // Transform: https://res.cloudinary.com/cloud-name/image/upload/v123456/folder/filename.jpg
        // To: https://res.cloudinary.com/cloud-name/image/upload/c_fill,g_face,h_300,w_300/v123456/folder/filename.jpg

        if (originalUrl.contains("/image/upload/")) {
            return originalUrl.replace(
                    "/image/upload/",
                    "/image/upload/c_fill,g_face,h_300,w_300/"
            );
        }

        return originalUrl;
    }

    public void deleteFile(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Error deleting file from Cloudinary: " + e.getMessage());
            throw new IOException("Error deleting file: " + e.getMessage());
        }
    }

    public String extractPublicId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            // Extract public_id from Cloudinary URL
            // URL format: https://res.cloudinary.com/cloud-name/image/upload/[transformations]/v123456/folder/filename.ext

            // Find the version part (starts with 'v' followed by numbers)
            int versionIndex = url.lastIndexOf("/v");
            if (versionIndex == -1) {
                // No version, try to extract differently
                String[] parts = url.split("/");
                if (parts.length >= 2) {
                    String fileNameWithExtension = parts[parts.length - 1];
                    int dotIndex = fileNameWithExtension.lastIndexOf('.');
                    if (dotIndex > 0) {
                        String fileName = fileNameWithExtension.substring(0, dotIndex);
                        return "hospital_avatars/" + fileName;
                    }
                }
                return null;
            }

            // Get everything after version
            String afterVersion = url.substring(versionIndex + 1);
            // Remove the version number (e.g., "v1234567890/")
            int slashIndex = afterVersion.indexOf('/');
            if (slashIndex == -1) {
                return null;
            }

            String pathWithExtension = afterVersion.substring(slashIndex + 1);
            // Remove extension
            int dotIndex = pathWithExtension.lastIndexOf('.');
            if (dotIndex > 0) {
                return pathWithExtension.substring(0, dotIndex);
            }

            return pathWithExtension;

        } catch (Exception e) {
            System.err.println("Error extracting public ID: " + e.getMessage());
            return null;
        }
    }
}