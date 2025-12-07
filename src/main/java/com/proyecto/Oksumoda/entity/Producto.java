package com.proyecto.Oksumoda.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id; 

    @Column(name = "cantidad_stock")
    private int stock; 
    
    private String nombre;
    private String descripcion;
    private String foto;
    private String estado;
    private BigDecimal precio;
    
    // 🔥 CAMPOS NUEVOS PARA EL CARRITO
    private String categoria; // Hombres, Mujeres, Niños, Otros
    private String subcategoria; // Camisetas, Pantalones, etc.
    private String colores; // "Negro,Blanco,Azul" separados por coma
    private String tallas; // "S,M,L,XL" separados por coma
    private BigDecimal precioAnterior; // Para mostrar descuentos
    private Boolean esNuevo; // Para etiqueta "NUEVO"

    // Constructores
    public Producto() {
        this.stock = 0;
        this.estado = "activo";
        this.esNuevo = false;
    }

    public Producto(Long id, String nombre, String descripcion, BigDecimal precio, int stock, String foto, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.foto = foto;
        this.estado = estado;
        this.esNuevo = false;
    }

    // Getters y Setters existentes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    // 🔥 GETTERS Y SETTERS NUEVOS
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
    
    public String getColores() { return colores; }
    public void setColores(String colores) { this.colores = colores; }
    
    public String getTallas() { return tallas; }
    public void setTallas(String tallas) { this.tallas = tallas; }
    
    public BigDecimal getPrecioAnterior() { return precioAnterior; }
    public void setPrecioAnterior(BigDecimal precioAnterior) { this.precioAnterior = precioAnterior; }
    
    public Boolean getEsNuevo() { return esNuevo; }
    public void setEsNuevo(Boolean esNuevo) { this.esNuevo = esNuevo; }
}