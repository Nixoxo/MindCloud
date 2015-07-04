package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.persistence.domain.User;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;

import static de.pm.mindcloud.web.controller.SessionController.USER;

/**
 * Created by samuel on 03.07.15.
 */
@Component
public class ProfileImageController {

    public byte[] getImage(User user) throws IOException {
        File imageFolder = getImageFolder();
        File profileImage = new File(imageFolder, user.getId());
        if (profileImage.exists()) {
            return IOUtils.toByteArray(new FileInputStream(profileImage));
        }
        try {
            File defaultImage = new ClassPathResource("resources/img/user.png").getFile();
            return IOUtils.toByteArray(new FileInputStream(defaultImage));
        } catch (Exception e) {
            return IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("resources/img/user.png"));
        }
    }

    private File getImageFolder() {
        File imageFolder = new File("profileimages");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        return imageFolder;
    }

    public void setImage(User user, byte[] image) throws IOException {
        File imageFolder = getImageFolder();
        File profileImage = new File(imageFolder, user.getId());
        if (image == null) {
            profileImage.delete();
        } else {
            IOUtils.write(image, new FileOutputStream(profileImage));
        }
    }
}