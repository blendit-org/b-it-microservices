package com.blenditorg.aiService.service;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class BlenderExe {

    private final Storage storage;
    private final String bucketName = "your-gcs-bucket"; // 🔹 replace with your bucket

    public BlenderExe() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String runBlenderAndUpload(String scriptPath, String jobId) throws Exception {
        File scriptFile = new File(scriptPath);
        File hostOutDir = new File(scriptFile.getParentFile(), "out");
        hostOutDir.mkdirs();
        File outBlend = new File(hostOutDir, "scene.blend");

        // Example docker command (Linux host)
        String cmd = String.join(" ", new String[]{
                "docker run --rm",
                "--user 1000:1000",
                "--cap-drop=ALL",
                "--security-opt no-new-privileges",
                "-v", scriptFile.getParentFile().getAbsolutePath() + ":/workspace",
                "blender:4.0",
                "--background --python /workspace/" + scriptFile.getName(),
                "--save /workspace/out/scene.blend"
        });

        Process process = Runtime.getRuntime().exec(cmd);
        if (process.waitFor() != 0) {
            throw new RuntimeException("Blender failed with exit code: " + process.exitValue());
        }

        if (!outBlend.exists()) {
            throw new RuntimeException("Blender did not produce output file: " + outBlend.getAbsolutePath());
        }

        // Upload to GCS
        String objectName = "aiJobs/" + jobId + "/scene.blend";
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/x-blender").build();
        storage.create(blobInfo, Files.readAllBytes(outBlend.toPath()));

        // Generate signed URL (valid for 1 hour)
        URL signedUrl = storage.signUrl(
                blobInfo,
                1,
                TimeUnit.HOURS,
                Storage.SignUrlOption.withV4Signature()
        );

        return signedUrl.toString();
    }
}