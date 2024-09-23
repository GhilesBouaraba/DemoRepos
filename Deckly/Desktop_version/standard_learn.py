import customtkinter as ctk
import socket
import sqlite3
from icecream import ic
import hashlib
import loginpage
import translate
from translate import Translator
import time
import random
import prediction_algo

def standard_mode(win, vbox2, deck, list_of_english_terms, user_id):
    def log_correct_interaction(term_id):
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("INSERT INTO user_interactions (user_id, term_id, is_correct) VALUES (?, ?, ?)", (user_id, term_id, True))
        connection.commit()
        connection.close()
        display_terms()
    def log_incorrect_interaction(term_id):
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("INSERT INTO user_interactions (user_id, term_id, is_correct) VALUES (?, ?, ?)", (user_id, term_id, False))
        connection.commit()
        connection.close()
        display_terms()
    def reveal_answer(english):
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        cursor.execute("SELECT id FROM decks WHERE name = ?", (deck,))
        deck_id = ((cursor.fetchone())[0])
        ic(deck_id)
        cursor.execute("SELECT id FROM flashcards WHERE english = ? AND deck_id = ?", (english, deck_id,))
        term_id = ((cursor.fetchone())[0])
        ic(term_id)
        cursor.execute("SELECT translated FROM flashcards WHERE english = ? AND deck_id = ?", (english, deck_id,))
        answer = ((cursor.fetchone())[0])
        answer_label = ctk.CTkLabel(master=vbox2, text=f"The correct answer is '{answer}', did you get it correct?", font=("Roboto", 24))
        answer_label.pack(pady=12, padx=10)
        yes_button = ctk.CTkButton(master=vbox2, text="Yes", command=lambda: log_correct_interaction(term_id))
        no_button = ctk.CTkButton(master=vbox2, text="No", command=lambda: log_incorrect_interaction(term_id))
        yes_button.pack(pady=12, padx=10)
        no_button.pack(pady=12, padx=10)

    def display_terms():
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        for widget in vbox2.winfo_children():
            widget.destroy()
        probability_of_correct = 1
        counter = 1
        while probability_of_correct > 0.85:
            integer = random.randint(0, len(list_of_english_terms) - 1)
            english = list_of_english_terms[integer]
            cursor.execute("SELECT id FROM flashcards WHERE english = ?", (english,))
            term_id = ((cursor.fetchone())[0])
            cursor.execute("SELECT id FROM decks WHERE name = ?", (deck,))
            deck_id = ((cursor.fetchone())[0])
            probability_of_correct = prediction_algo.get_data(deck_id, term_id)
            ic(english)
            ic(probability_of_correct)
            counter += 1
            if counter > len(list_of_english_terms) * 2:
                integer = random.randint(0, len(list_of_english_terms ) - 1)
                english = list_of_english_terms[integer]
                break
        label = ctk.CTkLabel(master=vbox2, text=english, font=("Roboto", 24))
        label.pack(pady=12, padx=10)
        reveal_button = ctk.CTkButton(master=vbox2, text="Reveal Answer", command=lambda: reveal_answer(english))
        reveal_button.pack(pady=12, padx=10)
    display_terms()