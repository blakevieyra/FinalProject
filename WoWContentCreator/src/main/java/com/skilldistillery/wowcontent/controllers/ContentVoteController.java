package com.skilldistillery.wowcontent.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.wowcontent.entities.Content;
import com.skilldistillery.wowcontent.entities.ContentVote;
import com.skilldistillery.wowcontent.services.ContentService;
import com.skilldistillery.wowcontent.services.ContentVoteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin({ "*", "http://localhost/" })
@RequestMapping("api")
@RestController
public class ContentVoteController {
	@Autowired
	private ContentVoteService voteService;
	@Autowired
	private ContentService contentService;

	@GetMapping("votes")
	public List<ContentVote>showAllVotes() {
		return voteService.index();
	}
	
	
	@GetMapping("content/{content_id}/votes")
	public List<ContentVote> showVotesByContentId(@PathVariable("contentId") int contentId, HttpServletResponse resp) {
		Content content = contentService.showContentById(contentId);
		return voteService.showVotesByContentId(contentId);
	}

	@PostMapping("{contentId}/votes")
	public ResponseEntity<ContentVote> create(@PathVariable int contentId, @RequestBody ContentVote vote,
			Principal principal) {
		ContentVote createdVote = voteService.create(principal.getName(), contentId, vote);
		if (createdVote != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdVote);
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("content/{content_id}/votes/{vote_id}")
	public ContentVote updateContentVote(Principal principal, @RequestBody ContentVote contentVote,
			@PathVariable("contentId") int contentId, @PathVariable("voteId") int voteId, HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			contentVote = voteService.update(principal.getName(), contentId, voteId, contentVote);
			if (contentVote != null) {
				resp.setStatus(201);
			}
		} catch (Exception e) {
			resp.setStatus(400);
			e.printStackTrace();
			contentVote = null;
		}
		return contentVote;
	}
}
