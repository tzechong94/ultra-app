package ultra.com.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.CommentDto;
import ultra.com.server.service.CommentService;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@CrossOrigin("*")
public class CommentController {
    
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        CommentDto commentPosted = commentService.save(commentDto);
        return new ResponseEntity<>(commentPosted, HttpStatus.CREATED);
    }

    @GetMapping(params = "postId")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@RequestParam Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

}
