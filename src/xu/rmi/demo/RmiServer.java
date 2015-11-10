package xu.rmi.demo;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/*
 * @author splendid
 * @date 2015.11.10 
 * @desc publish service
 * */
public class RmiServer {
	public static void main(String[] args) throws Exception{
		int port = 1099;
		String url = "rmi://localhost:1099/xu.rmi.demo.HelloServiceImpl";
		LocateRegistry.createRegistry(port);
		Naming.rebind(url, new HelloServiceImpl());
	}

}
