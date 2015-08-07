
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public class Cliente implements InterfaceCliente {

    private InterfaceServidor instanciaLocalServidor;
    private String nombreCliente;
    private static InterfaceCliente cliente;
    private static VentanaInicio ventanaInicio;

    Cliente(String cliente) {
        nombreCliente = cliente;
    }

    /**
     * Le indica a ventanaInicio que tiene que actualizar la lista de clientes
     * @throws RemoteException 
     */
    @Override
    public void actualizarCliente() throws RemoteException {
        ventanaInicio.actualizarClientes();
    }

    /**
     * Recibe un mensaje desde otro cliente
     * @param nombreCliente El nombre del cliente que manda el mensaje
     * @param cliente El cliente que manda el mensaje
     * @param mensaje El mensaje que se recibe
     * @throws RemoteException 
     */
    @Override
    public void recibirMensaje(String nombreCliente, InterfaceCliente cliente, String mensaje) throws RemoteException {
        ventanaInicio.recibirMensaje(nombreCliente, cliente, mensaje);
    }

    /**
     * Le dice al servidor que se va a desconectar
     */
    public void desconectar() {
        try {
            instanciaLocalServidor.desconectar(cliente, nombreCliente);
        } catch (RemoteException e) {
            System.err.println("CLIENTE exception:" + e.toString());
        }
    }

    public static void main(String args[]) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");

            String nombre_objeto_remoto = "un_nombre_para_obj_remoto";
            InterfaceServidor instanciaLocalServidor = (InterfaceServidor) registry.lookup(nombre_objeto_remoto);

            String nombreCliente = JOptionPane.showInputDialog("Escribe tu nombre:");
            while (nombreCliente.equals("") || !instanciaLocalServidor.nombreLibre(nombreCliente)) {
                nombreCliente = JOptionPane.showInputDialog("Escribe tu nombre:");
            }

            cliente = new Cliente(nombreCliente);

            InterfaceCliente stub
                    = (InterfaceCliente) UnicastRemoteObject.exportObject(cliente, 0);

            registry.rebind(nombreCliente, stub);

            ventanaInicio = new VentanaInicio(nombreCliente, cliente, instanciaLocalServidor);
            ventanaInicio.setVisible(true);

            instanciaLocalServidor.registrar(stub, nombreCliente);

        } catch (Exception e) {
            System.err.println("CLIENTE exception:" + e.toString());
        }

    }

}
