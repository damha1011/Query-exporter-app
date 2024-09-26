package net.javaguides.springboot.controller;

import com.viettel.security.PassTranformer;
import net.javaguides.springboot.dto.DatabaseDTO;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Database;
import net.javaguides.springboot.model.UserEntity;
import net.javaguides.springboot.repository.DatabaseRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.security.JWTGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class DatabaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DatabaseRepository databaseRepository;

	@Autowired
	private JWTGenerator tokenGenerator;
	// get all databases
	@GetMapping("/databases")
	public List<Database> getAllDatabases(@RequestHeader("Authorization") String authorizationHeader){
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
		}
		String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);
		LOGGER.info("Get Databases Infor by : ", username);
		List<Database> res = databaseRepository.findByUsernameFromJoinedTables(username);
		return res;
	}		
	
	// create database rest api
	@PostMapping("/databases")
	public Database createDatabase(@RequestHeader("Authorization") String authorizationHeader, @RequestBody DatabaseDTO databaseDTO) throws Exception {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
		}
		String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);
		LOGGER.info("Create Database by user : ", username);
		String linkDB = databaseDTO.getLink();
		String [] split = linkDB.split("://|@|:|/");

		List<Database> databases = databaseRepository.findByUsernameFromJoinedTables(username);
		for (Database database : databases) {
			System.out.println("hostname: " + database.getHostName() + " - " + split[5]);
			if (database.getName().equals(split[5]))
				throw new IllegalArgumentException("Database already exists.");
		}

		Database database = new Database();
		database.setLink(databaseDTO.getLink());
		database.setDsn(PassTranformer.encrypt(linkDB));

		String label = "hostname: " + split[3] + "\\n" + "serviceCode: " + databaseDTO.getServiceCode();
		database.setLabel(label +  "\\n" + databaseDTO.getLabel());
		database.setHostName(split[3]);
		database.setName(split[5]);
		database.setServiceCode(databaseDTO.getServiceCode());
		database.setAutoCommit(databaseDTO.getAutoCommit());
		database.setConnectSQL(databaseDTO.getConnectSQL());
		database.setKeepConnect(databaseDTO.getKeepConnect());
		UserEntity user = userRepository.findByUsername(username).get();
		database.setUser(user);
		return databaseRepository.save(database);
	}
	
	// get database by id rest api
	@GetMapping("/databases/{id}")
	public ResponseEntity<Database> getDatabaseById(@PathVariable Long id) {
		LOGGER.info("View Database by ID : ", id);
		Database database = databaseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Database not exist with id :" + id));
		return ResponseEntity.ok(database);
	}

	// update database rest api
	@PutMapping("/databases/{id}")
	public ResponseEntity<Database> updateDatabase(@PathVariable Long id, @RequestBody DatabaseDTO databaseDetails) throws Exception {
		Database database = databaseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Database not exist with id :" + id));
		LOGGER.info("Update Database by ID : ", id);

		System.out.println("connectSQL: " + database.getConnectSQL());

		String [] split = databaseDetails.getLink().split("://|@|:|/");
		database.setDsn( PassTranformer.encrypt(databaseDetails.getLink()));
		database.setLabel(databaseDetails.getLabel());
		database.setLink(databaseDetails.getLink());
		database.setHostName(split[3]);
		database.setName(split[5]);
		database.setConnectSQL(databaseDetails.getConnectSQL());
		database.setAutoCommit(databaseDetails.getAutoCommit());
		database.setKeepConnect(databaseDetails.getKeepConnect());
		database.setServiceCode(databaseDetails.getServiceCode());
		Database updatedDatabase = databaseRepository.save(database);
		return ResponseEntity.ok(updatedDatabase);
	}
	
	// delete database rest api
	@DeleteMapping("/databases/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteDatabase(@PathVariable Long id){
		LOGGER.info("Delete Database with ID : ", id);
		Database database = databaseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Database not exist with id :" + id));
		
		databaseRepository.delete(database);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}


