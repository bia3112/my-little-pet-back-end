package br.unipar.banner.dto;

import java.util.UUID;

public class BannerDTO {

    private UUID id;
    private String imageUrl;
    private String externLink;

    public BannerDTO(UUID id, String imageUrl, String externLink) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.externLink = externLink;
    }

    public String getExternLink() {
        return externLink;
    }

    public void setExternLink(String externLink) {
        this.externLink = externLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}


