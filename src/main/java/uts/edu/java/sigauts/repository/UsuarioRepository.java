package uts.edu.java.sigauts.repository;

import uts.edu.java.sigauts.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // SELECT * FROM usuario WHERE correo = ?
    Optional<Usuario> findByCorreo(String correo);

    // SELECT COUNT(*) > 0 FROM usuario WHERE correo = ?
    boolean existsByCorreo(String correo);
}