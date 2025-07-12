# Testing Email Functionality

## Prerequisites

1. **Email Configuration**: Ensure Gmail SMTP is properly configured in `application.properties`
2. **Application Running**: Start the Spring Boot application
3. **Test Email**: Use a real email address for testing

## Test Steps

### Step 1: Submit a Test Application

1. Navigate to `http://localhost:8080/apply`
2. Fill out the application form with:
   - **Name**: Test User
   - **Email**: [your-test-email@gmail.com]
   - **Education**: Bachelor's Degree
   - **Experience Level**: Mid-level
   - **Position**: AI Trading Algorithm Developer
   - **Additional Notes**: Test application for email functionality
   - **Resume**: Upload any PDF file
3. Submit the application
4. Check your email for the initial confirmation email

### Step 2: Test Status Updates

1. Login to admin panel: `http://localhost:8080/admin/applications`
   - Username: `admin`
   - Password: `admin123`

2. **Test PENDING Status**:
   - Click the edit button (pencil icon) for your test application
   - Select "PENDING" from the dropdown
   - Click "Update Status"
   - Check your email for the pending status notification

3. **Test REVIEWED Status**:
   - Click the edit button again
   - Select "REVIEWED" from the dropdown
   - Click "Update Status"
   - Check your email for the reviewed status notification

4. **Test ACCEPTED Status**:
   - Click the edit button again
   - Select "ACCEPTED" from the dropdown
   - Click "Update Status"
   - Check your email for the acceptance email with aptitude test and technical round details

5. **Test REJECTED Status**:
   - Submit another test application with a different email
   - Go to admin panel and change status to "REJECTED"
   - Check your email for the rejection email with sorry message

### Step 3: Verify Email Content

#### Acceptance Email Should Include:
- Congratulations message
- Aptitude test details (60 minutes, logical reasoning, quantitative analysis)
- Technical round information (45-60 minutes, coding challenges)
- Final interview details
- Contact information

#### Rejection Email Should Include:
- Professional rejection message
- Encouragement for future opportunities
- Information about following the company
- Application details for reference

#### Pending Email Should Include:
- Status update notification
- Timeline expectations (5-7 business days)
- Next steps information

#### Reviewed Email Should Include:
- Confirmation of review completion
- Information about technical team evaluation
- Timeline for next steps (2-3 business days)

## Expected Results

### Email Delivery
- ✅ Emails should be delivered to the applicant's email address
- ✅ Email subjects should match the expected format
- ✅ Email content should include all required information

### Log Messages
Check the application logs for:
- ✅ "Acceptance email sent successfully to: [email]"
- ✅ "Rejection email sent successfully to: [email]"
- ✅ "Pending status email sent successfully to: [email]"
- ✅ "Reviewed status email sent successfully to: [email]"

### Admin Interface
- ✅ Status updates should work without errors
- ✅ Email sent status should be tracked in the admin panel
- ✅ No errors in the browser console

## Troubleshooting

### Email Not Sending
1. Check Gmail configuration in `application.properties`
2. Verify Gmail app password is correct
3. Check application logs for email errors
4. Ensure 2FA is enabled on Gmail account

### Status Not Updating
1. Check browser console for JavaScript errors
2. Verify admin credentials are correct
3. Check application logs for controller errors

### Email Content Issues
1. Verify email templates in `EmailServiceImpl.java`
2. Check application logs for template formatting errors
3. Ensure all required fields are present in the application

## Test Data

### Sample Application Data
```
Name: John Doe
Email: test@example.com
Education: Master's Degree
Experience Level: Senior
Position: AI Trading Algorithm Developer
Additional Notes: Experienced in Python and machine learning
```

### Expected Email Flow
1. **Initial**: Application received confirmation
2. **PENDING**: Under review notification
3. **REVIEWED**: Review completed notification
4. **ACCEPTED**: Congratulations with interview details
5. **REJECTED**: Professional rejection message

## Notes

- The system logs all email activities for monitoring
- Email sending errors don't prevent status updates
- All email content is professional and company-branded
- The system handles missing email configuration gracefully 