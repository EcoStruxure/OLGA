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
package semanticstore.ontology.library.generator.api.interfaces;

import io.swagger.annotations.*;
import javax.annotation.Generated;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Generated(value = "io.swagger.codegen.languages.java.SpringCodegen",
    date = "2018-08-08T17:06:42.836-04:00[America/New_York]")
@RequestMapping("admin")
public interface LogManagerApi {

  @ApiOperation(value = "Requests deleting OLGA logs", nickname = "clearlogs",
      notes = "By calling this method OLGA clears the logs", tags = {"users",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OLGA clears the logs successfully"),
      @ApiResponse(code = 400, message = "Failled to clear logs")})
  @RequestMapping(value = "clearlogs", method = RequestMethod.GET)
  void clearlogs(HttpServletResponse response);

  @ApiOperation(value = "Requests OLGA log files zipped", nickname = "collectlogs",
      notes = "By calling this method OLGA returns the log files zipped", response = byte[].class,
      tags = {"users",})
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OLGA zipped and collected the log file successfully",
              response = byte[].class),
          @ApiResponse(code = 400, message = "Failled to collect logs")})
  @RequestMapping(value = "collectlogs", produces = {"application/zip"}, method = RequestMethod.GET)
  byte[] collectlogs(HttpServletResponse response);
}
