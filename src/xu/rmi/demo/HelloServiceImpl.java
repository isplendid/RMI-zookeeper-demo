package xu.rmi.demo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * @author splendid
 * @date 2015.11.10 
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService{

	protected HelloServiceImpl() throws RemoteException {
		super();
		
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return String.format("Hello %s", name);
	}
  
}
