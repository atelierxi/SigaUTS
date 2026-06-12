package uts.edu.java.sigauts.service;

import uts.edu.java.sigauts.model.Estudiante;
import uts.edu.java.sigauts.model.Usuario;
import uts.edu.java.sigauts.repository.EstudianteRepository;
import uts.edu.java.sigauts.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepo;
    private final UsuarioRepository    usuarioRepo;
    private final PasswordEncoder      passwordEncoder;

    public EstudianteService(EstudianteRepository estudianteRepo,
                             UsuarioRepository usuarioRepo,
                             PasswordEncoder passwordEncoder) {
        this.estudianteRepo  = estudianteRepo;
        this.usuarioRepo     = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Estudiante> listarTodos() {
        return estudianteRepo.findAll();
    }

    public Estudiante buscarPorId(Integer id) {
        return estudianteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + id));
    }

    @Transactional
    public void guardar(Integer idEstudiante, String nombre, String correo,
                        String contrasenaPlana, String codigo,
                        Integer semestre, Integer idPrograma) {

        if (idEstudiante == null) {
            // ── CREAR ──────────────────────────────────────────
            if (usuarioRepo.existsByCorreo(correo)) {
                throw new RuntimeException("Ya existe un usuario con ese correo.");
            }
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
            usuario.setRol(Usuario.RolUsuario.ESTUDIANTE);
            usuario.setActivo(true);
            usuarioRepo.save(usuario);

            Estudiante estudiante = new Estudiante();
            estudiante.setUsuario(usuario);
            estudiante.setCodigo(codigo);
            estudiante.setSemestre(semestre);
            estudiante.setIdPrograma(idPrograma != null ? idPrograma : 0);
            estudianteRepo.save(estudiante);

        } else {
            // ── EDITAR ─────────────────────────────────────────
            Estudiante estudiante = buscarPorId(idEstudiante);
            Usuario usuario = estudiante.getUsuario();

            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            if (contrasenaPlana != null && !contrasenaPlana.isBlank()) {
                usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
            }
            usuarioRepo.save(usuario);

            estudiante.setCodigo(codigo);
            estudiante.setSemestre(semestre);
            estudiante.setIdPrograma(idPrograma != null ? idPrograma : 0);
            estudianteRepo.save(estudiante);
        }
    }

    @Transactional
    public void toggleActivo(Integer id) {
        Estudiante est = buscarPorId(id);
        Usuario u = est.getUsuario();
        u.setActivo(!u.isActivo());
        usuarioRepo.save(u);
    }
}