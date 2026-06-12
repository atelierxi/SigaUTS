package uts.edu.java.sigauts.repository;

import uts.edu.java.sigauts.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    @Query("SELECT g FROM Grupo g JOIN FETCH g.materia JOIN FETCH g.docente d JOIN FETCH d.usuario")
    List<Grupo> findAllConDetalles();
}