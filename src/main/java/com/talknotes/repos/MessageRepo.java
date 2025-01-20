package com.talknotes.repos;

import com.talknotes.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Integer> {

    Iterable<Message> findByTag(String tag);

}
