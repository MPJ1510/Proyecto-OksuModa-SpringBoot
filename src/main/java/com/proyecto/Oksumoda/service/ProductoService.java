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

    /**
     * Filtra productos según criterios opcionales: nombre, estado, rango de precio y stock mínimo.
     * 
     * @param nombre nombre del producto a filtrar (opcional)
     * @param estado estado del producto a filtrar (opcional)
     * @param precioMin precio mínimo del producto (opcional)
     * @param precioMax precio máximo del producto (opcional)
     * @param stockMin stock mínimo del producto (opcional)
     * @return lista de productos que cumplen con los criterios de búsqueda
     */
    public List<Producto> filtrarProductos(String nombre, String estado, BigDecimal precioMin, 
                                          BigDecimal precioMax, Integer stockMin) {
        List<Producto> todos = productoRepository.findAll();

        return todos.stream()
            // Filtro por nombre
            .filter(p -> nombre == null || nombre.trim().isEmpty() ||
                    p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            // Filtro por estado
            .filter(p -> estado == null || estado.trim().isEmpty() ||
                    p.getEstado().equalsIgnoreCase(estado))
            // Filtro por precio mínimo
            .filter(p -> precioMin == null || p.getPrecio().compareTo(precioMin) >= 0)
            // Filtro por precio máximo
            .filter(p -> precioMax == null || p.getPrecio().compareTo(precioMax) <= 0)
            // Filtro por stock mínimo
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