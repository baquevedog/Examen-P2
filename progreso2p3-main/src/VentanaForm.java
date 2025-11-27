import javax.swing.*;
import java.util.ArrayList;

public class VentanaForm extends JFrame {
    //zambrano jose
    //Quevedo Bryan


    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;


    private JList<String> listTodos;
    private JButton btnMostrarTodos;
    private JSpinner spIdEditar;
    private JTextField txtNombreEditar;
    private JComboBox<String> cbCategoriaEditar;
    private JSpinner spCantidadEditar;
    private JTextField txtPrecioEditar;
    private JButton btnBuscarId;
    private JButton btnGuardarCambios;

    // TAB 2 - Buscar / Ordenar
    private JComboBox<String> cbCategoriaOrden;
    private JButton btnOrdenarCategoria;
    private JTextField txtBuscarNombre;
    private JButton btnBuscarNombre;
    private JButton btnMostrarAlmacenados;
    private JList<String> listResultados;

    // ==== CAMPOS DE LÓGICA (NO VAN EN EL .form) ====
    private DefaultListModel<String> modeloListaTodos;
    private DefaultListModel<String> modeloListaResultados;
    private InventarioProductos inventario;

    public VentanaForm() {
        // Panel principal del formulario
        setContentPane(mainPanel);
        setTitle("Inventario de Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Lógica de inventario
        inventario = new InventarioProductos();

        // Modelos para las listas
        modeloListaTodos = new DefaultListModel<>();
        listTodos.setModel(modeloListaTodos);

        modeloListaResultados = new DefaultListModel<>();
        listResultados.setModel(modeloListaResultados);

        // Combos de categorías (si no los llenaste en el diseñador)
        if (cbCategoriaEditar.getItemCount() == 0) {
            cbCategoriaEditar.addItem("ROUTER");
            cbCategoriaEditar.addItem("SWITCH");
            cbCategoriaEditar.addItem("EXPANSOR");
        }

        if (cbCategoriaOrden.getItemCount() == 0) {
            cbCategoriaOrden.addItem("ROUTER");
            cbCategoriaOrden.addItem("SWITCH");
            cbCategoriaOrden.addItem("EXPANSOR");
        }

        // Listeners de botones
        btnMostrarTodos.addActionListener(e -> cargarListaTodos());
        btnBuscarId.addActionListener(e -> buscarProductoParaEditar());
        btnGuardarCambios.addActionListener(e -> guardarCambiosProducto());

        btnOrdenarCategoria.addActionListener(e -> ordenarPorCategoria());
        btnBuscarNombre.addActionListener(e -> buscarPorNombre());
        btnMostrarAlmacenados.addActionListener(e -> cargarListaResultadosTodos());

        pack();
    }

    // ================== MÉTODOS DE APOYO ==================

    // Cargar todos los productos en la lista de la pestaña 1
    private void cargarListaTodos() {
        modeloListaTodos.clear();
        for (Producto p : inventario.getProductos()) {
            modeloListaTodos.addElement(p.toString());
        }
    }

    // Buscar producto por ID y cargarlo en los campos de edición
    private void buscarProductoParaEditar() {
        int id = (int) spIdEditar.getValue();
        // puedes cambiar a buscarPorIdBinaria(id) si quieres probar la binaria
        Producto p = inventario.buscarPorIdLineal(id);

        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró producto con ID " + id,
                    "Búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        txtNombreEditar.setText(p.getNombre());
        cbCategoriaEditar.setSelectedItem(p.getCategoria());
        spCantidadEditar.setValue(p.getCantidad());
        txtPrecioEditar.setText(String.valueOf(p.getPrecio()));
    }

    // Guardar cambios del producto editado
    private void guardarCambiosProducto() {
        try {
            int id = (int) spIdEditar.getValue();
            String nombre = txtNombreEditar.getText().trim();
            String categoria = (String) cbCategoriaEditar.getSelectedItem();
            int cantidad = (int) spCantidadEditar.getValue();
            float precio = Float.parseFloat(txtPrecioEditar.getText().trim());

            boolean actualizado = inventario.actualizarProducto(
                    id, nombre, categoria, cantidad, precio
            );

            if (actualizado) {
                JOptionPane.showMessageDialog(this,
                        "Datos actualizados correctamente.",
                        "Edición",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarListaTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró producto con ese ID.",
                        "Edición",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ordenar productos de una categoría por precio descendente
    private void ordenarPorCategoria() {
        String categoria = (String) cbCategoriaOrden.getSelectedItem();
        ArrayList<Producto> listaOrdenada =
                inventario.ordenarPorPrecioDescCategoria(categoria);

        modeloListaResultados.clear();
        for (Producto p : listaOrdenada) {
            modeloListaResultados.addElement(p.toString());
        }

        if (listaOrdenada.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en la categoría seleccionada.",
                    "Ordenar",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Buscar producto por nombre
    private void buscarPorNombre() {
        String nombre = txtBuscarNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un nombre a buscar.",
                    "Buscar",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = inventario.buscarPorNombre(nombre);

        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró producto con ese nombre.",
                    "Buscar",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Producto encontrado:\n" + p.toString(),
                    "Buscar",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Mostrar todos los productos en la lista de resultados (pestaña 2)
    private void cargarListaResultadosTodos() {
        modeloListaResultados.clear();
        for (Producto p : inventario.getProductos()) {
            modeloListaResultados.addElement(p.toString());
        }
    }

    // ================== MAIN ==================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaForm v = new VentanaForm();
            v.setVisible(true);
        });
    }
}
