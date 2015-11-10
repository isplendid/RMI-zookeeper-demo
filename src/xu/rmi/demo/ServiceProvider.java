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
	
	
	 // 用于等待 SyncConnected 事件触发后继续执行当前线程
    private CountDownLatch latch = new CountDownLatch(1);
    // 发布 RMI 服务并注册 RMI 地址到 ZooKeeper 中
    
    public void publish(Remote remote, String host, int port){
    	LOG.debug("publish start...");
    	String url = publishService(remote,host, port);  // 发布 RMI 服务并返回 RMI 地址
    	if(url !=null){
    		ZooKeeper zk = connectServer();   // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
    		if(zk !=null){
    			createNode(zk, url);   // 创建 ZNode 并将 RMI 地址放入 ZNode 上
    			System.out.println("create Node success");
    		}
    		
    	}
    }
    
    //发布RMI服务
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
    
    
    // 连接 ZooKeeper 服务器
    public  ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            
            System.out.println("connect sever is succesful!");
            latch.await(); // 使当前线程处于等待状态
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }
        return zk;
    }
    
    // 创建 ZNode
    private void createNode(ZooKeeper zk, String url) {
        try {
            byte[] data = url.getBytes();
            //String path0 = zk.create(Constant.ZK_REGISTRY_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            //System.out.println("path0 "+path0);
            String path = zk.create(Constant.ZK_PROVIDER_PATH, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL); // 创建一个临时性且有序的 ZNode
            //String path0 = zk.create("/zk", data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            //String path = zk.create("/zk/xu", data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("path is: "+path);
            LOG.debug("create zookeeper node ({} => {})", path, url);
        } catch (KeeperException | InterruptedException e) {
            LOG.error("", e);
        }
    }

}
