package com.proyecto.Oksumoda.controller;

import com.proyecto.Oksumoda.entity.Producto;
import com.proyecto.Oksumoda.entity.Rol;
import com.proyecto.Oksumoda.entity.Categoria;
import com.proyecto.Oksumoda.entity.Cliente;
import com.proyecto.Oksumoda.model.Usuario;
import com.proyecto.Oksumoda.service.ProductoService;
import com.proyecto.Oksumoda.service.UsuarioService;
import com.proyecto.Oksumoda.service.RolService;
import com.proyecto.Oksumoda.service.CategoriaService;
import com.proyecto.Oksumoda.service.ClienteService;
import com.proyecto.Oksumoda.Utils.PdfGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PdfGenerator pdfGenerator;

    @GetMapping("/dashboard")
    public String adminDashboard(
            @RequestParam(value = "view", required = false) String view,
            @RequestParam(value = "id", required = false) Long id,
            // Parámetros de filtro (compartidos)
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "estado", required = false) String estado,
            // Parámetros específicos de clientes
            @RequestParam(value = "telefono", required = false) String telefono,
            // Parámetros específicos de usuarios
            @RequestParam(value = "rolNombre", required = false) String rolNombre,
            // Parámetros específicos de productos
            @RequestParam(value = "precioMin", required = false) String precioMinStr,
            @RequestParam(value = "precioMax", required = false) String precioMaxStr,
            @RequestParam(value = "stockMin", required = false) String stockMinStr,
            Model model) {

        String currentView = view == null ? "summary" : view;
        model.addAttribute("currentView", currentView);

        // ========== VISTA DE RESUMEN ==========
        if ("summary".equals(currentView)) {
            model.addAttribute("totalUsuarios", usuarioService.findAll().size());
            model.addAttribute("productosActivos", productoService.findAll().size());
            model.addAttribute("ventasHoy", "$0");
        }

        // ========== VISTA DE PRODUCTOS CON FILTROS ==========
        else if ("productos".equals(currentView) || "new_producto".equals(currentView)
                || "edit_producto".equals(currentView)) {

            // Convertir parámetros de String a BigDecimal/Integer
            BigDecimal precioMinParam = null;
            BigDecimal precioMaxParam = null;
            Integer stockMinParam = null;

            try {
                if (precioMinStr != null && !precioMinStr.trim().isEmpty()) {
                    precioMinParam = new BigDecimal(precioMinStr);
                }
                if (precioMaxStr != null && !precioMaxStr.trim().isEmpty()) {
                    precioMaxParam = new BigDecimal(precioMaxStr);
                }
                if (stockMinStr != null && !stockMinStr.trim().isEmpty()) {
                    stockMinParam = Integer.parseInt(stockMinStr);
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Error al parsear parámetros numéricos: " + e.getMessage());
            }

            // Aplicar filtros si existen
            List<Producto> productosFiltrados;
            if (nombre != null || estado != null || precioMinParam != null || precioMaxParam != null
                    || stockMinParam != null) {
                productosFiltrados = productoService.filtrarProductos(nombre, estado, precioMinParam, precioMaxParam,
                        stockMinParam);
                System.out.println("🔍 Filtros aplicados - Productos encontrados: " + productosFiltrados.size());
            } else {
                productosFiltrados = productoService.findAll();
            }

            model.addAttribute("productos", productosFiltrados);

            // Pasar los valores de filtro a la vista para mantenerlos
            model.addAttribute("filtroNombre", nombre);
            model.addAttribute("filtroEstado", estado);
            model.addAttribute("filtroPrecioMin", precioMinStr);
            model.addAttribute("filtroPrecioMax", precioMaxStr);
            model.addAttribute("filtroStockMin", stockMinStr);

            if ("new_producto".equals(currentView)) {
                model.addAttribute("producto", new Producto());
            } else if ("edit_producto".equals(currentView) && id != null) {
                Producto producto = productoService.findById(id).orElse(new Producto());
                model.addAttribute("producto", producto);
            }
        }

        // ========== VISTA DE USUARIOS CON FILTROS ==========
        else if ("usuarios".equals(currentView) || "new_usuario".equals(currentView)
                || "edit_usuario".equals(currentView)) {

            // Aplicar filtros si existen
            List<Usuario> usuariosFiltrados;
            if (nombre != null || email != null || rolNombre != null || estado != null) {
                usuariosFiltrados = usuarioService.filtrarUsuarios(nombre, email, rolNombre, estado);
                System.out.println("🔍 Filtros aplicados - Usuarios encontrados: " + usuariosFiltrados.size());
            } else {
                usuariosFiltrados = usuarioService.findAll();
            }

            model.addAttribute("usuarios", usuariosFiltrados);
            model.addAttribute("roles", rolService.findAll());

            // Pasar los valores de filtro a la vista para mantenerlos
            model.addAttribute("filtroNombre", nombre);
            model.addAttribute("filtroEmail", email);
            model.addAttribute("filtroRolNombre", rolNombre);
            model.addAttribute("filtroEstado", estado);

            if ("new_usuario".equals(currentView)) {
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setEstado("activo");
                model.addAttribute("usuario", nuevoUsuario);
            } else if ("edit_usuario".equals(currentView) && id != null) {
                Usuario usuario = usuarioService.findById(id).orElse(new Usuario());
                model.addAttribute("usuario", usuario);
            }
        }

        // ========== VISTA DE CATEGORÍAS ==========
        else if ("categorias".equals(currentView) || "new_categoria".equals(currentView)
                || "edit_categoria".equals(currentView)) {

            // Aplicar filtros si existen
            List<Categoria> categoriasFiltradas;
            if (nombre != null && !nombre.trim().isEmpty()) {
                categoriasFiltradas = categoriaService.filtrarCategorias(nombre);
                System.out.println("🔍 Filtros aplicados - Categorías encontradas: " + categoriasFiltradas.size());
            } else {
                categoriasFiltradas = categoriaService.findAll();
            }

            model.addAttribute("categorias", categoriasFiltradas);

            // Pasar el valor de filtro a la vista para mantenerlo
            model.addAttribute("filtroNombre", nombre);

            if ("new_categoria".equals(currentView)) {
                model.addAttribute("categoria", new Categoria());
            } else if ("edit_categoria".equals(currentView) && id != null) {
                Categoria categoria = categoriaService.findById(id).orElse(new Categoria());
                model.addAttribute("categoria", categoria);
            }
        }

        // ========== VISTA DE CLIENTES CON FILTROS ==========
        else if ("clientes".equals(currentView) || "new_cliente".equals(currentView)
                || "edit_cliente".equals(currentView)) {

            // Aplicar filtros si existen
            List<Cliente> clientesFiltrados;
            if (nombre != null || email != null || telefono != null || estado != null) {
                clientesFiltrados = clienteService.filtrarClientes(nombre, email, telefono, estado);
                System.out.println("🔍 Filtros aplicados - Clientes encontrados: " + clientesFiltrados.size());
            } else {
                clientesFiltrados = clienteService.findAll();
            }

            model.addAttribute("clientes", clientesFiltrados);

            // Pasar los valores de filtro a la vista para mantenerlos
            model.addAttribute("filtroNombre", nombre);
            model.addAttribute("filtroEmail", email);
            model.addAttribute("filtroTelefono", telefono);
            model.addAttribute("filtroEstado", estado);

            if ("new_cliente".equals(currentView)) {
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setEstado("activo");
                model.addAttribute("cliente", nuevoCliente);
            } else if ("edit_cliente".equals(currentView) && id != null) {
                Cliente cliente = clienteService.findById(id).orElse(new Cliente());
                model.addAttribute("cliente", cliente);
            }
        }

        // ========== VISTA DE VENTAS ==========
        else if ("ventas".equals(currentView)) {
            model.addAttribute("ventas", java.util.Collections.emptyList());
        }

        // ========== VISTA DE REPORTES ==========
        else if ("reportes".equals(currentView)) {
            model.addAttribute("totalProductos", productoService.findAll().size());
            model.addAttribute("totalClientes", clienteService.findAll().size());
            model.addAttribute("totalVentas", 0);
            model.addAttribute("ingresosTotales", "$0");
        }

        return "admin/dashboard";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new CustomNumberEditor(Integer.class, true));
    }

    // ========== PRODUCTOS ==========
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.save(producto);
        return "redirect:/admin/dashboard?view=productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return "redirect:/admin/dashboard?view=productos";
    }

    // ========== USUARIOS ==========
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        System.out.println("💾 Guardando usuario: " + usuario.getNombre());
        System.out.println("📧 Email: " + usuario.getEmail());
        System.out.println("🔑 ID Usuario: " + usuario.getIdUsuario());
        System.out.println("👤 Rol recibido: " + (usuario.getRol() != null ? usuario.getRol().getIdRol() : "NULL"));

        if (usuario.getRol() != null && usuario.getRol().getIdRol() != null) {
            Rol rol = rolService.findById(usuario.getRol().getIdRol())
                    .orElseThrow(
                            () -> new RuntimeException("Rol no encontrado con ID: " + usuario.getRol().getIdRol()));
            usuario.setRol(rol);
        }

        usuarioService.save(usuario);

        System.out.println("✅ Usuario guardado exitosamente");
        return "redirect:/admin/dashboard?view=usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/admin/dashboard?view=usuarios";
    }

    // ========== CATEGORÍAS ==========
    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.save(categoria);
        return "redirect:/admin/dashboard?view=categorias";
    }

    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return "redirect:/admin/dashboard?view=categorias";
    }

    // ========== CLIENTES ==========
    @PostMapping("/clientes/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.save(cliente);
        return "redirect:/admin/dashboard?view=clientes";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.deleteById(id);
        return "redirect:/admin/dashboard?view=clientes";
    }

    // ========== REPORTES ==========

    @GetMapping("/reportes/usuarios")
    public void reporteUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String rolNombre,
            @RequestParam(required = false) String estado,
            HttpServletResponse response) throws Exception {

        System.out.println("🎯 MÉTODO reporteUsuarios() LLAMADO");
        System.out.println("📋 Filtros: nombre=" + nombre + ", email=" + email +
                ", rolNombre=" + rolNombre + ", estado=" + estado);

        // Aplicar filtros
        List<Usuario> usuarios = usuarioService.filtrarUsuarios(nombre, email, rolNombre, estado);

        Map<String, Object> model = new HashMap<>();
        model.put("usuarios", usuarios);
        model.put("titulo", "Reporte de Usuarios");

        pdfGenerator.generarPdf("reporte-usuarios", model, response, "reporte-usuarios");
    }

    @GetMapping("/reportes/productos")
    public void reporteProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String precioMin,
            @RequestParam(required = false) String precioMax,
            @RequestParam(required = false) String stockMin,
            HttpServletResponse response) throws Exception {

        System.out.println("🎯 MÉTODO reporteProductos() LLAMADO");
        System.out.println("📋 Filtros: nombre=" + nombre + ", estado=" + estado +
                ", precioMin=" + precioMin + ", precioMax=" + precioMax +
                ", stockMin=" + stockMin);

        // Convertir parámetros
        BigDecimal precioMinParam = null;
        BigDecimal precioMaxParam = null;
        Integer stockMinParam = null;

        try {
            if (precioMin != null && !precioMin.trim().isEmpty()) {
                precioMinParam = new BigDecimal(precioMin);
            }
            if (precioMax != null && !precioMax.trim().isEmpty()) {
                precioMaxParam = new BigDecimal(precioMax);
            }
            if (stockMin != null && !stockMin.trim().isEmpty()) {
                stockMinParam = Integer.parseInt(stockMin);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Error al parsear parámetros en reporte: " + e.getMessage());
        }

        // Aplicar filtros
        List<Producto> productos = productoService.filtrarProductos(nombre, estado, precioMinParam, precioMaxParam,
                stockMinParam);

        Map<String, Object> model = new HashMap<>();
        model.put("productos", productos);
        model.put("titulo", "Reporte de Productos");

        pdfGenerator.generarPdf("reporte-productos", model, response, "reporte-productos");
    }

    @GetMapping("/reportes/categorias")
    public void reporteCategorias(
            @RequestParam(required = false) String nombre,
            HttpServletResponse response) throws Exception {

        System.out.println("🎯 MÉTODO reporteCategorias() LLAMADO");
        System.out.println("📋 Filtro: nombre=" + nombre);

        // Aplicar filtros
        List<Categoria> categorias;
        if (nombre != null && !nombre.trim().isEmpty()) {
            categorias = categoriaService.filtrarCategorias(nombre);
        } else {
            categorias = categoriaService.findAll();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("categorias", categorias);
        model.put("titulo", "Reporte de Categorías");

        pdfGenerator.generarPdf("reporte-categorias", model, response, "reporte-categorias");
    }

    @GetMapping("/reportes/clientes")
    public void reporteClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String estado,
            HttpServletResponse response) throws Exception {

        System.out.println("🎯 MÉTODO reporteClientes() LLAMADO");
        System.out.println("📋 Filtros: nombre=" + nombre + ", email=" + email +
                ", telefono=" + telefono + ", estado=" + estado);

        // Aplicar filtros
        List<Cliente> clientes = clienteService.filtrarClientes(nombre, email, telefono, estado);

        Map<String, Object> model = new HashMap<>();
        model.put("clientes", clientes);
        model.put("titulo", "Reporte de Clientes");

        pdfGenerator.generarPdf("reporte-clientes", model, response, "reporte-clientes");
    }
}