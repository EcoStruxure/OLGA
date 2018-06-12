/*
-------------------------

MIT License

Copyright (c) 2018, Schneider Electric USA, Inc.    

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

---------------------
*/
package semanticstore.ontology.library.generator.generators;

import java.io.IOException;
import java.util.Map;
import javax.management.RuntimeErrorException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.semanticweb.owlapi.model.IRI;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import semanticstore.ontology.library.generator.exceptions.InvalidUriException;
import semanticstore.ontology.library.generator.exceptions.UnableToCompileGeneratedCodeException;
import semanticstore.ontology.library.generator.model.ZClass;

public interface Generator {

  void setParameters(Map<String, Object> inputParameters)
      throws IOException, XmlPullParserException, InvalidUriException;

  String generateCode(Map<IRI, ZClass> mapIRI_to_Zclass)
      throws IOException, TemplateException, MavenInvocationException,
      UnableToCompileGeneratedCodeException, RuntimeErrorException, MojoFailureException;

  void generateClass() throws IOException, TemplateException;

  void generatePom() throws TemplateNotFoundException, MalformedTemplateNameException,
      ParseException, IOException, TemplateException;
}
