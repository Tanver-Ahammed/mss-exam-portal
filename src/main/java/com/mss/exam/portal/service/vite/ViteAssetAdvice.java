package com.mss.exam.portal.service.vite;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ViteAssetAdvice {

    private final ViteManifestService viteManifest;

    public ViteAssetAdvice(ViteManifestService viteManifest) {
        this.viteManifest = viteManifest;
    }

    @ModelAttribute("viteJs")
    public String viteJs() {
        return viteManifest.asset("js/app.js");
    }

    @ModelAttribute("viteCss")
    public String viteCss() {
        return viteManifest.asset("js/app.css");
    }
}