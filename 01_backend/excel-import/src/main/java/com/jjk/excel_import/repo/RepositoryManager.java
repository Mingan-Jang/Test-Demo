package com.jjk.excel_import.repo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.jjk.excel_import.po.EventPO;
import com.jjk.excel_import.po.ExtendPO;

import jakarta.annotation.PostConstruct;

@Component
public class RepositoryManager {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ExtendRepository extendRepository;

    private Map<Class<?>, JpaRepository<?, Long>> repositoryMap = new HashMap<>();

    @PostConstruct
    public void init() {
        repositoryMap.put(EventPO.class, eventRepository);
        repositoryMap.put(ExtendPO.class, extendRepository);
    }

    @SuppressWarnings("unchecked")
    public <T> void saveToDatabase(T po) {
        JpaRepository<T, Long> repository = (JpaRepository<T, Long>) repositoryMap.get(po.getClass());
        if (repository != null) {
            repository.save(po);
        } else {
            throw new IllegalArgumentException("Unknown PO type: " + po.getClass().getName());
        }
    }
}
