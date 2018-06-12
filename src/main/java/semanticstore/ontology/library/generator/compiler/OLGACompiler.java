/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.compiler;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.log4j.Logger;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;

public class OLGACompiler {
  final static Logger log = Logger.getLogger(OLGACompiler.class);

  public static String maven_exec(String pomFilePath, List<String> goals)
      throws UnableToCompileGeneratedCodeException, MojoFailureException {
    if (log.isDebugEnabled()) {
      log.debug(" -Compiler ");
    }


    long start_millis_mvn = System.currentTimeMillis();
    InvocationRequest request = new DefaultInvocationRequest();
    request.setPomFile(new File(pomFilePath));
    request.setGoals(goals);
    InvocationResult result;
    Invoker invoker = new DefaultInvoker();    

    try {
      String maveRepoPath = System.getenv("M2_HOME");
      invoker.setMavenHome(new File(maveRepoPath));
      result = invoker.execute(request);
    } catch (MavenInvocationException e) {
      log.error(e);
      throw new UnableToCompileGeneratedCodeException(e);
    }catch (SecurityException | NullPointerException e) {
      log.error(e);
      throw e;
    }

    DateFormat dateFormatter = new SimpleDateFormat("mm:ss:SSS");
    long end_millis_mvn = System.currentTimeMillis();
    Date mvnTime = new Date(end_millis_mvn - start_millis_mvn);
    String mvnTimeString = dateFormatter.format(mvnTime);

    if (result.getExitCode() != 0) {
      return ("Failed");
    } else {
      System.out.println("Maven Compile and Packaging Time (mm:ss:SSS): " + mvnTimeString);
      return ("Success");
    }
  }

  public static String maven_clean_install(String pomFilePath)
      throws UnableToCompileGeneratedCodeException, MojoFailureException {
    return maven_exec(pomFilePath, Arrays.asList("clean", "install"));
  }

}
