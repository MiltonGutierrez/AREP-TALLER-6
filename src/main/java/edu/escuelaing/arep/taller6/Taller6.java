package edu.escuelaing.arep.taller6;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author milton.gutierrez-l
 */
@SpringBootApplication
public class Taller6 {
    public static void main(String[] args) { 
        SpringApplication app = new SpringApplication(Taller6.class); 
        app.setDefaultProperties(Collections.singletonMap("server.port", getPort())); 
        app.run(args); 
    }

    static String getPort(){
        String port = System.getenv("PORT");
        if(port != null){
            return port;
        }
        return "50001";
    }
}
