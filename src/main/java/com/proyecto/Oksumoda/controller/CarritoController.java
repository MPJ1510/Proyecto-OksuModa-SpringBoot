package com.proyecto.Oksumoda.controller;

import com.proyecto.Oksumoda.model.CarritoItem;
import com.proyecto.Oksumoda.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    
    /**
     * Mostrar página del carrito
     */
    @GetMapping
    public String verCarrito(Model model, Authentication authentication) {
        // Verificar si el usuario está autenticado
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("total", carritoService.calcularTotal());
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        return "carrito";
    }
    
    /**
     * Agregar producto al carrito (AJAX) - Recibe parámetros form-encoded
     */
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarAlCarrito(
            @RequestParam Long productoId,
            @RequestParam(defaultValue = "1") Integer cantidad,
            @RequestParam(required = false) String talla,
            @RequestParam(required = false) String color,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar autenticación
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Debe iniciar sesión para agregar productos al carrito");
                return ResponseEntity.ok(response);
            }
            
            System.out.println("🛒 Request agregar al carrito:");
            System.out.println("   - Producto ID: " + productoId);
            System.out.println("   - Cantidad: " + cantidad);
            System.out.println("   - Talla: " + talla);
            System.out.println("   - Color: " + color);
            
            // Agregar producto usando el método que acepta parámetros
            carritoService.agregarProductoPorId(productoId, cantidad, talla, color);
            
            response.put("success", true);
            response.put("message", "Producto agregado al carrito");
            response.put("cantidadItems", carritoService.obtenerCantidadItems());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al agregar al carrito: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Eliminar producto del carrito
     */
    @PostMapping("/eliminar/{productoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarDelCarrito(
            @PathVariable Long productoId,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Debe iniciar sesión");
                return ResponseEntity.ok(response);
            }
            
            carritoService.eliminarProducto(productoId);
            
            response.put("success", true);
            response.put("message", "Producto eliminado del carrito");
            response.put("cantidadItems", carritoService.obtenerCantidadItems());
            response.put("total", carritoService.calcularTotal());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar: " + e.getMessage());
            
            response.put("success", false);
            response.put("message", "Error al eliminar el producto");
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Actualizar cantidad de producto
     */
    @PostMapping("/actualizar/{productoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarCantidad(
            @PathVariable Long productoId, 
            @RequestParam Integer cantidad,
            Authentication authentication) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Debe iniciar sesión");
                return ResponseEntity.ok(response);
            }
            
            if (cantidad <= 0) {
                carritoService.eliminarProducto(productoId);
            } else {
                carritoService.actualizarCantidad(productoId, cantidad);
            }
            
            response.put("success", true);
            response.put("cantidadItems", carritoService.obtenerCantidadItems());
            response.put("total", carritoService.calcularTotal());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar: " + e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Obtener cantidad de items (para actualizar el contador en el navbar)
     */
    @GetMapping("/cantidad")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerCantidad(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            response.put("cantidadItems", carritoService.obtenerCantidadItems());
        } else {
            response.put("cantidadItems", 0);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Vaciar carrito
     */
    @PostMapping("/vaciar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> vaciarCarrito(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "Debe iniciar sesión");
                return ResponseEntity.ok(response);
            }
            
            carritoService.vaciarCarrito();
            
            response.put("success", true);
            response.put("message", "Carrito vaciado");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al vaciar el carrito");
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Ir a checkout
     */
    @GetMapping("/checkout")
    public String checkout(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        if (carritoService.estaVacio()) {
            return "redirect:/carrito";
        }
        
        model.addAttribute("items", carritoService.obtenerItems());
        model.addAttribute("subtotal", carritoService.calcularTotal());
        
        // Calcular envío (ejemplo: gratis si es mayor a 150.000)
        BigDecimal subtotal = carritoService.calcularTotal();
        BigDecimal envio = subtotal.compareTo(new BigDecimal("150000")) >= 0 
            ? BigDecimal.ZERO 
            : new BigDecimal("10000");
        
        model.addAttribute("envio", envio);
        model.addAttribute("total", subtotal.add(envio));
        
        return "checkout";
    }
}