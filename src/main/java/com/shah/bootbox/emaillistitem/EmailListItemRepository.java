package com.shah.bootbox.emaillistitem;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {

    List<EmailListItem> findAllByKey_IdAndKey_Label(String id, String label);
}
