package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Producto;
import com.proyecto.Oksumoda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    // 🔥 MÉTODO NUEVO: Buscar por categoría
    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // 🔥 MÉTODO NUEVO: Buscar por categoría y estado
    public List<Producto> findByCategoriaAndEstado(String categoria, String estado) {
        return productoRepository.findByCategoriaAndEstado(categoria, estado);
    }

    /**
     * Filtra productos según criterios opcionales
     */
    public List<Producto> filtrarProductos(String nombre, String estado, BigDecimal precioMin, 
                                          BigDecimal precioMax, Integer stockMin) {
        List<Producto> todos = productoRepository.findAll();

        return todos.stream()
            .filter(p -> nombre == null || nombre.trim().isEmpty() ||
                    p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(p -> estado == null || estado.trim().isEmpty() ||
                    p.getEstado().equalsIgnoreCase(estado))
            .filter(p -> precioMin == null || p.getPrecio().compareTo(precioMin) >= 0)
            .filter(p -> precioMax == null || p.getPrecio().compareTo(precioMax) <= 0)
            .filter(p -> stockMin == null || p.getStock() >= stockMin)
            .collect(Collectors.toList());
    }

    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
}