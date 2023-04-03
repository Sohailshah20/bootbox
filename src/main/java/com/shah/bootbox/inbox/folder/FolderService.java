package com.shah.bootbox.inbox.folder;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private final UnreadEmailStatsRepository unreadEmailStatsRepository;

    public FolderService(UnreadEmailStatsRepository unreadEmailStatsRepository) {
        this.unreadEmailStatsRepository = unreadEmailStatsRepository;
    }

    public List<Folder> getDefaultFolders(String userId){
        return Arrays.asList(
                new Folder(userId,"email","blue"),
                new Folder(userId,"sent items","green"),
                new Folder(userId,"important","red")
        );
    }

    public Map<String ,Integer> mapCountToLabel(String id){
            List<UnreadEmailCounter> stats = unreadEmailStatsRepository.findAllById(id);
            return stats.stream()
                    .collect(Collectors.toMap(UnreadEmailCounter::getLabel,UnreadEmailCounter::getUnreadcount));
    }
}
