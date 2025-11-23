package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Rol; // O .model
import com.proyecto.Oksumoda.repository.RolRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Optional<Rol> findById(Integer id) { // El método que UsuarioService necesita
        return rolRepository.findById(id);
    }
}