package servlet;

import RabbitMQ.RabbitMQChannelFactory;
import RabbitMQ.RabbitMQChannelPool;
import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import model.LiftRide;
import model.Skier;

@WebServlet(name = "servlet.SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  private static final String HOST = "172.31.24.63";

  private static final String QUEUE_NAME = "skier_queue";

  private static final int TOMCAT_THREADS=30;

  private  RabbitMQChannelPool pool;

  @Override
  public void init(){

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(HOST);
    factory.setPort(5672);
//    factory.setUsername("guest");
//    factory.setPassword("guest");
    final Connection RMQconn;
    try {
      RMQconn = factory.newConnection();
    } catch (IOException  | TimeoutException e) {
      throw new RuntimeException("Error: servlet init " + e.toString());
    }
    RabbitMQChannelFactory MQFactory=new RabbitMQChannelFactory(RMQconn);
    pool= new RabbitMQChannelPool (TOMCAT_THREADS,MQFactory);


  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return true;
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    //set response and request format
    response.setCharacterEncoding("UTF-8");
    try {
      request.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    Gson gson = new Gson();

    StringBuilder sb = new StringBuilder();
    String s;
    try {
      while ((s = request.getReader().readLine()) != null) {
        sb.append(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    //valid the lifeRide object
    LiftRide liftRide = gson.fromJson(sb.toString(), LiftRide.class);
    if (liftRide.getTime() == null || liftRide.getLiftID() == null ||
        liftRide.getLiftID() < 1 || liftRide.getLiftID() > 40 ||
        liftRide.getTime() < 1 || liftRide.getTime() > 360) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      return;
    }
    String[] paths = request.getPathInfo().split("/");
    if (!valid(paths)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      //out.println("path 0->"+paths[0]);
      return;
    }
    //write to the rabbit mq after validation
    String exchangeName = "fanout_exchange";
    Skier skier=new Skier();
    skier.setLiftRide(liftRide);
    skier.setSkierID(Integer.parseInt(paths[7]));
    skier.setSeasonID(paths[3]);
    skier.setDayID(paths[5]);
    skier.setResortID(Integer.parseInt(paths[1]));
    try {
      //get channel from pool
      Channel channel=pool.getChannel();
      //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
      String objectMessage = gson.toJson(skier);
      //direct mq
      //channel.basicPublish("", QUEUE_NAME, null, objectMessage.getBytes("UTF-8"));

      //fanout mq
      channel.basicPublish(exchangeName,"",null,objectMessage.getBytes("UTF-8"));

      //put the channel back to pool
      pool.putChannelBackToPool(channel);
      response.getWriter().write("successful sent message to queue");
    } catch (IOException e) {
      Logger.getLogger(SkierServlet.class.getName()).log(Level.INFO,"problem in sending message to queue",e);
    }


  }

  public boolean valid(String[] paths) {
    if (paths.length != 8) {
      return false;
    }

    if (!paths[2].equals("seasons")) {
      return false;
    }
    if (!paths[4].equals("days")) {
      return false;
    }
    if (!paths[6].equals("skiers")) {
      return false;
    }
    if (Integer.parseInt(paths[1]) < 1 || Integer.parseInt(paths[1]) > 10) {
      return false;
    }
    if (!paths[3].equals("2022")) {
      return false;
    }
    if (!paths[5].equals("1")) {
      return false;
    }
    if (Integer.parseInt(paths[7]) < 1 || Integer.parseInt(paths[7]) > 100000) {
      return false;
    }
    return true;
  }

}
