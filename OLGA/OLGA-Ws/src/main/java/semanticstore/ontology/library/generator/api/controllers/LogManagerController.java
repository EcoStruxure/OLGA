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

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import semanticstore.ontology.library.generator.Exception.NotFoundException;
import semanticstore.ontology.library.generator.api.interfaces.LogManagerApi;
import semanticstore.ontology.library.generator.utils.Utils;

@RestController
public class LogManagerController implements LogManagerApi {
  private static final Logger log = Logger.getLogger(LogManagerController.class);

  @Autowired
  Utils utilitiesBean;

  @Autowired
  public LogManagerController() {}

  public void clearlogs(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    log.info("Start Clear Logs Operation");
    try {
    	String logFileLocation = utilitiesBean.getLogFileLocation();
    	
    	if(logFileLocation == null)
    		throw new NotFoundException(400,"Can't get log directory");
    	
      File logFile = new File(logFileLocation);
      if(logFile.getParentFile() != null) {
      for (File log : logFile.getParentFile().listFiles()) {
    	  if(!log.getName().equals(logFile.getName()))
    		  utilitiesBean.deleteFileRecursively(log);
      }
      }
    } catch (SecurityException | NotFoundException e) {
      log.error(e);
      System.out.println(e.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    log.info("============================================\n");
  }

  public byte[] collectlogs(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    log.info("Start Collecting Logs Operation");
    byte[] buffer = null;
    File logFile = null;
    try {
      logFile = new File(utilitiesBean.getLogFileLocation());
      buffer = utilitiesBean.zipAndSerializeOutputDirectory(logFile.getParentFile());
    } catch (NullPointerException | IllegalArgumentException | IOException | OutOfMemoryError e) {
      log.error(e);
      System.out.println(e.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      if (logFile != null) {
        log.info("Clean up");
        utilitiesBean.deleteFileRecursively(new File(logFile.getParentFile().getPath() + ".zip"));
      }
    }

    log.info("============================================\n");
    return buffer;
  }
}
