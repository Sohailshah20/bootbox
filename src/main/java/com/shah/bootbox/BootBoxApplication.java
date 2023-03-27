package com.shah.bootbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shah.bootbox.config.DataStaxAstraProperties;
import com.shah.bootbox.emaillistitem.EmailListItem;
import com.shah.bootbox.emaillistitem.EmailListItemKey;
import com.shah.bootbox.emaillistitem.EmailListItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
public class BootBoxApplication {


    public static void main(String[] args) {
        SpringApplication.run(BootBoxApplication.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

//    @PostConstruct
//    public void init(){
//        for (int i = 0; i < 10; i++) {
//            EmailListItemKey key = new EmailListItemKey();
//            key.setId("SohailShah");
//            key.setLabel("Inbox");
//            key.setTimeUUID(Uuids.timeBased());
//
//            EmailListItem item = new EmailListItem();
//            item.setKey(key);
//            item.setTo(Arrays.asList("SohailShah"));
//            item.setSubject("Subject "+ i);
//            item.setUnread(true);
//
//            emailListItemRepository.save(item);
//        }
//    }
}
