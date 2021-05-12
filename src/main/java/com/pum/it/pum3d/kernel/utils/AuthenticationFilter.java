package com.pum.it.pum3d.kernel.utils;

import com.pum.it.pum3d.kernel.service.IExternalWebService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER = "Bearer ";
  private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);
  @Value("${auth.validation.url}")
  private String authValidationUrl;

  @Autowired
  private IExternalWebService wsService;

  /**
   * Permet de filtrer les requêtes à destination du kernel back pour vérifier
   * auprès du service d'authentification le JWT.
   *
   * <p>
   * Si le service d'auth valide le jwt, alors la reqûete poursuit sa route.
   * Sinon, on bloque et on renvoie la réponse du service d'auth.
   * - 302 => Redirection vers le process d'authentification
   * - Autre status : Erreur
   * </p>
   *
   * @param request
   *     Requête d'origine
   * @param response
   *     Réponse attendu
   * @param filterChain
   *     manager de la chaîne de filtre
   *
   * @throws ServletException
   *     Exception du FilterChain
   * @throws IOException
   *     Exception d'accès à l'écriture du body de la réponse
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {
    // Certaine requête ne doivent pas passer par ce filter
    if (!isFiltered(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Récupération du JWT
    String jwt = "";
    final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

    if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
      jwt = requestTokenHeader.substring(BEARER.length());
    }

    LOGGER.info("JWT = " + jwt);

    ResponseEntity<String> authServiceResponse =
        wsService.callAuthenticationService(authValidationUrl, jwt);

    String authResponseBody = "";

    if (authServiceResponse.getBody() != null) {
      authResponseBody = authServiceResponse.getBody();
    }
    if (authServiceResponse.getStatusCode().equals(HttpStatus.OK)) {
      response.addHeader("user", authServiceResponse.getBody());
      filterChain.doFilter(request, response);
    } else if (authServiceResponse.getStatusCode().equals(HttpStatus.TEMPORARY_REDIRECT)) {
      response.reset();
      response.addHeader("Location", authServiceResponse.getHeaders().getLocation().toString());
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    } else {
      response.setStatus(authServiceResponse.getStatusCodeValue());
      response.setContentLength(authResponseBody.length());
      response.getWriter().write(authResponseBody);
    }
  }

  private boolean isFiltered(HttpServletRequest request) {
    HashMap<String, HttpMethod> uriAndMethodsToNotFilter = new HashMap<>();
    uriAndMethodsToNotFilter.put("/modules", HttpMethod.POST);
    for (Map.Entry<String, HttpMethod> entry : uriAndMethodsToNotFilter.entrySet()) {
      String uri = entry.getKey();
      HttpMethod method = entry.getValue();
      if (request.getRequestURI().equals(uri) && method.matches(request.getMethod())) {
        return false;
      }
    }
    return true;
  }
}

