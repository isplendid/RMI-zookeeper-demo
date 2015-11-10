package xu.rmi.demo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * @author splendid
 * @date 2015.11.10 
 * @desc rmi interface
 * */

public interface HelloService extends Remote{
	String sayHello(String name) throws RemoteException;

}
