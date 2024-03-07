package com.skilldistillery.wowcontent.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.wowcontent.entities.Content;
import com.skilldistillery.wowcontent.entities.ContentComment;
import com.skilldistillery.wowcontent.entities.User;
import com.skilldistillery.wowcontent.repositories.CommentRepository;
import com.skilldistillery.wowcontent.repositories.ContentRepository;
import com.skilldistillery.wowcontent.repositories.UserRepository;


@Service
public class CommentServiceImples implements CommentService {

	@Autowired
	private CommentRepository commentRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContentRepository contentRepo;
	
	
	@Override
	public List<ContentComment> index() {
		return commentRepo.findAll();
	}

	@Override
	public List<ContentComment> showAllUserComments(String userName) {
		return commentRepo.findByUser_Username(userName);
	}

	@Override
	public ContentComment create(String userName, ContentComment contentComment, int contentId) {
		User user = userRepo.findByUsername(userName);
		Content content = null;
		Optional<Content> contentOpt = contentRepo.findById(contentId);
		if (contentOpt.isPresent()) {
			content = contentOpt.get();
		}
		if (user != null && content != null) {
			contentComment.setUser(user);
			contentComment.setContent(content);
			return commentRepo.saveAndFlush(contentComment);
		}
		return null;
	}

	@Override
	public ContentComment update(String userName, int commentId, int contentId, ContentComment contentComment) {
		User user = userRepo.findByUsername(userName);
		ContentComment managedComment = commentRepo.findByUser_UsernameAndId(userName, commentId);
		if(managedComment != null) {
			managedComment.setUser(user);
			managedComment.setContent(contentComment.getContent());
			managedComment.setReplyToId(contentComment.getReplyToId());
			managedComment.setMessage(contentComment.getMessage());
			managedComment.setCommentDate(contentComment.getCommentDate());
			managedComment.setUpdatedDate(LocalDateTime.now());
			managedComment.setEnabled(contentComment.isEnabled());
			managedComment.setImageUrl(contentComment.getImageUrl());
			return commentRepo.saveAndFlush(managedComment);
		}
		return null;
	}

	@Override
	public boolean destroy(String userName, int commentId) {
		boolean isDeleted = false;
		if (commentRepo.existsByUser_UsernameAndId(userName, commentId)) {
			commentRepo.deleteById(commentId);
			isDeleted = true;
		}
		return isDeleted;
	}

	@Override
	public List <ContentComment> showCommentsByContentId(int contentId) {
		Optional<Content>contentOpt = contentRepo.findById(contentId);
		if(contentOpt.isPresent()) {
			return contentOpt.get().getContentComments();
		}
		return null;
	}

}