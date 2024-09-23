from kivy.app import App 
from kivy.uix.widget import Widget
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.anchorlayout import AnchorLayout
from kivy.uix.gridlayout import GridLayout
from kivy.uix.stacklayout import StackLayout
from kivy.uix.floatlayout import FloatLayout
from kivy.metrics import dp
from kivy.properties import StringProperty, BooleanProperty
from kivy.uix.screenmanager import ScreenManager, Screen, NoTransition
from kivy.core.window import Window
from kivy.logger import Logger
import threading
import server
import hashlib
import sqlite3
from icecream import ic
import socket
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
connection.commit()
connection.close()
#print("Successful creation of database")
Logger.info("Database created")
server_thread = threading.Thread(target=server.start_server)
server_thread.daemon = True
server_thread.start()
Window.size = (375, 680)
global logged_in
logged_in = False

class EditingScreen(Screen, FloatLayout):
    pass


class EditDeckScreen(Screen, FloatLayout, BoxLayout):
    def get_deck_entries(self):
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("SELECT name FROM decks WHERE user_id = ?", (user_id,))
        list_of_decks = cursor.fetchall()
        ic(list_of_decks)
        return list_of_decks

    def on_deck_button_press(self, inst):
        print(f"Selected deck: {inst.text}")
        

    def on_enter(self):
        list_of_decks = self.get_deck_entries()
        deck_buttons_box = self.ids.deck_buttons_box
        deck_buttons_box.clear_widgets()
        for each in list_of_decks:
            deckname = each[0]
            ic(deckname)
            btn = Button(text=deckname, size_hint_y=None, height=50)
            btn.bind(on_press=self.on_deck_button_press)
            deck_buttons_box.add_widget(btn)




class AutomaticCreationScreen(Screen, FloatLayout):
    def add_automatic_deck_to_database(self):
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        name, language1 = self.ids.automatic_deck_name.text, self.ids.automatic_language.text
        cursor.execute("INSERT INTO decks (user_id, name, type, language) VALUES (?, ?, ?, ?)", (user_id, name, "automatic", language1))
        connection.commit()
        connection.close()

class ManualCreationScreen(Screen, FloatLayout):
    def add_manual_deck_to_database(self):
        name, language1 = self.ids.manual_deck_name.text, self.ids.manual_language.text
        ic(name)
        ic(language1)
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("INSERT INTO decks (user_id, name, type, language) VALUES (?, ?, ?, ?)", (user_id, name, "manual", language1))
        connection.commit()
        connection.close()


class DeckTypeScreen(Screen, FloatLayout):
    pass

class DeckOptionScreen(Screen, FloatLayout):
    pass

class HomeScreen(Screen, FloatLayout):
    pass

class WelcomeScreen(Screen, FloatLayout):
    pass

class LoginScreen(Screen, FloatLayout):
    def test(self):
        username = self.ids.username_input.text
        password = self.ids.password_input.text
        # ic(username)
        # ic(password)
        pass
    def server_login(self):
        username = self.ids.username_input.text
        password = self.ids.password_input.text
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client.connect(("localhost", 9999))
        message = client.recv(1024).decode()
        client.send(username.encode())
        message = client.recv(1024).decode()
        client.send(password.encode())
        server_response = client.recv(1024).decode()
        ic(server_response)
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("SELECT id FROM users WHERE username = ?", (username,))
        global user_id
        #ic(cursor.fetchone())
        # if ((cursor.fetchone())[0]) != None:
        user_id = cursor.fetchone()[0]
        ic(user_id)
        connection.close()
        global login_successful
        if server_response == "Login successful!":
            login_successful = True
            self.manager.current = 'homescreen'
            Logger.info("Login successful")

        else:
            login_successful = False
            Logger.info("Login failed bruv pattern")
        


class SignUpScreen(Screen, FloatLayout):
    def test(self):
        username = self.ids.username_input.text
        password = self.ids.password_input.text
        # print(username)
        # print(password)
    
    def create_account(self):
        username = self.ids.username_input.text
        password = self.ids.password_input.text
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        username1, password1 = username, hashlib.sha256((password).encode()).hexdigest()
        cursor.execute("INSERT INTO users (username, password) VALUES (?, ?)", (username1, password1))
        connection.commit()
        connection.close()
        Logger.info("account successfully created!")


class languagelearner(App):
    def build(self):
        sm = ScreenManager(transition=NoTransition())
        sm.add_widget(WelcomeScreen(name='welcomescreen'))
        sm.add_widget(LoginScreen(name='loginscreen'))
        sm.add_widget(SignUpScreen(name='signup'))
        sm.add_widget(HomeScreen(name='homescreen'))
        sm.add_widget(DeckOptionScreen(name='deckoptionscreen'))
        sm.add_widget(DeckTypeScreen(name='decktypescreen'))
        sm.add_widget(ManualCreationScreen(name='manualcreationscreen'))
        sm.add_widget(AutomaticCreationScreen(name='automaticcreationscreen'))
        sm.add_widget(EditDeckScreen(name='editdeckscreen'))
        sm.current = 'welcomescreen'
        return sm

languagelearner().run()