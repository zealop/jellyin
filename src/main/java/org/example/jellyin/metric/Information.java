package org.example.jellyin.metric;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Information {

  public static String getMemoryInformation(long nonHeapSize){
    long heapSize = Runtime.getRuntime().totalMemory();
    return String.format("Current memory usage: HEAP: %s, NO-HEAP: %s",formatSize(heapSize),formatSize(nonHeapSize));
  }

  public static String formatSize(long v) {
    if (v < 1024) return v + " B";
    int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
    return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
  }
}