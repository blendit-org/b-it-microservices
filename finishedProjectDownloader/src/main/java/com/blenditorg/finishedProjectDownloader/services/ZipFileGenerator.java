package com.blenditorg.finishedProjectDownloader.services;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.gax.paging.Page;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.ByteStreams;

@Service
public class ZipFileGenerator {
	
	@Value("${gcs.BucketName}")
	private String bucketName;
	
	@Value("${gcs.ProjectId}")
	private String gcsProjectId; // This projectId is Google Cloud Service projectId, DON'T get confused with ProjectFile -> projectId

	
	public String generateDownloadUrlForZip(String userId, Long projectId) throws Exception{
		Storage storage = StorageOptions.getDefaultInstance().getService();
		Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(userId + "/" + projectId + "/" + "rendered/"));
		File tempZip = File.createTempFile(userId + "-" + projectId, ".zip");
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZip))) {
			for (Blob blob: blobs.iterateAll()) {
				ZipEntry entry = new ZipEntry(blob.getName().replace(userId + "/" + projectId + "/" + "rendered/", ""));
				zos.putNextEntry(entry);
				ReadChannel reader = blob.reader();
				WritableByteChannel writer = Channels.newChannel(zos);
				ByteStreams.copy(reader, writer);
				zos.closeEntry();
			}
		}
		
		BlobId zipId = BlobId.of(bucketName, userId + "/" + projectId + "/renderedzip");
		BlobInfo zipInfo = BlobInfo.newBuilder(zipId).build();
		storage.create(zipInfo, Files.readAllBytes(tempZip.toPath()));
		
		URL url = storage.signUrl(zipInfo, 1, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());
		return url.toString();
	}
}
