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

# def text_entry_mode(win, vbox2, deck, list_of_english_terms):
#     for widget in vbox2.winfo_children():
#         widget.destroy()
#     loop = True
#     while loop == True:
#         integer = random.randint(1, len(list_of_english_terms) - 1)
#         english = list_of_english_terms[integer]
#         label = ctk.CTkLabel(master=vbox2, text=english, font=("Roboto", 24))
#         label.pack(pady=12, padx=10)
#         entry = ctk.CTkEntry(master=vbox2, placeholder_text="Translation")
#         entry.pack(pady=12, padx=10)
#         submit = ctk.CTkButton(master=vbox2, text="Check Answer", command=lambda s=submit: check_answer(s))

def text_entry_mode(win, vbox2, deck, list_of_english_terms, user_id):
    def check_answer(entry, english):
        input = entry.get()
        connection = sqlite3.connect("userdata.db")
        cursor = connection.cursor()
        
        cursor.execute("SELECT id FROM flashcards WHERE english = ?", (english,))
        term_id = ((cursor.fetchone())[0])
        ic(term_id)



        cursor.execute("SELECT id FROM decks WHERE name = ?", (deck,))
        deck_id = ((cursor.fetchone())[0])
        ic(deck_id)
        cursor.execute("SELECT translated FROM flashcards WHERE deck_id = ? AND english = ?", (deck_id, english,))
        actual_answer = ((cursor.fetchone())[0])
        if input == actual_answer:
            cursor.execute("INSERT INTO user_interactions (user_id, term_id, is_correct) VALUES (?, ?, ?)", (user_id, term_id, True))
            connection.commit()
            correct_label = ctk.CTkLabel(master=vbox2, text="Correct!", font=("Roboto", 24))
            correct_label.pack(pady=12, padx=10)
            win.after(1700, display_terms)
        else:
            cursor.execute("INSERT INTO user_interactions (user_id, term_id, is_correct) VALUES (?, ?, ?)", (user_id, term_id, False))
            connection.commit()
            incorrect_label = ctk.CTkLabel(master=vbox2, text="Incorrect!", font=("Roboto", 24))
            incorrect_label.pack(pady=12, padx=10)
            correction = ctk.CTkLabel(master=vbox2, text=f"The correct answer is '{actual_answer}'", font=("Roboto", 24))
            correction.pack(pady=12, padx=10)
            win.after(1700, display_terms)



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
                integer = random.randint(0, len(list_of_english_terms) - 1)
                english = list_of_english_terms[integer]
                break
        label = ctk.CTkLabel(master=vbox2, text=english, font=("Roboto", 24))
        label.pack(pady=12, padx=10)
        entry = ctk.CTkEntry(master=vbox2, placeholder_text="Translation")
        entry.pack(pady=12, padx=10)
        submit = ctk.CTkButton(master=vbox2, text="Check Answer", command=lambda: check_answer(entry, english))
        submit.pack(pady=12, padx=10)

    
    # def display_terms():
    #     for widget in vbox2.winfo_children():
    #         widget.destroy()
    #     integer = random.randint(0, len(list_of_english_terms) - 1)
    #     english = list_of_english_terms[integer]



    #     connection = sqlite3.connect("userdata.db")
    #     cursor = connection.cursor()
    #     cursor.execute("SELECT id FROM flashcards WHERE english = ?", (english,))
    #     term_id = ((cursor.fetchone())[0])
    #     cursor.execute("SELECT id FROM decks WHERE name = ?", (deck,))
    #     deck_id = ((cursor.fetchone())[0])
    #     probability_of_correct = prediction_algo.get_data(deck_id, term_id)
    #     ic(probability_of_correct)
    #     label = ctk.CTkLabel(master=vbox2, text=english, font=("Roboto", 24))
    #     label.pack(pady=12, padx=10)
    #     entry = ctk.CTkEntry(master=vbox2, placeholder_text="Translation")
    #     entry.pack(pady=12, padx=10)
    #     submit = ctk.CTkButton(master=vbox2, text="Check Answer", command=lambda: check_answer(entry, english))
    #     submit.pack(pady=12, padx=10)
    display_terms()