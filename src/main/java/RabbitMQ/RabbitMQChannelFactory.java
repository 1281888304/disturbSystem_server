package RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
public class RabbitMQChannelFactory extends BasePooledObjectFactory<Channel>{

  private final Connection connection;
  // used to count created channels for debugging
  private int count;

  public RabbitMQChannelFactory(Connection connection) {
    this.connection = connection;
    this.count=0;
  }

  @Override
  public synchronized Channel create() throws Exception {
    count++;
    Channel channel=connection.createChannel();
    return channel;
  }

  @Override
  public PooledObject<Channel> wrap(Channel channel) {

    return new DefaultPooledObject<>(channel);
  }

  public int getChannelCount(){
    return count;
  }
}
