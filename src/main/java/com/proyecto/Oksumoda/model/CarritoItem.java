package com.proyecto.Oksumoda.model;

import java.math.BigDecimal;

public class CarritoItem {
    
    private Long productoId;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private String imagen;
    private String talla;
    private String color;
    
    // Constructores
    public CarritoItem() {
    }
    
    public CarritoItem(Long productoId, String nombre, BigDecimal precio, Integer cantidad, String imagen) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }
    
    // Método para calcular subtotal
    public BigDecimal getSubtotal() {
        return precio.multiply(new BigDecimal(cantidad));
    }
    
    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public String getTalla() {
        return talla;
    }
    
    public void setTalla(String talla) {
        this.talla = talla;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}