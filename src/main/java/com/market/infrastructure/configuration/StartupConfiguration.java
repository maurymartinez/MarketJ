package com.market.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class StartupConfiguration {

    @Value("${server.port}")
    private String apiPort;

    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent() {
        popUpDocumentation();
    }

    private void popUpDocumentation() {
        try {
            var url = String.format("http://localhost:%s", apiPort);
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().browse(new URI(url));
            else if (isWindows())
                Runtime.getRuntime().exec("rundll32 url.dll,fileprotocolhandler " + url);
            else
                Runtime.getRuntime().exec("xdg-open " + url);
        } catch (IOException | URISyntaxException ignored) {
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
