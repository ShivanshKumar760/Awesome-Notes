# Hono.js: Complete Beginner's Guide

## Table of Contents
1. [What is Backend?](#what-is-backend)
2. [What is Hono.js?](#what-is-honojs)
3. [Hono vs Express](#hono-vs-express)
4. [Architecture & Internal Working](#architecture--internal-working)
5. [Basic Syntax](#basic-syntax)
6. [Routing](#routing)
7. [Middleware](#middleware)
8. [Connecting to Database](#connecting-to-database)
9. [WebSockets](#websockets)
10. [Project 1: CRUD App](#project-1-crud-app)
11. [Project 2: Chat Room with Sockets](#project-2-chat-room-with-sockets)

---

## What is Backend?

Imagine a restaurant:
- **Frontend** = The dining area where customers sit and order (what users see in browser)
- **Backend** = The kitchen where food is prepared (server that processes requests)
- **Database** = The pantry where ingredients are stored (where data is saved)

**Backend handles:**
- Processing user requests (login, save data, fetch posts)
- Talking to databases (saving/retrieving information)
- Business logic (calculations, validations)
- Security (authentication, authorization)

---

## What is Hono.js?

**Hono** (炎 - means "flame" in Japanese) is a **small, fast, and modern web framework** for building backend APIs.

### Key Features:
- ⚡ **Ultra Fast**: Built for speed, works on edge runtimes
- 🪶 **Lightweight**: Only ~12KB (Express is ~200KB)
- 🌐 **Multi-runtime**: Works on Cloudflare Workers, Deno, Bun, Node.js
- 🎯 **TypeScript-first**: Amazing type safety out of the box
- 🛠️ **Modern**: Built for modern JavaScript

### Where Hono Runs:
```
┌─────────────────────────────────────┐
│   Hono.js Application               │
├─────────────────────────────────────┤
│ Can run on:                         │
│ • Node.js (traditional servers)     │
│ • Cloudflare Workers (edge)         │
│ • Deno (secure runtime)             │
│ • Bun (fast runtime)                │
│ • AWS Lambda (serverless)           │
└─────────────────────────────────────┘
```

---

## Hono vs Express

| Feature | Hono.js | Express.js |
|---------|---------|------------|
| **Speed** | 🚀 Blazing fast | 🐢 Moderate |
| **Size** | 12KB | 200KB |
| **TypeScript** | Built-in, excellent | Requires @types, basic |
| **Runtime** | Multi-runtime | Node.js only |
| **Learning Curve** | Easy, modern | Easy, traditional |
| **Ecosystem** | Growing | Massive |
| **Middleware** | Built-in + custom | Tons available |
| **Best For** | Modern apps, edge, APIs | Traditional apps, large ecosystem |

**Think of it this way:**
- **Express** = Toyota Camry (reliable, proven, lots of parts)
- **Hono** = Tesla Model 3 (modern, fast, efficient)

---

## Architecture & Internal Working

### Request-Response Cycle

```
1. Client sends request
   │
   ↓
2. Hono receives request
   │
   ↓
3. Middleware chain processes request
   │ (logging, auth, parsing, etc.)
   ↓
4. Router matches URL to handler
   │
   ↓
5. Handler function executes
   │ (your business logic)
   ↓
6. Response sent back to client
```

### Internal Architecture

```
┌──────────────────────────────────────┐
│         Hono Application             │
├──────────────────────────────────────┤
│  ┌────────────────────────────────┐  │
│  │   Router                       │  │
│  │  (matches URLs to handlers)    │  │
│  └────────────────────────────────┘  │
│             ↓                         │
│  ┌────────────────────────────────┐  │
│  │   Middleware Stack             │  │
│  │  [Auth] → [Logger] → [CORS]    │  │
│  └────────────────────────────────┘  │
│             ↓                         │
│  ┌────────────────────────────────┐  │
│  │   Context (c)                  │  │
│  │  • Request data                │  │
│  │  • Response methods            │  │
│  │  • Variables storage           │  │
│  └────────────────────────────────┘  │
│             ↓                         │
│  ┌────────────────────────────────┐  │
│  │   Handler Function             │  │
│  │  (your code)                   │  │
│  └────────────────────────────────┘  │
└──────────────────────────────────────┘
```

**How it works internally:**

1. **Request arrives** → Hono creates a `Context` object (c)
2. **Context contains:**
   - `c.req` - Request information (body, headers, params)
   - `c.res` - Response methods (json, text, html)
   - `c.var` - Variables to share between middleware
3. **Router tree matching** → Uses Trie-based routing for speed
4. **Middleware chain** → Executes in order, can stop or continue
5. **Handler execution** → Your function runs
6. **Response** → Sent back to client

---

## Basic Syntax

### Installation

```bash
# For Node.js
npm install hono

# For Bun (faster)
bun add hono

# For Deno
# No installation needed, just import from URL
```

### Your First Hono App

```javascript
// app.js
import { Hono } from 'hono';

// Create new Hono app
const app = new Hono();

// Define a route
app.get('/', (c) => {
  return c.text('Hello Hono!');
});

// Start server (Node.js)
export default app;

// For Node.js, add this:
import { serve } from '@hono/node-server';
serve(app, (info) => {
  console.log(`Server running on http://localhost:${info.port}`);
});
```

**Run it:**
```bash
node app.js
# Visit: http://localhost:3000
```

### Understanding the Context Object (c)

The `c` parameter is the **Context** - your control panel for everything:

```javascript
app.get('/demo', (c) => {
  // REQUEST DATA
  c.req.param('id')           // URL parameters
  c.req.query('search')       // Query strings
  c.req.header('Content-Type') // Headers
  await c.req.json()          // JSON body
  await c.req.formData()      // Form data
  
  // RESPONSE METHODS
  c.text('Plain text')        // Text response
  c.json({ msg: 'hi' })       // JSON response
  c.html('<h1>Hi</h1>')       // HTML response
  c.redirect('/new-url')      // Redirect
  c.notFound()                // 404 error
  
  // VARIABLES (shared across middleware)
  c.set('user', userData)     // Set variable
  c.get('user')               // Get variable
  
  // STATUS CODES
  c.status(201)               // Set status code
  
  return c.json({ success: true });
});
```

---

## Routing

### Basic Routes

```javascript
import { Hono } from 'hono';
const app = new Hono();

// HTTP Methods
app.get('/users', (c) => c.text('Get all users'));
app.post('/users', (c) => c.text('Create user'));
app.put('/users/:id', (c) => c.text('Update user'));
app.delete('/users/:id', (c) => c.text('Delete user'));
```

### Route Parameters

```javascript
// URL Parameters
app.get('/users/:id', (c) => {
  const id = c.req.param('id');
  return c.json({ userId: id });
});

// Multiple Parameters
app.get('/posts/:postId/comments/:commentId', (c) => {
  const postId = c.req.param('postId');
  const commentId = c.req.param('commentId');
  return c.json({ postId, commentId });
});
```

### Query Parameters

```javascript
// URL: /search?q=hono&sort=date
app.get('/search', (c) => {
  const query = c.req.query('q');      // "hono"
  const sort = c.req.query('sort');    // "date"
  return c.json({ query, sort });
});
```

### Grouped Routes (Like folders)

```javascript
const app = new Hono();

// API v1 routes
const api = new Hono();
api.get('/users', (c) => c.text('Users'));
api.get('/posts', (c) => c.text('Posts'));

// Mount at /api/v1
app.route('/api/v1', api);

// Now accessible at:
// /api/v1/users
// /api/v1/posts
```

### Chaining Routes

```javascript
app
  .get('/users', getAllUsers)
  .post('/users', createUser)
  .get('/users/:id', getUser)
  .put('/users/:id', updateUser)
  .delete('/users/:id', deleteUser);
```

---

## Middleware

**Middleware** = Functions that run BEFORE your handler. Think of them as security checkpoints at an airport.

### How Middleware Works

```
Request → [Middleware 1] → [Middleware 2] → [Handler] → Response
          (Logger)         (Auth Check)      (Your code)
```

### Built-in Middleware

```javascript
import { Hono } from 'hono';
import { logger } from 'hono/logger';
import { cors } from 'hono/cors';
import { prettyJSON } from 'hono/pretty-json';

const app = new Hono();

// Logger - logs every request
app.use('*', logger());

// CORS - allows cross-origin requests
app.use('*', cors());

// Pretty JSON - formats JSON nicely
app.use('*', prettyJSON());

app.get('/', (c) => c.json({ message: 'Hello' }));
```

### Custom Middleware

```javascript
// Simple middleware
const customLogger = async (c, next) => {
  console.log(`[${new Date().toISOString()}] ${c.req.method} ${c.req.url}`);
  await next(); // Continue to next middleware/handler
};

app.use('*', customLogger);

// Authentication middleware
const authMiddleware = async (c, next) => {
  const token = c.req.header('Authorization');
  
  if (!token) {
    return c.json({ error: 'No token provided' }, 401);
  }
  
  // Verify token (simplified)
  if (token !== 'secret-token') {
    return c.json({ error: 'Invalid token' }, 401);
  }
  
  // Store user in context
  c.set('user', { id: 1, name: 'John' });
  
  await next(); // Proceed to handler
};

// Apply to specific routes
app.use('/api/*', authMiddleware);

app.get('/api/protected', (c) => {
  const user = c.get('user');
  return c.json({ message: `Hello ${user.name}` });
});
```

### Middleware Order Matters!

```javascript
// ✅ CORRECT ORDER
app.use('*', logger());        // 1. Log request
app.use('*', authMiddleware);  // 2. Check auth
app.get('/api/data', handler); // 3. Handle request

// ❌ WRONG ORDER
app.get('/api/data', handler); // Handler runs first!
app.use('*', authMiddleware);  // Auth never runs
```

---

## Connecting to Database

### Using PostgreSQL with Prisma

**Step 1: Install Prisma**
```bash
npm install @prisma/client
npm install -D prisma
npx prisma init
```

**Step 2: Define Schema** (`prisma/schema.prisma`)
```prisma
datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

generator client {
  provider = "prisma-client-js"
}

model User {
  id        Int      @id @default(autoincrement())
  email     String   @unique
  name      String?
  posts     Post[]
  createdAt DateTime @default(now())
}

model Post {
  id        Int      @id @default(autoincrement())
  title     String
  content   String?
  published Boolean  @default(false)
  author    User     @relation(fields: [authorId], references: [id])
  authorId  Int
  createdAt DateTime @default(now())
}
```

**Step 3: Create Database**
```bash
npx prisma migrate dev --name init
```

**Step 4: Use with Hono**
```javascript
import { Hono } from 'hono';
import { PrismaClient } from '@prisma/client';

const app = new Hono();
const prisma = new PrismaClient();

// Get all users
app.get('/users', async (c) => {
  const users = await prisma.user.findMany({
    include: { posts: true }
  });
  return c.json(users);
});

// Get user by ID
app.get('/users/:id', async (c) => {
  const id = parseInt(c.req.param('id'));
  const user = await prisma.user.findUnique({
    where: { id },
    include: { posts: true }
  });
  
  if (!user) {
    return c.json({ error: 'User not found' }, 404);
  }
  
  return c.json(user);
});

// Create user
app.post('/users', async (c) => {
  const { email, name } = await c.req.json();
  
  const user = await prisma.user.create({
    data: { email, name }
  });
  
  return c.json(user, 201);
});
```

### Environment Variables

**.env file:**
```
DATABASE_URL="postgresql://user:password@localhost:5432/mydb"
```

**Load in app:**
```javascript
import 'dotenv/config';
// or for Bun: process.env is automatically loaded
```

---

## WebSockets

Hono supports WebSockets for real-time communication!

### Basic WebSocket Setup

```javascript
import { Hono } from 'hono';
import { createBunWebSocket } from 'hono/bun';

const app = new Hono();

const { upgradeWebSocket, websocket } = createBunWebSocket();

// WebSocket route
app.get('/ws', upgradeWebSocket((c) => {
  return {
    onOpen(evt, ws) {
      console.log('Connection opened');
      ws.send('Welcome to WebSocket!');
    },
    
    onMessage(evt, ws) {
      console.log(`Message received: ${evt.data}`);
      ws.send(`Echo: ${evt.data}`);
    },
    
    onClose(evt, ws) {
      console.log('Connection closed');
    },
    
    onError(evt, ws) {
      console.log('Error:', evt);
    }
  };
}));

// Regular HTTP routes
app.get('/', (c) => c.text('WebSocket server running'));

export default {
  fetch: app.fetch,
  websocket
};
```

**Client-side (HTML):**
```html
<!DOCTYPE html>
<html>
<body>
  <script>
    const ws = new WebSocket('ws://localhost:3000/ws');
    
    ws.onopen = () => {
      console.log('Connected!');
      ws.send('Hello Server!');
    };
    
    ws.onmessage = (event) => {
      console.log('Received:', event.data);
    };
  </script>
</body>
</html>
```

---

## Project 1: CRUD App

Let's build a **Todo List API** with full CRUD operations.

### Setup

```bash
mkdir todo-api
cd todo-api
npm init -y
npm install hono @hono/node-server
```

### Complete Code

```javascript
// app.js
import { Hono } from 'hono';
import { serve } from '@hono/node-server';

const app = new Hono();

// In-memory database (array)
let todos = [
  { id: 1, title: 'Learn Hono', completed: false },
  { id: 2, title: 'Build API', completed: false }
];

let nextId = 3;

// ============ ROUTES ============

// GET all todos
app.get('/todos', (c) => {
  return c.json({
    success: true,
    count: todos.length,
    data: todos
  });
});

// GET single todo
app.get('/todos/:id', (c) => {
  const id = parseInt(c.req.param('id'));
  const todo = todos.find(t => t.id === id);
  
  if (!todo) {
    return c.json({
      success: false,
      error: 'Todo not found'
    }, 404);
  }
  
  return c.json({
    success: true,
    data: todo
  });
});

// CREATE todo
app.post('/todos', async (c) => {
  const { title } = await c.req.json();
  
  if (!title) {
    return c.json({
      success: false,
      error: 'Title is required'
    }, 400);
  }
  
  const newTodo = {
    id: nextId++,
    title,
    completed: false
  };
  
  todos.push(newTodo);
  
  return c.json({
    success: true,
    data: newTodo
  }, 201);
});

// UPDATE todo
app.put('/todos/:id', async (c) => {
  const id = parseInt(c.req.param('id'));
  const { title, completed } = await c.req.json();
  
  const todoIndex = todos.findIndex(t => t.id === id);
  
  if (todoIndex === -1) {
    return c.json({
      success: false,
      error: 'Todo not found'
    }, 404);
  }
  
  // Update fields
  if (title !== undefined) todos[todoIndex].title = title;
  if (completed !== undefined) todos[todoIndex].completed = completed;
  
  return c.json({
    success: true,
    data: todos[todoIndex]
  });
});

// DELETE todo
app.delete('/todos/:id', (c) => {
  const id = parseInt(c.req.param('id'));
  const todoIndex = todos.findIndex(t => t.id === id);
  
  if (todoIndex === -1) {
    return c.json({
      success: false,
      error: 'Todo not found'
    }, 404);
  }
  
  todos.splice(todoIndex, 1);
  
  return c.json({
    success: true,
    message: 'Todo deleted'
  });
});

// DELETE all completed todos
app.delete('/todos/completed/all', (c) => {
  const beforeCount = todos.length;
  todos = todos.filter(t => !t.completed);
  const deletedCount = beforeCount - todos.length;
  
  return c.json({
    success: true,
    message: `Deleted ${deletedCount} completed todos`
  });
});

// Start server
serve(app, (info) => {
  console.log(`🚀 Server running on http://localhost:${info.port}`);
});
```

### Testing the API

```bash
# Get all todos
curl http://localhost:3000/todos

# Create todo
curl -X POST http://localhost:3000/todos \
  -H "Content-Type: application/json" \
  -d '{"title": "Learn WebSockets"}'

# Update todo
curl -X PUT http://localhost:3000/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"completed": true}'

# Delete todo
curl -X DELETE http://localhost:3000/todos/1
```

---

## Project 2: Chat Room with Sockets

A real-time chat application using WebSockets!

### Full Chat Backend

```javascript
// chat-server.js
import { Hono } from 'hono';
import { createBunWebSocket } from 'hono/bun';
import { serveStatic } from 'hono/bun';

const app = new Hono();
const { upgradeWebSocket, websocket } = createBunWebSocket();

// Store connected clients
const clients = new Set();
const messages = []; // Message history

// Serve static HTML
app.get('/', serveStatic({ path: './public/index.html' }));

// WebSocket endpoint
app.get('/chat', upgradeWebSocket((c) => {
  let username = 'Anonymous';
  
  return {
    onOpen(evt, ws) {
      clients.add(ws);
      console.log(`New connection. Total clients: ${clients.size}`);
      
      // Send message history to new user
      ws.send(JSON.stringify({
        type: 'history',
        messages: messages
      }));
      
      // Notify others
      broadcast({
        type: 'system',
        message: 'A user joined the chat',
        timestamp: new Date().toISOString()
      }, ws);
    },
    
    onMessage(evt, ws) {
      const data = JSON.parse(evt.data);
      
      if (data.type === 'setUsername') {
        username = data.username;
        
        broadcast({
          type: 'system',
          message: `${username} joined the chat`,
          timestamp: new Date().toISOString()
        });
        
      } else if (data.type === 'message') {
        const message = {
          type: 'message',
          username: username,
          message: data.message,
          timestamp: new Date().toISOString()
        };
        
        // Store message
        messages.push(message);
        if (messages.length > 100) {
          messages.shift(); // Keep only last 100 messages
        }
        
        // Broadcast to all clients
        broadcast(message);
      }
    },
    
    onClose(evt, ws) {
      clients.delete(ws);
      console.log(`Connection closed. Total clients: ${clients.size}`);
      
      broadcast({
        type: 'system',
        message: `${username} left the chat`,
        timestamp: new Date().toISOString()
      });
    },
    
    onError(evt, ws) {
      console.log('WebSocket error:', evt);
      clients.delete(ws);
    }
  };
}));

// Broadcast message to all clients
function broadcast(message, exclude = null) {
  const data = JSON.stringify(message);
  
  for (const client of clients) {
    if (client !== exclude && client.readyState === 1) {
      client.send(data);
    }
  }
}

// API endpoint to get active users count
app.get('/api/stats', (c) => {
  return c.json({
    activeUsers: clients.size,
    totalMessages: messages.length
  });
});

export default {
  fetch: app.fetch,
  websocket,
  port: 3000
};
```

### Frontend (public/index.html)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Hono Chat Room</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }
    
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 20px;
    }
    
    #app {
      background: white;
      border-radius: 12px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.3);
      width: 100%;
      max-width: 600px;
      height: 600px;
      display: flex;
      flex-direction: column;
    }
    
    #header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 20px;
      border-radius: 12px 12px 0 0;
    }
    
    #header h1 {
      font-size: 24px;
      margin-bottom: 5px;
    }
    
    #status {
      font-size: 14px;
      opacity: 0.9;
    }
    
    #messages {
      flex: 1;
      overflow-y: auto;
      padding: 20px;
      background: #f7f7f7;
    }
    
    .message {
      margin-bottom: 15px;
      padding: 10px 15px;
      border-radius: 8px;
      background: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    .message.system {
      background: #e3f2fd;
      text-align: center;
      font-style: italic;
      color: #1976d2;
    }
    
    .message-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 5px;
    }
    
    .username {
      font-weight: bold;
      color: #667eea;
    }
    
    .timestamp {
      font-size: 12px;
      color: #999;
    }
    
    .message-text {
      color: #333;
    }
    
    #input-area {
      padding: 20px;
      border-top: 1px solid #ddd;
      background: white;
      border-radius: 0 0 12px 12px;
    }
    
    #username-form, #message-form {
      display: flex;
      gap: 10px;
    }
    
    input {
      flex: 1;
      padding: 12px 15px;
      border: 2px solid #ddd;
      border-radius: 8px;
      font-size: 14px;
      transition: border-color 0.3s;
    }
    
    input:focus {
      outline: none;
      border-color: #667eea;
    }
    
    button {
      padding: 12px 24px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s;
    }
    
    button:hover {
      transform: translateY(-2px);
    }
    
    button:active {
      transform: translateY(0);
    }
    
    .hidden {
      display: none;
    }
  </style>
</head>
<body>
  <div id="app">
    <div id="header">
      <h1>🔥 Hono Chat Room</h1>
      <div id="status">Connecting...</div>
    </div>
    
    <div id="messages"></div>
    
    <div id="input-area">
      <form id="username-form">
        <input 
          type="text" 
          id="username-input" 
          placeholder="Enter your name..."
          required
        >
        <button type="submit">Join Chat</button>
      </form>
      
      <form id="message-form" class="hidden">
        <input 
          type="text" 
          id="message-input" 
          placeholder="Type a message..."
          required
        >
        <button type="submit">Send</button>
      </form>
    </div>
  </div>

  <script>
    let ws;
    let username = '';
    
    const messagesDiv = document.getElementById('messages');
    const statusDiv = document.getElementById('status');
    const usernameForm = document.getElementById('username-form');
    const messageForm = document.getElementById('message-form');
    const usernameInput = document.getElementById('username-input');
    const messageInput = document.getElementById('message-input');
    
    // Connect to WebSocket
    function connect() {
      ws = new WebSocket('ws://localhost:3000/chat');
      
      ws.onopen = () => {
        statusDiv.textContent = 'Connected';
        statusDiv.style.color = '#4ade80';
      };
      
      ws.onmessage = (event) => {
        const data = JSON.parse(event.data);
        
        if (data.type === 'history') {
          // Display message history
          data.messages.forEach(msg => addMessage(msg));
        } else {
          addMessage(data);
        }
      };
      
      ws.onclose = () => {
        statusDiv.textContent = 'Disconnected';
        statusDiv.style.color = '#ef4444';
        setTimeout(connect, 3000); // Reconnect after 3s
      };
      
      ws.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
    }
    
    // Add message to UI
    function addMessage(data) {
      const messageEl = document.createElement('div');
      messageEl.className = 'message';
      
      if (data.type === 'system') {
        messageEl.classList.add('system');
        messageEl.textContent = data.message;
      } else {
        const time = new Date(data.timestamp).toLocaleTimeString();
        messageEl.innerHTML = `
          <div class="message-header">
            <span class="username">${data.username}</span>
            <span class="timestamp">${time}</span>
          </div>
          <div class="message-text">${escapeHtml(data.message)}</div>
        `;
      }
      
      messagesDiv.appendChild(messageEl);
      messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }
    
    // Handle username submission
    usernameForm.onsubmit = (e) => {
      e.preventDefault();
      username = usernameInput.value.trim();
      
      if (username) {
        ws.send(JSON.stringify({
          type: 'setUsername',
          username: username
        }));
        
        usernameForm.classList.add('hidden');
        messageForm.classList.remove('hidden');
        messageInput.focus();
      }
    };
    
    // Handle message submission
    messageForm.onsubmit = (e) => {
      e.preventDefault();
      const message = messageInput.value.trim();
      
      if (message) {
        ws.send(JSON.stringify({
          type: 'message',
          message: message
        }));
        
        messageInput.value = '';
      }
    };
    
    // Escape HTML to prevent XSS
    function escapeHtml(text) {
      const div = document.createElement('div');
      div.textContent = text;
      return div.innerHTML;
    }
    
    // Start connection
    connect();
  </script>
</body>
</html>
```

### Run the Chat Server

```bash
# For Bun (recommended for WebSockets)
bun run chat-server.js

# For Node.js (requires additional setup)
npm install ws
node chat-server.js
```

### Features Implemented:

✅ Real-time messaging  
✅ Username support  
✅ Message history  
✅ System notifications (user joined/left)  
✅ Auto-reconnect  
✅ Multiple users support  
✅ Message timestamps  
✅ Beautiful UI  

---

## Quick Reference Cheat Sheet

### Common Patterns

```javascript
// 1. Basic route
app.get('/hello', (c) => c.text('Hello!'));

// 2. JSON response
app.get('/data', (c) => c.json({ name: 'John' }));

// 3. Get request data
app.post('/users', async (c) => {
  const body = await c.req.json();
  const name = c.req.query('name');
  const id = c.req.param('id');
  return c.json({ received: body });
});

// 4. Middleware
app.use('*', async (c, next) => {
  console.log('Before');
  await next();
  console.log('After');
});

// 5. Error handling
app.get('/error', (c) => {
  return c.json({ error: 'Something went wrong' }, 500);
});

// 6. Grouped routes
const api = new Hono();
api.get('/users', handler);
app.route('/api', api);
```

---

## Next Steps

1. **Learn TypeScript** - Hono shines with TypeScript
2. **Try different runtimes** - Cloudflare Workers, Bun, Deno
3. **Add authentication** - JWT, sessions
4. **Deploy** - Cloudflare, Vercel, Railway
5. **Build real projects** - Blog API, URL shortener, file upload service

---

## Resources

- [Official Docs](https://hono.dev/)
- [GitHub](https://github.com/honojs/hono)
- [Examples](https://github.com/honojs/examples)
- [Discord Community](https://discord.gg/hono)

---

**You're now ready to build blazing-fast APIs with Hono! 🔥**