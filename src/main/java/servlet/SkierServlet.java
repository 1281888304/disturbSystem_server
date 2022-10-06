package servlet;

import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import model.LifeRide;
import model.Skier;
import com.alibaba.fastjson.JSON;
import response.ResponseResult;

@WebServlet(name = "servlet.SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

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
  public void doPost(HttpServletRequest request, HttpServletResponse response)
       {
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
      try{
        while ((s = request.getReader().readLine()) != null) {
          sb.append(s);
        }
      }catch (IOException e){
        e.printStackTrace();
      }
      //valid the lifeRide object
      LifeRide lifeRide = gson.fromJson(sb.toString(), LifeRide.class);
      if (lifeRide.getTime() == null || lifeRide.getLiftID() == null ||
          lifeRide.getLiftID() < 1 || lifeRide.getLiftID() > 40 ||
          lifeRide.getTime() < 1 || lifeRide.getTime() > 360) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return;
      }
      String[] paths = request.getPathInfo().split("/");
      if (!valid(paths)) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        //out.println("path 0->"+paths[0]);
        return;
      }else{
        response.setStatus(HttpServletResponse.SC_OK);
      }
  }

  public boolean valid(String[] paths) {
    if (paths.length != 8) {
      return false;
    }

    if(!paths[2].equals("seasons")){
      return false;
    }
    if(!paths[4].equals("days")){
      return false;
    }
    if(!paths[6].equals("skiers")){
      return false;
    }
    if(Integer.parseInt(paths[1])<1 || Integer.parseInt(paths[1])>10){
      return false;
    }
//    if(!paths[3].equals("2022")){
//      return false;
//    }
//    if(!paths[5].equals("1")){
//      return false;
//    }
//    if(Integer.parseInt(paths[7])<1 || Integer.parseInt(paths[7])>100000){
//      return false;
//    }
    return true;
  }


}
