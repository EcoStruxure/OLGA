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
package semanticstore.ontology.library.generator.api.controllers;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import semanticstore.ontology.library.generator.Exception.NotFoundException;
import semanticstore.ontology.library.generator.Exception.ProjectGenerationException;
import semanticstore.ontology.library.generator.api.interfaces.GeneratorApi;
import semanticstore.ontology.library.generator.exceptions.InvalidClassNameException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.global.CODE;
import semanticstore.ontology.library.generator.service.OlgaService;
import semanticstore.ontology.library.generator.utils.Helper;
import semanticstore.ontology.library.generator.utils.Utils;

@RestController
public class GeneratorController implements GeneratorApi {

  Map<String, Object> inputCmdParameters;
  String result = "Fail";

  OlgaService service = new OlgaService();

  @Autowired
  Utils utilitiesBean;

  final Charset UTF_8 = Charset.forName("UTF-8");
  private static final Logger log = Logger.getLogger(GeneratorController.class);

  @Autowired
  public GeneratorController() {}

  public byte[] generate(@RequestParam(value = "code", required = true) String code,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "library", required = true) String library,
      @RequestParam(value = "preserve", required = false, defaultValue = "false") String preserve,
      @RequestParam(value = "version", required = false) String version,
      @RequestParam(value = "partial", required = false, defaultValue = "false") String partial,
      @RequestParam(value = "skipCompile", required = false,
          defaultValue = "false") boolean skipCompile,
      @RequestParam(value = "file", required = true) MultipartFile file,
      HttpServletResponse response) throws Exception {
    response.addHeader("Content-disposition", "attachment;filename=" + name + ".zip");
    response.setStatus(HttpServletResponse.SC_OK);

    if (System.getenv("M2_HOME") == null) {
      NotFoundException exception = new NotFoundException(400,
          "Please set M2_HOME in your environment to point to a valid maven binaries");
      throw exception;
    }
    byte[] buffer = null;
    DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");
    long start_millis_overall = System.currentTimeMillis();
    log.info("Operation Starts");
    String outPath = null;
    try {
      // Parsing the user arguments
      inputCmdParameters =
          Helper.parseInputs(code, name, library, preserve, version, partial, skipCompile);
      log.info("Parsing Inputs");
      outPath = (String) inputCmdParameters.get("out");

      File f = File.createTempFile(file.getName(), null);
      FileOutputStream o = new FileOutputStream(f);
      IOUtils.copy(file.getInputStream(), o);
      o.close();

      log.info("Invoke Olga Service");
      service.invokeOlga(inputCmdParameters, f);
      result = service.getResult();
      long end_millis_overall = System.currentTimeMillis();
      Date time_diff_overall = new Date(end_millis_overall - start_millis_overall);
      String overallTime = dateFormatter.format(time_diff_overall);
      log.info("========== " + inputCmdParameters.get("name") + "-"
          + (inputCmdParameters.get("library").toString().isEmpty() ? "'Unsupported'"
              : inputCmdParameters.get("library") + " ("
                  + ((CODE) inputCmdParameters.get("code")).getText() + ") by OLGA : Build "
                  + result + " in (mm:ss:SSS) " + overallTime + " ===========\n"));
      if (!result.contains("Success")) {
        System.err.println(result);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        throw new ProjectGenerationException(500, "Project Generation Failed");
      }
      log.info("Buffer The Output");
      File[] subDirectories = new File(outPath).listFiles(File::isDirectory);
      if(subDirectories != null && subDirectories.length > 0) {
    	  File outputDirectory = subDirectories[0];
    	  buffer = utilitiesBean.zipAndSerializeOutputDirectory(outputDirectory);
      }
    } catch (InvalidClassNameException | UnableToCompileGeneratedCodeException
        | InvalidUriException e) {
      result = "Fail";
      log.error(e);
      System.out.println(e.getMessage());
    } catch (Exception e) {
      result = "Fail";
      log.error(e);
      System.out.println(e.getMessage());
    } finally {
      if (inputCmdParameters != null) {
        log.info("Clean up");
        utilitiesBean.deleteFileRecursively(
            new File(inputCmdParameters.get("pathToOntologiesParam").toString()));
        utilitiesBean.deleteFileRecursively(new File(outPath));
      }
      log.info("============================================\n");
    }

    return buffer;
  }
}
