package io.grx.common.service;

import java.io.InputStream;

public interface FileUploadService {
    String upload(InputStream is, String path, String prefix, String suffix);
}
