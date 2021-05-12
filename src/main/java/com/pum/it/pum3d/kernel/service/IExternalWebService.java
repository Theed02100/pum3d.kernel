package com.pum.it.pum3d.kernel.service;

import org.springframework.http.ResponseEntity;

public interface IExternalWebService {

  ResponseEntity<String> callAuthenticationService(String authValidationUrl, String jwt);

}
