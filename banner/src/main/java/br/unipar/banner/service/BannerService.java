package br.unipar.banner.service;

import br.unipar.banner.dto.BannerDTO;
import br.unipar.banner.images.ImageStorageProperties;
import br.unipar.banner.model.Banner;
import br.unipar.banner.repositories.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BannerService {


//    [ ] dever ser possivel pegar 3 banner por sort;
//    GET : /banners/sort BannerDTO[]
//[ ] deve ser possivel registrar um servico de banner;
////    POST : /banners/idLoja; json:Banner
//[ ] deve ser possivel gerenciar os banners cadastrados;
//    GET: /banners/listByLojaId={idloja}
//[ ] deve ser possivel adicionar um click ao banner;
//    Quando cadastrar o banner deve ter um campo para ele adicionar um link externo;
//[ ] deve ser possivel desligar um banner;
//    DELETE : /banners/id={id}
//[ ] deve ser possivel pagar o servico do banner;
//[ ] Deve ser possivel carregar a imagem
//    GET: /banners/image?imageUrl=


    private final BannerRepository bannerRepository;
    private final Path imageStorageLocation;

    @Autowired
    public BannerService(BannerRepository bannerRepository, ImageStorageProperties imageStorageProperties) {
        this.bannerRepository = bannerRepository;
        this.imageStorageLocation = Paths.get(imageStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    public Banner insert(Banner banner) {
        banner.setCreatedAt(new Date());
        return bannerRepository.save(banner);
    }

    public String storeImage(MultipartFile file, String imageName) throws IOException {
        Path targetLocation = this.imageStorageLocation.resolve(imageName);
        Files.createDirectories(targetLocation.getParent()); // Ensure the directory exists
        file.transferTo(targetLocation);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/banner/images/download/")
                .path(imageName)
                .toUriString();
    }

}
