package org.maia.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class UUIDGeneration {
    public static String generationUUID() {
        UUID uuid = UUID.randomUUID();
        log.info("uuid Gerado com Sucesso -> " + uuid);
        return uuid.toString();
    }
}
