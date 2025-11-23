package com.proyecto.Oksumoda.repository;

import com.proyecto.Oksumoda.entity.Producto; // 👈 Importa la Entidad Producto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // JpaRepository<[Nombre de la Entidad], [Tipo de Dato de la Llave Primaria]>
}