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
package semanticstore.ontology.library.generator.exceptions;

import semanticstore.ontology.library.generator.resources.ResourceManager;

/**
 * InvalidUriException is an exception object thrown when an entity IRI or the ontology URI is
 * invalid.
 */

@SuppressWarnings("serial")
public class InvalidUriException extends RuntimeException {

  private static String resourcesMessage =
      ResourceManager.getResource("InvalidUriExceptionMessage");

  public InvalidUriException() {
    super(resourcesMessage);
  }

  public InvalidUriException(String message) {
    super(resourcesMessage + message);
  }

  public InvalidUriException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidUriException(Throwable cause) {
    super(cause);
  }

}
