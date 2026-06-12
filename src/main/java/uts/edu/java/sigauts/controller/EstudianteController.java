package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.service.EstudianteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uts.edu.java.sigauts.model.Estudiante;

@Controller
@RequestMapping("/admin/estudiantes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estudiantes", estudianteService.listarTodos());
        return "estudiantes/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("modoEdicion", false);
        return "estudiantes/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Integer id, Model model) {
        model.addAttribute("estudiante", estudianteService.buscarPorId(id));
        model.addAttribute("modoEdicion", true);
        return "estudiantes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idEstudiante,
                          @RequestParam String nombre,
                          @RequestParam String correo,
                          @RequestParam(required = false) String contrasenaPlana,
                          @RequestParam(required = false) String codigo,
                          @RequestParam(required = false) Integer semestre,
                          @RequestParam(required = false) Integer idPrograma,
                          RedirectAttributes ra) {
        try {
            estudianteService.guardar(idEstudiante, nombre, correo,
                    contrasenaPlana, codigo, semestre, idPrograma);
            ra.addFlashAttribute("exito", "Estudiante guardado correctamente.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/estudiantes";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Integer id, RedirectAttributes ra) {
        estudianteService.toggleActivo(id);
        ra.addFlashAttribute("exito", "Estado actualizado.");
        return "redirect:/admin/estudiantes";
    }
}