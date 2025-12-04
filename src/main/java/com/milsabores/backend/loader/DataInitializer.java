package com.milsabores.backend.loader;

import com.milsabores.backend.model.*;
import com.milsabores.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private OrdenRepository ordenRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verificamos si ya existen categorías para no duplicar datos
        if (categoriaRepository.count() == 0) {
            System.out.println("--- CARGANDO DATOS INICIALES ---");

            // 1. ROLES
            Rol rolAdmin = new Rol(null, "ADMIN");
            Rol rolCliente = new Rol(null, "CLIENTE");
            Rol rolEmpleado = new Rol(null, "EMPLEADO");
            rolRepository.saveAll(Arrays.asList(rolAdmin, rolCliente, rolEmpleado));

            // 2. USUARIOS
            // Usuario Administrador
            Usuario admin = new Usuario(null, "11111111-1", "Admin", "MilSabores", "admin@milsabores.cl", "admin", "Central", "Metropolitana", "Santiago", rolAdmin);
            // Usuario Cliente de prueba
            Usuario clienteLeon = new Usuario(null, "22222222-2", "Leon", "Kennedy", "leon@gmail.com", "123", "Calle 1", "Metropolitana", "Puente Alto", rolCliente);
            Usuario clienteAda = new Usuario(null, "3333333-3", "Ada", "Wong", "ada@gmail.com", "123", "Calle 2", "Metropolitana", "Maipú", rolCliente);
            // Usuario Empleado
            Usuario empMarioHugo = new Usuario(null, "44444444-4", "Ernesto Felipe Mario Hugo", "Hermoso Desconocido", "mariohugo@milsabores.cl", "123", "Central", "Metropolitana", "Santiago", rolEmpleado);
            usuarioRepository.saveAll(Arrays.asList(admin, clienteLeon, clienteAda,  empMarioHugo));

            // 3. CATEGORÍAS
            // Creamos las categorías principales basadas en tu JSON original
            Categoria catBizcochuelo = new Categoria(null, "Bizcochuelo", "Deliciosas tortas elaboradas con suaves capas de bizcochuelo y diversos rellenos.", "bizcochuelo.jpg");
            Categoria catBrazoReina = new Categoria(null, "Brazo de Reina", "Rollo tradicional de bizcochuelo relleno con cremas artesanales y decoraciones únicas.", "brazo-de-reina.jpg");
            Categoria catHojarasca = new Categoria(null, "Hojarasca", "Crujientes capas de hojarasca con manjar, crema y mermeladas caseras.", "hojarasca.jpg");
            Categoria catKuchen = new Categoria(null, "Kuchen", "Tartas de fruta y crema inspiradas en la repostería alemana del sur de Chile.", "kuchen.jpg");
            Categoria catDulces = new Categoria(null, "Dulces chilenos", "Pastelitos típicos chilenos elaborados de manera artesanal, generalmente con manjar.", "dulces-chilenos.jpg");
            Categoria catCheesecake = new Categoria(null, "Cheesecake", "Suaves cheesecakes horneados con coberturas de frutas naturales.", "cheesecake.jpg");
            Categoria catMasas = new Categoria(null, "Masas y Galletas", "Galletas y masas dulces ideales para acompañar con té o café.", "masas-galletas.jpg");
            Categoria catEventos = new Categoria(null, "Eventos y Celebraciones", "Productos especiales para cumpleaños, festividades y celebraciones familiares.", "eventos-celebraciones.jpg");

            categoriaRepository.saveAll(Arrays.asList(catBizcochuelo, catBrazoReina, catHojarasca, catKuchen, catDulces, catCheesecake, catMasas, catEventos));

            // 4. PRODUCTOS Y VARIANTES
            // --- BIZCOCHUELO ---
            crearProductoConVariantes("Torta Selva Negra", "Bizcocho negro, crema chantilly y mermelada de guinda ácida.", "torta-selva-negra.jpg", 42000, catBizcochuelo, Arrays.asList(
                    crearVariante("12 personas", 42000, "Peso: 2.2 kg"),
                    crearVariante("20 personas", 70000, "Peso: 3.6 kg"),
                    crearVariante("30 personas", 105000, "Peso: 5.4 kg")
            ));

            crearProductoConVariantes("Torta Tres Leches", "Bizcocho blanco bañado en mezcla de leches, cubierto con merengue.", "torta-tres-leches.jpeg", 42000, catBizcochuelo, Arrays.asList(
                    crearVariante("12 personas", 42000, "Peso: 2.2 kg"),
                    crearVariante("20 personas", 70000, "Peso: 3.6 kg")
            ));

            // --- BRAZO DE REINA ---
            crearProductoConVariantes("Brazo de Reina Lúcuma", "Bizcochuelo blanco con crema de lúcuma.", "brazo-lucuma.jpeg", 42000, catBrazoReina, Arrays.asList(
                    crearVariante("Tamaño único", 42000, "Peso: 1.8 kg")
            ));

            // --- HOJARASCA ---
            crearProductoConVariantes("Torta Hojarasca Manjar", "Capas de hojarasca con manjar.", "torta-hojarasca-nuez.jpg", 42000, catHojarasca, Arrays.asList(
                    crearVariante("20 personas", 70000, "Peso: 3.6 kg")
            ));

            // --- KUCHEN ---
            crearProductoConVariantes("Kuchen de Frutilla", "Kuchen de frutillas frescas.", "kuchen-frutilla.jpg", 35500, catKuchen, Arrays.asList(
                    crearVariante("Tamaño único", 35500, "Peso: 1.6 kg")
            ));

            // --- DULCES CHILENOS ---
            crearProductoConVariantes("Chilenitos (Docena)", "Pastelitos rellenos con manjar.", "chilenitos.jpg", 12000, catDulces, Arrays.asList(
                    crearVariante("Docena", 12000, "Peso: 0.6 kg")
            ));

            // --- CHEESECAKE ---
            crearProductoConVariantes("Cheesecake de Maracuyá", "Queso crema horneado con maracuyá.", "cheesecake-maracuya.jpg", 35000, catCheesecake, Arrays.asList(
                    crearVariante("Tamaño único", 35000, "Peso: 1.9 kg")
            ));

            // 5. ORDEN DE PRUEBA 6 Meses (Hace 2 Meses)
            Producto p1 = productoRepository.findAll().get(0); // Torta Selva Negra
            VarianteProducto v1 = p1.getVariantes().get(0); // 12 personas

            Orden ordenReciente = new Orden();
            ordenReciente.setFecha(LocalDateTime.now().minusMonths(2));
            ordenReciente.setTotal(42000);
            ordenReciente.setEstado("ENTREGADO");
            ordenReciente.setUsuario(clienteLeon);
            ordenReciente.setDetalles(new ArrayList<>());

            DetalleOrden detalle1 = new DetalleOrden(null, 1, 42000, 42000, ordenReciente, p1, v1);
            ordenReciente.getDetalles().add(detalle1);

            ordenRepository.save(ordenReciente);

            //  6. ORDEN ANTIGUA Anual (Hace 10 Meses)
            Orden ordenAntigua = new Orden();
            ordenAntigua.setFecha(LocalDateTime.now().minusMonths(10)); // <--- LA CLAVE ES ESTA FECHA
            ordenAntigua.setTotal(84000); // Monto diferente para distinguirlo
            ordenAntigua.setEstado("ENTREGADO");
            ordenAntigua.setUsuario(clienteAda);
            ordenAntigua.setDetalles(new ArrayList<>());

            // Digamos que compró 2 tortas esa vez
            DetalleOrden detalle2 = new DetalleOrden(null, 2, 42000, 84000, ordenAntigua, p1, v1);
            ordenAntigua.getDetalles().add(detalle2);

            ordenRepository.save(ordenAntigua);

            System.out.println("--- DATOS CARGADOS ---");
        } else {
            System.out.println("--- LA BASE DE DATOS YA TIENE DATOS, OMITE CARGA INICIAL ---");
        }
    }

    // Métodos auxiliares para simplificar la creación de productos y variantes
    private void crearProductoConVariantes(String nombre, String desc, String img, Integer precioBase, Categoria cat, java.util.List<VarianteProducto> variantes) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(desc);
        producto.setImagen(img); // Nota: En el futuro esto será una URL de uploads/
        producto.setPrecioBase(precioBase);
        producto.setCategoria(cat);
        producto.setVariantes(new ArrayList<>());

        for (VarianteProducto v : variantes) {
            v.setProducto(producto);
            producto.getVariantes().add(v);
        }
        productoRepository.save(producto);
    }

    private VarianteProducto crearVariante(String nombre, Integer precio, String info) {
        VarianteProducto v = new VarianteProducto();
        v.setNombre(nombre);
        v.setPrecio(precio);
        v.setInfoNutricional(info);
        v.setStock(100); // Stock inicial por defecto
        return v;
    }
}