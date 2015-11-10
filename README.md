hello, my first github

1.RMI 服务与调用示例</br>
发布一个RMI服务：</br>
  >定义一个RMI接口 :HelloSerive</br>
  >编写RMI接口的实现类：HelloServiceImpl</br>
  >通过JNDI发布RMI服务：RmiSever</br>
  >调用RMI服务：RmiClient</br>
  rmi://localhost:1099/xu.rmi.demo.HelloServiceImpl</br>
  
2.JNDI 就是一个注册表，服务端将服务对象放入到注册表中，客户端从注册表中获取服务对象。</br>
在服务端我们发布了 RMI 服务，并在 JNDI 中进行了注册，此时就在服务端创建了一个 Skeleton（骨架），</br>
当客户端第一次成功连接 JNDI 并获取远程服务对象后，立马就在本地创建了一个 Stub（存根）。</br>
远程通信实际上是通过 Skeleton 与 Stub 来完成的，数据是基于 TCP/IP 协议，在“传输层”上发送的。</br>
理论上 RMI 一定比 WebService 要快，毕竟 WebService 是基于 HTTP 的，而HTTP所携 带的数据是 通过</br> “应用层”来传输的，传输层较应用层更为底层，越底层越快</br>

通过Zookeeper充当服务注册吧（Service Registry),让多个服务提供者（Service Provider)形成一个集群，</br>
让服务消费者（Service Consumer)通过服务注册表获取具体的服务访问地址（也就是RMI服务地址）去访问具</br>
体的</br>
服务提供者。</br>
>服务提供者： ServiceProvider</br>
>服务消费者： ServiceConsumer</br>
>发布服务：  Server</br>
>调用服务：  Client</br>
