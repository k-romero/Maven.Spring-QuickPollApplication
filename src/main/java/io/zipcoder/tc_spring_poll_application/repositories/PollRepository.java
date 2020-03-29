package io.zipcoder.tc_spring_poll_application.repositories;


import io.zipcoder.tc_spring_poll_application.domain.Poll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll,Long> {
}
