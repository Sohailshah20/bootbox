package com.shah.bootbox.controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shah.bootbox.emaillistitem.EmailListItem;
import com.shah.bootbox.emaillistitem.EmailListItemKey;
import com.shah.bootbox.emaillistitem.EmailListItemRepository;
import com.shah.bootbox.inbox.folder.Folder;
import com.shah.bootbox.inbox.folder.FolderRepository;
import com.shah.bootbox.inbox.folder.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class InboxController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final EmailListItemRepository emailListItemRepository;

    @Autowired
    public InboxController(
            FolderRepository folderRepository,
            FolderService folderService,
            EmailListItemRepository emailListItemRepository
    ) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailListItemRepository = emailListItemRepository;
    }

    @GetMapping("/")
    public String homePage(
            @RequestParam(required = false) String folder,
            @AuthenticationPrincipal OAuth2User principal,
            Model model
    ) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        String id = principal.getAttribute("login");
        init(id);

        //fetching folders
        List<Folder> userFolders = folderRepository.findAllById(id);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.getDefaultFolders(id);
        model.addAttribute("defaultFolders", defaultFolders);

        //fetching messages
        if (!StringUtils.hasText(folder)){
            folder = "Inbox";
        }

        List<EmailListItem> emailList = emailListItemRepository.findAllByKey_IdAndKey_Label(id, folder);
        if (emailList.isEmpty()) {
            System.out.println("list is empty");
        }

        PrettyTime prettyTime = new PrettyTime();

        emailList.stream().forEach(emailItem -> {
            UUID timeUUID = emailItem.getKey().getTimeUUID();
            Date date = new Date(Uuids.unixTimestamp(timeUUID));
            emailItem.setAgeTimeStamp(prettyTime.format(date));
        });

        model.addAttribute("folderName", folder);
        model.addAttribute("emailList", emailList);


        return "inbox-page";
    }


    public void init(String id) {
        for (int i = 0; i < 5; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId(id);
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem item = new EmailListItem();
            item.setKey(key);
            item.setTo(Arrays.asList("SohailShah"));
            item.setSubject("Subject " + i);
            item.setUnread(true);

            emailListItemRepository.save(item);
        }
    }
}
