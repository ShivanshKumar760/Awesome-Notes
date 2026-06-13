// ============================================================
// FILE: src/main/resources/application.properties
// ============================================================
/*
spring.application.name=springboot-judge
server.port=8080

# Database Configuration (H2 for simplicity)
spring.datasource.url=jdbc:h2:mem:judgedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Temp directory for code execution
judge.temp.directory=/tmp/springboot-judge
judge.timeout.seconds=30
*/

// ============================================================
// FILE: pom.xml (dependencies section)
// ============================================================
/*
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
*/

// ============================================================
// FILE: src/main/java/com/example/judge/SpringBootJudgeApplication.java
// ============================================================
package com.example.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootJudgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootJudgeApplication.class, args);
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/entity/Submission.java
// ============================================================
package com.example.judge.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String projectType; // "todo" or "blog"
    private Integer taskId;
    
    @Column(length = 10000)
    private String code;
    
    private String status; // PENDING, RUNNING, PASSED, FAILED
    
    @Column(length = 5000)
    private String output;
    
    private Integer testsPassed;
    private Integer testsTotal;
    
    private LocalDateTime submittedAt;
    private LocalDateTime evaluatedAt;
    
    @PrePersist
    public void prePersist() {
        submittedAt = LocalDateTime.now();
        status = "PENDING";
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/entity/Task.java
// ============================================================
package com.example.judge.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String projectType;
    private Integer taskNumber;
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(length = 5000)
    private String instructions;
    
    @Column(length = 5000)
    private String starterCode;
    
    @Column(length = 10000)
    private String testCode; // JUnit test code
    
    @Column(length = 10000)
    private String validationRules; // JSON format
}

// ============================================================
// FILE: src/main/java/com/example/judge/dto/SubmitCodeRequest.java
// ============================================================
package com.example.judge.dto;

import lombok.Data;

@Data
public class SubmitCodeRequest {
    private String projectType;
    private Integer taskId;
    private String code;
}

// ============================================================
// FILE: src/main/java/com/example/judge/dto/SubmissionResponse.java
// ============================================================
package com.example.judge.dto;

import lombok.Data;

@Data
public class SubmissionResponse {
    private Long submissionId;
    private String status;
    private String output;
    private Integer testsPassed;
    private Integer testsTotal;
    private Boolean allPassed;
}

// ============================================================
// FILE: src/main/java/com/example/judge/dto/TestResult.java
// ============================================================
package com.example.judge.dto;

import lombok.Data;

@Data
public class TestResult {
    private String testName;
    private Boolean passed;
    private String message;
    private String expected;
    private String actual;
}

// ============================================================
// FILE: src/main/java/com/example/judge/model/ValidationRule.java
// ============================================================
package com.example.judge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationRule {
    private String type; // CONTAINS, REGEX, ANNOTATION, METHOD, FIELD
    private String pattern;
    private String description;
    private String errorMessage;
}

// ============================================================
// FILE: src/main/java/com/example/judge/repository/SubmissionRepository.java
// ============================================================
package com.example.judge.repository;

import com.example.judge.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByProjectTypeAndTaskIdOrderBySubmittedAtDesc(String projectType, Integer taskId);
}

// ============================================================
// FILE: src/main/java/com/example/judge/repository/TaskRepository.java
// ============================================================
package com.example.judge.repository;

import com.example.judge.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByProjectTypeAndTaskNumber(String projectType, Integer taskNumber);
    List<Task> findByProjectTypeOrderByTaskNumber(String projectType);
}

// ============================================================
// FILE: src/main/java/com/example/judge/service/ValidationRuleProvider.java
// ============================================================
package com.example.judge.service;

import com.example.judge.model.ValidationRule;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationRuleProvider {
    
    public List<ValidationRule> getRulesForTask(String projectType, Integer taskId) {
        List<ValidationRule> rules = new ArrayList<>();
        
        if ("todo".equals(projectType)) {
            switch (taskId) {
                case 1: // Todo Entity
                    rules.add(new ValidationRule("ANNOTATION", "@Entity", "Has @Entity annotation", "Missing @Entity annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@Id", "Has @Id annotation", "Missing @Id annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@GeneratedValue", "Has @GeneratedValue annotation", "Missing @GeneratedValue annotation"));
                    rules.add(new ValidationRule("FIELD", "Long\\s+id", "Has id field of type Long", "Missing id field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+title", "Has title field", "Missing title field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+description", "Has description field", "Missing description field"));
                    rules.add(new ValidationRule("FIELD", "boolean\\s+completed", "Has completed field", "Missing completed field"));
                    break;
                
                case 2: // Todo Repository
                    rules.add(new ValidationRule("CONTAINS", "interface TodoRepository", "Has TodoRepository interface", "Missing TodoRepository interface"));
                    rules.add(new ValidationRule("CONTAINS", "extends JpaRepository", "Extends JpaRepository", "Must extend JpaRepository"));
                    rules.add(new ValidationRule("METHOD", "findByCompleted", "Has findByCompleted method", "Missing findByCompleted method"));
                    break;
                
                case 3: // Todo DTO
                    rules.add(new ValidationRule("REGEX", "(class|record)\\s+TodoDTO", "Has TodoDTO class/record", "Missing TodoDTO class or record"));
                    rules.add(new ValidationRule("CONTAINS", "Long id", "Has id field", "Missing id field"));
                    rules.add(new ValidationRule("CONTAINS", "String title", "Has title field", "Missing title field"));
                    rules.add(new ValidationRule("CONTAINS", "String description", "Has description field", "Missing description field"));
                    rules.add(new ValidationRule("CONTAINS", "boolean completed", "Has completed field", "Missing completed field"));
                    break;
                
                case 4: // Todo Service
                    rules.add(new ValidationRule("ANNOTATION", "@Service", "Has @Service annotation", "Missing @Service annotation"));
                    rules.add(new ValidationRule("CONTAINS", "TodoRepository", "Injects TodoRepository", "Missing TodoRepository dependency"));
                    rules.add(new ValidationRule("METHOD", "getAllTodos", "Has getAllTodos method", "Missing getAllTodos method"));
                    rules.add(new ValidationRule("METHOD", "getTodoById", "Has getTodoById method", "Missing getTodoById method"));
                    rules.add(new ValidationRule("METHOD", "createTodo", "Has createTodo method", "Missing createTodo method"));
                    rules.add(new ValidationRule("METHOD", "updateTodo", "Has updateTodo method", "Missing updateTodo method"));
                    rules.add(new ValidationRule("METHOD", "deleteTodo", "Has deleteTodo method", "Missing deleteTodo method"));
                    break;
                
                case 5: // Todo Controller
                    rules.add(new ValidationRule("ANNOTATION", "@RestController", "Has @RestController annotation", "Missing @RestController annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@RequestMapping", "Has @RequestMapping annotation", "Missing @RequestMapping annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@GetMapping", "Has GET mapping", "Missing @GetMapping"));
                    rules.add(new ValidationRule("ANNOTATION", "@PostMapping", "Has POST mapping", "Missing @PostMapping"));
                    rules.add(new ValidationRule("ANNOTATION", "@PutMapping", "Has PUT mapping", "Missing @PutMapping"));
                    rules.add(new ValidationRule("ANNOTATION", "@DeleteMapping", "Has DELETE mapping", "Missing @DeleteMapping"));
                    break;
            }
        } else if ("blog".equals(projectType)) {
            switch (taskId) {
                case 1: // User Entity
                    rules.add(new ValidationRule("ANNOTATION", "@Entity", "Has @Entity annotation", "Missing @Entity annotation"));
                    rules.add(new ValidationRule("FIELD", "Long\\s+id", "Has id field", "Missing id field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+username", "Has username field", "Missing username field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+password", "Has password field", "Missing password field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+email", "Has email field", "Missing email field"));
                    rules.add(new ValidationRule("CONTAINS", "Set<String> roles", "Has roles field", "Missing roles field"));
                    rules.add(new ValidationRule("ANNOTATION", "@ElementCollection", "Has @ElementCollection for roles", "Missing @ElementCollection"));
                    break;
                
                case 2: // Post Entity
                    rules.add(new ValidationRule("ANNOTATION", "@Entity", "Has @Entity annotation", "Missing @Entity annotation"));
                    rules.add(new ValidationRule("FIELD", "String\\s+title", "Has title field", "Missing title field"));
                    rules.add(new ValidationRule("FIELD", "String\\s+content", "Has content field", "Missing content field"));
                    rules.add(new ValidationRule("FIELD", "LocalDateTime\\s+createdAt", "Has createdAt field", "Missing createdAt field"));
                    rules.add(new ValidationRule("ANNOTATION", "@ManyToOne", "Has @ManyToOne relationship", "Missing @ManyToOne annotation"));
                    rules.add(new ValidationRule("FIELD", "User\\s+author", "Has author field", "Missing author field"));
                    break;
                
                case 3: // JWT Utility
                    rules.add(new ValidationRule("ANNOTATION", "@Component", "Has @Component annotation", "Missing @Component annotation"));
                    rules.add(new ValidationRule("METHOD", "generateToken", "Has generateToken method", "Missing generateToken method"));
                    rules.add(new ValidationRule("METHOD", "extractUsername", "Has extractUsername method", "Missing extractUsername method"));
                    rules.add(new ValidationRule("METHOD", "validateToken", "Has validateToken method", "Missing validateToken method"));
                    rules.add(new ValidationRule("CONTAINS", "Jwts.builder()", "Uses Jwts.builder()", "Should use Jwts.builder() for token creation"));
                    break;
                
                case 4: // Auth Filter
                    rules.add(new ValidationRule("ANNOTATION", "@Component", "Has @Component annotation", "Missing @Component annotation"));
                    rules.add(new ValidationRule("CONTAINS", "extends OncePerRequestFilter", "Extends OncePerRequestFilter", "Must extend OncePerRequestFilter"));
                    rules.add(new ValidationRule("METHOD", "doFilterInternal", "Has doFilterInternal method", "Missing doFilterInternal method"));
                    rules.add(new ValidationRule("CONTAINS", "Authorization", "Checks Authorization header", "Should check Authorization header"));
                    rules.add(new ValidationRule("CONTAINS", "SecurityContextHolder", "Sets SecurityContext", "Should use SecurityContextHolder"));
                    break;
                
                case 5: // Security Config
                    rules.add(new ValidationRule("ANNOTATION", "@Configuration", "Has @Configuration annotation", "Missing @Configuration annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@EnableWebSecurity", "Has @EnableWebSecurity annotation", "Missing @EnableWebSecurity annotation"));
                    rules.add(new ValidationRule("CONTAINS", "SecurityFilterChain", "Defines SecurityFilterChain", "Missing SecurityFilterChain bean"));
                    rules.add(new ValidationRule("CONTAINS", "sessionManagement", "Configures session management", "Missing session management configuration"));
                    rules.add(new ValidationRule("CONTAINS", "SessionCreationPolicy.STATELESS", "Sets session to STATELESS", "Should set STATELESS session policy"));
                    rules.add(new ValidationRule("CONTAINS", "csrf", "Configures CSRF", "Missing CSRF configuration"));
                    rules.add(new ValidationRule("CONTAINS", "/api/auth/**", "Permits auth endpoints", "Should permit /api/auth/** endpoints"));
                    rules.add(new ValidationRule("CONTAINS", "BCryptPasswordEncoder", "Defines BCryptPasswordEncoder", "Missing BCryptPasswordEncoder bean"));
                    break;
                
                case 6: // Auth Service & Controller
                    rules.add(new ValidationRule("ANNOTATION", "@Service", "Has @Service annotation", "Missing @Service annotation"));
                    rules.add(new ValidationRule("METHOD", "register", "Has register method", "Missing register method"));
                    rules.add(new ValidationRule("METHOD", "login", "Has login method", "Missing login method"));
                    rules.add(new ValidationRule("CONTAINS", "passwordEncoder.encode", "Encodes password", "Should encode password"));
                    rules.add(new ValidationRule("ANNOTATION", "@RestController", "Has @RestController annotation", "Missing @RestController annotation"));
                    rules.add(new ValidationRule("ANNOTATION", "@PostMapping", "Has POST mapping", "Missing @PostMapping"));
                    rules.add(new ValidationRule("CONTAINS", "/register", "Has register endpoint", "Missing /register endpoint"));
                    rules.add(new ValidationRule("CONTAINS", "/login", "Has login endpoint", "Missing /login endpoint"));
                    break;
            }
        }
        
        return rules;
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/service/CodeEvaluationService.java
// ============================================================
package com.example.judge.service;

import com.example.judge.dto.TestResult;
import com.example.judge.model.ValidationRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeEvaluationService {
    
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/springboot-judge";
    
    private final ValidationRuleProvider ruleProvider;
    
    public List<TestResult> evaluateCode(String code, String projectType, Integer taskId) {
        List<TestResult> results = new ArrayList<>();
        
        try {
            // Create temp directory
            Files.createDirectories(Paths.get(TEMP_DIR));
            
            // Get validation rules for this task
            List<ValidationRule> rules = ruleProvider.getRulesForTask(projectType, taskId);
            
            // Run validation checks
            for (ValidationRule rule : rules) {
                TestResult result = new TestResult();
                result.setTestName(rule.getDescription());
                
                boolean passed = validateRule(code, rule);
                result.setPassed(passed);
                result.setMessage(passed ? "✓ " + rule.getDescription() : "✗ " + rule.getErrorMessage());
                result.setExpected(rule.getPattern());
                
                results.add(result);
            }
            
            // Additional compilation check
            if (shouldCompile(code)) {
                TestResult compileResult = checkCompilation(code);
                results.add(compileResult);
            }
            
        } catch (Exception e) {
            log.error("Error evaluating code", e);
            TestResult errorResult = new TestResult();
            errorResult.setTestName("Evaluation Error");
            errorResult.setPassed(false);
            errorResult.setMessage("Error: " + e.getMessage());
            results.add(errorResult);
        }
        
        return results;
    }
    
    private boolean validateRule(String code, ValidationRule rule) {
        switch (rule.getType()) {
            case "CONTAINS":
                return code.contains(rule.getPattern());
            
            case "REGEX":
                Pattern pattern = Pattern.compile(rule.getPattern(), Pattern.DOTALL);
                return pattern.matcher(code).find();
            
            case "ANNOTATION":
                return checkAnnotation(code, rule.getPattern());
            
            case "METHOD":
                return checkMethod(code, rule.getPattern());
            
            case "FIELD":
                return checkField(code, rule.getPattern());
            
            default:
                return false;
        }
    }
    
    private boolean checkAnnotation(String code, String annotation) {
        return code.contains("@" + annotation);
    }
    
    private boolean checkMethod(String code, String methodSignature) {
        // Simple method signature check
        String cleanCode = code.replaceAll("\\s+", " ");
        return cleanCode.contains(methodSignature.replaceAll("\\s+", " "));
    }
    
    private boolean checkField(String code, String fieldPattern) {
        Pattern pattern = Pattern.compile("(private|public|protected)\\s+" + fieldPattern);
        return pattern.matcher(code).find();
    }
    
    private boolean shouldCompile(String code) {
        // Only compile if it looks like a complete class
        return code.contains("class ") && code.contains("{") && code.contains("}");
    }
    
    private TestResult checkCompilation(String code) {
        TestResult result = new TestResult();
        result.setTestName("Code Compilation");
        
        try {
            // Extract class name
            Pattern classPattern = Pattern.compile("class\\s+(\\w+)");
            Matcher matcher = classPattern.matcher(code);
            
            if (!matcher.find()) {
                result.setPassed(false);
                result.setMessage("✗ Could not find class declaration");
                return result;
            }
            
            String className = matcher.group(1);
            
            // Create temporary Java file
            Path tempFile = Paths.get(TEMP_DIR, className + ".java");
            Files.write(tempFile, code.getBytes());
            
            // Try to compile using javac (if available)
            ProcessBuilder pb = new ProcessBuilder("javac", tempFile.toString());
            pb.directory(new File(TEMP_DIR));
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                result.setPassed(true);
                result.setMessage("✓ Code compiles successfully");
            } else {
                result.setPassed(false);
                result.setMessage("✗ Compilation errors:\n" + output.toString());
            }
            
            // Clean up
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(Paths.get(TEMP_DIR, className + ".class"));
            
        } catch (Exception e) {
            // If javac is not available, do basic syntax checks
            result.setPassed(checkBasicSyntax(code));
            result.setMessage(result.getPassed() ? 
                "✓ Basic syntax checks passed (javac not available for full compilation)" : 
                "✗ Basic syntax checks failed");
        }
        
        return result;
    }
    
    private boolean checkBasicSyntax(String code) {
        // Basic syntax checks
        int openBraces = code.length() - code.replace("{", "").length();
        int closeBraces = code.length() - code.replace("}", "").length();
        
        if (openBraces != closeBraces) return false;
        
        int openParens = code.length() - code.replace("(", "").length();
        int closeParens = code.length() - code.replace(")", "").length();
        
        return openParens == closeParens;
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/service/SubmissionService.java
// ============================================================
package com.example.judge.service;

import com.example.judge.dto.*;
import com.example.judge.entity.Submission;
import com.example.judge.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final CodeEvaluationService evaluationService;
    
    @Transactional
    public SubmissionResponse submitCode(SubmitCodeRequest request) {
        // Create submission
        Submission submission = new Submission();
        submission.setProjectType(request.getProjectType());
        submission.setTaskId(request.getTaskId());
        submission.setCode(request.getCode());
        submission.setStatus("RUNNING");
        
        submission = submissionRepository.save(submission);
        
        try {
            // Evaluate code
            List<TestResult> testResults = evaluationService.evaluateCode(
                request.getCode(),
                request.getProjectType(),
                request.getTaskId()
            );
            
            // Calculate results
            long passedCount = testResults.stream().filter(TestResult::getPassed).count();
            int totalTests = testResults.size();
            boolean allPassed = passedCount == totalTests;
            
            // Build output
            StringBuilder output = new StringBuilder();
            if (allPassed) {
                output.append("✅ All tests passed! Excellent work!\n\n");
            } else {
                output.append("❌ Some tests failed:\n\n");
            }
            
            for (TestResult result : testResults) {
                output.append(result.getMessage()).append("\n");
                if (!result.getPassed() && result.getExpected() != null) {
                    output.append("   Expected pattern: ").append(result.getExpected()).append("\n");
                }
            }
            
            // Update submission
            submission.setStatus(allPassed ? "PASSED" : "FAILED");
            submission.setTestsPassed((int) passedCount);
            submission.setTestsTotal(totalTests);
            submission.setOutput(output.toString());
            submission.setEvaluatedAt(LocalDateTime.now());
            
            submission = submissionRepository.save(submission);
            
            // Build response
            SubmissionResponse response = new SubmissionResponse();
            response.setSubmissionId(submission.getId());
            response.setStatus(submission.getStatus());
            response.setOutput(submission.getOutput());
            response.setTestsPassed(submission.getTestsPassed());
            response.setTestsTotal(submission.getTestsTotal());
            response.setAllPassed(allPassed);
            
            return response;
            
        } catch (Exception e) {
            log.error("Error evaluating submission", e);
            
            submission.setStatus("FAILED");
            submission.setOutput("Error: " + e.getMessage());
            submission.setEvaluatedAt(LocalDateTime.now());
            submissionRepository.save(submission);
            
            throw new RuntimeException("Failed to evaluate code: " + e.getMessage());
        }
    }
    
    public Submission getSubmission(Long id) {
        return submissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Submission not found"));
    }
    
    public List<Submission> getSubmissions(String projectType, Integer taskId) {
        return submissionRepository.findByProjectTypeAndTaskIdOrderBySubmittedAtDesc(projectType, taskId);
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/controller/JudgeController.java
// ============================================================
package com.example.judge.controller;

import com.example.judge.dto.*;
import com.example.judge.entity.Submission;
import com.example.judge.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judge")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JudgeController {
    
    private final SubmissionService submissionService;
    
    @PostMapping("/submit")
    public ResponseEntity<SubmissionResponse> submitCode(@RequestBody SubmitCodeRequest request) {
        SubmissionResponse response = submissionService.submitCode(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/submission/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
        Submission submission = submissionService.getSubmission(id);
        return ResponseEntity.ok(submission);
    }
    
    @GetMapping("/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(
            @RequestParam String projectType,
            @RequestParam Integer taskId) {
        List<Submission> submissions = submissionService.getSubmissions(projectType, taskId);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Spring Boot Online Judge is running!");
    }
}

// ============================================================
// FILE: src/main/java/com/example/judge/config/WebConfig.java
// ============================================================
package com.example.judge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}