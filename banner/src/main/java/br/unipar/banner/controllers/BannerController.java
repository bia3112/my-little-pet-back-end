package br.unipar.banner.controllers;

import br.unipar.banner.model.Banner;
import br.unipar.banner.service.BannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping("/{storeId}")
    public ResponseEntity<Banner> insert(@RequestParam("banner") String bannerData,
                                         @RequestParam("file") MultipartFile file,
                                         @PathVariable String storeId) {
        try {
            // Parse the banner data from JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Banner banner = objectMapper.readValue(bannerData, Banner.class);
            banner.setStoreId(storeId);

            // Handle file upload and set imageUrl
            String imageName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileDownloadUri = bannerService.storeImage(file, imageName);
            banner.setImageUrl(fileDownloadUri);

            // Save the banner
            Banner savedBanner = bannerService.insert(banner);

            return ResponseEntity.ok(savedBanner);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
