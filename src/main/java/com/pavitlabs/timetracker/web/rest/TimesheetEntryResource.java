package com.pavitlabs.timetracker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pavitlabs.timetracker.domain.TimesheetEntry;
import com.pavitlabs.timetracker.repository.TimesheetEntryRepository;
import com.pavitlabs.timetracker.web.rest.util.PaginationUtil;
import com.pavitlabs.timetracker.web.rest.dto.TimesheetEntryDTO;
import com.pavitlabs.timetracker.web.rest.mapper.TimesheetEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing TimesheetEntry.
 */
@RestController
@RequestMapping("/api")
public class TimesheetEntryResource {

    private final Logger log = LoggerFactory.getLogger(TimesheetEntryResource.class);

    @Inject
    private TimesheetEntryRepository timesheetEntryRepository;

    @Inject
    private TimesheetEntryMapper timesheetEntryMapper;

    /**
     * POST  /timesheetEntrys -> Create a new timesheetEntry.
     */
    @RequestMapping(value = "/timesheetEntrys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetEntryDTO> create(@Valid @RequestBody TimesheetEntryDTO timesheetEntryDTO) throws URISyntaxException {
        log.debug("REST request to save TimesheetEntry : {}", timesheetEntryDTO);
        if (timesheetEntryDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new timesheetEntry cannot already have an ID").body(null);
        }
        TimesheetEntry timesheetEntry = timesheetEntryMapper.timesheetEntryDTOToTimesheetEntry(timesheetEntryDTO);
        TimesheetEntry result = timesheetEntryRepository.save(timesheetEntry);
        return ResponseEntity.created(new URI("/api/timesheetEntrys/" + timesheetEntryDTO.getId())).body(timesheetEntryMapper.timesheetEntryToTimesheetEntryDTO(result));
    }

    /**
     * PUT  /timesheetEntrys -> Updates an existing timesheetEntry.
     */
    @RequestMapping(value = "/timesheetEntrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetEntryDTO> update(@Valid @RequestBody TimesheetEntryDTO timesheetEntryDTO) throws URISyntaxException {
        log.debug("REST request to update TimesheetEntry : {}", timesheetEntryDTO);
        if (timesheetEntryDTO.getId() == null) {
            return create(timesheetEntryDTO);
        }
        TimesheetEntry timesheetEntry = timesheetEntryMapper.timesheetEntryDTOToTimesheetEntry(timesheetEntryDTO);
        TimesheetEntry result = timesheetEntryRepository.save(timesheetEntry);
        return ResponseEntity.ok().body(timesheetEntryMapper.timesheetEntryToTimesheetEntryDTO(result));
    }

    /**
     * GET  /timesheetEntrys -> get all the timesheetEntrys.
     */
    @RequestMapping(value = "/timesheetEntrys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TimesheetEntryDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TimesheetEntry> page = timesheetEntryRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timesheetEntrys", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(timesheetEntryMapper::timesheetEntryToTimesheetEntryDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /timesheetEntrys/:id -> get the "id" timesheetEntry.
     */
    @RequestMapping(value = "/timesheetEntrys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetEntryDTO> get(@PathVariable Long id) {
        log.debug("REST request to get TimesheetEntry : {}", id);
        return Optional.ofNullable(timesheetEntryRepository.findOne(id))
            .map(timesheetEntryMapper::timesheetEntryToTimesheetEntryDTO)
            .map(timesheetEntryDTO -> new ResponseEntity<>(
                timesheetEntryDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /timesheetEntrys/:id -> delete the "id" timesheetEntry.
     */
    @RequestMapping(value = "/timesheetEntrys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete TimesheetEntry : {}", id);
        timesheetEntryRepository.delete(id);
    }
}
