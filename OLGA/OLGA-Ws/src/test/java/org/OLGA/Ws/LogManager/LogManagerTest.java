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
package org.OLGA.Ws.LogManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.OLGA.Ws.utils.ZipUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import semanticstore.ontology.library.generator.Swagger2SpringBoot;
import semanticstore.ontology.library.generator.api.controllers.LogManagerController;
import semanticstore.ontology.library.generator.utils.Utils;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {LogManagerController.class})
@ContextConfiguration(classes = Swagger2SpringBoot.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogManagerTest {

  @Spy
  @Autowired
  private Utils utilitiesBean;

  @InjectMocks
  private LogManagerController controller;

  @Autowired
  private MockMvc mockMvc;

  MockHttpServletRequestBuilder requestColleccLogs =
      MockMvcRequestBuilders.get("/admin/collectlogs");

  MockHttpServletRequestBuilder requestClearLogs = MockMvcRequestBuilders.get("/admin/clearlogs");

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void testA_collectLogs() throws Exception {

    MvcResult result = mockMvc.perform(requestColleccLogs).andExpect(status().isOk()).andReturn();
    File file = File.createTempFile("test", ".zip");
    file.deleteOnExit();
    Path folder = Files.createTempDirectory("test");
    folder.toFile().deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(file)) {
      out.write(result.getResponse().getContentAsByteArray());
    }
    ZipUtils.unZipIt(file, folder.toFile());
    assertTrue(Files.exists(folder.resolve("Logger/log4j-application.log")));
  }

  @Test
  public void testB_collectLogsFails() throws Exception {

    doThrow(NullPointerException.class).when(utilitiesBean)
        .zipAndSerializeOutputDirectory(any(File.class));
    mockMvc.perform(requestColleccLogs).andExpect(status().isInternalServerError());

  }

  @Test
  public void testC_clearLogs() throws Exception {

    Path log = Paths.get(utilitiesBean.getLogFileLocation());
    // input dummy log file from previous days
    File f = new File(log.getParent().resolve("log1.log").toString());
    f.createNewFile();
    mockMvc.perform(requestClearLogs).andExpect(status().isOk());
    assertTrue(checkOnlyOneLogFile(log));
  }

  @Test
  public void testD_clearLogsFails() throws Exception {

    // input dummy log file from previous days
    Path log = Paths.get(utilitiesBean.getLogFileLocation());
    File f = new File(log.getParent().resolve("log1.log").toString());
    f.createNewFile();
    // fake an exception being thrown when accessing log file to be deleted
    doThrow(SecurityException.class).when(utilitiesBean).deleteFileRecursively(f);
    mockMvc.perform(requestClearLogs).andExpect(status().isInternalServerError());
    f.delete();
  }

  private boolean checkOnlyOneLogFile(Path pathToLog) {
    File[] filesInLogFolder = pathToLog.getParent().toFile().listFiles();
    return filesInLogFolder.length != 0 && Arrays.stream(filesInLogFolder).allMatch(
        f -> f.isFile() && f.getAbsolutePath().equals(pathToLog.toAbsolutePath().toString()));
  }
}
