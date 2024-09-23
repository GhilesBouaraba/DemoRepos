import customtkinter as ctk
import socket
import sqlite3
import icecream as ic
import hashlib
from PIL import Image, ImageTk
ctk.set_appearance_mode("dark")
ctk.set_default_color_theme("dark-blue")
connection = sqlite3.connect("userdata.db")
cursor = connection.cursor()

cursor.execute("""
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS decks (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    language VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS flashcards (
    id INTEGER PRIMARY KEY,
    deck_id INTEGER,
    english VARCHAR(255) NOT NULL,
    translated VARCHAR(255) NOT NULL,
    FOREIGN KEY (deck_id) REFERENCES decks(id)
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS user_interactions (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    term_id INTEGER,
    is_correct BOOLEAN,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(term_id) REFERENCES flashcards(id)
)
""")
main_window = ctk.CTk()

main_window.geometry("500x500")
back_button_image = Image.open("back-button.png")
back_button_photo = ImageTk.PhotoImage(back_button_image)

def server_login(username_entry, password_entry):
    username = username_entry.get()
    password = password_entry.get()
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(("localhost", 9999))
    message = client.recv(1024).decode()
    client.send(username.encode())
    message = client.recv(1024).decode()
    client.send(password.encode())
    server_response = client.recv(1024).decode()
    print(server_response)
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT id FROM users WHERE username = ?", (username,))
    global user_id
    user_id = cursor.fetchone()[0]
    print(user_id)
    connection.close()
    global login_successful
    if server_response == "Login successful!":
        login_successful = True
        main_window.destroy()
    else:
        login_successful = False
    
def return_login_successfulness():
    return login_successful

def return_user_id():
    return user_id
def new_account_on_server(username_entry, password_entry):
    username = username_entry.get()
    password = password_entry.get()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    username1, password1 = username, hashlib.sha256((password).encode()).hexdigest()
    cursor.execute("INSERT INTO users (username, password) VALUES (?, ?)", (username1, password1))
    connection.commit()
    connection.close()

def loginpage(main_window, back_button_photo):
    for widget in main_window.winfo_children():
        widget.destroy()
    vbox = ctk.CTkFrame(master=main_window)
    vbox.pack(pady=20, padx=60, fill="both", expand="True")
    username_entry = ctk.CTkEntry(master=vbox, placeholder_text="Username")
    password_entry = ctk.CTkEntry(master=vbox, placeholder_text="Password", show="*")
    login_button = ctk.CTkButton(master=vbox, text="Login", command=lambda: server_login(username_entry, password_entry))
    back_button = ctk.CTkButton(master=vbox, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: choicepage(main_window, False, back_button_photo), width=30)
    username_entry.pack(pady=12, padx=10)
    password_entry.pack(pady=12, padx=10)
    login_button.pack(pady=12, padx=10)
    back_button.place(x=0, y=0)

def sign_up_page(main_window, back_button_photo):
    for widget in main_window.winfo_children():
        widget.destroy()
    vbox = ctk.CTkFrame(master=main_window)
    vbox.pack(pady=20, padx=60, fill="both", expand="True")
    username_entry = ctk.CTkEntry(master=vbox, placeholder_text="Username")
    password_entry = ctk.CTkEntry(master=vbox, placeholder_text="Password", show="*")
    back_button = ctk.CTkButton(master=vbox, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: choicepage(main_window, False, back_button_photo), width=30)
    back_button.place(x=0, y=0)
    login_button = ctk.CTkButton(master=vbox, text="Sign Up", command=lambda: new_account_on_server(username_entry, password_entry))
    username_entry.pack(pady=12, padx=10)
    password_entry.pack(pady=12, padx=10)
    login_button.pack(pady=12, padx=10)


def choicepage(main_window, accounts_exist, back_button_photo):
    for widget in main_window.winfo_children():
        widget.destroy()
    vbox = ctk.CTkFrame(master=main_window)
    vbox.pack(pady=20, padx=60, fill="both", expand="True")
    welcome_text = ctk.CTkLabel(master=vbox, text="Welcome!", font=("Roboto", 24))
    welcome_text.pack(pady=12, padx=10)
    if not accounts_exist:
        login_button = ctk.CTkButton(master=vbox, text="Login", command=lambda: loginpage(main_window, back_button_photo), width=200, height=65)
        login_button.pack(pady=100, padx=10)
    sign_up_button = ctk.CTkButton(master=vbox, text="Sign Up", command=lambda: sign_up_page(main_window, back_button_photo), width=200, height=65)
    sign_up_button.pack(pady=12, padx=10)
    
connection = sqlite3.connect('userdata.db')
cursor = connection.cursor()

cursor.execute("SELECT COUNT(*) FROM users")
accounts_exist = cursor.fetchone()[0] == 0
print(accounts_exist)
if accounts_exist:
    print("No accounts yet")
else:
    print("Accounts exist")
cursor.execute("SELECT COUNT(*) FROM decks")
decks_exist = cursor.fetchone()[0] == 0
if decks_exist:
    print("No decks exist yet")
else:
    print("Decks exist")
choicepage(main_window, accounts_exist, back_button_photo)
print("Made itttttt")
main_window.mainloop()