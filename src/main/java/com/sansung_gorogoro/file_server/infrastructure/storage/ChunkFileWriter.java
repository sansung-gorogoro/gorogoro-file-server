package com.sansung_gorogoro.file_server.infrastructure.storage;

import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@RequiredArgsConstructor
public class ChunkFileWriter {

    private final UploadPolicyProperties props;

    public long writeAtOffset(String tempFilePath, long offset, InputStream in) throws IOException {
        byte[] buf = new byte[1024 * 64];
        long written = 0;

        try (FileChannel ch = FileChannel.open(
                Paths.get(tempFilePath),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        )){
            ch.position(offset);

            int read;

            while ((read = in.read(buf)) != -1) {
                written += read;

                if (written > props.chunkMaxSizeBytes()) {
                    throw BusinessException.builder(FileErrorCode.UPLOAD_CHUNK_SIZE_EXCEEDS_LIMIT).build();
                }
                ByteBuffer bb = ByteBuffer.wrap(buf, 0, read);

                while (bb.hasRemaining()) {
                    ch.write(bb);
                }
            }
        }
        return written;
    }
}