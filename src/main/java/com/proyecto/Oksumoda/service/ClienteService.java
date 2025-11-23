package com.proyecto.Oksumoda.service;

import com.proyecto.Oksumoda.entity.Cliente;
import com.proyecto.Oksumoda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    /**
     * Filtra clientes según criterios opcionales: nombre, email, teléfono y estado.
     * 
     * @param nombre nombre del cliente a filtrar (opcional)
     * @param email email del cliente a filtrar (opcional)
     * @param telefono teléfono del cliente a filtrar (opcional)
     * @param estado estado del cliente a filtrar (opcional)
     * @return lista de clientes que cumplen con los criterios de búsqueda
     */
    public List<Cliente> filtrarClientes(String nombre, String email, String telefono, String estado) {
        List<Cliente> todos = clienteRepository.findAll();
        
        return todos.stream()
            .filter(c -> nombre == null || nombre.trim().isEmpty() || 
                    c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(c -> email == null || email.trim().isEmpty() || 
                    c.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(c -> telefono == null || telefono.trim().isEmpty() || 
                    (c.getTelefono() != null && c.getTelefono().contains(telefono)))
            .filter(c -> estado == null || estado.trim().isEmpty() || 
                    c.getEstado().equalsIgnoreCase(estado))
            .collect(Collectors.toList());
    }

    public void save(Cliente cliente) {
        System.out.println("🔄 ClienteService.save() - Procesando cliente: " + cliente.getEmail());
        
        // Si es un nuevo cliente
        if (cliente.getIdCliente() == null) {
            System.out.println("➕ Creando nuevo cliente");
            
            // Si la contraseña está vacía, asignar una por defecto
            if (cliente.getContrasena() == null || cliente.getContrasena().trim().isEmpty()) {
                System.out.println("⚠️ Contraseña vacía, asignando contraseña por defecto");
                cliente.setContrasena(passwordEncoder.encode("cliente123"));
            } else {
                System.out.println("🔐 Hasheando contraseña de nuevo cliente");
                cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
            }
        } 
        // Si es edición
        else {
            System.out.println("✏️ Editando cliente existente ID: " + cliente.getIdCliente());
            
            Cliente clienteExistente = clienteRepository.findById(cliente.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            // Si la contraseña está vacía, mantener la anterior
            if (cliente.getContrasena() == null || cliente.getContrasena().trim().isEmpty()) {
                System.out.println("🔒 Manteniendo contraseña anterior");
                cliente.setContrasena(clienteExistente.getContrasena());
            } 
            // Si la contraseña NO está hasheada, hashearla
            else if (!cliente.getContrasena().startsWith("$2a$")) {
                System.out.println("🔐 Hasheando nueva contraseña");
                cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
            } else {
                System.out.println("✅ Contraseña ya hasheada");
            }
        }
        
        clienteRepository.save(cliente);
        System.out.println("✅ Cliente guardado exitosamente en la BD");
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}