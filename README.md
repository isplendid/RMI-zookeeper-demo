hello, my first github

1.RMI 服务与调用示例
发布一个RMI服务：
  >定义一个RMI接口 :HelloSerive
  >编写RMI接口的实现类：HelloServiceImpl
  >通过JNDI发布RMI服务：RmiSever
  >调用RMI服务：RmiClient
  rmi://localhost:1099/xu..HelloServiceImpl
  
2.JNDI 就是一个注册表，服务端将服务对象放入到注册表中，客户端从注册表中获取服务对象。
在服务端我们发布了 RMI 服务，并在 JNDI 中进行了注册，此时就在服务端创建了一个 Skeleton（骨架），
当客户端第一次成功连接 JNDI 并获取远程服务对象后，立马就在本地创建了一个 Stub（存根）。
，远程通信实际上是通过 Skeleton 与 Stub 来完成的，数据是基于 TCP/IP 协议，在“传输层”上发送的。
理论上 RMI 一定比 WebService 要快，毕竟 WebService 是基于 HTTP 的，而 HTTP 所携带的数据是通过“应用层”来传输的，传输层较应用层更为底层，越底层越快

通过Zookeeper充当服务注册吧（Service Registry),让多个服务提供者（Service Provider)形成一个集群，让
服务消费者（Service Consumer)通过服务注册表获取具体的服务访问地址（也就是RMI服务地址）去访问具体的
服务提供者。
>服务提供者： ServiceProvider
>服务消费者： ServiceConsumer
>发布服务：  Server
>调用服务：  Client
