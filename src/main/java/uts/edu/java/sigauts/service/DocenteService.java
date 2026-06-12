package uts.edu.java.sigauts.service;

import uts.edu.java.sigauts.model.Docente;
import uts.edu.java.sigauts.model.Usuario;
import uts.edu.java.sigauts.repository.DocenteRepository;
import uts.edu.java.sigauts.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DocenteService {

    private final DocenteRepository docenteRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder   passwordEncoder;

    public DocenteService(DocenteRepository docenteRepo,
                          UsuarioRepository usuarioRepo,
                          PasswordEncoder passwordEncoder) {
        this.docenteRepo     = docenteRepo;
        this.usuarioRepo     = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Docente> listarTodos() { return docenteRepo.findAll(); }

    public Docente buscarPorId(Integer id) {
        return docenteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + id));
    }

    @Transactional
    public void guardar(Integer idDocente, String nombre, String correo,
                        String contrasenaPlana, String cedula, String especialidad) {

        if (idDocente == null) {
            // ── CREAR ──────────────────────────────────────────
            if (usuarioRepo.existsByCorreo(correo)) {
                throw new RuntimeException("Ya existe un usuario con ese correo.");
            }
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
            usuario.setRol(Usuario.RolUsuario.DOCENTE);
            usuario.setActivo(true);
            usuarioRepo.save(usuario);

            Docente docente = new Docente();
            docente.setUsuario(usuario);
            docente.setCedula(cedula);
            docente.setEspecialidad(especialidad);
            docenteRepo.save(docente);

        } else {
            // ── EDITAR ─────────────────────────────────────────
            Docente docente = buscarPorId(idDocente);
            Usuario usuario = docente.getUsuario();

            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            if (contrasenaPlana != null && !contrasenaPlana.isBlank()) {
                usuario.setContrasena(passwordEncoder.encode(contrasenaPlana));
            }
            usuarioRepo.save(usuario);

            docente.setCedula(cedula);
            docente.setEspecialidad(especialidad);
            docenteRepo.save(docente);
        }
    }

    @Transactional
    public void toggleActivo(Integer id) {
        Docente d = buscarPorId(id);
        Usuario u = d.getUsuario();
        u.setActivo(!u.isActivo());
        usuarioRepo.save(u);
    }
}