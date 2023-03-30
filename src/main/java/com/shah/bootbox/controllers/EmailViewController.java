package com.shah.bootbox.controllers;

import com.shah.bootbox.emaillistitem.EmailListItemRepository;
import com.shah.bootbox.inbox.email.Email;
import com.shah.bootbox.inbox.email.EmailRepository;
import com.shah.bootbox.inbox.folder.Folder;
import com.shah.bootbox.inbox.folder.FolderRepository;
import com.shah.bootbox.inbox.folder.FolderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.tomcat.util.security.ConcurrentMessageDigest.init;

@Controller
public class EmailViewController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final EmailRepository emailRepository;

    public EmailViewController(FolderRepository folderRepository, FolderService folderService, EmailRepository emailRepository) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailRepository = emailRepository;
    }

    @GetMapping("/email/{id}")
    public String emailView(
            @PathVariable UUID id,
            @AuthenticationPrincipal OAuth2User principal,
            Model model
    ) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        String userId = principal.getAttribute("login");

        //fetching folders
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.getDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (!optionalEmail.isPresent()){
            return "inbox-page";
        }

        Email email = optionalEmail.get();
        String toIds = String.join(",", email.getTo());

        model.addAttribute("email",email);
        model.addAttribute("toIds",toIds);

        return "email-page";

    }
}
