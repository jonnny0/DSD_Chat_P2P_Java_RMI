
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public class VentanaInicio extends javax.swing.JFrame {

    private final InterfaceCliente cliente;
    private final InterfaceServidor servidor;
    private String nombreCliente;
    private ArrayList<VentanaCliente> ventanasClientesP2P;
    private ArrayList<String> nombresClientesP2P;

    public VentanaInicio(String nombreCliente, InterfaceCliente cliente, InterfaceServidor servidor) {
        initComponents();
        this.cliente = cliente;
        this.servidor = servidor;
        this.nombreCliente = nombreCliente;
        ventanasClientesP2P = new ArrayList<>();
        nombresClientesP2P = new ArrayList<>();

        this.setTitle("Cliente: " + nombreCliente);

        //listener para que al cerrar la ventana ejecute el método salir()
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    salir();
                } catch (RemoteException ex) {
                    Logger.getLogger(VentanaCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //listener para escuchar el doble click sobre un elemento de la lista de clientes
        clickSobreCliente();
    }

    /**
     * Al pulsar sobre un elemento de la lista de clientes
     */
    public void clickSobreCliente() {
        usuariosConectados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                //Si se hace doble click
                if (evt.getClickCount() == 2) {
                    String clienteSeleccionado = usuariosConectados.getSelectedValue().toString();
                    
                    //si no me he seleccionado a mí mismo y no había ya una ventana abierta con ese cliente
                    if (!clienteSeleccionado.equals(nombreCliente) && !nombresClientesP2P.contains(clienteSeleccionado)) {
                        try {
                            //crear ventana nueva y mostrarla
                            nuevaConversacion(clienteSeleccionado, servidor.solicitarCliente(clienteSeleccionado));                            
                        } catch (RemoteException ex) {
                            Logger.getLogger(VentanaInicio.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    /**
     * Actualiza la lista de clientes conectados
     * @throws RemoteException 
     */
    public void actualizarClientes() throws RemoteException {
        DefaultListModel modelo = new DefaultListModel();

        for (String str : servidor.getClientes()) {
            modelo.addElement(str);
        }
        usuariosConectados.setModel(modelo);
    }

    /**
     * Le indica al servidor que se va a desconectar y muestra mensaje de salida
     * @throws RemoteException 
     */
    public void salir() throws RemoteException {
        try {
            servidor.desconectar(cliente, nombreCliente);
        } catch (RemoteException e) {
            System.err.println("VentanaInicio exception:" + e.toString());
        }
        JOptionPane.showMessageDialog(null, "¡Hasta pronto " + nombreCliente + "!");
        System.exit(0);
    }

    /**
     * Abre una nueva ventana de conversación con un cliente remoto
     * @param nombreClienteRemoto El nombre del cliente con el que queremos iniciar una conversación
     * @param clienteRemoto El cliente con el que queremos iniciar una conversación
     * @throws RemoteException 
     */
    public void nuevaConversacion(String nombreClienteRemoto, InterfaceCliente clienteRemoto) throws RemoteException {
        //creo una nueva ventana con el cliente que pincha y sobre el seleccionado
        VentanaCliente nuevaVentanaCliente = new VentanaCliente(nombreCliente, cliente, nombreClienteRemoto, clienteRemoto, VentanaInicio.this);

        //añado la nueva ventana al array
        ventanasClientesP2P.add(nuevaVentanaCliente);

        //añado el nombre a la lista de clientes P2P
        nombresClientesP2P.add(nombreClienteRemoto);

        //muestro la ventana
        ventanasClientesP2P.get(ventanasClientesP2P.indexOf(nuevaVentanaCliente)).setVisible(true);
    }

    /**
     * Termina una conversación con un cliente, borrando sus datos de los arrayList
     * @param nombreCliente 
     */
    public void cerrarConversacion(String nombreCliente) {
        ventanasClientesP2P.remove(nombresClientesP2P.indexOf(nombreCliente));
        nombresClientesP2P.remove(nombreCliente);
    }

    /**
     * Recibe un mensaje de un cliente remoto, abriendo una nueva conversación si no la tenía o actualizando la lista de mensajes
     * @param nombreClienteRemoto Nombre del cliente del que se recibe el mensaje
     * @param clienteRemoto Cliente del que se recibe el mensaje
     * @param mensaje El mensaje recibido
     * @throws RemoteException 
     */
    public void recibirMensaje(String nombreClienteRemoto, InterfaceCliente clienteRemoto, String mensaje) throws RemoteException {
        //si no tenía la venta abierta
        if (!nombresClientesP2P.contains(nombreClienteRemoto)) {
            nuevaConversacion(nombreClienteRemoto, clienteRemoto);
        }
        ventanasClientesP2P.get(nombresClientesP2P.indexOf(nombreClienteRemoto)).actualizarMensajes(mensaje);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        usuariosConectados = new javax.swing.JList();
        etiquetaClientesConectados = new javax.swing.JLabel();
        botonDesconectar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane.setViewportView(usuariosConectados);

        etiquetaClientesConectados.setText("Clientes conectados:");

        botonDesconectar.setText("Desconectar");
        botonDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDesconectarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(etiquetaClientesConectados)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(botonDesconectar)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etiquetaClientesConectados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonDesconectar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDesconectarActionPerformed
        try {
            salir();
        } catch (RemoteException ex) {
            Logger.getLogger(VentanaInicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonDesconectarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonDesconectar;
    private javax.swing.JLabel etiquetaClientesConectados;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JList usuariosConectados;
    // End of variables declaration//GEN-END:variables

}
