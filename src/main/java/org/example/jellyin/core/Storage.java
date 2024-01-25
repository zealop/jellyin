package org.example.jellyin.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.example.jellyin.core.Information.formatSize;

@Component
@Slf4j
public class Storage {

  private final Map<String, Movie> movieLibrary = new HashMap<>();

  public void put(String name, MultipartFile file) throws java.io.IOException {
    var length = (int) file.getSize();
    var byteBuffer = ByteBuffer.allocateDirect(length);
    try (var channel = file.getResource().readableChannel()) {
      IOUtils.readFully(channel, byteBuffer);
    }
    byteBuffer.position(0);
    movieLibrary.put(name, new Movie(byteBuffer, length));
    log.info(format("Added a new movie '%s' with size %s", name, formatSize(length)));
  }

  public Optional<Movie> pull(String name) {
    return Optional.of(movieLibrary.get(name));
  }

  public Optional<Movie> pullFromDisk(String name) throws java.io.IOException{
    File file = new File(name);
    try (
        SeekableByteChannel channel = Files.newByteChannel(file.toPath(), StandardOpenOption.READ)
    ) {
      int bufferSize = (int)file.length();
      if (bufferSize > channel.size()) {
        bufferSize = (int) channel.size();
      }
      ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

//      IOUtils.readFully(channel, byteBuffer);

      byteBuffer.position(0);

      return Optional.of(new Movie(byteBuffer, bufferSize));
    }
  }

  public SeekableByteChannel pullFileToChannel(String lame) throws java.io.IOException{
    File file = new File(lame);
    SeekableByteChannel channel = Files.newByteChannel(file.toPath(), StandardOpenOption.READ);
        return channel;
  }

  public Set<String> getMovieNames() {
    return movieLibrary.keySet();
  }

  public Long getTotalNoHeapMemoryUsage() {
    return movieLibrary.values().stream()
        .map(Movie::size)
        .map(Long::valueOf)
        .reduce(0L, Long::sum);
  }
}