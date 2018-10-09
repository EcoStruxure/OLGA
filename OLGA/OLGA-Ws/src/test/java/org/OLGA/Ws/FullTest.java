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
package org.OLGA.Ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.OLGA.Ws.utils.ZipUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import semanticstore.ontology.library.generator.Swagger2SpringBoot;
import semanticstore.ontology.library.generator.api.controllers.GeneratorController;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GeneratorController.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
public class FullTest {

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

  @Test
  public void skipCompile_nothing() throws Exception {

    MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    File file = File.createTempFile("test", ".zip");
    file.deleteOnExit();
    Path folder = Files.createTempDirectory("test");
    folder.toFile().deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(file)) {
      out.write(result.getResponse().getContentAsByteArray());
    }
    ZipUtils.unZipIt(file, folder.toFile());

    Path binFolder = folder.resolve("test-dotnetTrinity" + File.separator + "bin");
    assertTrue("Compilation was skipped, bin folder not found", Files.exists(binFolder));

  }
}
