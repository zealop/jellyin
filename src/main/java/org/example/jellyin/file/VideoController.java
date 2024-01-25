package org.example.jellyin.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jellyin.core.Storage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VideoController {

  private final Storage storage;

  @GetMapping(value = "/videos/{path}")
  public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable String path) throws IOException {

    byte[] decodedBytes = Base64.getDecoder().decode(path);
    String decodedPath = new String(decodedBytes);
    log.debug("Getting videos {}", decodedPath);

    InputStream inputStream = new FileInputStream(decodedPath);

    StreamingResponseBody body = os -> readAndWrite(inputStream, os);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        .body(body);

  }

  private void readAndWrite(InputStream inputStream, OutputStream os) throws IOException {
    byte[] data = new byte[2048];
    int read = 0;
    while ((read = inputStream.read(data)) > 0) {
      os.write(data, 0, read);
    }
    os.flush();
  }
}
