package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.model.Docente;
import uts.edu.java.sigauts.service.DocenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/docentes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class DocenteController {

    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        return "docentes/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("docente", new Docente());
        model.addAttribute("modoEdicion", false);
        return "docentes/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Integer id, Model model) {
        model.addAttribute("docente", docenteService.buscarPorId(id));
        model.addAttribute("modoEdicion", true);
        return "docentes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idDocente,
                          @RequestParam String nombre,
                          @RequestParam String correo,
                          @RequestParam(required = false) String contrasenaPlana,
                          @RequestParam String cedula,
                          @RequestParam(required = false) String especialidad,
                          RedirectAttributes ra) {
        try {
            docenteService.guardar(idDocente, nombre, correo,
                    contrasenaPlana, cedula, especialidad);
            ra.addFlashAttribute("exito", "Docente guardado correctamente.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/docentes";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Integer id, RedirectAttributes ra) {
        docenteService.toggleActivo(id);
        ra.addFlashAttribute("exito", "Estado actualizado.");
        return "redirect:/admin/docentes";
    }
}