package com.pum.it.pum3d.kernel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  /**
   * Lancement de l'application.
   *
   * @param args arguments pour lancer le programme
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * Permet l'utilisation de l'app en mode WAR.
   *
   * @param application Application
   * @return Application
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }
}
