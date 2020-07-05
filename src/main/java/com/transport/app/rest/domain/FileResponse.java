package com.transport.app.rest.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
