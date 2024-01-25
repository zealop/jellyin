package org.example.jellyin.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.stream.Stream;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
@Slf4j
public class FolderController {

  private final ResourcePatternResolver resourcePatternResolver;

  @GetMapping
  public Object get(@RequestParam String path) throws IOException {
    log.debug("get stuff {}", path);
    Resource[] resources = resourcePatternResolver.getResources(path);

    return Stream.of(resources)
        .map(Resource::getFilename)
        .toList();
  }
}
