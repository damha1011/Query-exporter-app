package net.javaguides.springboot.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.javaguides.springboot.dto.HomeDTO;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.security.JWTGenerator;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.javaguides.springboot.mapper.DatabaseMapper;
import net.javaguides.springboot.mapper.MetricMapper;
import net.javaguides.springboot.mapper.QueryMapper;
import net.javaguides.springboot.model.*;
import net.javaguides.springboot.repository.DatabaseRepository;
import net.javaguides.springboot.repository.MetricRepository;
import net.javaguides.springboot.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QueryRepository queryRepository;
	@Autowired
	private MetricRepository metricRepository;
	@Autowired
	private DatabaseRepository databaseRepository;
	@Autowired
	private JWTGenerator tokenGenerator;

	@GetMapping("/home/downloadYaml")
	public ResponseEntity<byte[]> downloadYaml(@RequestHeader("Authorization") String authorizationHeader) throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
		}
		String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);

		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		Yaml yaml = new Yaml(dumperOptions);
		List<Database> allDB = databaseRepository.findByUsernameFromJoinedTables(username);
		DatabaseMapper mapperDB = new DatabaseMapper(databaseRepository);
		List<Metric> allMetrics = metricRepository.findByUsernameFromJoinedTables(username);
		MetricMapper mapperMetric = new MetricMapper(metricRepository);
		List<Queries> allQueries = queryRepository.findByUsernameFromJoinedTables(username);
		QueryMapper mapperQuery = new QueryMapper(queryRepository);
		Path currentWorkingDirectory = Paths.get("").toAbsolutePath();
		String currentPath = currentWorkingDirectory.toString()+"/test.yaml";
		System.out.println("Current Working Directory: " + currentWorkingDirectory);
		FileExport finalYamlExport = new FileExport(mapperDB.toYamlMapAdd(allDB),mapperMetric.toYamlMapAdd(allMetrics),mapperQuery.toYamlMapAdd(allQueries));
		Map<String,Object> dbExport = new HashMap<String,Object>();
		dbExport.put("databases",finalYamlExport.getDatabases());
		Map<String,Object> metricExport = new HashMap<String,Object>();
		metricExport.put("metrics",finalYamlExport.getMetrics());
		Map<String,Object> queryExport = new HashMap<String,Object>();
		queryExport.put("queries",finalYamlExport.getQueries());
		try(FileWriter writer = new FileWriter(new File(currentPath))) {
			yaml.dump(dbExport , writer);
//			dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
//			Yaml yaml2 = new Yaml(dumperOptions);
			yaml.dump(metricExport, writer);
			yaml.dump(queryExport, writer);
			System.out.println(" Export to yaml success " );
		} catch (IOException e) {
			System.out.println(" Export to yaml failed " + e.toString() );
			throw new RuntimeException(e);
		}
//		// Replace "path/to/your/file.yaml" with the actual path to your YAML file
		Path path = Paths.get(currentPath);
		// Read the file content
		byte[] content = Files.readAllBytes(path);

		// Set the headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/x-yaml"));
		headers.setContentDispositionFormData("attachment", "file.yaml");
		headers.setContentLength(content.length);
		return ResponseEntity.ok().headers(headers).body(content);
	}
	@GetMapping("/home/viewYaml")
	public String viewYaml(@RequestHeader("Authorization") String authorizationHeader) throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
		}
		String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);

		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setExplicitStart(true); // Bắt buộc để tránh việc xuất dữ liệu mở đầu bằng '---'
		dumperOptions.setExplicitEnd(true);
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
		Yaml yaml = new Yaml(dumperOptions);
		List<Database> allDB = databaseRepository.findByUsernameFromJoinedTables(username);
		DatabaseMapper mapperDB = new DatabaseMapper(databaseRepository);
		List<Metric> allMetrics = metricRepository.findByUsernameFromJoinedTables(username);
		MetricMapper mapperMetric = new MetricMapper(metricRepository);
		List<Queries> allQueries = queryRepository.findByUsernameFromJoinedTables(username);
		QueryMapper mapperQuery = new QueryMapper(queryRepository);
		Path currentWorkingDirectory = Paths.get("").toAbsolutePath();
		String currentPath = currentWorkingDirectory.toString()+"/test.yaml";
		System.out.println("Current Working Directory: " + currentWorkingDirectory);
		FileExport finalYamlExport = new FileExport(mapperDB.toYamlMapAdd(allDB),mapperMetric.toYamlMapAdd(allMetrics),mapperQuery.toYamlMapAdd(allQueries));
		String yamlData = yaml.dump(finalYamlExport);
		return yamlData;
	}
	@GetMapping("/home")
	public HomeDTO homeView(@RequestHeader("Authorization") String authorizationHeader) throws IOException {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
		}
		String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);

		List<Database> allDBbyUser = databaseRepository.findByUsernameFromJoinedTables(username);
		List<Metric> allMetricByUser = metricRepository.findByUsernameFromJoinedTables(username);
		List<Queries> allQueriesByUser = queryRepository.findByUsernameFromJoinedTables(username);
		HomeDTO data = new HomeDTO(allDBbyUser, allQueriesByUser, allMetricByUser);

		return data;
	}
	@PostMapping("/upload-yaml")
	ResponseEntity<String> uploadYamlFile(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
		}

		try {
				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				authorizationHeader = authorizationHeader.substring(7); // Skip "Bearer " prefix
			}
			String username = tokenGenerator.getUsernameFromJWT(authorizationHeader);
			UserEntity user = userRepository.findByUsername(username).get();
//			ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
			byte[] bytes = file.getBytes();
			String yamlData = new String(bytes);
			Yaml yaml = new Yaml();
			FileExport data = yaml.loadAs(yamlData,FileExport.class);
//			FileExport data = objectMapper.readValue(yamlData, FileExport.class);
			DatabaseMapper mapperDB = new DatabaseMapper(databaseRepository);
			MetricMapper mapperMetric = new MetricMapper(metricRepository);
			QueryMapper mapperQuery = new QueryMapper(queryRepository);

			List<Database> databases =  mapperDB.toModelFromYaml(data.getDatabases(),user);
			List<Metric> metrics = mapperMetric.toModelFromYaml(data.getMetrics(),user);
			List<Queries> queries = mapperQuery.toModelFromYaml(data.getQueries(),user);

			if (databases == null) {
				throw new IllegalArgumentException("One of databases has already existed");
			}
			if (metrics == null) {
				throw new IllegalArgumentException("One of metrics has already existed");
			}
			if (queries == null) {
				throw new IllegalArgumentException("One of queries has already existed");
			}

			databaseRepository.saveAll(databases);
			metricRepository.saveAll(metrics);
			queryRepository.saveAll(queries);

			// Xử lý dữ liệu YAML ở đây (ví dụ: lưu vào cơ sở dữ liệu)
			System.out.println("Received YAML data:\n" + yamlData);

			return new ResponseEntity<>("File uploaded and processed successfully", HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error processing the file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	class CustomRepresenter extends Representer {
		public CustomRepresenter() {
			// Override the represent method for List
			this.multiRepresenters.put(List.class, new Represent() {
				@Override
				public Node representData(Object data) {
					List<String> list = (List<String>) data;
					if (list.size() == 1) {
						return representScalar(Tag.SEQ, list.get(0), DumperOptions.ScalarStyle.PLAIN);
					} else {
						return representSequence(Tag.SEQ, list, DumperOptions.FlowStyle.FLOW);
					}
				}
			});
		}
	}
}
