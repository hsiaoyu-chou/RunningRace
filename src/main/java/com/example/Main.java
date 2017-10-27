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

      //r1.setName("runner1");
      //r2.setName("runner2");
      //r3.setName("runner3");

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

}
