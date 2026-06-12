package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.model.Usuario;
import uts.edu.java.sigauts.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder   passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo     = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Usuario.RolUsuario.values());
        model.addAttribute("modoEdicion", false);
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
                          @RequestParam String contrasenaPlana,
                          RedirectAttributes ra) {
        try {
            if (usuario.getIdUsuario() == null) {
                if (usuarioRepo.existsByCorreo(usuario.getCorreo())) {
                    ra.addFlashAttribute("error", "Ya existe un usuario con ese correo.");
                    return "redirect:/admin/usuarios/nuevo";
                }
                usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
            } else {
                if (contrasenaPlana != null && !contrasenaPlana.isBlank()) {
                    usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
                } else {
                    String actual = usuarioRepo.findById(usuario.getIdUsuario())
                            .map(Usuario::getContrasena).orElse("");
                    usuario.setContrasena(actual);
                }
            }
            usuarioRepo.save(usuario);
            ra.addFlashAttribute("exito", "Usuario guardado correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado")));
        model.addAttribute("roles", Usuario.RolUsuario.values());
        model.addAttribute("modoEdicion", true);
        return "usuarios/form";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Integer id, RedirectAttributes ra) {
        usuarioRepo.findById(id).ifPresent(u -> {
            u.setActivo(!u.isActivo());
            usuarioRepo.save(u);
        });
        ra.addFlashAttribute("exito", "Estado actualizado.");
        return "redirect:/admin/usuarios";
    }
}