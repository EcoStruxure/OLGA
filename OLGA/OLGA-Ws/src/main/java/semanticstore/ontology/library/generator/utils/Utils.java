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
package semanticstore.ontology.library.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Utils {

  final static Logger log = Logger.getLogger(Utils.class);
  private List<String> fileList;

  @Autowired
  public Utils() {
    this.fileList = new ArrayList<String>();
  }

  public boolean deleteFileRecursively(File file) throws SecurityException {
    File[] flist = null;

    if (file == null) {
      return false;
    }

    if (file.isFile()) {
      return file.delete();
    }

    if (!file.isDirectory()) {
      return false;
    }

    flist = file.listFiles();
    if (flist != null && flist.length > 0) {
      for (File f : flist) {
        if (!deleteFileRecursively(f)) {
          return false;
        }
      }
    }
    return file.delete();
  }

  public byte[] zipAndSerializeOutputDirectory(File outputDirectory) throws NullPointerException,
      IllegalArgumentException, ZipException, IOException, OutOfMemoryError {

    prepareFilesList(outputDirectory);
    zipIt(outputDirectory.getPath() + ".zip", outputDirectory);
    return Files.readAllBytes(Paths.get(outputDirectory.getPath() + ".zip"));
  }

  public String getLogFileLocation() {
    String fileLocation = null;
    Enumeration<?> e = Logger.getRootLogger().getAllAppenders();
    while (e.hasMoreElements()) {
      Appender app = (Appender) e.nextElement();
      if (app instanceof FileAppender) {
        fileLocation = ((FileAppender) app).getFile();
      }
    }
    return fileLocation;
  }

  private void zipIt(String outZipFile, File sourceFolder)
      throws NullPointerException, IllegalArgumentException, ZipException, IOException {
    if (log.isDebugEnabled()) {
      throw new NullPointerException();
    }

    byte[] buffer = new byte[1024];
    String source = sourceFolder.getName();
        
    try (FileOutputStream outputStream = new FileOutputStream(outZipFile); ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream); ){
      
      List<Path> filesList = Files.walk(Paths.get(sourceFolder.getPath()))
          .filter(Files::isRegularFile).collect(Collectors.toList());
      for (Path file : filesList) {
        String filePath =
            file.toString().substring(file.toString().indexOf(source), file.toString().length());
        ZipEntry ze = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(ze);
        try (FileInputStream in = new FileInputStream(file.toString());) {
          int len;
          while ((len = in.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, len);
          }
        } catch (SecurityException | IOException e) {
          log.error(e);
          throw e;
        }
      }

      zipOutputStream.closeEntry();
      System.out.println("Folder successfully compressed");

    } catch (Exception e) {
      log.error(e);
      throw e;
    } 
  }

  private void prepareFilesList(File sourceFolder) {
    try {
      generateFileList(sourceFolder, sourceFolder.getPath());
    } catch (UnsupportedOperationException | ClassCastException | NullPointerException
        | IllegalArgumentException | SecurityException e) {
      log.error(e);
      throw e;
    }
  }

  private void generateFileList(File node, String parentNode) throws UnsupportedOperationException,
      ClassCastException, NullPointerException, IllegalArgumentException, SecurityException {
	if(node == null || parentNode == null)
		return;
	
    if (node.isFile()) {
      fileList.add(generateZipEntry(node.toString(), parentNode));
    }

    if (node.isDirectory()) {
      String[] subNote = node.list();
      if(subNote != null) {
      for (String filename : subNote) {
    		  generateFileList(new File(node, filename), parentNode);
      }
    }
    }
  }

  private String generateZipEntry(String file, String parentNode) throws IndexOutOfBoundsException {
    return file.substring(parentNode.length() + 1, file.length());
  }

}
