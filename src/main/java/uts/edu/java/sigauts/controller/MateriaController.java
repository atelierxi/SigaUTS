package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.model.Materia;
import uts.edu.java.sigauts.service.MateriaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/materias")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materias", materiaService.listarTodas());
        return "materias/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("materia", new Materia());
        model.addAttribute("modoEdicion", false);
        return "materias/form";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Integer id, Model model) {
        model.addAttribute("materia", materiaService.buscarPorId(id));
        model.addAttribute("modoEdicion", true);
        return "materias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idMateria,
                          @RequestParam String nombre,
                          @RequestParam String codigo,
                          @RequestParam Integer creditos,
                          @RequestParam(required = false, defaultValue = "0") Integer idPrograma,
                          RedirectAttributes ra) {
        try {
            Materia materia = new Materia();
            materia.setIdMateria(idMateria);
            materia.setNombre(nombre);
            materia.setCodigo(codigo);
            materia.setCreditos(creditos);
            materia.setIdPrograma(idPrograma);
            materiaService.guardar(materia);
            ra.addFlashAttribute("exito", "Materia guardada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al guardar: código duplicado u otro problema.");
        }
        return "redirect:/admin/materias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            materiaService.eliminar(id);
            ra.addFlashAttribute("exito", "Materia eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar: la materia tiene grupos asociados.");
        }
        return "redirect:/admin/materias";
    }
}