package ultra.com.server.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import ultra.com.server.dto.LikeDto;
import ultra.com.server.exception.PostNotFoundException;
import ultra.com.server.model.Liked;
import ultra.com.server.model.Post;
import ultra.com.server.repo.LikeRepo;
import ultra.com.server.repo.PostRepo;

@Service
@AllArgsConstructor
public class LikeService {
    

    private final PostRepo postRepo;
    private final LikeRepo likeRepo;
    private final AuthService authService;

    @Transactional
    public boolean likePost(LikeDto likeDto) {
        Post post = postRepo.findById(likeDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException("Post not found"));
        System.out.println(post + "POST in server");
    
        Integer likedCount = likeRepo.countByPost(post);
        boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());

        if (likedCount > 0) {
            // boolean currentUserLiked = likeRepo.existsByPostAndUser(post, authService.getCurrentUser());
            System.out.println(currentUserLiked + "CURRENT USR LIKED");
            if (currentUserLiked) {
                post.setLikeCount(likedCount - 1);
                likeRepo.deleteByPostAndUser(post, authService.getCurrentUser());
            } else {
                post.setLikeCount(likedCount + 1);
                likeRepo.save(mapToLiked(likeDto, post));
            }
        } else {
            post.setLikeCount(1);
            likeRepo.save(mapToLiked(likeDto, post));
        }
    
        postRepo.save(post);
        System.out.println(currentUserLiked);
        return !currentUserLiked;
    }
        

    private Liked mapToLiked(LikeDto likeDto, Post post) {
        return Liked.builder()
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
