package ultra.com.server.service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
// import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileUploadService {
    
    @Value("${do.storage.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String upload(MultipartFile file) throws IOException {

        Map<String, String> userData = new HashMap<>();
        userData.put("name", "ultra");
        userData.put("uploadTime", Instant.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

        String key = UUID.randomUUID().toString().substring(0,8);

        // StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");
        System.out.println("file original name " + file.getOriginalFilename());

        // int count = 0;
        // String filenameExt = "";
        // String finalFileUpload = "";

        // while(tk.hasMoreTokens()){
        //     System.out.println(tk.nextToken() + "next token");
        //     if (count == 1) {
        //         filenameExt = tk.nextToken();
        //         System.out.println(filenameExt);
        //         break;
        //     }
        //     count++;
        //     System.out.println(filenameExt);

        // }

        // if (filenameExt.equals("blob")) {
        //     finalFileUpload = filenameExt + ".png";
        // }

        PutObjectRequest putRequest = new PutObjectRequest(
            bucketName, "images/%s.%s".formatted(key,file.getOriginalFilename()), 
            file.getInputStream(),
            metadata);

        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);
        return "images/%s.%s".formatted(key, file.getOriginalFilename());
        

    }
}


