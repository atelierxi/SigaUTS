package uts.edu.java.sigauts.repository;

import uts.edu.java.sigauts.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    @Query("SELECT m FROM Matricula m JOIN FETCH m.estudiante e JOIN FETCH e.usuario WHERE m.grupo.idGrupo = :idGrupo")
    List<Matricula> findByGrupoConEstudiante(@Param("idGrupo") Integer idGrupo);

    boolean existsByEstudiante_IdEstudianteAndGrupo_IdGrupo(Integer idEstudiante, Integer idGrupo);
}