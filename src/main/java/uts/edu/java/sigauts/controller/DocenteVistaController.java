package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.repository.EstudianteRepository;
import uts.edu.java.sigauts.repository.GrupoRepository;
import uts.edu.java.sigauts.repository.MateriaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docente")
@PreAuthorize("hasRole('DOCENTE')")
public class DocenteVistaController {

    private final MateriaRepository   materiaRepo;
    private final GrupoRepository     grupoRepo;
    private final EstudianteRepository estudianteRepo;

    public DocenteVistaController(MateriaRepository materiaRepo,
                                   GrupoRepository grupoRepo,
                                   EstudianteRepository estudianteRepo) {
        this.materiaRepo    = materiaRepo;
        this.grupoRepo      = grupoRepo;
        this.estudianteRepo = estudianteRepo;
    }

    @GetMapping("/materias")
    public String verMaterias(Model model) {
        model.addAttribute("materias", materiaRepo.findAll());
        return "docente/materias";
    }

    @GetMapping("/grupos")
    public String verGrupos(Model model) {
        model.addAttribute("grupos", grupoRepo.findAllConDetalles());
        return "docente/grupos";
    }

    @GetMapping("/estudiantes")
    public String verEstudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        return "docente/estudiantes";
    }
}