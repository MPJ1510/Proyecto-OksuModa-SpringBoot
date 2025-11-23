package com.proyecto.Oksumoda.repository;

import com.proyecto.Oksumoda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // CAMBIO CRUCIAL: Buscar por Email, no por Username
    Usuario findByEmail(String email);

    //Usuario findByUsername(String username);
    
    // Si tu ID es Integer (como en Usuario.java), usa Integer aquí.
}