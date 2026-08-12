package com.officine.losto.config;

import org.springframework.boot.context.properties.*;

import java.nio.file.*;

@ConfigurationProperties(prefix = "officine.product-photos")
public class ProductPhotosProperties {

    private Path directory = Path.of(System.getProperty("user.home"), ".officine", "product-photos");

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path directory) {
        this.directory = directory;
    }
}
