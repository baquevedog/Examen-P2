import java.util.ArrayList;

public class InventarioProductos {
    //zambrano jose
    //Quevedo Bryan
    private ArrayList<Producto> productos;

    public InventarioProductos() {
        productos = new ArrayList<>();
        cargarDatosIniciales();
    }

    // a) Agregar 6 productos con ID únicos en desorden
    private void cargarDatosIniciales() {
        agregarProducto(new Producto(15, "Router TP-Link", "ROUTER", 10, 120.50f));
        agregarProducto(new Producto(3, "Switch Cisco", "SWITCH", 5, 350.00f));
        agregarProducto(new Producto(27, "Expansor Xiaomi", "EXPANSOR", 20, 80.99f));
        agregarProducto(new Producto(9, "Router Huawei", "ROUTER", 8, 210.00f));
        agregarProducto(new Producto(1, "Switch D-Link", "SWITCH", 12, 150.75f));
        agregarProducto(new Producto(18, "Expansor TP-Link", "EXPANSOR", 15, 95.30f));
        // IDs: 15,3,27,9,1,18 → desordenados
    }

    // Asegurar que no se inserten productos con IDs repetidos
    public boolean agregarProducto(Producto nuevo) {
        if (buscarPorIdLineal(nuevo.getId()) != null) {
            return false; // ID repetido
        }
        productos.add(nuevo);
        return true;
    }

    // Devolver todos los productos (para mostrarlos en los JList)
    public ArrayList<Producto> getProductos() {
        return productos;
    }

    // --------- BÚSQUEDAS ----------

    // Búsqueda lineal por ID
    public Producto buscarPorIdLineal(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // Búsqueda binaria por ID (requiere lista ordenada por ID)
    public Producto buscarPorIdBinaria(int id) {
        // Primero ordenamos por ID usando nuestro propio algoritmo
        ordenarPorIdAscendente();

        int inicio = 0;
        int fin = productos.size() - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            Producto pMedio = productos.get(medio);

            if (pMedio.getId() == id) {
                return pMedio;
            } else if (id < pMedio.getId()) {
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }
        return null;
    }

    // Búsqueda por categoría (lineal)
    public ArrayList<Producto> buscarPorCategoria(String categoria) {
        ArrayList<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Búsqueda por nombre (lineal)
    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    // Búsqueda lineal del menor precio en todo el inventario
    public Producto buscarMenorPrecioLineal() {
        if (productos.isEmpty()) return null;
        Producto menor = productos.get(0);
        for (Producto p : productos) {
            if (p.getPrecio() < menor.getPrecio()) {
                menor = p;
            }
        }
        return menor;
    }

    // --------- ORDENAMIENTOS (sin Collections.sort) ----------

    // Ordenar por ID ascendente (para usar en la búsqueda binaria)
    private void ordenarPorIdAscendente() {
        // Bubble sort
        for (int i = 0; i < productos.size() - 1; i++) {
            for (int j = 0; j < productos.size() - 1 - i; j++) {
                if (productos.get(j).getId() > productos.get(j + 1).getId()) {
                    Producto tmp = productos.get(j);
                    productos.set(j, productos.get(j + 1));
                    productos.set(j + 1, tmp);
                }
            }
        }
    }

    // Ordenar productos de una categoría por precio descendente
    public ArrayList<Producto> ordenarPorPrecioDescCategoria(String categoria) {
        ArrayList<Producto> listaCat = buscarPorCategoria(categoria);

        // Bubble sort descendente por precio
        for (int i = 0; i < listaCat.size() - 1; i++) {
            for (int j = 0; j < listaCat.size() - 1 - i; j++) {
                if (listaCat.get(j).getPrecio() < listaCat.get(j + 1).getPrecio()) {
                    Producto tmp = listaCat.get(j);
                    listaCat.set(j, listaCat.get(j + 1));
                    listaCat.set(j + 1, tmp);
                }
            }
        }

        return listaCat;
    }

    // --------- ACTUALIZAR PRODUCTO (MÉTODO QUE TE FALTABA) ----------

    public boolean actualizarProducto(int id, String nombre, String categoria,
                                      int cantidad, float precio) {
        // Puedes cambiar a buscarPorIdBinaria(id) si quieres probar la binaria
        Producto p = buscarPorIdLineal(id);
        if (p == null) {
            return false;
        }
        p.setNombre(nombre);
        p.setCategoria(categoria);
        p.setCantidad(cantidad);
        p.setPrecio(precio);
        return true;
    }
}
