package com.shah.bootbox.controllers;

import com.shah.bootbox.inbox.EmailService;
import com.shah.bootbox.inbox.folder.Folder;
import com.shah.bootbox.inbox.folder.FolderRepository;
import com.shah.bootbox.inbox.folder.FolderService;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ComposeController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;

    private final EmailService emailService;

    public ComposeController(FolderRepository folderRepository, FolderService folderService,EmailService emailService) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailService = emailService;
    }

    @GetMapping("/compose")
    public String getComposePage(
            @RequestParam(required = false) String to,
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

        List<String> uniqueIds = splitIds(to);
        model.addAttribute("toIds", String.join(", ", uniqueIds));

        return "compose-page";
    }

    private static List<String> splitIds(String to) {
        if (!StringUtils.hasText(to)) {
            return new ArrayList<String>();
        }

        String[] splitIds = to.split(",");
        List<String> uniqueIds = Arrays.asList(splitIds)
                .stream()
                .map(id -> StringUtils.trimWhitespace(id))
                .filter(id -> StringUtils.hasText(id))
                .distinct()
                .collect(Collectors.toList());
        return uniqueIds;
    }

    public ModelAndView sendEmail(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody MultiValueMap<String, String> formData
    ) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }

        String from = principal.getAttribute("login");
        List<String> to = splitIds(formData.getFirst("toIds"));
        String subject = formData.get("subject");
        String body = formData.get("body");

        return new ModelAndView("redirect:/");


    }
}
