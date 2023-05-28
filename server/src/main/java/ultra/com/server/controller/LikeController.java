package ultra.com.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.LikeDto;
import ultra.com.server.service.LikeService;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin("*")
@AllArgsConstructor
public class LikeController {
    
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Boolean> likePost(@RequestBody LikeDto likeDto) {
        System.out.println(likeDto + "LIKEDTO");
        return new ResponseEntity<Boolean>(likeService.likePost(likeDto), HttpStatus.OK);
    }

}
