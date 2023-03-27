package com.shah.bootbox.controllers;

import com.shah.bootbox.inbox.folder.Folder;
import com.shah.bootbox.inbox.folder.FolderRepository;
import com.shah.bootbox.inbox.folder.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InboxController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;

    @Autowired
    public InboxController(FolderRepository folderRepository, FolderService folderService) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        String id = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(id);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.getDefaultFolders(id);
        model.addAttribute("defaultFolders",defaultFolders);

        return "inbox-page";
    }
}
