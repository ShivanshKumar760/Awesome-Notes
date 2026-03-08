# =============================================
# FastAPI One-to-One Chat Backend (Room Based)
# =============================================

from fastapi import FastAPI, WebSocket, WebSocketDisconnect, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Dict, List
from uuid import uuid4

app = FastAPI(title="One-to-One Chat App")

# -----------------------------
# CORS (for frontend apps)
# -----------------------------
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# -----------------------------
# In-Memory Storage
# -----------------------------

# Active rooms
rooms: Dict[str, List[WebSocket]] = {}

# Optional: store message history (in-memory)
chat_history: Dict[str, List[str]] = {}


# -----------------------------
# Pydantic Schemas
# -----------------------------

class RoomCreateResponse(BaseModel):
    room_code: str


# -----------------------------
# Room Manager Class
# -----------------------------

class ConnectionManager:

    async def connect(self, websocket: WebSocket, room_code: str):
        await websocket.accept()

        if room_code not in rooms:
            rooms[room_code] = []
            chat_history[room_code] = []

        if len(rooms[room_code]) >= 2:
            await websocket.send_text("Room is full")
            await websocket.close()
            return False

        rooms[room_code].append(websocket)
        return True

    def disconnect(self, websocket: WebSocket, room_code: str):
        if room_code in rooms and websocket in rooms[room_code]:
            rooms[room_code].remove(websocket)

            if len(rooms[room_code]) == 0:
                del rooms[room_code]
                del chat_history[room_code]

    async def send_personal_message(self, message: str, websocket: WebSocket):
        await websocket.send_text(message)

    async def broadcast(self, message: str, room_code: str, sender: WebSocket):
        if room_code in rooms:
            for connection in rooms[room_code]:
                if connection != sender:
                    await connection.send_text(message)


manager = ConnectionManager()


# -----------------------------
# REST Endpoints
# -----------------------------

@app.post("/create-room", response_model=RoomCreateResponse)
def create_room():
    room_code = str(uuid4())[:8]
    rooms[room_code] = []
    chat_history[room_code] = []
    return {"room_code": room_code}


@app.get("/rooms")
def list_rooms():
    return {"active_rooms": list(rooms.keys())}


@app.get("/rooms/{room_code}/history")
def get_chat_history(room_code: str):
    if room_code not in chat_history:
        raise HTTPException(status_code=404, detail="Room not found")
    return {"messages": chat_history[room_code]}


# -----------------------------
# WebSocket Endpoint
# -----------------------------

@app.websocket("/ws/{room_code}")
async def websocket_endpoint(websocket: WebSocket, room_code: str):
    connected = await manager.connect(websocket, room_code)
    if not connected:
        return

    try:
        while True:
            data = await websocket.receive_text()

            # Save message in history
            chat_history[room_code].append(data)

            # Broadcast to the other user
            await manager.broadcast(data, room_code, websocket)

    except WebSocketDisconnect:
        manager.disconnect(websocket, room_code)


# -----------------------------
# Health Check
# -----------------------------

@app.get("/")
def root():
    return {"message": "One-to-One Chat Backend Running"}


# =============================================
# Run using:
# uvicorn main:app --reload
# =============================================
