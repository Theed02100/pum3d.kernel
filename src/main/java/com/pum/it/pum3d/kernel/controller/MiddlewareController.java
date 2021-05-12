package com.pum.it.pum3d.kernel.controller;

import com.pum.it.pum3d.kernel.service.IMiddlewareService;
import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/routing")
public class MiddlewareController {

  @Autowired
  private IMiddlewareService middlewareService;

  /**
   * Controller global pour le routage des calls depuis le front.
   *
   * <p>
   * Tous les calls HTTP passent par cette méthode qui ne fait que
   * passe-plat. Le body des réponses seront formatté en String.
   * </p>
   *
   * @param body
   *     (optionnel) Body de la requête d'origine
   * @param method
   *     Méthode http utilisée
   * @param request
   *     Requête complète d'origine
   * @param targetId
   *     Module visé par la requête
   *
   * @return ResponseEntity : Réponse du service appelé
   * @throws HttpResponseException
   *     - 500 si URI invalide ou erreur interne
   *     - 404 si le targetId ne correspond pas à un module en BdD
   */
  @RequestMapping(
      path = "/{targetId}/**",
      method = {
          RequestMethod.GET,
          RequestMethod.POST,
          RequestMethod.DELETE,
          RequestMethod.PUT,
          RequestMethod.PATCH
      }
  )
  @ResponseBody
  public ResponseEntity<String> generalRouting(
      @RequestBody(required = false) String body,
      HttpMethod method,
      HttpServletRequest request,
      @PathVariable String targetId)
      throws HttpResponseException {
    ResponseEntity<String> responseEntity =
        middlewareService.exchange(body, method, request, targetId);
    return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
  }
}
