package io.zipcoder.tc_spring_poll_application.controllers;

import io.zipcoder.tc_spring_poll_application.domain.Poll;
import io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import io.zipcoder.tc_spring_poll_application.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class PollController {

    @Autowired
    private PollRepository repo;

    @GetMapping("/polls")
    public ResponseEntity<Iterable<Poll>> getAllPolls(Pageable p) {
        Page<Poll> allPolls = repo.findAll(p);
        return new ResponseEntity<>(allPolls, HttpStatus.OK);
    }

    @PostMapping("/polls/create")
    @Valid
    public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        poll = repo.save(poll);
        URI newPollUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(poll.getId())
                .toUri();
        HttpHeaders response = new HttpHeaders();
        response.setLocation(newPollUri);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/polls/{pollId}")
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        Poll p = repo.findOne(pollId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PutMapping("/polls/{pollId}")
    @Valid
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId){
        verifyPoll(pollId);
        Poll p = repo.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/polls/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        repo.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void verifyPoll(Long pollId){
        if(repo.findOne(pollId) == null){
            throw new ResourceNotFoundException();
        }
    }


}
