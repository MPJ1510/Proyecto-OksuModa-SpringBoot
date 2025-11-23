package com.proyecto.Oksumoda.model;

import com.proyecto.Oksumoda.entity.Rol;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    // ========== IMPLEMENTACIÓN DE UserDetails ==========
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ✅ IMPORTANTE: El rol en la BD debe ser "ADMIN" o "CLIENTE" (sin el prefijo ROLE_)
        // Spring Security añade automáticamente "ROLE_" cuando se usa hasRole()
        String roleName = rol.getNombre().toUpperCase();
        
        // Si el rol ya tiene el prefijo ROLE_, no lo añadimos de nuevo
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        
        System.out.println("🔐 getAuthorities() llamado para: " + this.email);
        System.out.println("🔐 Rol en BD: " + rol.getNombre());
        System.out.println("🔐 Authority generado: " + roleName);
        
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "activo".equalsIgnoreCase(this.estado);
    }
}