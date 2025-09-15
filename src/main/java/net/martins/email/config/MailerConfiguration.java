package net.martins.email.config;

import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MailerConfiguration {
    
    @ConfigProperty(name = "app.mailer.provider.default", defaultValue = "primary")
    String defaultProvider;
    
    // Primary mailer (configured via application.properties)
    @Produces
    @Singleton
    @Named("primary")
    Mailer primaryMailer;
    
    // Secondary mailer for fallback
    @Produces
    @Singleton
    @Named("secondary")
    Mailer secondaryMailer;
    
    public String getDefaultProvider() {
        return defaultProvider;
    }
}