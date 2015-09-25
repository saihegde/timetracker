package com.pavitlabs.timetracker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pavitlabs.timetracker.domain.Client;
import com.pavitlabs.timetracker.repository.ClientRepository;
import com.pavitlabs.timetracker.web.rest.util.PaginationUtil;
import com.pavitlabs.timetracker.web.rest.dto.ClientDTO;
import com.pavitlabs.timetracker.web.rest.mapper.ClientMapper;
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
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientMapper clientMapper;

    /**
     * POST  /clients -> Create a new client.
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> create(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new client cannot already have an ID").body(null);
        }
        Client client = clientMapper.clientDTOToClient(clientDTO);
        Client result = clientRepository.save(client);
        return ResponseEntity.created(new URI("/api/clients/" + clientDTO.getId())).body(clientMapper.clientToClientDTO(result));
    }

    /**
     * PUT  /clients -> Updates an existing client.
     */
    @RequestMapping(value = "/clients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> update(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to update Client : {}", clientDTO);
        if (clientDTO.getId() == null) {
            return create(clientDTO);
        }
        Client client = clientMapper.clientDTOToClient(clientDTO);
        Client result = clientRepository.save(client);
        return ResponseEntity.ok().body(clientMapper.clientToClientDTO(result));
    }

    /**
     * GET  /clients -> get all the clients.
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ClientDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Client> page = clientRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clients", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(clientMapper::clientToClientDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /clients/:id -> get the "id" client.
     */
    @RequestMapping(value = "/clients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClientDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        return Optional.ofNullable(clientRepository.findOne(id))
            .map(clientMapper::clientToClientDTO)
            .map(clientDTO -> new ResponseEntity<>(
                clientDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clients/:id -> delete the "id" client.
     */
    @RequestMapping(value = "/clients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientRepository.delete(id);
    }
}
