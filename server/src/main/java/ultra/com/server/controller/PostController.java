package ultra.com.server.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.PostEditDto;
import ultra.com.server.dto.PostRequest;
import ultra.com.server.dto.PostResponse;
import ultra.com.server.service.FileUploadService;
import ultra.com.server.service.PostService;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
@CrossOrigin("*")
public class PostController {

    @Autowired
    private FileUploadService fileSvc;

    // JsonObject jsonPayload = null;
    
    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @RequestParam Long id,
            @RequestParam String postReflections,
            @RequestParam String postName,
            @RequestParam(required = false) MultipartFile mediaFile,
            @RequestParam(required = false) String latLngString
            ) {
        
        String key;
        String postLocation;

        try {
            PostRequest postRequest = new PostRequest();
            postRequest.setMediaUrl(null);

            if (!(mediaFile == null)) {
                key = this.fileSvc.upload(mediaFile);
                System.out.println(key);
                postRequest.setMediaUrl(key);
            }
            if (!(latLngString == null)) {
                postLocation = this.postService.getReverseGeolocation(latLngString);
                postRequest.setPostLocation(postLocation);
            }
            // jsonPayload = Json.createObjectBuilder()
            //     .add("image-key", key)
            //     .build();
            postRequest.setId(id);
            postRequest.setPostName(postName);
            postRequest.setPostReflections(postReflections);
            PostResponse postResponse = postService.save(postRequest);
            System.out.println(postResponse);
            return new ResponseEntity<>(postResponse, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(params = "projectId")
    public ResponseEntity<List<PostResponse>> getAllPostsByProjectId(@RequestParam Long projectId) {
        System.out.println(projectId + " id");
        List<PostResponse> listOfPostResponses = postService.getAllPostsByProjectId(projectId);
        return new ResponseEntity<List<PostResponse>>(listOfPostResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostByPostId(@PathVariable Long id) throws JsonMappingException, JsonProcessingException {
        
        PostResponse postResponse = postService.getPost(id);
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @PutMapping(path = "/{postId}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> editPost(@PathVariable Long postId, 
        @RequestParam String postReflections,
        @RequestParam String postName,
        @RequestParam(required = false) MultipartFile mediaFile
    ) {

        String key;
        
        try {
            PostEditDto postEditDto = new PostEditDto();

            if (!(mediaFile == null)) {
                key = this.fileSvc.upload(mediaFile);
                postEditDto.setMediaUrl(key);
            }
            postEditDto.setPostId(postId);
            postEditDto.setPostName(postName);
            postEditDto.setPostReflections(postReflections);
            PostResponse postResponse = postService.updatePost(postId, postEditDto);
            System.out.println(postResponse);
            return new ResponseEntity<>(postResponse, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        
        System.out.println("deleted");
        return ResponseEntity.status(HttpStatus.OK)
                            .body("Project deleted");
    }

}

