package com.revplay.playbackservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class MediaStreamingService {
    
    @Value("${media.streaming.chunk-size:8192}")
    private int chunkSize;
    
    @Value("${media.streaming.buffer-size:16384}")
    private int bufferSize;
    
    @Value("${media.supported-formats:mp3,wav,flac,aac,ogg}")
    private String supportedFormats;
    
    public ResponseEntity<Resource> streamMedia(String filePath, String rangeHeader) throws IOException {
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        
        File file = path.toFile();
        long fileLength = file.length();
        
        // Check if file format is supported
        String fileExtension = getFileExtension(filePath);
        if (!isFormatSupported(fileExtension)) {
            return ResponseEntity.badRequest().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        // Handle range requests for streaming
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            return handleRangeRequest(resource, fileLength, rangeHeader);
        } else {
            // Full file response
            return ResponseEntity.ok()
                    .contentType(getMediaType(fileExtension))
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                    .body(resource);
        }
    }
    
    private ResponseEntity<Resource> handleRangeRequest(Resource resource, long fileLength, String rangeHeader) {
        try {
            String[] ranges = rangeHeader.substring(6).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;
            
            if (start >= fileLength || end >= fileLength || start > end) {
                return ResponseEntity.status(416).build(); // Range Not Satisfiable
            }
            
            long contentLength = end - start + 1;
            
            return ResponseEntity.status(206) // Partial Content
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    public byte[] readChunk(String filePath, long start, int length) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            file.seek(start);
            byte[] chunk = new byte[length];
            int bytesRead = file.read(chunk);
            
            if (bytesRead < length) {
                byte[] actualChunk = new byte[bytesRead];
                System.arraycopy(chunk, 0, actualChunk, 0, bytesRead);
                return actualChunk;
            }
            
            return chunk;
        }
    }
    
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    public long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }
    
    public String getFileFormat(String filePath) {
        return getFileExtension(filePath);
    }
    
    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filePath.substring(lastDot + 1).toLowerCase();
    }
    
    private boolean isFormatSupported(String format) {
        List<String> supported = Arrays.asList(supportedFormats.split(","));
        return supported.contains(format.toLowerCase());
    }
    
    private MediaType getMediaType(String format) {
        switch (format.toLowerCase()) {
            case "mp3":
                return MediaType.parseMediaType("audio/mpeg");
            case "wav":
                return MediaType.parseMediaType("audio/wav");
            case "flac":
                return MediaType.parseMediaType("audio/flac");
            case "aac":
                return MediaType.parseMediaType("audio/aac");
            case "ogg":
                return MediaType.parseMediaType("audio/ogg");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
