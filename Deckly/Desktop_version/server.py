import sqlite3
import hashlib
import socket
import threading

# server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# server.bind(("localhost", 9999))

# server.listen()
# def handle_connection(client):
#     client.send("Username: ".encode())
#     username = client.recv(1024).decode()
#     client.send("Password: ".encode())
#     password = client.recv(1024)
#     password = hashlib.sha256(password).hexdigest()
#     connection = sqlite3.connect("userdata.db")
#     cursor = connection.cursor()
#     cursor.execute("SELECT * FROM users WHERE username = ? AND password = ?", (username, password))
#     if cursor.fetchall():
#         client.send("Login successful!".encode())
#     else:
#         client.send("Login failed".encode())

# while True:
#     client, addr = server.accept()
#     threading.Thread(target = handle_connection, args=(client,)).start()



def start_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind(("localhost", 9999))
    server.listen()

    def handle_connection(client):
        client.send("Username: ".encode())
        username = client.recv(1024).decode()
        client.send("Password: ".encode())
        password = client.recv(1024)
        password = hashlib.sha256(password).hexdigest()
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("SELECT * FROM users WHERE username = ? AND password = ?", (username, password))
        if cursor.fetchall():
            client.send("Login successful!".encode())
        else:
            client.send("Login failed".encode())
    print("Server started and listening on localhost:9999")
    while True:
        client, addr = server.accept()
        threading.Thread(target = handle_connection, args=(client,)).start()
