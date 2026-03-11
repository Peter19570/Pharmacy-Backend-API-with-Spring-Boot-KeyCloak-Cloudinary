package com.example.pharmacy.apps.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pharmacy.apps.common.dto.response.CloudinaryResponseDto;
import com.example.pharmacy.exception.custom.CloudinaryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryResponseDto upload(
            MultipartFile file, String folder){
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder
                    ));

            String profileUrl = String.valueOf(result.get("secure_url"));
            String profileId = String.valueOf(result.get("public_id"));
            return new CloudinaryResponseDto(profileUrl, profileId);

        } catch (IOException e){
            throw new CloudinaryException("Upload failed");
        }
    }

    public void delete(String publicId){
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId,
                    ObjectUtils.asMap("invalidate", true));
        } catch (IOException e) {
            throw new CloudinaryException("Delete failed");
        }
    }
}
