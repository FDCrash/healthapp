package com.app.health.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${upload.path}")
    private String uploadPath;

    private final static String IMAGE_FORMAT = "jpg";

    public String uploadFileAndGetPath(MultipartFile file) {
        String fileName = null;
        if(file != null && !file.getOriginalFilename().isEmpty()) {
            File dir = new File(uploadPath);

            if(!dir.exists()){
                dir.mkdir();
            }
            String uniqueFileName = UUID.randomUUID().toString();
            fileName = uniqueFileName + "." + IMAGE_FORMAT;

            try {
                var is = file.getInputStream();
                Files.copy(is, Paths.get(uploadPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
                convertImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public void convertImage(String fileName) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(uploadPath + "/" + fileName));

            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

            ImageIO.write(newBufferedImage, "jpg", new File(uploadPath + "/" + fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getStreamOfImage(String name)  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            InputStream is = Files.newInputStream(Paths.get(uploadPath + "/" + name));
            BufferedImage image = ImageIO.read(is);
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

}
