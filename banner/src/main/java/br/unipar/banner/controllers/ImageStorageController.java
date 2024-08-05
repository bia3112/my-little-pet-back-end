package br.unipar.banner.controllers;

import br.unipar.banner.images.ImageStorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/banners/images")
public class ImageStorageController {

    private final Path imageStorageLocation;

    public ImageStorageController(ImageStorageProperties imageStorageProperties) {
        this.imageStorageLocation = Paths.get(imageStorageProperties.getUploadDir()).
                toAbsolutePath().normalize();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            Path targetLocation = this.imageStorageLocation.resolve(imageName);
            file.transferTo(targetLocation);

            String fileDownLoadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                    path("banner/images/download/").
                    path(imageName).
                    toUriString();

            return ResponseEntity.ok("Updload completed! Download link: " + fileDownLoadUri);

        }catch (IOException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/download/{imageName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String imageName,
                                                 HttpServletRequest request) throws IOException {
        Path filePath = imageStorageLocation.resolve(imageName).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());

            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //lista de todos os arquivos
    @GetMapping("/list")
    public ResponseEntity<List<String>> listImages() throws IOException {
        List<String> imageNames = Files.list(imageStorageLocation)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());

        return ResponseEntity.ok(imageNames);
    }

}
