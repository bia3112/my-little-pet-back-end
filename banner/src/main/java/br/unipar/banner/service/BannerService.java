package br.unipar.banner.service;

import br.unipar.banner.dto.BannerDTO;
import br.unipar.banner.model.Banner;
import br.unipar.banner.repositories.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

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

//    public  List<Banner> listSelectecBanners() {
//        List<Banner> allBanners = bannerRepository.findAll();
//        Random random = new Random();
//
//        return IntStream.range(0, 3)
//                .mapToObj(i -> allBanners.get(random.nextInt(allBanners.size())))
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    public Banner insert(Banner banner) {
        banner.setId(UUID.randomUUID());
        banner.setCreatedAt(new Date());
        banner.setIsActive(true);
        return bannerRepository.save(banner);
    }

//    private BannerDTO convertToDTO(Banner banner) {
//        return new BannerDTO(
//                banner.getId(),
//                banner.getImageUrl(),
//                banner.getExternLink()
//        );
//    }

}
