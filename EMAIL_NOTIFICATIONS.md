# Email Notifications for Application Status Changes

## Overview

The Candlesty job application system now includes automatic email notifications when admin users change application statuses. This feature provides professional communication to applicants about their application progress.

## Email Templates

### 1. Acceptance Email (Status: ACCEPTED)
- **Subject**: "Congratulations! Your Application Has Been Accepted - Candlesty"
- **Content**: 
  - Congratulations message
  - Detailed interview process information
  - Aptitude test details (60 minutes, logical reasoning, quantitative analysis)
  - Technical round information (45-60 minutes, coding challenges)
  - Final interview details
  - Contact information

### 2. Rejection Email (Status: REJECTED)
- **Subject**: "Application Status Update - Candlesty"
- **Content**:
  - Professional rejection message
  - Encouragement for future opportunities
  - Information about following the company
  - Application details for reference

### 3. Pending Email (Status: PENDING)
- **Subject**: "Application Status Update - Under Review - Candlesty"
- **Content**:
  - Status update notification
  - Timeline expectations (5-7 business days)
  - Next steps information

### 4. Reviewed Email (Status: REVIEWED)
- **Subject**: "Application Reviewed - Next Steps - Candlesty"
- **Content**:
  - Confirmation of review completion
  - Information about technical team evaluation
  - Timeline for next steps (2-3 business days)

## How It Works

1. **Admin Action**: When an admin changes an application status in the admin panel
2. **Status Check**: The system compares the old status with the new status
3. **Email Trigger**: If the status has changed, the appropriate email is sent
4. **Email Service**: The `EmailServiceImpl` handles the email sending logic
5. **Logging**: All email activities are logged for monitoring

## Configuration

### Email Settings
The email functionality uses the following configuration in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=candlesty1@gmail.com
spring.mail.password=wfhvflopcavewniu
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Gmail Setup
To use Gmail SMTP:
1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password for "Mail"
3. Update the configuration with your email and app password

## Admin Interface

### Status Update Process
1. Navigate to `/admin/applications`
2. Click the edit button (pencil icon) for any application
3. Select the new status from the dropdown:
   - **PENDING**: Application under review
   - **REVIEWED**: Application reviewed, awaiting technical evaluation
   - **ACCEPTED**: Application accepted, interview process begins
   - **REJECTED**: Application not selected
4. Click "Update Status"
5. Email is automatically sent to the applicant

### Available Actions
- **View**: See application details
- **Download**: Download applicant's resume
- **Update**: Change application status (triggers email)
- **Delete**: Remove application from system

## Error Handling

- **Email Configuration**: If email is not configured, the system logs the attempt but doesn't fail
- **Network Issues**: Email sending errors are logged but don't prevent status updates
- **Invalid Status**: Unknown statuses are logged with a warning

## Testing

### Manual Testing
1. Submit a test application with a valid email
2. Login to admin panel
3. Change the application status
4. Check the applicant's email for the notification

### Email Verification
- Check application logs for email sending confirmations
- Verify email content matches the templates
- Test all four status types (PENDING, REVIEWED, ACCEPTED, REJECTED)

## Security Considerations

- Email addresses are validated before sending
- Sensitive information is not included in emails
- Email content is sanitized to prevent injection attacks
- Logs don't contain sensitive email content

## Future Enhancements

- HTML email templates for better formatting
- Email tracking and delivery confirmation
- Customizable email templates via admin interface
- Bulk status updates with email notifications
- Email scheduling for delayed notifications 