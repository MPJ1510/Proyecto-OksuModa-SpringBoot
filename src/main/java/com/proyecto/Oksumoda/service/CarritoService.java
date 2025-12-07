package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Producto;
import com.proyecto.Oksumoda.model.CarritoItem;
import com.proyecto.Oksumoda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope // 🔥 IMPORTANTE: Una instancia por sesión HTTP
public class CarritoService {
    
    private List<CarritoItem> items = new ArrayList<>();
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * Agregar producto al carrito
     */
    public void agregarProducto(CarritoItem item) {
        System.out.println("🛒 Agregando producto al carrito - ID: " + item.getProductoId());
        
        // Verificar si el producto existe en BD
        Producto producto = productoRepository.findById(item.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Verificar stock
        if (producto.getStock() < item.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Buscar si ya existe el producto en el carrito
        Optional<CarritoItem> existente = items.stream()
            .filter(i -> i.getProductoId().equals(item.getProductoId()))
            .findFirst();
        
        if (existente.isPresent()) {
            // Si existe, actualizar cantidad
            CarritoItem itemExistente = existente.get();
            int nuevaCantidad = itemExistente.getCantidad() + item.getCantidad();
            
            // Verificar stock para la nueva cantidad
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para la cantidad solicitada");
            }
            
            itemExistente.setCantidad(nuevaCantidad);
            System.out.println("✅ Cantidad actualizada: " + nuevaCantidad);
        } else {
            // Si no existe, agregarlo
            items.add(item);
            System.out.println("✅ Producto agregado al carrito");
        }
    }
    
    /**
     * Agregar producto por ID (método alternativo)
     */
    public void agregarProductoPorId(Long productoId, Integer cantidad, String talla, String color) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        CarritoItem item = new CarritoItem();
        item.setProductoId(producto.getId());
        item.setNombre(producto.getNombre());
        item.setPrecio(producto.getPrecio());
        item.setCantidad(cantidad);
        item.setImagen(producto.getFoto());
        item.setTalla(talla);
        item.setColor(color);
        
        agregarProducto(item);
    }
    
    /**
     * Eliminar producto del carrito
     */
    public void eliminarProducto(Long productoId) {
        System.out.println("🗑️ Eliminando producto: " + productoId);
        items.removeIf(item -> item.getProductoId().equals(productoId));
        System.out.println("✅ Producto eliminado");
    }
    
    /**
     * Actualizar cantidad de un producto
     */
    public void actualizarCantidad(Long productoId, Integer nuevaCantidad) {
        System.out.println("🔄 Actualizando cantidad - Producto: " + productoId + ", Nueva cantidad: " + nuevaCantidad);
        
        if (nuevaCantidad <= 0) {
            eliminarProducto(productoId);
            return;
        }
        
        // Verificar stock
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (producto.getStock() < nuevaCantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        // Actualizar cantidad
        items.stream()
            .filter(item -> item.getProductoId().equals(productoId))
            .findFirst()
            .ifPresent(item -> {
                item.setCantidad(nuevaCantidad);
                System.out.println("✅ Cantidad actualizada a: " + nuevaCantidad);
            });
    }
    
    /**
     * Obtener todos los items del carrito
     */
    public List<CarritoItem> obtenerItems() {
        return new ArrayList<>(items); // Retornar copia para evitar modificaciones externas
    }
    
    /**
     * Calcular el total del carrito
     */
    public BigDecimal calcularTotal() {
        return items.stream()
            .map(CarritoItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Obtener cantidad de items (suma de todas las cantidades)
     */
    public Integer obtenerCantidadItems() {
        return items.stream()
            .mapToInt(CarritoItem::getCantidad)
            .sum();
    }
    
    /**
     * Vaciar el carrito
     */
    public void vaciarCarrito() {
        System.out.println("🗑️ Vaciando carrito");
        items.clear();
        System.out.println("✅ Carrito vaciado");
    }
    
    /**
     * Verificar si el carrito está vacío
     */
    public boolean estaVacio() {
        return items.isEmpty();
    }
    
    /**
     * Verificar stock disponible
     */
    public boolean verificarStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        return producto.getStock() >= cantidad;
    }
}