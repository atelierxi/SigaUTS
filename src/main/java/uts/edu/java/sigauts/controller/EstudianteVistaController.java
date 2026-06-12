package uts.edu.java.sigauts.controller;

import uts.edu.java.sigauts.repository.DocenteRepository;
import uts.edu.java.sigauts.repository.GrupoRepository;
import uts.edu.java.sigauts.repository.MateriaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estudiante")
@PreAuthorize("hasRole('ESTUDIANTE')")
public class EstudianteVistaController {

    private final MateriaRepository materiaRepo;
    private final GrupoRepository   grupoRepo;
    private final DocenteRepository docenteRepo;

    public EstudianteVistaController(MateriaRepository materiaRepo,
                                      GrupoRepository grupoRepo,
                                      DocenteRepository docenteRepo) {
        this.materiaRepo = materiaRepo;
        this.grupoRepo   = grupoRepo;
        this.docenteRepo = docenteRepo;
    }

    @GetMapping("/materias")
    public String verMaterias(Model model) {
        model.addAttribute("materias", materiaRepo.findAll());
        return "estudiante/materias";
    }

    @GetMapping("/grupos")
    public String verGrupos(Model model) {
        model.addAttribute("grupos", grupoRepo.findAllConDetalles());
        return "estudiante/grupos";
    }

    @GetMapping("/docentes")
    public String verDocentes(Model model) {
        model.addAttribute("docentes", docenteRepo.findAll());
        return "estudiante/docentes";
    }
}