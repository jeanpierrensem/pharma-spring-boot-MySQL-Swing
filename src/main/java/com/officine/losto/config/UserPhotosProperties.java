package com.officine.losto.config;

import org.springframework.boot.context.properties.*;

import java.nio.file.*;

@ConfigurationProperties(prefix = "officine.user-photos")
public class UserPhotosProperties {

    /**
     * Directory where profile image files are stored (absolute or user.home relative).
     */
    private Path directory = Path.of(System.getProperty("user.home"), ".officine", "user-photos");

    public Path getDirectory() {
        return directory;
    }

    public void setDirectory(Path directory) {
        this.directory = directory;
    }
}
