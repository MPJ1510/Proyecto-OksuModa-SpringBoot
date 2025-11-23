package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Rol;
import com.proyecto.Oksumoda.model.Usuario;
import com.proyecto.Oksumoda.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolService = rolService;
    }

    // ==========================================================
    // MÉTODOS DE GESTIÓN (CRUD)
    // ==========================================================
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Filtra usuarios según criterios opcionales: nombre, email, rol y estado.
     * 
     * @param nombre nombre del usuario a filtrar (opcional)
     * @param email email del usuario a filtrar (opcional)
     * @param rolNombre nombre del rol a filtrar (opcional)
     * @param estado estado del usuario a filtrar (opcional)
     * @return lista de usuarios que cumplen con los criterios de búsqueda
     */
    public List<Usuario> filtrarUsuarios(String nombre, String email, String rolNombre, String estado) {
        List<Usuario> todos = usuarioRepository.findAll();
        
        return todos.stream()
            .filter(u -> nombre == null || nombre.trim().isEmpty() || 
                    u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(u -> email == null || email.trim().isEmpty() || 
                    u.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(u -> rolNombre == null || rolNombre.trim().isEmpty() || 
                    u.getRol().getNombre().equalsIgnoreCase(rolNombre))
            .filter(u -> estado == null || estado.trim().isEmpty() || 
                    u.getEstado().equalsIgnoreCase(estado))
            .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por ID. Recibe Long (desde AdminController) y lo convierte a Integer.
     */
    public Optional<Usuario> findById(Long id) {
        if (id == null) return Optional.empty();
        return usuarioRepository.findById(id.intValue()); 
    }

    public void save(Usuario usuario) {
        System.out.println("🔄 UsuarioService.save() - Procesando usuario: " + usuario.getEmail());
        
        // Si es edición (tiene ID)
        if (usuario.getIdUsuario() != null) {
            System.out.println("✏️ Editando usuario existente ID: " + usuario.getIdUsuario());
            
            // Cargar el usuario existente
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Si la contraseña está vacía o null, mantener la contraseña anterior
            if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                System.out.println("🔒 Manteniendo contraseña anterior");
                usuario.setContrasena(usuarioExistente.getContrasena());
            } 
            // Si la contraseña NO está hasheada (no empieza con $2a$), hashearla
            else if (!usuario.getContrasena().startsWith("$2a$")) {
                System.out.println("🔐 Hasheando nueva contraseña");
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            } else {
                System.out.println("✅ Contraseña ya hasheada");
            }
        } 
        // Si es nuevo usuario
        else {
            System.out.println("➕ Creando nuevo usuario");
            
            // La contraseña es obligatoria para nuevos usuarios
            if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
                throw new RuntimeException("La contraseña es obligatoria para nuevos usuarios");
            }
            
            // Hashear la contraseña si no está hasheada
            if (!usuario.getContrasena().startsWith("$2a$")) {
                System.out.println("🔐 Hasheando contraseña de nuevo usuario");
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
        }
        
        usuarioRepository.save(usuario);
        System.out.println("✅ Usuario guardado exitosamente en la BD");
    }

    /**
     * Elimina un usuario. Recibe Long (desde AdminController) y lo convierte a Integer.
     */
    public void deleteById(Long id) { 
        if (id != null) {
            usuarioRepository.deleteById(id.intValue());
        }
    }

    public Usuario registrarNuevoUsuario(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        // Obtener el objeto Rol de Cliente (asumiendo que 2 es el ID)
        Rol clienteRol = rolService.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol de Cliente (ID 2) no encontrado."));
        usuario.setRol(clienteRol);

        return usuarioRepository.save(usuario);
    }

    // ==========================================================
    // MÉTODOS DE AUTENTICACIÓN
    // ==========================================================
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }
        return usuario; 
    }
}