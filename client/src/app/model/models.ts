export interface SignupRequestPayload {
    username: string,
    email: string,
    password: string
}

export interface LoginRequestPayload {
    username: string,
    password: string
}

export interface LoginResponse {
    authenticationToken: string,
    refreshToken: string,
    expiresAt: Date,
    username: string,
}

export interface Post {
    postId: number,
    postName: string,
    postReflections: string,
    likeCount: number,
    username: string,
    id: number,
    commentCount: number,
    postLiked: boolean,
    duration: string,
    userId: number,
    name: string,
    mediaUrl: string,
    currentUserLiked: boolean,
    postLocation: string
}

export interface User {
    email: string,
    profileImageUrl: string,
    passwordHash: string,
    userCreated: Date,
    userId: number,
    username: string,
    projectCount: number
}

export interface CreateProjectModel {
    name: string,
    description: string,
    projectDuration: number,
    completed: boolean
}

export interface EditProjectModel {
    id: number,
    name: string,
    description: string,
    projectDuration: number,
    completed: boolean
}


export interface Project {
    id: number,
    name: string,
    description: string,
    username?: string,
    projectDuration: number,
    postCount: number,
    createdDate: Date,
    duration?: string,
    completed: boolean
}

export interface CreatePostPayload {
    id: number, //projectId
    postName: string,
    postReflections: string,
}


// export interface Update {
//     updateName: string,
//     projectId: number,
//     updateId: number,
//     updateLocation: string,
//     updateCreated: Date,
//     updateReflections: string,
// }

export interface CommentPayload{
    text: string,
    postId: number,
    username: string,
    duration: string,
}

export interface Comment {
    commentId: number,
    text: string,
    postId: number
    username: number,
    createdDate: Date,
    duration?: string;

}

export interface LikePayload {
    postId: number
}

export interface Like {
    likeId: number,
    userId: number,
    updateId: number,
    likeCreated: Date
}

