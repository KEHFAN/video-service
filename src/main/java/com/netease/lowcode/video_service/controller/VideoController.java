package com.netease.lowcode.video_service.controller;

import com.netease.lowcode.video_service.io.PartialFileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Value("${video.chunk.unit}")
    private Long chunkUnit;
    @Value("${video.chunk.size}")
    private Long chunkSize;

    @CrossOrigin(originPatterns = "*",methods = {RequestMethod.GET},allowCredentials = "true",allowedHeaders = {"DNT","X-CustomHeader","Keep-Alive","User-Agent","X-Requested-With","If-Modified-Since","Cache-Control","Content-Type","Range","range"})
    @GetMapping("/get/4jPEHXdG_9209150941_uhd.mp4")
    public ResponseEntity<Resource> getVideo(@RequestHeader(value = "Range", required = false) String range) throws IOException {

        String file = "C:\\Users\\fankehu\\Pictures\\4jPEHXdG_9209150941_uhd.mp4";
        long size = Files.size(Paths.get(file));

        // 返回完整视频资源
        if (Objects.isNull(range) || !range.startsWith("bytes=")) {

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp4; charset=UTF-8"))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(size))
                    .body(null);
        }



        long start = 0;
        String[] split = range.substring(6).split("-");
        if (split.length > 0) {
            // 暂时不做校验
            start = Long.valueOf(split[0]);
        }

        long end = 0;
        if (size - start > chunkSize * chunkUnit) {
            end = start + chunkSize * chunkUnit - 1;
        } else {
            end = size - 1;
        }


        // 得提前对视频切片，这种方式 越往后由于skip，chunk返回的越慢
        PartialFileResource partialFileResource = new PartialFileResource(file, start, end);
        //FileSystemResource fileSystemResource = new FileSystemResource(file + chunkNum);
        //InputStreamResource inputStreamResource = chunkService.getChunkResource();

        // 返回部分资源
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.valueOf("video/mp4; charset=UTF-8"))
                // 切片大小
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(partialFileResource.contentLength()))
                // bytes 切片起始偏移-切片结束偏移/资源总大小
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %s-%s/%s", start, end, size))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"4jPEHXdG_9209150941_uhd.mp4\"")
                .header(HttpHeaders.CACHE_CONTROL,"max-age=2592000")
                .header(HttpHeaders.AGE,"1213706")
                .header("Timing-Allow-Origin","*")
                .body(partialFileResource);
    }
}
