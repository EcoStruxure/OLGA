/*
 * -------------------------
 * 
 * MIT License
 * 
 * Copyright (c) 2018, Schneider Electric USA, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * ---------------------
 */
package org.OLGA.Ws.Generator.Options;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import semanticstore.ontology.library.generator.Swagger2SpringBoot;
import semanticstore.ontology.library.generator.api.controllers.GeneratorController;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.global.LIBRARY;
import semanticstore.ontology.library.generator.service.OlgaService;
import semanticstore.ontology.library.generator.utils.Utils;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Purpose of these test is to test to the olga service is called with the correct parameters
 * 
 * @author SESA496078
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GeneratorController.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
public class SkipCompileOptionTest {

  @Spy
  OlgaService service;

  @Spy
  @InjectMocks
  private GeneratorController controller;

  @Spy
  @InjectMocks
  private Utils utilitiesBean;

  @Autowired
  private MockMvc mockMvc;

  MockHttpServletRequestBuilder request;

  {
    try {
      request = MockMvcRequestBuilders.post("/generate").param("code", "cs").param("name", "test")
          .param("library", "trinity").contentType("application/xml")
          .content(new String(
              Files.readAllBytes(Paths.get(this.getClass().getResource("/simple.owl").toURI())),
              "utf-8"));
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void skipCompile_true() throws Exception {

    ArgumentCaptor<Map<String, Object>> input =
        ArgumentCaptor.forClass((Class<Map<String, Object>>) (Class<?>) Map.class);
    ArgumentCaptor<List<InputStream>> listOfFiles =
        ArgumentCaptor.forClass((Class<List<InputStream>>) (Class<?>) List.class);
    mockMvc.perform(request.param("skipCompile", "true")).andExpect(status().isOk());

    verify(service).invokeOlga(input.capture(), listOfFiles.capture());

    assertTrue(input.getValue() != null);
    Map<String, Object> inputParm = input.getValue();
    assertTrue(
        inputParm.containsKey("code") && ((CODE) inputParm.get("code")).equals(CODE.C_SHARP));
    assertTrue(inputParm.containsKey("library")
        && ((LIBRARY) inputParm.get("library")).equals(LIBRARY.TRINITY));
    assertTrue(inputParm.containsKey("name") && ((String) inputParm.get("name")).equals("test"));
    assertTrue(inputParm.containsKey("skipCompile") && (boolean) inputParm.get("skipCompile"));

  }

  @SuppressWarnings("unchecked")
  @Test
  public void skipCompile_false() throws Exception {

    ArgumentCaptor<Map<String, Object>> input =
        ArgumentCaptor.forClass((Class<Map<String, Object>>) (Class<?>) Map.class);
    ArgumentCaptor<List<InputStream>> listOfFiles =
        ArgumentCaptor.forClass((Class<List<InputStream>>) (Class<?>) List.class);
    mockMvc.perform(request.param("skipCompile", "false")).andExpect(status().isOk());

    verify(service).invokeOlga(input.capture(), listOfFiles.capture());

    assertTrue(input.getValue() != null);
    Map<String, Object> inputParm = input.getValue();
    assertTrue(
        inputParm.containsKey("code") && ((CODE) inputParm.get("code")).equals(CODE.C_SHARP));
    assertTrue(inputParm.containsKey("library")
        && ((LIBRARY) inputParm.get("library")).equals(LIBRARY.TRINITY));
    assertTrue(inputParm.containsKey("name") && ((String) inputParm.get("name")).equals("test"));
    assertTrue(inputParm.containsKey("skipCompile") && !(boolean) inputParm.get("skipCompile"));

  }

  @SuppressWarnings("unchecked")
  @Test
  public void skipCompile_nothing() throws Exception {

    ArgumentCaptor<Map<String, Object>> input =
        ArgumentCaptor.forClass((Class<Map<String, Object>>) (Class<?>) Map.class);
    ArgumentCaptor<List<InputStream>> listOfFiles =
        ArgumentCaptor.forClass((Class<List<InputStream>>) (Class<?>) List.class);
    mockMvc.perform(request).andExpect(status().isOk());

    verify(service).invokeOlga(input.capture(), listOfFiles.capture());

    assertTrue(input.getValue() != null);
    Map<String, Object> inputParm = input.getValue();
    assertTrue(
        inputParm.containsKey("code") && ((CODE) inputParm.get("code")).equals(CODE.C_SHARP));
    assertTrue(inputParm.containsKey("library")
        && ((LIBRARY) inputParm.get("library")).equals(LIBRARY.TRINITY));
    assertTrue(inputParm.containsKey("name") && ((String) inputParm.get("name")).equals("test"));
    assertTrue(inputParm.containsKey("skipCompile") && !(boolean) inputParm.get("skipCompile"));

  }

  @Test
  public void skipCompile_wrong() throws Exception {

    mockMvc.perform(request.param("skipCompile", "fals")).andExpect(status().isBadRequest());

  }

}
