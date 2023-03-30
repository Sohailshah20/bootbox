package com.shah.bootbox.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shah.bootbox.emaillistitem.EmailListItem;
import com.shah.bootbox.emaillistitem.EmailListItemKey;
import com.shah.bootbox.emaillistitem.EmailListItemRepository;
import com.shah.bootbox.inbox.email.Email;
import com.shah.bootbox.inbox.email.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private EmailRepository emailRepository;
    private EmailListItemRepository emailListItemRepository;

    public EmailService(EmailRepository emailRepository, EmailListItemRepository emailListItemRepository) {
        this.emailRepository = emailRepository;
        this.emailListItemRepository = emailListItemRepository;
    }

    public void sendEmail(
            String from,
            List<String> to,
            String subject,
            String body
    ) {
        Email email = new Email();
        email.setFrom(from);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        email.setTo(to);
        email.setSubject(subject);

        emailRepository.save(email);

        to.forEach(toIds -> {
            EmailListItem item = getEmailListItem(to, subject, email, toIds,"Inbox");
            emailListItemRepository.save(item);
        });

        EmailListItem sentItems = getEmailListItem(to, subject, email, from, "Sent Items");
        emailListItemRepository.save(sentItems);

    }

    private EmailListItem getEmailListItem(List<String> to, String subject, Email email, String itemOwner,String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());
        EmailListItem item = new EmailListItem();
        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);
        return item;
    }
}
