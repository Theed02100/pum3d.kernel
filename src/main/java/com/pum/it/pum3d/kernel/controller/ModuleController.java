package com.pum.it.pum3d.kernel.controller;

import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.service.IModuleService;
import com.pum.it.pum3d.kernel.utils.KernelProperties;
import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleController {

  private static final Logger LOGGER = LogManager.getLogger(ModuleController.class);

  @Autowired
  private IModuleService moduleService;

  @Autowired
  private KernelProperties kernelProperties;

  /**
   * Rajoute en BDD le module reçu dans le corps de la requête.
   *
   * <p>
   * On vérifie que les champs de l'objet (sauf id) ne sont pas null.
   * Si pas d'api key -> 403
   * </p>
   *
   * @param module
   *     Module à sauvegarder en base
   *
   * @return HTTP response 201
   */
  @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addModules(
      @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
      @RequestBody Module module)
      throws HttpResponseException {
    if (apiKey == null || !apiKey.equals(kernelProperties.getAdminApiKey())) {
      throw new HttpResponseException(HttpStatus.FORBIDDEN);
    }
    if (StringUtils.isAnyEmpty(module.getLibelle(), module.getUrl(), module.getId())
        || module.getBinded() == null) {
      throw new HttpResponseException(HttpStatus.BAD_REQUEST);
    }
    moduleService.addModule(module);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Renvoie tout les modules ordonnées par libellé.
   *
   * @return Tout les modules en base triés
   */
  @GetMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Module>> getAllModules() throws HttpResponseException {
    List<Module> modules = moduleService.findAllModulesSortedByLibelle();
    return new ResponseEntity<>(modules, HttpStatus.OK);
  }
}
