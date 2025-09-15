# Hostinger Email Configuration

This guide explains how to configure the email sender service to work with Hostinger email accounts.

## Hostinger SMTP Settings

Based on common Hostinger configurations, the service is pre-configured with the following settings:

### Standard Hostinger SMTP Configuration:
- **SMTP Server**: smtp.hostinger.com
- **Ports**:
  - 465 (SSL/TLS encryption - recommended)
  - 587 (STARTTLS encryption)
  - 25 (No encryption - not recommended)
- **Authentication**: Required (username and password)
- **Encryption**: SSL/TLS or STARTTLS

## Configuration Steps

1. **Update the .env file** with your actual Hostinger email account details:
   ```
   MAILER_FROM=your-email@yourdomain.com
   MAILER_HOST=smtp.hostinger.com
   MAILER_PORT=465
   MAILER_SSL=true
   MAILER_USERNAME=your-email@yourdomain.com
   MAILER_PASSWORD=your-email-password
   MAILER_AUTH=true
   ```

2. **Replace the placeholder values**:
   - `your-email@yourdomain.com` with your actual Hostinger email address
   - `your-email-password` with your actual email password or app-specific password

3. **Choose the appropriate port**:
   - For SSL/TLS encryption (recommended): Use port 465 with `MAILER_SSL=true`
   - For STARTTLS encryption: Use port 587 with `MAILER_SSL=false`
   - Avoid port 25 unless absolutely necessary as it's not encrypted

4. **Secondary Mailer (Optional)**:
   - You can configure a secondary email provider as a fallback
   - This can be another Hostinger account or a different provider like Gmail

## Usage with Docker

When running with Docker, you can pass the environment variables in several ways:

1. **Using the .env file**:
   ```bash
   docker run --env-file .env -p 8081:8081 email-sender-service
   ```

2. **Passing individual variables**:
   ```bash
   docker run -e MAILER_HOST=smtp.hostinger.com -e MAILER_PORT=465 -e MAILER_USERNAME=your-email@yourdomain.com -e MAILER_PASSWORD=your-password -p 8081:8081 email-sender-service
   ```

## Testing the Configuration

After updating the configuration:

1. Start the service:
   ```bash
   java -jar target/quarkus-app/quarkus-run.jar
   ```

2. Send a test email using the API:
   ```bash
   curl -X POST http://localhost:8081/email/send \
     -H "Content-Type: application/json" \
     -d '{
       "to": ["test@example.com"],
       "subject": "Test Email",
       "body": "This is a test email from Hostinger email service"
     }'
   ```

## Troubleshooting

If you encounter issues:

1. **Verify your credentials** - Ensure your username and password are correct
2. **Check port and encryption settings** - Try switching between port 465/587 and SSL/TLS/STARTTLS
3. **Enable less secure apps** - Some email providers require this setting
4. **Use app-specific passwords** - For providers like Gmail that require 2FA
5. **Check firewall settings** - Ensure outbound connections on SMTP ports are allowed

## Security Best Practices

1. **Use environment variables** instead of hardcoding credentials
2. **Use app-specific passwords** when available
3. **Enable SSL/TLS encryption** (port 465) when possible
4. **Regularly rotate passwords** for email accounts
5. **Monitor email sending logs** for unusual activity