package net.martins.email.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.martins.email.model.EmailRequest;
import org.jboss.logging.Logger;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class EmailService {
    
    private static final Logger LOG = Logger.getLogger(EmailService.class);
    
    @Inject
    @Named("primary")
    Mailer primaryMailer;
    
    @Inject
    @Named("secondary")
    Mailer secondaryMailer;
    
    private Mailer currentMailer;
    
    public EmailService() {
        // Default to primary mailer
        this.currentMailer = primaryMailer;
    }
    
    public Uni<Void> sendEmailAsync(EmailRequest request) {
        return Uni.createFrom().item(() -> {
            sendEmail(request);
            return null;
        });
    }
    
    public void sendEmail(EmailRequest request) {
        try {
            Mail mail = Mail.withHtml(request.getTo(), request.getSubject(), request.getBody());
            
            if (request.getCc() != null && !request.getCc().isEmpty()) {
                mail.setCc(request.getCc());
            }
            
            if (request.getBcc() != null && !request.getBcc().isEmpty()) {
                mail.setBcc(request.getBcc());
            }
            
            if (!request.isHtml()) {
                mail.setText(request.getBody());
            }
            
            currentMailer.send(mail);
            LOG.infof("Email sent successfully to %s with subject: %s", request.getTo(), request.getSubject());
        } catch (Exception e) {
            LOG.errorf("Failed to send email to %s with subject: %s. Error: %s", 
                      request.getTo(), request.getSubject(), e.getMessage(), e);
            
            // Try fallback mailer
            try {
                Mailer fallbackMailer = (currentMailer == primaryMailer) ? secondaryMailer : primaryMailer;
                fallbackMailer.send(mail);
                LOG.infof("Email sent successfully using fallback mailer to %s with subject: %s", 
                         request.getTo(), request.getSubject());
                // Switch to fallback mailer for subsequent requests
                currentMailer = fallbackMailer;
            } catch (Exception fallbackException) {
                LOG.errorf("Failed to send email using fallback mailer. Error: %s", fallbackException.getMessage());
                throw new RuntimeException("Failed to send email using both primary and fallback mailers", fallbackException);
            }
        }
    }
    
    public void switchToPrimaryMailer() {
        this.currentMailer = primaryMailer;
    }
    
    public void switchToSecondaryMailer() {
        this.currentMailer = secondaryMailer;
    }
}