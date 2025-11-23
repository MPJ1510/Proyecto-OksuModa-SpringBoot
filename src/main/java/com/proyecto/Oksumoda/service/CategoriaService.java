package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Categoria;
import com.proyecto.Oksumoda.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ✅ ESTE ES EL MÉTODO QUE FALTABA
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> filtrarCategorias(String nombre) {
        List<Categoria> todas = categoriaRepository.findAll();

        return todas.stream()
            .filter(c -> nombre == null || nombre.trim().isEmpty() ||
                        c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .collect(Collectors.toList());
    }

    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}