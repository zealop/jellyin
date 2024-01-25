package org.example.jellyin.core;

import java.nio.ByteBuffer;

public record Movie(ByteBuffer movieByteBuffer, Integer size) {
}