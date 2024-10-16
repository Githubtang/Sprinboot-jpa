package com.example.springboottyy.service;

import com.example.springboottyy.model.SysPost;
import com.example.springboottyy.repository.SysPostRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Insight
 * @Description: 岗位service
 * @Date: 2024/10/13 1:16
 * @Version: 1.0
 */
@Service
public class PostService {
    @Autowired
    private SysPostRepository postRepository;

    public ApiResponse<?> findAllPost() {
        List<SysPost> posts = postRepository.findAll();
        if (!ObjectUtils.isEmpty(posts)) {
            return ApiResponse.success("全部岗位", posts);
        }
        if (ObjectUtils.isEmpty(posts)) {
            return ApiResponse.error("部门为空");
        }
        return ApiResponse.error();
    }

    public ApiResponse<?> findPostById(Long id) {
        return postRepository.findById(id)
                .map(post -> ApiResponse.success("成功", post))
                .orElse(ApiResponse.error());
    }

    public ApiResponse<?> createPost(SysPost post) {
        SysPost post1 = postRepository.save(post);
        return ApiResponse.success("新增岗位成功", post1);
    }

    public ApiResponse<?> updatePost(SysPost post) {
        Optional<SysPost> post1 = postRepository.findById(post.getId());
        if (post1.isPresent()) {
            SysPost save = postRepository.save(post);
            return ApiResponse.success("修改岗位成功",save);
        }
        return ApiResponse.error();
    }

    public ApiResponse<?> softDeletePost(List<Long> ids) {
        List<SysPost> posts = postRepository.findAllById(ids);
        ArrayList<SysPost> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(posts)) {
            for (SysPost post : posts) {
                post.setDeleted(true);
                postRepository.save(post);
                list.add(post);
            }
            return ApiResponse.success("删除成功", list);
        }
        return ApiResponse.error("删除失败");
    }

    /*关闭岗位*/
    public ApiResponse<?> enabledPost(List<Long> ids) {
        List<SysPost> posts = postRepository.findAllById(ids);
        ArrayList<SysPost> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(posts)) {
            for (SysPost post : posts) {
                post.setEnabled(false);
                postRepository.save(post);
                list.add(post);
            }
            return ApiResponse.success("关闭岗位成功", list);
        }
        return ApiResponse.error();
    }

    /*开启岗位*/
    public ApiResponse<?> unEnabledPost(List<Long> ids) {
        List<SysPost> posts = postRepository.findAllById(ids);
        ArrayList<SysPost> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(posts)) {
            for (SysPost post : posts) {
                post.setEnabled(true);
                postRepository.save(post);
                list.add(post);
            }
            return ApiResponse.success("开启岗位成功", list);
        }
        return ApiResponse.error();
    }
}
