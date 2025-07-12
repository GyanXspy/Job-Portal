package in.cd.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class SettingsController {
    
    @GetMapping("/settings")
    public String settingsPage(Model model) {
        // Add any settings data to the model
        model.addAttribute("pageTitle", "System Settings");
        return "admin/settings";
    }
    
    @PostMapping("/settings/update")
    public String updateSettings(@RequestParam(required = false) String emailNotifications,
                               @RequestParam(required = false) String autoApprove,
                               @RequestParam(required = false) String maintenanceMode,
                               RedirectAttributes redirectAttributes) {
        try {
            // Here you would typically save settings to database or configuration
            log.info("Settings updated - Email Notifications: {}, Auto Approve: {}, Maintenance Mode: {}", 
                    emailNotifications, autoApprove, maintenanceMode);
            
            redirectAttributes.addFlashAttribute("success", "Settings updated successfully!");
            return "redirect:/admin/settings";
        } catch (Exception e) {
            log.error("Error updating settings", e);
            redirectAttributes.addFlashAttribute("error", "Error updating settings: " + e.getMessage());
            return "redirect:/admin/settings";
        }
    }
} 