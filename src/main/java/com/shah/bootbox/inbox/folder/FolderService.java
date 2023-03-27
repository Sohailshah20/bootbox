package com.shah.bootbox.inbox.folder;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> getDefaultFolders(String userId){
        return Arrays.asList(
                new Folder(userId,"email","blue"),
                new Folder(userId,"sent items","green"),
                new Folder(userId,"important","red")
        );
    }
}
