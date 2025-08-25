package com.dat.plantbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ImageService {

    public float[][][][] preprocessImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        // Resize về 256x256
        BufferedImage resized = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(image, 0, 0, 256, 256, null);
        g2d.dispose();

        // Chuyển thành tensor [1, 3, 256, 256]
        float[][][][] tensor = new float[1][3][256][256];

        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 256; x++) {
                int rgb = resized.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Normalize [0,1]
                tensor[0][0][y][x] = r / 255.0f;  // Red
                tensor[0][1][y][x] = g / 255.0f;  // Green
                tensor[0][2][y][x] = b / 255.0f;  // Blue
            }
        }

        return tensor;
    }
}
