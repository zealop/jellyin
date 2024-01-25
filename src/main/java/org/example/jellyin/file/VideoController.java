package org.example.jellyin.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@Slf4j
public class VideoController {

  @GetMapping("/videos/{path}")
  public ResponseEntity<Resource> streamVideo(
      @RequestHeader(value = HttpHeaders.RANGE, required = false) String rangeHeader,
      @PathVariable String path
  ) throws IOException {

    log.debug("Getting videos {}", path);
    Resource videoResource = new FileSystemResource(Path.of(path));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentLength(videoResource.contentLength());
    headers.setContentDispositionFormData("attachment", videoResource.getFilename());

    if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
      // Handle byte-range request
      return processByteRangeRequest(rangeHeader, videoResource, headers);
    }

    return ResponseEntity.ok()
        .headers(headers)
        .body(videoResource);
  }

  private ResponseEntity<Resource> processByteRangeRequest(String rangeHeader, Resource videoResource,
      HttpHeaders headers)
      throws IOException {

    long fileSize = videoResource.contentLength();
    String[] rangeValues = rangeHeader.substring(6).split("-");
    long start = Long.parseLong(rangeValues[0]);
    long end = rangeValues.length > 1 ? Long.parseLong(rangeValues[1]) : fileSize - 1;

    if (start > end) {
      return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
          .headers(headers)
          .body(videoResource);
    }

    long contentLength = end - start + 1;
    headers.setContentLength(contentLength);
    headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
    headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");

    return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
        .headers(headers)
        .body(videoResource);
  }
}
