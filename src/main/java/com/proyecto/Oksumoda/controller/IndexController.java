package com.proyecto.Oksumoda.controller;

import com.proyecto.Oksumoda.entity.Producto;
import com.proyecto.Oksumoda.service.CarritoService;
import com.proyecto.Oksumoda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        return "Inicio";
    }
    
    @GetMapping("/404")
    public String error404() {
        return "404";
    }

    @GetMapping("/403")
    public String error403() {
        return "403";
    }

    @GetMapping("/contactanos")
    public String contactanos() {
        return "contactanos";
    }

    @GetMapping("/hombres")
    public String hombres(Model model) {
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        List<Producto> productos = productoService.findByCategoriaAndEstado("Hombres", "activo");
        model.addAttribute("productos", productos);
        return "hombres";
    }

    @GetMapping("/mujeres")
    public String mujeres(Model model) {
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        List<Producto> productos = productoService.findByCategoriaAndEstado("Mujeres", "activo");
        model.addAttribute("productos", productos);
        return "mujeres";
    }

    @GetMapping("/ninos")
    public String ninos(Model model) {
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        List<Producto> productos = productoService.findByCategoriaAndEstado("Niños", "activo");
        model.addAttribute("productos", productos);
        return "ninos";
    }

    @GetMapping("/otros")
    public String otros(Model model) {
        model.addAttribute("cantidadItems", carritoService.obtenerCantidadItems());
        List<Producto> productos = productoService.findByCategoriaAndEstado("Otros", "activo");
        model.addAttribute("productos", productos);
        return "otros";
    }
    
}