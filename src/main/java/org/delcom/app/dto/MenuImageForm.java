package org.delcom.app.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public class MenuImageForm {

    private UUID id;
    private MultipartFile imageFile;

    // Constructor
    public MenuImageForm() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    // Helper methods
    public boolean isEmpty() {
        return imageFile == null || imageFile.isEmpty();
    }

    public boolean isValidImage() {
        if (isEmpty()) {
            return false;
        }
        
        String contentType = imageFile.getContentType();
        if (contentType == null) {
            return false;
        }
        
        return contentType.equals("image/jpeg") || 
               contentType.equals("image/png") || 
               contentType.equals("image/gif") ||
               contentType.equals("image/jpg");
    }

    public boolean isSizeValid(long maxSize) {
        if (isEmpty()) {
            return false;
        }
        return imageFile.getSize() <= maxSize;
    }

    public String getOriginalFilename() {
        if (isEmpty()) {
            return null;
        }
        return imageFile.getOriginalFilename();
    }

    public String getContentType() {
        if (isEmpty()) {
            return null;
        }
        return imageFile.getContentType();
    }

    public long getSize() {
        if (isEmpty()) {
            return 0;
        }
        return imageFile.getSize();
    }
}