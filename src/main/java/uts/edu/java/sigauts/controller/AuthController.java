package uts.edu.java.sigauts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(value = "error",  required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }

        if (error  != null) model.addAttribute("mensajeError",
                "Correo o contraseña incorrectos. Verifique sus datos.");
        if (logout != null) model.addAttribute("mensajeLogout",
                "Ha cerrado sesión exitosamente.");

        return "auth/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String rol = authentication.getAuthorities()
                .iterator().next().getAuthority();

        return switch (rol) {
            case "ROLE_ADMINISTRADOR" -> "redirect:/admin/dashboard";
            case "ROLE_DOCENTE"       -> "redirect:/docente/dashboard";
            case "ROLE_ESTUDIANTE"    -> "redirect:/estudiante/dashboard";
            default                   -> "redirect:/login";
        };
    }

    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(Model model, Authentication auth) {
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("rol", "Administrador");
        return "dashboard/admin";
    }

    @GetMapping("/docente/dashboard")
    public String dashboardDocente(Model model, Authentication auth) {
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("rol", "Docente");
        return "dashboard/docente";
    }

    @GetMapping("/estudiante/dashboard")
    public String dashboardEstudiante(Model model, Authentication auth) {
        model.addAttribute("nombreUsuario", auth.getName());
        model.addAttribute("rol", "Estudiante");
        return "dashboard/estudiante";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model, Authentication auth) {
        if (auth != null) model.addAttribute("nombreUsuario", auth.getName());
        return "auth/acceso-denegado";
    }
}