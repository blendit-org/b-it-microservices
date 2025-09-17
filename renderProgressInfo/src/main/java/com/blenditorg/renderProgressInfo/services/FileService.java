package com.blenditorg.renderProgressInfo.services;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

@Service
public class FileService {
	
	@Value("${gcs.BucketName}")
	private String bucketName;
	
	@Value("${gcs.ProjectId}")
	private String projectId; // This projectId is Google Cloud Service projectId, DON'T get confused with ProjectFile -> projectId

	/**
	 * for downloading/GET a file from GCS
	 * 
	 * @param filename
	 * @return
	 */
	public String generateGetPresignedUrl(String objectName) throws StorageException{
		Storage storage = StorageOptions
				.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
		BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
		URL url = storage.signUrl(
				blobInfo, 
				15, 
				TimeUnit.MINUTES,
				Storage.SignUrlOption.withV4Signature());
		return url.toString();
	}
	
	public String generatePutPresignedUrl(String objectName) throws StorageException{
		
		Storage storage = StorageOptions
				.newBuilder()
				.setProjectId(projectId)
				.build()
				.getService();
		
		
		BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
		
		
		Map<String, String> extensionHeaders = new HashMap<>();
		extensionHeaders.put("Content-Type", "application/octet-stream");
		
		System.out.println("hole 2: " + extensionHeaders);
		
		URL url = storage.signUrl(
				blobInfo, 
				15, 
				TimeUnit.MINUTES, 
				Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
				Storage.SignUrlOption.withExtHeaders(extensionHeaders),
				Storage.SignUrlOption.withV4Signature());
		
		
		return url.toString();
	}
}
