package br.unipar.banner.controllers;

import br.unipar.banner.images.ImageStorageProperties;
import br.unipar.banner.model.Banner;
import br.unipar.banner.service.BannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import br.unipar.banner.exceptions.ImageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    //lugar para armazenar o caminho do arquivo
    private final Path imageStorageLocation;

    //inicializando o local de armazenamento
    public BannerController(ImageStorageProperties imageStorageProperties) {
        //local é relativo, então precisa transformar em absoluto
        this.imageStorageLocation = Paths.get(imageStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    @PostMapping("/store/{storeId}")
    public ResponseEntity<Banner> insert(@RequestParam("banner") String bannerData,
                                         @RequestParam("file") MultipartFile file,
                                         @PathVariable String storeId) {
        try {
            // Parse the banner data from JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Banner banner = objectMapper.readValue(bannerData, Banner.class);
            banner.setStoreId(storeId);

            // Handle file upload and set imageUrl
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = imageStorageLocation.resolve(fileName);
            try {
                file.transferTo(targetLocation);
            } catch (IOException e) {
                throw new ImageNotFoundException("Erro ao baixar o arquivo: " + file.getOriginalFilename(), e);
            }

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/banners/download/")
                    .path(fileName)
                    .toUriString();
            banner.setImageUrl(fileDownloadUri);

            Banner savedBanner = bannerService.insert(banner, file);

            return ResponseEntity.ok(savedBanner);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Banner>> sortBanner() {
        List<Banner> banners = bannerService.sortBanner();
        return ResponseEntity.ok(banners);
    }


//    @GetMapping("/getAllByLojaId/{storeId}")
//    public ResponseEntity<List<Banner>> getByLojaId(@PathVariable String storeId) {
//
//    }


}
