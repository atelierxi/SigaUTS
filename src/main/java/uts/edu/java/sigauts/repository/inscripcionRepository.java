package uts.edu.java.sigauts.repository;

import uts.edu.java.sigauts.model.inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface inscripcionRepository extends JpaRepository<inscripcion, Integer> {
    List<inscripcion> findByGrupo_IdGrupo(Integer idGrupo);
    boolean existsByGrupo_IdGrupoAndUsuario_IdUsuario(Integer idGrupo, Integer idUsuario);
}