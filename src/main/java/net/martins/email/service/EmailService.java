package net.martins.email.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.martins.email.config.MailerConfiguration;
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
    
    @Inject
    MailerConfiguration mailerConfiguration;
    
    public Uni<Void> sendEmailAsync(EmailRequest request) {
        return Uni.createFrom().item(() -> {
            sendEmail(request);
            return null;
        });
    }
    
    public void sendEmail(EmailRequest request) {
        Mailer mailerToUse;
        
        // Determine which mailer to use based on configuration
        String defaultProvider = mailerConfiguration.getDefaultProvider();
        if ("secondary".equals(defaultProvider)) {
            mailerToUse = secondaryMailer;
            LOG.info("Using secondary mailer as default provider");
        } else {
            mailerToUse = primaryMailer;
            LOG.info("Using primary mailer as default provider");
        }
        
        try {
            var getTo = String.join(",", request.to());
            var mail = Mail.withHtml(getTo, request.subject(), request.body());
            if (request.cc() != null && !request.cc().isEmpty()) mail.setCc(request.cc());
            if (request.bcc() != null && !request.bcc().isEmpty()) mail.setBcc(request.bcc());
            if (!request.html()) mail.setText(request.body());
            
            mailerToUse.send(mail);
            LOG.infof("Email sent successfully to %s with subject: %s", getTo, request.subject());
        } catch (Exception e) {
            LOG.errorf("Failed to send email to %s with subject: %s. Error: %s", 
                      request.to(), request.subject(), e.getMessage(), e);
        }
    }
}