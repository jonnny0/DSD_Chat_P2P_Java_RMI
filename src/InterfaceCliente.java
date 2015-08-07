
import java.rmi.RemoteException;

/**
 *
 * @author Antonio Espinosa Jiménez
 */
public interface InterfaceCliente extends java.rmi.Remote {
    public void actualizarCliente() throws RemoteException;
       
    public void recibirMensaje(String nombreCliente, InterfaceCliente cliente, String mensaje) throws RemoteException;    
}
