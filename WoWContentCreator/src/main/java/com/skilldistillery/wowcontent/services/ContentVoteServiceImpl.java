package com.skilldistillery.wowcontent.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.skilldistillery.wowcontent.entities.Content;
import com.skilldistillery.wowcontent.entities.ContentVote;
import com.skilldistillery.wowcontent.entities.User;
import com.skilldistillery.wowcontent.repositories.ContentRepository;
import com.skilldistillery.wowcontent.repositories.ContentVoteRepository;
import com.skilldistillery.wowcontent.repositories.UserRepository;

public class ContentVoteServiceImpl implements ContentVoteService {

	@Autowired
	private ContentVoteRepository voteRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContentRepository contentRepo;
	
	@Override
	public List<ContentVote> index() {
		return voteRepo.findAll();
	}

	@Override
	public ContentVote create(String username, int contentId, ContentVote vote) {
		User user = userRepo.findByUsername(username);
		Content content = null;
		Optional<Content> contentOpt = contentRepo.findById(contentId);
		if (contentOpt.isPresent()) {
			content = contentOpt.get();
		}
		if (user != null && content != null) {
			vote.setUser(user);
			vote.setContent(content);
			return voteRepo.saveAndFlush(vote);
		}
		return null;
	}

	@Override
	public ContentVote update(String username, int contentId, int voteId, ContentVote vote) {
		ContentVote managedVote = voteRepo.findByUser_UsernameAndId(username, voteId);
		if (managedVote != null) {
			managedVote.setUpvoted(vote.getUpvoted());
			return voteRepo.saveAndFlush(vote);
		}
		return null;
	}

}
