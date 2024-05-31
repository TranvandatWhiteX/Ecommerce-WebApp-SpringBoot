package com.dattran.ecommerceapp.service;

import com.dattran.ecommerceapp.dto.ArticleDTO;
import com.dattran.ecommerceapp.entity.Article;

import java.util.List;

public interface IArticleService {
    List<Article> getAllArticles();
    Article createArticle(ArticleDTO articleDTO);
    void deleteArticle(String id);
    Article updateArticle(Article article);
}