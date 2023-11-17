package com.eversis.esa.geoss.curated.resources.event;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.EntryEvent;
import com.eversis.esa.geoss.curated.resources.repository.EntryEventRepository;
import com.eversis.esa.geoss.curated.resources.repository.EntryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * The type Entry event poller.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class EntryEventPoller {

    private final EntryEventRepository entryEventRepository;

    private final EntryRepository entryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Pool entry events.
     */
    @Scheduled(
            initialDelayString = "${geoss.curated.resources.entry-event-pooler.initial-delay}",
            fixedDelayString = "${geoss.curated.resources.entry-event-pooler.fixed-delay}"
    )
    void poolEntryEvents() {
        List<EntryEvent> entryEvents = entryEventRepository.findAll();
        log.info("Pool entryEvents:{}", entryEvents.size());
        for (var entryEvent : entryEvents) {
            log.debug("entryEvent:{}", entryEvent);
            Optional<Entry> entry = entryRepository.findById(entryEvent.getEntryId());
            entry.ifPresent(applicationEventPublisher::publishEvent);
            entryEventRepository.delete(entryEvent);
        }
    }
}
