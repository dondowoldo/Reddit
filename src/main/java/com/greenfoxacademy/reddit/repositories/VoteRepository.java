package com.greenfoxacademy.reddit.repositories;

import com.greenfoxacademy.reddit.models.Vote;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends ListCrudRepository<Vote, Long> {
}
