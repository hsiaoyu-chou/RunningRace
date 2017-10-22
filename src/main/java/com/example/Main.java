/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
@SpringBootApplication
public class Main {
    private Running r1;
    private Running r2;
    private Running r3;
    private int total = 100;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }


  @RequestMapping("/")
  String index(Map<String, Object> model) {

      model.put("message", "please press the button to start");
      model.put("running_status", false);
      model.put("runner1", 0);
      model.put("runner2", 0);
      model.put("runner3", 0);
      model.put("total", total);

      return "index";
  }


  @RequestMapping(value = "/", method = RequestMethod.POST)
  String racing(Map<String, Object> model,
                @RequestParam(name = "running_status") boolean running_status,
                @RequestParam(name = "runner1") int runner1,
                @RequestParam(name = "runner2") int runner2,
                @RequestParam(name = "runner3") int runner3){

      r1 = new Running();
      r2 = new Running();
      r3 = new Running();

      r1.setName("runner1");
      r2.setName("runner2");
      r3.setName("runner3");


      if(running_status == false){

          r1.setValue(0);
          r2.setValue(0);
          r3.setValue(0);
      }
      else {
          r1.setValue(runner1);
          r2.setValue(runner2);
          r3.setValue(runner3);
      }

      r1.start();
      r2.start();
      r3.start();

      running_status = true;

      Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

      Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
      //System.out.println("threadSet.size() :" + threadSet.size());

      try {
          r1.join();
          r2.join();
          r3.join();
      } catch (InterruptedException e) {
          System.out.println("thread is broken" + e.getMessage());
      }

      runner1 = r1.getValue();
      runner2 = r2.getValue();
      runner3 = r3.getValue();

      //System.out.println("values :" + runner1 + " " + runner2 + " " + runner3);

      if(runner1 == total || runner2 == total || runner3 == total){

          String winner = "";
          boolean multiWinner = false;
          if(runner1 == total){
              winner = "runner1";
              multiWinner = true;
          }
          if(runner2 == total){
            if(multiWinner) winner = winner + " and ";
            winner = winner + "runner2";
          }
          if(runner3 == total){
              if(multiWinner) winner = winner + " and ";
              winner = winner + "runner3";
          }
          String msg = "the race is now finished.<br/>";
          msg = msg + "winner : " + winner + ".<br />";
          msg = msg +"please press the button to start again.";
          model.put("message", msg);
          model.put("running_status", false);
      }
      else {
          model.put("message", "the race is now processing");
          model.put("running_status", running_status);
      }

      model.put("runner1", runner1);
      model.put("runner2", runner2);
      model.put("runner3", runner3);
      model.put("total", total);

      return "index";
  }
/*
  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

  public void sendGet(String url)throws Exception {

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    System.out.println(response.toString());
  }

  public String sendPost(String action, String url, String data, String Dropbox_API_Arg, byte[] bytes) throws Exception {

    //String url = "https://selfsolve.apple.com/wcResults.do";
    URL obj = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    //add reuqest header
    con.setRequestMethod("POST");

    if(action == "get_account" && user.access_tkn != null){
      System.out.println("\nsendPost action : get_account");
      System.out.println("user.access_tkn != null");
      con.setRequestProperty("Authorization", "Bearer " + user.access_tkn);
      con.addRequestProperty("Content-Type", "application/json");
    }
    else if(action == "upload"){
      System.out.println("\nsendPost action : upload");
      con.setRequestProperty("Authorization", "Bearer " + user.access_tkn);
      con.setRequestProperty("Content-Type", "application/octet-stream");
      con.setRequestProperty("Dropbox-API-Arg", "{\"path\":\"/"+ Dropbox_API_Arg +"\"}");
    }
    else System.out.println("\nsendPost action : token");

    System.out.println("getRequestProperties : " + con.getRequestProperties().toString());

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    if(data != null) wr.writeBytes(data);
    if(bytes != null) wr.write(bytes);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    String responseMsg = con.getResponseMessage();

    System.out.println("\nSending 'POST' request to URL : " + url);
    //System.out.println("Post parameters : " + data);
    System.out.println("Response Code : " + responseCode);
    System.out.println("Response Meg : " + responseMsg);

    BufferedReader in;
    if(responseCode == 200) in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    else in = new BufferedReader(new InputStreamReader(con.getErrorStream()));

    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    System.out.println(response.toString());
    return response.toString();

  }
*/
}
