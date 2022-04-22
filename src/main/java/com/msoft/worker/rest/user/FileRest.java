package com.msoft.worker.rest.user;

import com.msoft.worker.service.FileService;
import com.msoft.worker.utils.SecurityUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Slf4j
@RequestMapping("file")
@RestController
@Api(value = "文件API", tags = "文件API")
public record FileRest(FileService fileService) {

    @PostMapping(produces = "application/json;charset=UTF-8")
    public Mono<String> upload(@RequestPart FilePart file) {
        return SecurityUtil.getUserVo().map(user -> {
            String  uuid = UUID.randomUUID().toString().replace("-", "");
            try {
                Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), file.filename());
                AsynchronousFileChannel channel = AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
                DataBufferUtils.write(file.content(), channel, 0)
                        .doOnComplete(() -> {
                            fileService.upload(tempFile.toFile(), uuid);
                            log.info("删除临时文件:{}", tempFile.toFile().delete());
                        }).subscribe();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return uuid;
        });
    }

    @GetMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> upload(@PathVariable String id) throws UnsupportedEncodingException {
        return fileService.download(id);
    }
}
