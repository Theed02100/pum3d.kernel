package com.pum.it.pum3d.kernel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pum.it.pum3d.kernel.dao.ModuleDao;
import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.service.IExternalWebService;
import com.pum.it.pum3d.kernel.utils.KernelProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModuleControllerTests {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final ArrayList<Module> mockedModules = new ArrayList<>();
  TestRestTemplate restTemplate = new TestRestTemplate();
  HttpHeaders headers = new HttpHeaders();
  @LocalServerPort
  private int port;
  @MockBean
  private ModuleDao moduleDao;
  @MockBean
  private IExternalWebService wsService;
  @Autowired
  private KernelProperties kernelProperties;

  @Before
  public void setup() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

    Module google = new Module();
    google.setId("id_1");
    google.setLibelle("Google");
    google.setUrl("https://www.google.fr");
    google.setBinded(false);
    mockedModules.add(google);
    Module yahoo = new Module();
    yahoo.setId("id_2");
    yahoo.setLibelle("Yahoo");
    yahoo.setUrl("https://www.yahoo.fr");
    yahoo.setBinded(true);
    mockedModules.add(yahoo);
    Module bing = new Module();
    bing.setId("id_3");
    bing.setLibelle("Bing");
    bing.setUrl("https://www.bing.fr");
    bing.setBinded(true);
    mockedModules.add(bing);
  }

  /*
      #### GET /modules
   */

  @Test
  public void givenModulesInDBAndAuthOk_whenGetModules_thenRespondSortedByLibelleDBData() {
    // Mock l'appel de la méthode findAll() du DAO and Auth call
    mockedModules.sort(Comparator.comparing(Module::getLibelle));
    Mockito.when(moduleDao.findAll(any(Sort.class))).thenReturn(mockedModules);
    Mockito.when(wsService.callAuthenticationService(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

    ResponseEntity<Module[]> responseEntity =
        restTemplate.getForEntity(createURLWithPort("/modules"), Module[].class);
    Module[] modules = responseEntity.getBody();

    Assert.assertEquals(200, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(modules);
    Assert.assertEquals(modules.length, mockedModules.size());
    List<Module> resultsArrayList = Arrays.stream(modules).collect(Collectors.toList());

    ArrayList<Module> expectedModules = new ArrayList<>();
    Module bing = new Module();
    bing.setLibelle("Bing");
    bing.setUrl("https://www.bing.fr");
    bing.setBinded(true);
    expectedModules.add(bing);
    Module google = new Module();
    google.setLibelle("Google");
    google.setUrl("https://www.google.fr");
    google.setBinded(false);
    expectedModules.add(google);
    Module yahoo = new Module();
    yahoo.setLibelle("Yahoo");
    yahoo.setUrl("https://www.yahoo.fr");
    yahoo.setBinded(true);
    expectedModules.add(yahoo);
    Assert.assertEquals(expectedModules, resultsArrayList);
  }

  @Test
  public void givenNoModulesInDBAndAuthOk_whenGetModules_thenRespond404() {
    Mockito.when(moduleDao.findAll(any(Sort.class))).thenReturn(new ArrayList<>());
    Mockito.when(wsService.callAuthenticationService(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    ResponseEntity<?> responseEntity =
        restTemplate.getForEntity(createURLWithPort("/modules"), Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());
  }

  /*
      #### POST /modules
  */

  @Test
  public void givenAuthOK_whenPostModulesWithGoodBody_thenRespond201() throws Exception {
    Mockito.when(wsService.callAuthenticationService(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

    Module google = new Module();
    google.setId("google");
    google.setLibelle("Google");
    google.setUrl("https://www.google.fr");
    google.setBinded(false);

    String googleAsJson = OBJECT_MAPPER.writeValueAsString(google);
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-KEY", kernelProperties.getAdminApiKey());
    Mockito.when(moduleDao.save(any(Module.class))).thenReturn(null);

    HttpEntity<String> request =
        new HttpEntity<String>(googleAsJson, headers);
    ResponseEntity<?> responseEntity = restTemplate.postForEntity(createURLWithPort("/modules"), request, Object.class);
    Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  @Test
  public void givenAuthOK_whenPostModulesWithBadBody_thenRespond400() throws Exception {
    Module google = new Module();
    google.setLibelle(null);
    google.setUrl(null);
    google.setBinded(null);

    String googleAsJson = OBJECT_MAPPER.writeValueAsString(google);
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-API-KEY", kernelProperties.getAdminApiKey());
    Mockito.when(moduleDao.save(any(Module.class))).thenReturn(null);

    HttpEntity<String> request =
        new HttpEntity<String>(googleAsJson, headers);
    ResponseEntity<?> responseEntity = restTemplate.postForEntity(createURLWithPort("/modules"), request, Object.class);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  public void givenAPIKeyControlled_whenPostModulesWithGoodBodyAndNoApiKey_thenRespond403() throws Exception {
    Module google = new Module();
    google.setLibelle("Google");
    google.setUrl("https://www.google.fr");
    google.setBinded(false);

    String googleAsJson = OBJECT_MAPPER.writeValueAsString(google);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Mockito.when(moduleDao.save(any(Module.class))).thenReturn(null);

    HttpEntity<String> request =
        new HttpEntity<String>(googleAsJson, headers);
    ResponseEntity<?> responseEntity = restTemplate.postForEntity(createURLWithPort("/modules"), request, Object.class);
    Assert.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
