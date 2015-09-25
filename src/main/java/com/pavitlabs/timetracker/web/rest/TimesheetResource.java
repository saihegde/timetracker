package com.pavitlabs.timetracker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pavitlabs.timetracker.domain.Timesheet;
import com.pavitlabs.timetracker.repository.TimesheetRepository;
import com.pavitlabs.timetracker.web.rest.util.PaginationUtil;
import com.pavitlabs.timetracker.web.rest.dto.TimesheetDTO;
import com.pavitlabs.timetracker.web.rest.mapper.TimesheetMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Timesheet.
 */
@RestController
@RequestMapping("/api")
public class TimesheetResource {

    private final Logger log = LoggerFactory.getLogger(TimesheetResource.class);

    @Inject
    private TimesheetRepository timesheetRepository;

    @Inject
    private TimesheetMapper timesheetMapper;

    /**
     * POST  /timesheets -> Create a new timesheet.
     */
    @RequestMapping(value = "/timesheets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetDTO> create(@RequestBody TimesheetDTO timesheetDTO) throws URISyntaxException {
        log.debug("REST request to save Timesheet : {}", timesheetDTO);
        if (timesheetDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new timesheet cannot already have an ID").body(null);
        }
        Timesheet timesheet = timesheetMapper.timesheetDTOToTimesheet(timesheetDTO);
        Timesheet result = timesheetRepository.save(timesheet);
        return ResponseEntity.created(new URI("/api/timesheets/" + timesheetDTO.getId())).body(timesheetMapper.timesheetToTimesheetDTO(result));
    }

    /**
     * PUT  /timesheets -> Updates an existing timesheet.
     */
    @RequestMapping(value = "/timesheets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetDTO> update(@RequestBody TimesheetDTO timesheetDTO) throws URISyntaxException {
        log.debug("REST request to update Timesheet : {}", timesheetDTO);
        if (timesheetDTO.getId() == null) {
            return create(timesheetDTO);
        }
        Timesheet timesheet = timesheetMapper.timesheetDTOToTimesheet(timesheetDTO);
        Timesheet result = timesheetRepository.save(timesheet);
        return ResponseEntity.ok().body(timesheetMapper.timesheetToTimesheetDTO(result));
    }

    /**
     * GET  /timesheets -> get all the timesheets.
     */
    @RequestMapping(value = "/timesheets",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TimesheetDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Timesheet> page = timesheetRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timesheets", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(timesheetMapper::timesheetToTimesheetDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /timesheets/:id -> get the "id" timesheet.
     */
    @RequestMapping(value = "/timesheets/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimesheetDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Timesheet : {}", id);
        return Optional.ofNullable(timesheetRepository.findOne(id))
            .map(timesheetMapper::timesheetToTimesheetDTO)
            .map(timesheetDTO -> new ResponseEntity<>(
                timesheetDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /timesheets/:id -> delete the "id" timesheet.
     */
    @RequestMapping(value = "/timesheets/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Timesheet : {}", id);
        timesheetRepository.delete(id);
    }
}
