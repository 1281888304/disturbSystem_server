package RabbitMQ;

import com.rabbitmq.client.Channel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RabbitMQChannelPool {

  private final BlockingQueue<Channel> pool;
  // fixed size pool
  private int capacity;
  // used to ceate channels
  private RabbitMQChannelFactory factory;

  public RabbitMQChannelPool(int size, RabbitMQChannelFactory factory) {
    this.capacity = size;
    this.factory = factory;
    pool=new LinkedBlockingQueue<>(capacity);
    for(int i=0;i<size;i++){
      Channel channel;
      try {
        channel=factory.create();
        pool.put(channel);
      } catch (Exception e) {
        System.out.println("error on create channel in pool initilization");
      }
    }
  }

  public Channel getChannel(){
    try {
      return pool.take();
    } catch (InterruptedException e) {
      throw new RuntimeException("Error: no channels available" + e.toString());
    }
  }

  public void putChannelBackToPool(Channel channel){
    if(channel!=null){
      pool.add(channel);
    }
  }

  public BlockingQueue<Channel> getPool() {
    return pool;
  }
}
