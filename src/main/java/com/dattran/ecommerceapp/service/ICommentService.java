package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.CommentDTO;
import com.dattran.ecommerceapp.dto.response.CommentResponse;
import com.dattran.ecommerceapp.entity.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> getAllCommentForProducts(String productId);
    Comment createComment(CommentDTO commentDTO);
    List<CommentResponse> getAllCommentWithStarGreaterThan(int star);
}