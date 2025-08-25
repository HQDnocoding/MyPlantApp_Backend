package com.dat.plantbackend.controllers;

import ai.onnxruntime.OrtException;
import com.dat.plantbackend.dto.PredictionResult;
import com.dat.plantbackend.services.ImageService;
import com.dat.plantbackend.services.ONNXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ONNXService onnxService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/predict")
    public ResponseEntity<?> predict(@RequestParam("image") MultipartFile image) {
        try {
            // Bước 1: Tiền xử lý ảnh đầu vào
            float[][][][] tensor = imageService.preprocessImage(image);

            // Bước 2: Dự đoán bằng ONNX
            PredictionResult result = onnxService.predict(tensor);

            // Bước 3: Trả kết quả
            return ResponseEntity.ok(result);

        } catch (IOException ioEx) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể đọc ảnh. Vui lòng kiểm tra định dạng file.");
        } catch (OrtException ortEx) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi chạy mô hình ONNX: " + ortEx.getMessage());
        } catch (   Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi không xác định: " + e.getMessage());
        }
    }

}
