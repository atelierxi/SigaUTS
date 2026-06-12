package uts.edu.java.sigauts.service;

import uts.edu.java.sigauts.model.Materia;
import uts.edu.java.sigauts.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepo;

    public MateriaService(MateriaRepository materiaRepo) {
        this.materiaRepo = materiaRepo;
    }

    public List<Materia> listarTodas() { return materiaRepo.findAll(); }

    public Materia buscarPorId(Integer id) {
        return materiaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada: " + id));
    }

    @Transactional
    public void guardar(Materia materia) {
        materiaRepo.save(materia);
    }

    @Transactional
    public void eliminar(Integer id) {
        materiaRepo.deleteById(id);
    }
}