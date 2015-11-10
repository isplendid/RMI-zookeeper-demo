package xu.rmi.demo;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ServiceProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServiceProvider.class);
	
	
	 // ���ڵȴ� SyncConnected �¼����������ִ�е�ǰ�߳�
    private CountDownLatch latch = new CountDownLatch(1);
    // ���� RMI ����ע�� RMI ��ַ�� ZooKeeper ��
    
    public void publish(Remote remote, String host, int port){
    	LOG.debug("publish start...");
    	String url = publishService(remote,host, port);  // ���� RMI ���񲢷��� RMI ��ַ
    	if(url !=null){
    		ZooKeeper zk = connectServer();   // ���� ZooKeeper ����������ȡ ZooKeeper ����
    		if(zk !=null){
    			createNode(zk, url);   // ���� ZNode ���� RMI ��ַ���� ZNode ��
    			System.out.println("create Node success");
    		}
    		
    	}
    }
    
    //����RMI����
    private String publishService(Remote remote, String host, int port){
    	String  url = null;
    	try {
			url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getName());
			LocateRegistry.createRegistry(port);
			Naming.rebind(url, remote);
			LOG.debug("public rmi service (url:{})", url);
		} catch (Exception e) {
			LOG.error("",e);
		}
    	
    	return url;
    }
    
    
    // ���� ZooKeeper ������
    public  ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // ���ѵ�ǰ����ִ�е��߳�
                    }
                }
            });
            
            System.out.println("connect sever is succesful!");
            latch.await(); // ʹ��ǰ�̴߳��ڵȴ�״̬
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }
        return zk;
    }
    
    // ���� ZNode
    private void createNode(ZooKeeper zk, String url) {
        try {
            byte[] data = url.getBytes();
            //String path0 = zk.create(Constant.ZK_REGISTRY_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            //System.out.println("path0 "+path0);
            String path = zk.create(Constant.ZK_PROVIDER_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); // ����һ����ʱ��������� ZNode
            //String path0 = zk.create("/zk", data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            //String path = zk.create("/zk/xu", data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("path is: "+path);
            LOG.debug("create zookeeper node ({} => {})", path, url);
        } catch (KeeperException | InterruptedException e) {
            LOG.error("", e);
        }
    }

}
