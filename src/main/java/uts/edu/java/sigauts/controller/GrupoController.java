package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.model.Grupo;
import uts.edu.java.sigauts.service.GrupoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/grupos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("grupos", grupoService.listarTodos());
        return "grupos/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("grupo",    new Grupo());
        model.addAttribute("materias", grupoService.listarMaterias());
        model.addAttribute("docentes", grupoService.listarDocentes());
        model.addAttribute("modoEdicion", false);
        return "grupos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Grupo grupo,
                          @RequestParam Integer idMateria,
                          @RequestParam Integer idDocente,
                          RedirectAttributes ra) {
        try {
            grupoService.guardar(grupo, idMateria, idDocente);
            ra.addFlashAttribute("exito", "Grupo guardado correctamente.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/grupos";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Integer id, Model model) {
        Grupo grupo = grupoService.buscarPorId(id);
        model.addAttribute("grupo",    grupo);
        model.addAttribute("materias", grupoService.listarMaterias());
        model.addAttribute("docentes", grupoService.listarDocentes());
        model.addAttribute("modoEdicion", true);
        return "grupos/form";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            grupoService.eliminar(id);
            ra.addFlashAttribute("exito", "Grupo eliminado correctamente.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "No se puede eliminar: el grupo tiene matrículas asociadas.");
        }
        return "redirect:/admin/grupos";
    }

    @GetMapping("/{id}/alumnos")
    public String verAlumnos(@PathVariable Integer id, Model model) {
        model.addAttribute("grupo",       grupoService.buscarPorId(id));
        model.addAttribute("matriculas",  grupoService.listarMatriculas(id));
        model.addAttribute("estudiantes", grupoService.listarEstudiantes());
        return "grupos/detalle";
    }

    @PostMapping("/{id}/matricular")
    public String matricular(@PathVariable Integer id,
                             @RequestParam Integer idEstudiante,
                             RedirectAttributes ra) {
        try {
            grupoService.matricular(id, idEstudiante);
            ra.addFlashAttribute("exito", "Estudiante matriculado correctamente.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/grupos/" + id + "/alumnos";
    }

    @PostMapping("/matricula/cancelar/{idMatricula}")
    public String cancelarMatricula(@PathVariable Integer idMatricula,
                                    @RequestParam Integer idGrupo,
                                    RedirectAttributes ra) {
        try {
            grupoService.cancelarMatricula(idMatricula);
            ra.addFlashAttribute("exito", "Matrícula cancelada.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/grupos/" + idGrupo + "/alumnos";
    }
}