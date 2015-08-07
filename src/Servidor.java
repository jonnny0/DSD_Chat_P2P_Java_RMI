
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public class Servidor implements InterfaceServidor {

    private ArrayList<InterfaceCliente> listaClientes;
    private ArrayList<String> nombresClientes;

    public Servidor() throws RemoteException {
        listaClientes = new ArrayList<>();
        nombresClientes = new ArrayList<>();
    }

    /**
     * Registra un nuevo cliente y actualiza el resto
     * @param cliente El cliente a registrar
     * @param nombre El nombre del cliente a registrar
     * @throws RemoteException 
     */
    public void registrar(InterfaceCliente cliente, String nombre) throws RemoteException {
        listaClientes.add(cliente);
        nombresClientes.add(nombre);

        for (InterfaceCliente c : listaClientes) {
            c.actualizarCliente();
        }        
    }

    /**
     * Desconecta un cliente y actualiza el resto
     * @param cliente El cliente a desconectar
     * @param nombre El nombre del cliente a desconectar
     * @throws RemoteException 
     */
    @Override
    public void desconectar(InterfaceCliente cliente, String nombre) throws RemoteException {
        listaClientes.remove(cliente);
        nombresClientes.remove(nombre);

        for (InterfaceCliente c : listaClientes) {
            c.actualizarCliente();
        }        
    }

    /**
     * Devuelve la lista de clientes conectados
     * @return la lista de los clientes conectados
     * @throws RemoteException 
     */
    @Override
    public ArrayList<String> getClientes() throws RemoteException {
        return nombresClientes;
    }
    
    @Override
    public boolean nombreLibre(String nombre) throws RemoteException{
        return !nombresClientes.contains(nombre);
    }
   
    /**
     * Devuelve un cliente con un nombre específico
     * @param nombre El nombre del cliente solicitado
     * @return El cliente solicitado
     * @throws RemoteException 
     */
    @Override
    public InterfaceCliente solicitarCliente(String nombre) throws RemoteException {        
        return listaClientes.get(nombresClientes.indexOf(nombre));
    }

    public static void main(String[] args) throws RemoteException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            InterfaceServidor interfaceServidor = new Servidor();
            InterfaceServidor stub
                    = (InterfaceServidor) UnicastRemoteObject.exportObject(interfaceServidor, 0);
            Registry registry = LocateRegistry.getRegistry();
            String nombre_objeto_remoto = "un_nombre_para_obj_remoto";
            registry.rebind(nombre_objeto_remoto, stub);
            System.out.println("Servidor ejecutándose...");
        } catch (Exception e) {
            System.err.println("SERVIDOR exception:");
            e.printStackTrace();
        }
    }

}
