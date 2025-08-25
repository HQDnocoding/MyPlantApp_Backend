package com.dat.plantbackend.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class CommonUtils {
    public static float[] applySoftmax(float[] logits) {
        float[] probabilities = new float[logits.length];
        float sum = 0.0f;

        // Tính e^x cho mỗi phần tử
        for (int i = 0; i < logits.length; i++) {
            probabilities[i] = (float) Math.exp(logits[i]);
            sum += probabilities[i];
        }

        // Normalize để tổng = 1
        for (int i = 0; i < logits.length; i++) {
            probabilities[i] = probabilities[i] / sum;
        }

        return probabilities;
    }


    public static String uploadFileToCloud(Cloudinary cloudinary, MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload lên Cloudinary", e);
        }
    }
}
