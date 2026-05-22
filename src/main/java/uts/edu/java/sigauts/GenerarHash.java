package uts.edu.java.sigauts;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        String[][] usuarios = {
            {"Admin Sistema",  "admin@uts.edu.co",     "admin123",       "ADMINISTRADOR"},
            {"Carlos Pérez",   "cperez@uts.edu.co",    "docente123",     "DOCENTE"},
            {"María López",    "mlopez@uts.edu.co",    "docente456",     "DOCENTE"},
            {"Juan García",    "2023001@uts.edu.co",   "estudiante123",  "ESTUDIANTE"},
            {"Ana Torres",     "2023002@uts.edu.co",   "estudiante456",  "ESTUDIANTE"},
            {"Luis Martínez",  "2022015@uts.edu.co",   "estudiante789",  "ESTUDIANTE"}
        };

        System.out.println("INSERT INTO usuario (nombre, correo, contrasena, rol, activo) VALUES");
        for (int i = 0; i < usuarios.length; i++) {
            String hash = encoder.encode(usuarios[i][2]);
            String coma = (i < usuarios.length - 1) ? "," : ";";
            System.out.println("('" + usuarios[i][0] + "', '" + usuarios[i][1] + "', '" + hash + "', '" + usuarios[i][3] + "', 1)" + coma);
        }
    }
}