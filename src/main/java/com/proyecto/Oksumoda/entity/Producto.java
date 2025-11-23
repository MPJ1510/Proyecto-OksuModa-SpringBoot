package com.proyecto.Oksumoda.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importamos BigDecimal para precisión monetaria

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto") // <--- Columna SQL: id_producto
    private Long id; 

    // 2. 🔢 STOCK: Mapeado al campo 'stock' con la columna 'cantidad_stock'
    @Column(name = "cantidad_stock") // <--- Columna SQL: cantidad_stock
    private int stock; 
    
    // Otros campos (sin anotación @Column ya que el nombre de la variable coincide, excepto precio)
    private String nombre;
    private String descripcion;
    private String foto;
    private String estado;
    private BigDecimal precio;

    public Producto() {
    this.stock = 0;
    this.estado = "activo";
}

    public Producto(Long id, String nombre, String descripcion, BigDecimal precio, int stock, String foto, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.foto = foto;
        this.estado = estado;
    }

    // --- Getters y Setters ---

    // (Genera/añade todos los Getters y Setters, incluyendo los de 'foto' y
    // 'estado')

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Tipo cambiado a BigDecimal
    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Getters y Setters para foto y estado
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}