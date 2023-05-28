package ultra.com.server.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ultra.com.server.dto.PostEditDto;
import ultra.com.server.dto.PostRequest;
import ultra.com.server.dto.PostResponse;
import ultra.com.server.exception.PostNotFoundException;
import ultra.com.server.exception.ProjectNotFoundException;
import ultra.com.server.exception.UltraException;
import ultra.com.server.mapper.PostMapper;
import ultra.com.server.model.Post;
import ultra.com.server.model.Project;
import ultra.com.server.model.User;
import ultra.com.server.repo.LikeRepo;
import ultra.com.server.repo.PostRepo;
import ultra.com.server.repo.ProjectRepo;

@Service
@Transactional
public class PostService {
    @Value("${googleapikey}")
    private String googleApiKey;

    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeRepo likeRepo;
    
    // // latlng=40.714224,-73.961452&key=YOUR_API_KEY


    public String getReverseGeolocation(String latLngString) {
        String REVERSEGEOLOCATIONURL_STRING = "https://maps.googleapis.com/maps/api/geocode/json";

    
        String url = UriComponentsBuilder.fromUriString(REVERSEGEOLOCATIONURL_STRING)
                        .queryParam("latlng", latLngString)
                        .queryParam("key", googleApiKey)
                        .build()
                        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> res = null;
        res = template.exchange(req, String.class);
        String payload = res.getBody();
        System.out.println("Payload" + payload);
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject geolocationResults = reader.readObject();
        JsonArray jsonArray = geolocationResults.getJsonArray("results");

        JsonObject countryObj = jsonArray.getJsonObject(jsonArray.size()-1);
        JsonObject cityObj = jsonArray.getJsonObject(jsonArray.size()-2);
        
        String countryName = countryObj.getJsonArray("address_components").getJsonObject(0).getJsonString("long_name").toString();
        String cityName = cityObj.getJsonArray("address_components").getJsonObject(0).getJsonString("long_name").toString();
        String postLocation = cityName + ", " + countryName;
        return postLocation;
    }


    public PostResponse save(PostRequest postRequest) {
        Project project = projectRepo.findById(postRequest.getId())
                .orElseThrow(() -> new ProjectNotFoundException("project not found" +(postRequest.getId().toString())));
        User user = project.getUser();

        if (user == authService.getCurrentUser()){
            System.out.println("SAVING POST");
            return postMapper.mapToDto(postRepo.save(postMapper.map(postRequest, project, authService.getCurrentUser())));
        }
        System.out.println("NOT SAVING");
        return null;
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) throws JsonMappingException, JsonProcessingException {
        Post post = postRepo.findById(postId)
                        .orElseThrow(() -> new PostNotFoundException("post id " + postId.toString() + " not found"));
        boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
        
        PostResponse postResponse = postMapper.mapToDto(post);
        postResponse.setCurrentUserLiked(currentUserLiked);
        
        return postResponse;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPostsByProjectId(Long id) {
        Project project = projectRepo.findById(id).orElseThrow(()-> new ProjectNotFoundException("Project not found"));
        List<Post> listOfPosts = postRepo.findAllByProject(project);
        List<PostResponse> listOfPostResponses = new ArrayList<PostResponse>();
        for (Post post : listOfPosts) {
            boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
            PostResponse postResponse = postMapper.mapToDto(post);
            postResponse.setCurrentUserLiked(currentUserLiked);
            listOfPostResponses.add(postResponse);
        }
        return listOfPostResponses;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> listOfPosts = postRepo.findAll();
        List<PostResponse> listOfPostResponses = new ArrayList<PostResponse>();
        for (Post post : listOfPosts) {
            boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
            PostResponse postResponse = postMapper.mapToDto(post);
            postResponse.setCurrentUserLiked(currentUserLiked);
            listOfPostResponses.add(postResponse);
        }

        return listOfPostResponses;

    }

    public PostResponse updatePost(Long postId, PostEditDto postEditDto) {
        Post existingPost = postRepo.findById(postId)
            .orElseThrow(() -> new UltraException("No post found"));
        existingPost.setPostName(postEditDto.getPostName());
        existingPost.setPostReflections(postEditDto.getPostReflections());
        if (postEditDto.getMediaUrl()!=null){
            existingPost.setMediaUrl(postEditDto.getMediaUrl());
        } 
        return postMapper.mapToDto(postRepo.save(existingPost));
    }


    public void deletePost(Long id) {
        Post post = postRepo.findById(id)
            .orElseThrow(() -> new UltraException("No post found"));
    
        postRepo.delete(post);
    }


}
