package com.pum.it.pum3d.kernel.service.impl;

import com.pum.it.pum3d.kernel.service.IExternalWebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExternalWebServiceImpl implements IExternalWebService {
  private static final Logger LOGGER = LogManager.getLogger(ExternalWebServiceImpl.class);

  @Override
  public ResponseEntity<String> callAuthenticationService(String authValidationUrl, String jwt) {
    LOGGER.info("AUTH_VALIDATION_URL : " + authValidationUrl);
    // TODO : supprimer commentaire quand le service d'auth sera up
    /*
    String url = AUTH_VALIDATION_URL + "?token=" +jwt;
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForEntity(url, String.class );
     */

    return new ResponseEntity<>("M5779337", HttpStatus.OK);
  }
}
