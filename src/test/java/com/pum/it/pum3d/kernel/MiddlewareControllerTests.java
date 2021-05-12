
package com.pum.it.pum3d.kernel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pum.it.pum3d.kernel.dao.ModuleDao;
import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.service.IExternalWebService;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MiddlewareControllerTests {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  TestRestTemplate restTemplate = new TestRestTemplate();
  HttpHeaders headers = new HttpHeaders();
  @LocalServerPort
  private int port;

  @MockBean
  private ModuleDao moduleDao;
  @MockBean
  private IExternalWebService wsService;

  private String myPumDefaultResponseBeginning;
  private String myPumErrorResponseBeginning;


  @Before
  public void setup() {
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    myPumDefaultResponseBeginning = "<!doctype html><html lang=\"fr\"><head><meta charset=\"utf-8\">";
    myPumErrorResponseBeginning = "<!doctype html><html lang=\"en\">";
  }

  @Test
  public void whenRoutingToUnknown_thenRespond404() {
    // Mock auth call
    Mockito.when(wsService.callAuthenticationService(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    // Mock find module -> nothing found
    Mockito.when(moduleDao.findById(anyString())).thenReturn(Optional.empty());
    // Mock find widget
    // TODO

    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>("{}", headers);


    ResponseEntity<?> responseEntity = restTemplate.exchange(createURLWithPort("routing/test/home/"), HttpMethod.GET, null, Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());

    responseEntity = restTemplate.exchange(createURLWithPort("routing/test/home/"), HttpMethod.POST, entity, Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());

    responseEntity = restTemplate.exchange(createURLWithPort("routing/test/home/"), HttpMethod.PUT, entity, Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());

    responseEntity = restTemplate.exchange(createURLWithPort("routing/test/home/"), HttpMethod.PATCH, entity, Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());

    responseEntity = restTemplate.exchange(createURLWithPort("routing/test/home/"), HttpMethod.DELETE, null, Object.class);
    Assert.assertEquals(404, responseEntity.getStatusCodeValue());
  }


/**
   * On mock le comportement comme si on faisant des requetes à www.mypum.fr.
   *
   * <p>
   * Si le test ne passe plus, vérifier avec Postman le comportement par défaut
   * sur un call à www.mypum.fr pour chaque méthode.
   * </p>
   */

  @Test
  public void whenRouting_thenTransmitTargetResponse() {
    // Mock authentification
    Mockito.when(wsService.callAuthenticationService(anyString(), anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    // Mock  module
    Module mockedModule = new Module();
    mockedModule.setId("mypum");
    mockedModule.setLibelle("MyPum");
    mockedModule.setBinded(true);
    mockedModule.setUrl("https://www.mypum.fr");
    Mockito.when(moduleDao.findById(anyString())).thenReturn(Optional.of(mockedModule));


    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>("{}", headers);

    ResponseEntity<String> responseEntity = restTemplate.exchange(createURLWithPort("routing/mypum/"), HttpMethod.GET, null, String.class);
    Assert.assertEquals(200, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().startsWith(myPumDefaultResponseBeginning));

    responseEntity = restTemplate.exchange(createURLWithPort("routing/mypum/"), HttpMethod.POST, entity, String.class);
    Assert.assertEquals(200, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().startsWith(myPumDefaultResponseBeginning));


    responseEntity = restTemplate.exchange(createURLWithPort("routing/mypum/"), HttpMethod.PUT, entity, String.class);
    Assert.assertEquals(405, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().startsWith(myPumErrorResponseBeginning));


    responseEntity = restTemplate.exchange(createURLWithPort("routing/mypum/"), HttpMethod.PATCH, entity, String.class);
    Assert.assertEquals(501, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().startsWith(myPumErrorResponseBeginning));


    responseEntity = restTemplate.exchange(createURLWithPort("routing/mypum/"), HttpMethod.DELETE, null, String.class);
    Assert.assertEquals(405, responseEntity.getStatusCodeValue());
    Assert.assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().startsWith(myPumErrorResponseBeginning));
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}

