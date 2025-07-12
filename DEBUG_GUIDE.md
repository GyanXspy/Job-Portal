# Debug Guide for Email Functionality

## Issue Description
The status update is showing an alert message but not changing the status field and not sending emails.

## Debugging Steps

### Step 1: Check Application Logs
1. Start the application: `./mvnw spring-boot:run`
2. Open browser console (F12) and go to Network tab
3. Try to update a status
4. Check the application logs for any errors

### Step 2: Test Email Service Directly
1. Go to: `http://localhost:8080/admin/test`
2. Enter an application ID and select a status
3. Click "Test Email"
4. Check if the email is sent and logs show success

### Step 3: Check Browser Console
1. Open browser developer tools (F12)
2. Go to Console tab
3. Try to update a status
4. Look for any JavaScript errors or network errors

### Step 4: Verify CSRF Configuration
The security configuration has been updated to ignore CSRF for admin endpoints:
- `/admin/application/*/status`
- `/admin/application/*`
- `/admin/test-email/*`

### Step 5: Check Network Requests
1. Open browser developer tools (F12)
2. Go to Network tab
3. Try to update a status
4. Look for the POST request to `/admin/application/{id}/status`
5. Check the request payload and response

## Common Issues and Solutions

### Issue 1: CSRF Token Missing
**Symptoms**: 403 Forbidden error
**Solution**: CSRF is now disabled for admin endpoints

### Issue 2: Application Not Found
**Symptoms**: "Application not found" error
**Solution**: Check if the application ID exists in the database

### Issue 3: Email Configuration
**Symptoms**: Email not sent but status updated
**Solution**: Check email configuration in `application.properties`

### Issue 4: JavaScript Errors
**Symptoms**: Console shows JavaScript errors
**Solution**: Check browser console for specific error messages

## Testing Commands

### Check Application Status
```bash
curl -X POST http://localhost:8080/admin/test-email/1 \
  -F "status=ACCEPTED" \
  -H "Content-Type: multipart/form-data"
```

### Check Application Logs
Look for these log messages:
- "Updating application status - ID: X, Status: Y"
- "Status updated successfully for application: email@example.com"
- "Acceptance email sent successfully to: email@example.com"

## Expected Behavior

### When Status is Updated:
1. ✅ Alert shows "Status updated successfully"
2. ✅ Page reloads automatically
3. ✅ Status field shows new status
4. ✅ Email is sent to applicant
5. ✅ Logs show success messages

### When Email is Sent:
1. ✅ Log shows "Acceptance email sent successfully to: email@example.com"
2. ✅ Email arrives in applicant's inbox
3. ✅ Email contains correct content based on status

## Debug Information

### Application Properties
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=candlesty1@gmail.com
spring.mail.password=wfhvflopcavewniu
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Test URLs
- Admin Applications: `http://localhost:8080/admin/applications`
- Test Page: `http://localhost:8080/admin/test`
- Login: `http://localhost:8080/login` (admin/admin123)

### Log Messages to Look For
- ✅ "Updating application status - ID: X, Status: Y"
- ✅ "Status updated successfully for application: email@example.com"
- ✅ "Acceptance email sent successfully to: email@example.com"
- ❌ "Error updating application status"
- ❌ "Error sending acceptance email"

## Next Steps

1. **Test the test page** first to isolate the issue
2. **Check application logs** for specific error messages
3. **Verify email configuration** is correct
4. **Test with a real email address** to ensure delivery
5. **Check browser console** for JavaScript errors

If the test page works but the main applications page doesn't, the issue is in the JavaScript or CSRF configuration.
If the test page doesn't work, the issue is in the backend email service or configuration. 