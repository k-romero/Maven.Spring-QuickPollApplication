package io.zipcoder.tc_spring_poll_application.controllers;

import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.dtos.OptionCount;
import io.zipcoder.tc_spring_poll_application.dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

@RestController
public class ComputeResultController {

    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/computeresult")
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);

        Map<Long,OptionCount> map = new TreeMap<>();
        Integer totals = 0;
        for (Vote v : allVotes) {
            totals++;
            Long vId = v.getId();
            if(map.containsKey(vId)){
                OptionCount count = map.get(v.getId());
                count.setCount(count.getCount()+1);
                map.replace(vId,map.get(vId),count);
            } else {
               OptionCount newCount = new OptionCount();
               newCount.setCount(1);
               newCount.setOptionId(v.getOption().getId());
               map.put(v.getId(),newCount);
            }
        }
        voteResult.setTotalVotes(totals);
        voteResult.setResults(map.values());
        return new ResponseEntity<>(voteResult, HttpStatus.OK);
    }
}