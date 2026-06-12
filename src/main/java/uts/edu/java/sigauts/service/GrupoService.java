package uts.edu.java.sigauts.service;

import uts.edu.java.sigauts.model.*;
import uts.edu.java.sigauts.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository      grupoRepo;
    private final MatriculaRepository  matriculaRepo;
    private final MateriaRepository    materiaRepo;
    private final DocenteRepository    docenteRepo;
    private final EstudianteRepository estudianteRepo;

    public GrupoService(GrupoRepository grupoRepo,
                        MatriculaRepository matriculaRepo,
                        MateriaRepository materiaRepo,
                        DocenteRepository docenteRepo,
                        EstudianteRepository estudianteRepo) {
        this.grupoRepo      = grupoRepo;
        this.matriculaRepo  = matriculaRepo;
        this.materiaRepo    = materiaRepo;
        this.docenteRepo    = docenteRepo;
        this.estudianteRepo = estudianteRepo;
    }

    // ── Grupos ───────────────────────────────────────────────

    public List<Grupo> listarTodos() {
        return grupoRepo.findAllConDetalles();
    }

    public Grupo buscarPorId(Integer id) {
        return grupoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado: " + id));
    }

    public List<Materia> listarMaterias() {
        return materiaRepo.findAll();
    }

    public List<Docente> listarDocentes() {
        return docenteRepo.findAll();
    }

    @Transactional
    public void guardar(Grupo grupo, Integer idMateria, Integer idDocente) {
        Materia materia = materiaRepo.findById(idMateria)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada."));
        Docente docente = docenteRepo.findById(idDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado."));
        grupo.setMateria(materia);
        grupo.setDocente(docente);
        grupoRepo.save(grupo);
    }

    @Transactional
    public void eliminar(Integer id) {
        grupoRepo.deleteById(id);
    }

    // ── Matrículas ───────────────────────────────────────────

    public List<Matricula> listarMatriculas(Integer idGrupo) {
        return matriculaRepo.findByGrupoConEstudiante(idGrupo);
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepo.findAll();
    }

    @Transactional
    public void matricular(Integer idGrupo, Integer idEstudiante) {
        if (matriculaRepo.existsByEstudiante_IdEstudianteAndGrupo_IdGrupo(idEstudiante, idGrupo)) {
            throw new RuntimeException("El estudiante ya está matriculado en este grupo.");
        }
        Grupo grupo = buscarPorId(idGrupo);
        Estudiante estudiante = estudianteRepo.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));

        if (grupo.getCupoMaximo() != null) {
            long inscritos = matriculaRepo.findByGrupoConEstudiante(idGrupo)
                    .stream().filter(m -> m.getEstado() == Matricula.EstadoMatricula.Activa).count();
            if (inscritos >= grupo.getCupoMaximo()) {
                throw new RuntimeException("El grupo ya alcanzó el cupo máximo (" + grupo.getCupoMaximo() + ").");
            }
        }

        Matricula m = new Matricula();
        m.setGrupo(grupo);
        m.setEstudiante(estudiante);
        matriculaRepo.save(m);
    }

    @Transactional
    public void cancelarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepo.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada."));
        m.setEstado(Matricula.EstadoMatricula.Cancelada);
        matriculaRepo.save(m);
    }
}