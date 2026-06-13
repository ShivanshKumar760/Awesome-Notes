import React, { useState, useEffect } from 'react';
import { Play, CheckCircle, XCircle, Book, Code, ChevronRight, Loader, AlertCircle, History } from 'lucide-react';

const API_URL = 'http://localhost:8080/api/judge';

const PROJECTS = {
  todo: {
    name: "Todo App (No Auth)",
    description: "Build a simple Todo application with CRUD operations",
    tasks: [
      {
        id: 1,
        title: "Create Todo Entity",
        description: "Create a Todo entity with id, title, description, completed fields",
        instructions: `Create a Todo entity class with the following requirements:
- Use @Entity annotation
- Fields: Long id, String title, String description, boolean completed
- Use @Id and @GeneratedValue for the id field
- Include proper getters and setters or use @Data from Lombok`,
        starter: `package com.example.todo.entity;

import jakarta.persistence.*;
import lombok.Data;

// TODO: Create Todo entity class here

`
      },
      {
        id: 2,
        title: "Create Todo Repository",
        description: "Create a JPA repository for Todo entity",
        instructions: `Create a TodoRepository interface:
- Extend JpaRepository<Todo, Long>
- Add a custom method to find all todos by completed status`,
        starter: `package com.example.todo.repository;

import com.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// TODO: Create TodoRepository interface

`
      },
      {
        id: 3,
        title: "Create Todo DTO",
        description: "Create a Data Transfer Object for Todo",
        instructions: `Create TodoDTO class:
- Fields: Long id, String title, String description, boolean completed
- Use record or regular class with getters/setters`,
        starter: `package com.example.todo.dto;

// TODO: Create TodoDTO

`
      },
      {
        id: 4,
        title: "Create Todo Service",
        description: "Create a service layer with business logic",
        instructions: `Create TodoService class:
- Use @Service annotation
- Inject TodoRepository
- Implement methods: getAllTodos(), getTodoById(), createTodo(), updateTodo(), deleteTodo()`,
        starter: `package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

// TODO: Create TodoService

`
      },
      {
        id: 5,
        title: "Create Todo Controller",
        description: "Create REST controller with CRUD endpoints",
        instructions: `Create TodoController class:
- Use @RestController and @RequestMapping("/api/todos")
- Implement GET, POST, PUT, DELETE endpoints
- Use proper HTTP status codes with ResponseEntity`,
        starter: `package com.example.todo.controller;

import com.example.todo.dto.TodoDTO;
import com.example.todo.service.TodoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import java.util.List;

// TODO: Create TodoController

`
      }
    ]
  },
  blog: {
    name: "Blog App (With Authentication)",
    description: "Build a blog application with JWT authentication and security",
    tasks: [
      {
        id: 1,
        title: "Create User Entity",
        description: "Create a User entity for authentication",
        instructions: `Create a User entity:
- Fields: Long id, String username, String password, String email, Set<String> roles
- Use @Entity, @Id, @GeneratedValue
- Use @ElementCollection for roles
- Add unique constraints on username and email`,
        starter: `package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

// TODO: Create User entity

`
      },
      {
        id: 2,
        title: "Create Post Entity",
        description: "Create a Post entity for blog posts",
        instructions: `Create a Post entity:
- Fields: Long id, String title, String content, LocalDateTime createdAt, User author
- Use @ManyToOne for author relationship
- Use @PrePersist to set createdAt automatically`,
        starter: `package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

// TODO: Create Post entity

`
      },
      {
        id: 3,
        title: "Create JWT Utility",
        description: "Create a utility class for JWT token operations",
        instructions: `Create JwtUtil class:
- Use @Component annotation
- Method to generate token from username: generateToken(String username)
- Method to extract username from token: extractUsername(String token)
- Method to validate token: validateToken(String token, String username)
- Use a secret key for signing tokens`,
        starter: `package com.example.blog.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

// TODO: Create JwtUtil class

`
      },
      {
        id: 4,
        title: "Create JWT Authentication Filter",
        description: "Create a filter to validate JWT tokens on requests",
        instructions: `Create JwtAuthenticationFilter:
- Extend OncePerRequestFilter
- Use @Component annotation
- Extract JWT token from Authorization header
- Validate token and set authentication in SecurityContext
- Inject JwtUtil and UserDetailsService`,
        starter: `package com.example.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// TODO: Create JwtAuthenticationFilter

`
      },
      {
        id: 5,
        title: "Create Security Configuration",
        description: "Configure Spring Security with JWT",
        instructions: `Create SecurityConfig class:
- Use @Configuration and @EnableWebSecurity
- Configure HttpSecurity to:
  * Disable CSRF (for REST API)
  * Permit /api/auth/** endpoints
  * Require authentication for all other requests
  * Set session management to STATELESS
  * Add JWT filter before UsernamePasswordAuthenticationFilter
- Define PasswordEncoder bean (BCryptPasswordEncoder)
- Define AuthenticationManager bean`,
        starter: `package com.example.blog.config;

import com.example.blog.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// TODO: Create SecurityConfig

`
      },
      {
        id: 6,
        title: "Create Auth Service & Controller",
        description: "Create authentication service and REST endpoints",
        instructions: `Create AuthService and AuthController:

AuthService should have:
- Method register(username, password, email) - encode password and save user
- Method login(username, password) - validate credentials and return JWT token
- Inject UserRepository, PasswordEncoder, and JwtUtil

AuthController should have:
- POST /api/auth/register endpoint
- POST /api/auth/login endpoint that returns JWT token
- Use @RestController and @RequestMapping("/api/auth")`,
        starter: `package com.example.blog.service;

import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

// TODO: Create AuthService and AuthController

`
      }
    ]
  }
};

export default function SpringBootIDE() {
  const [selectedProject, setSelectedProject] = useState('todo');
  const [currentTaskIndex, setCurrentTaskIndex] = useState(0);
  const [code, setCode] = useState('');
  const [output, setOutput] = useState('');
  const [isRunning, setIsRunning] = useState(false);
  const [submissionId, setSubmissionId] = useState(null);
  const [testResults, setTestResults] = useState([]);
  const [allPassed, setAllPassed] = useState(false);

  const project = PROJECTS[selectedProject];
  const currentTask = project.tasks[currentTaskIndex];

  useEffect(() => {
    setCode(currentTask.starter);
    setOutput('');
    setTestResults([]);
    setAllPassed(false);
    setSubmissionId(null);
  }, [currentTaskIndex, selectedProject]);

  const runTests = async () => {
    setIsRunning(true);
    setOutput('Submitting code to online judge...\n');
    setTestResults([]);
    
    try {
      const response = await fetch(`${API_URL}/submit`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          projectType: selectedProject,
          taskId: currentTask.id,
          code: code
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      
      setSubmissionId(result.submissionId);
      setOutput(result.output);
      setAllPassed(result.allPassed);
      
      // Parse test results from output (you can enhance this)
      const lines = result.output.split('\n').filter(line => line.trim());
      const parsedResults = lines.slice(2).map(line => ({
        message: line,
        passed: line.includes('✓')
      }));
      setTestResults(parsedResults);
      
    } catch (error) {
      console.error('Error submitting code:', error);
      setOutput(`⚠️ Error connecting to judge server: ${error.message}\n\nMake sure the Spring Boot backend is running on http://localhost:8080`);
    } finally {
      setIsRunning(false);
    }
  };

  const nextTask = () => {
    if (currentTaskIndex < project.tasks.length - 1) {
      setCurrentTaskIndex(currentTaskIndex + 1);
    }
  };

  const prevTask = () => {
    if (currentTaskIndex > 0) {
      setCurrentTaskIndex(currentTaskIndex - 1);
    }
  };

  return (
    <div className="h-screen flex flex-col bg-gray-900 text-gray-100">
      {/* Header */}
      <div className="bg-gray-800 border-b border-gray-700 p-4">
        <div className="flex items-center justify-between mb-2">
          <h1 className="text-2xl font-bold text-blue-400">Spring Boot Online Judge</h1>
          <div className="flex items-center gap-2 text-sm">
            <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
            <span className="text-gray-400">Backend: localhost:8080</span>
          </div>
        </div>
        <p className="text-sm text-gray-400 mb-3">Learn Spring Boot by building real applications with automated testing</p>
        <div className="flex gap-4">
          <button
            onClick={() => {
              setSelectedProject('todo');
              setCurrentTaskIndex(0);
            }}
            className={`px-4 py-2 rounded transition-colors ${selectedProject === 'todo' ? 'bg-blue-600' : 'bg-gray-700 hover:bg-gray-600'}`}
          >
            📝 Todo App (No Auth)
          </button>
          <button
            onClick={() => {
              setSelectedProject('blog');
              setCurrentTaskIndex(0);
            }}
            className={`px-4 py-2 rounded transition-colors ${selectedProject === 'blog' ? 'bg-blue-600' : 'bg-gray-700 hover:bg-gray-600'}`}
          >
            🔐 Blog App (With Auth)
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex overflow-hidden">
        {/* Left Panel - Question */}
        <div className="w-1/2 border-r border-gray-700 overflow-auto">
          <div className="p-6">
            <div className="mb-4">
              <h2 className="text-xl font-bold text-blue-400 mb-2">{project.name}</h2>
              <p className="text-gray-400">{project.description}</p>
            </div>

            {/* Task Progress */}
            <div className="mb-6">
              <div className="flex items-center gap-2 mb-2">
                <Book className="w-5 h-5 text-blue-400" />
                <span className="font-semibold">Task {currentTaskIndex + 1} of {project.tasks.length}</span>
              </div>
              <div className="flex gap-1">
                {project.tasks.map((task, idx) => (
                  <div
                    key={task.id}
                    className={`h-2 flex-1 rounded transition-colors ${
                      idx < currentTaskIndex ? 'bg-green-500' :
                      idx === currentTaskIndex ? 'bg-blue-500' :
                      'bg-gray-700'
                    }`}
                  />
                ))}
              </div>
            </div>

            {/* Current Task */}
            <div className="bg-gray-800 rounded-lg p-6">
              <h3 className="text-2xl font-bold mb-4">{currentTask.title}</h3>
              <p className="text-gray-300 mb-4">{currentTask.description}</p>
              
              <div className="bg-gray-900 rounded p-4 mb-4">
                <h4 className="font-semibold text-blue-400 mb-2">📋 Instructions:</h4>
                <pre className="text-sm text-gray-300 whitespace-pre-wrap font-sans">{currentTask.instructions}</pre>
              </div>

              <div className="bg-blue-900 bg-opacity-30 border border-blue-700 rounded p-4">
                <h4 className="font-semibold text-blue-400 mb-2">💡 How it works:</h4>
                <p className="text-sm text-gray-300">
                  Write your code in the editor and click "Run Tests". Your code will be sent to the Spring Boot backend where it will be validated against multiple test criteria including annotations, method signatures, and code structure.
                </p>
              </div>
            </div>

            {/* Navigation */}
            <div className="flex gap-4 mt-6">
              <button
                onClick={prevTask}
                disabled={currentTaskIndex === 0}
                className="px-4 py-2 bg-gray-700 hover:bg-gray-600 disabled:bg-gray-800 disabled:text-gray-600 disabled:cursor-not-allowed rounded transition-colors"
              >
                ← Previous
              </button>
              <button
                onClick={nextTask}
                disabled={currentTaskIndex === project.tasks.length - 1 || !allPassed}
                className="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-500 disabled:bg-gray-800 disabled:text-gray-600 disabled:cursor-not-allowed rounded flex items-center justify-center gap-2 transition-colors"
              >
                {!allPassed && currentTaskIndex < project.tasks.length - 1 ? (
                  <>Complete this task to unlock <ChevronRight className="w-4 h-4" /></>
                ) : (
                  <>Next Task <ChevronRight className="w-4 h-4" /></>
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Right Panel - IDE */}
        <div className="w-1/2 flex flex-col">
          {/* Code Editor */}
          <div className="flex-1 flex flex-col">
            <div className="bg-gray-800 px-4 py-2 border-b border-gray-700 flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Code className="w-5 h-5 text-blue-400" />
                <span className="font-semibold">Code Editor</span>
                <span className="text-xs text-gray-500">Java / Spring Boot</span>
              </div>
              <div className="flex gap-2">
                {submissionId && (
                  <div className="flex items-center gap-2 text-xs text-gray-400 bg-gray-700 px-3 py-1 rounded">
                    <History className="w-3 h-3" />
                    Submission #{submissionId}
                  </div>
                )}
                <button
                  onClick={runTests}
                  disabled={isRunning}
                  className="px-4 py-2 bg-green-600 hover:bg-green-500 disabled:bg-gray-700 disabled:cursor-not-allowed rounded flex items-center gap-2 transition-colors font-semibold"
                >
                  {isRunning ? (
                    <>
                      <Loader className="w-4 h-4 animate-spin" />
                      Running Tests...
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4" />
                      Run Tests
                    </>
                  )}
                </button>
              </div>
            </div>
            <textarea
              value={code}
              onChange={(e) => setCode(e.target.value)}
              className="flex-1 bg-gray-900 text-gray-100 p-4 font-mono text-sm resize-none focus:outline-none"
              style={{ tabSize: 4 }}
              spellCheck="false"
              placeholder="Write your Spring Boot code here..."
            />
          </div>

          {/* Output Panel */}
          <div className="h-1/3 border-t border-gray-700 flex flex-col">
            <div className="bg-gray-800 px-4 py-2 border-b border-gray-700 font-semibold flex items-center gap-2">
              <AlertCircle className="w-4 h-4" />
              Test Results
              {testResults.length > 0 && (
                <span className="ml-auto text-sm text-gray-400">
                  {testResults.filter(r => r.passed).length} / {testResults.length} passed
                </span>
              )}
            </div>
            <div className="flex-1 bg-gray-900 p-4 overflow-auto">
              {output ? (
                <pre className="text-sm text-gray-300 whitespace-pre-wrap font-sans leading-relaxed">{output}</pre>
              ) : (
                <div className="h-full flex items-center justify-center">
                  <div className="text-center text-gray-500">
                    <Code className="w-12 h-12 mx-auto mb-3 opacity-50" />
                    <p className="italic">Click "Run Tests" to validate your code...</p>
                    <p className="text-xs mt-2">Your code will be tested by the Spring Boot backend</p>
                  </div>
                </div>
              )}
            </div>
            
            {allPassed && (
              <div className="bg-green-900 border-t border-green-700 p-4 flex items-center gap-3">
                <CheckCircle className="w-6 h-6 text-green-400 flex-shrink-0" />
                <div>
                  <p className="font-semibold text-green-400">🎉 Task Complete!</p>
                  <p className="text-sm text-green-300">All tests passed. {currentTaskIndex < project.tasks.length - 1 ? 'Ready for the next challenge!' : 'Project completed! 🚀'}</p>
                </div>
              </div>
            )}
            
            {output && !allPassed && testResults.length > 0 && (
              <div className="bg-red-900 bg-opacity-30 border-t border-red-700 p-4 flex items-center gap-3">
                <XCircle className="w-6 h-6 text-red-400 flex-shrink-0" />
                <div>
                  <p className="font-semibold text-red-400">Some tests failed</p>
                  <p className="text-sm text-red-300">Review the feedback above and try again</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}