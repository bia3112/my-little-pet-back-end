package br.unipar.banner.controllers;

import br.unipar.banner.model.Banner;
import br.unipar.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping("/{storeId}")
    public Banner insert(@RequestBody Banner banner, @PathVariable String storeId) {
        banner.setStoreId(storeId);
        return bannerService.insert(banner);
    }

}
