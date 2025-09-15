package net.martins.email.messaging;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import net.martins.email.model.EmailRequest;
import net.martins.email.service.EmailService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EmailMessageProcessor {
    
    private static final Logger LOG = Logger.getLogger(EmailMessageProcessor.class);
    
    private final EmailService emailService;
    
    public EmailMessageProcessor(EmailService emailService) {
        this.emailService = emailService;
    }
    
    @Incoming("emails")
    @Outgoing("emails-out")
    @Blocking
    public EmailRequest process(EmailRequest emailRequest) {
        LOG.infof("Processing email request to %s with subject: %s", 
                 emailRequest.getTo(), emailRequest.getSubject());
        return emailRequest;
    }
    
    @Incoming("emails-out")
    @Blocking
    public void send(EmailRequest emailRequest) {
        LOG.infof("Sending email to %s with subject: %s", 
                 emailRequest.getTo(), emailRequest.getSubject());
        emailService.sendEmail(emailRequest);
    }
}