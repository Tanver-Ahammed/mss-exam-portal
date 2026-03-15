package com.mss.exam.portal.service.vite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ViteManifestService {

    private Map<String, String> manifest = Collections.emptyMap();

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("static/assets/.vite/manifest.json");

            System.out.println("[Vite] Manifest exists: " + resource.exists());
            System.out.println("[Vite] Manifest path: " + resource.getURL());

            // ✅ Skip gracefully if manifest not found (dev mode / frontend not built yet)
            if (!resource.exists()) {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Map<String, Object>> raw = mapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<Map<String, Map<String, Object>>>() {
                    }
            );

            this.manifest = raw.entrySet().stream()
                    .filter(e -> e.getValue().get("file") != null)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().get("file").toString()
                    ));

        } catch (IOException e) {
            // Log warning but do NOT crash the app
            System.err.println("[ViteManifestService] Warning: Could not load Vite manifest — " + e.getMessage());
        }
    }

    /**
     * Resolves a Vite entry to its hashed asset path.
     * Returns the entry itself as fallback (useful in dev mode).
     *
     * @param entry e.g. "js/app.js"
     * @return e.g. "/assets/app.abc123.js"
     */
    public String asset(String entry) {
        return "/assets/" + manifest.getOrDefault(entry, entry);
    }
}