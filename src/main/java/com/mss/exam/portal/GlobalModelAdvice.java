package com.mss.exam.portal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injects sidebar data and logged-in user into every controller's model.
 * <p>
 * Expects the login flow to store the username in the session under the
 * key "loggedInUser" — e.g. in your LoginController after authentication:
 * <p>
 * session.setAttribute("loggedInUser", username);
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model,
                                    HttpServletRequest request,
                                    HttpSession session) {

        // ── Sidebar menu ──────────────────────────────────────────
        model.addAttribute("sideMenuGrouped", Permission.sideMenuGrouped());

        // ── Active URL for link highlighting ─────────────────────
        model.addAttribute("activeUrl", request.getRequestURI());

        // ── Logged-in username from session ───────────────────────
        Object user = session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user != null ? user.toString() : null);
    }
}