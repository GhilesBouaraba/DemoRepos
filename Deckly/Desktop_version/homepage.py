import customtkinter as ctk
from PIL import Image, ImageTk
import socket
import sqlite3
from icecream import ic
import hashlib


import threading
import server

server_thread = threading.Thread(target=server.start_server)
server_thread.daemon = True
server_thread.start()


import loginpage
import translate
from translate import Translator
import time
import random
import text_Entry
import standard_learn
global user_id
user_id = loginpage.return_user_id()
print(loginpage.return_user_id())

win = ctk.CTk()
win.geometry("1280x720")
win.title()
back_button_image = Image.open("back-button.png")
back_button_photo = ImageTk.PhotoImage(back_button_image)

#I WAS MID WAY THRU FIXING THE ISSUE WHERE THE DECKNAME DOESNT GET SENT TO THE NEXT FUNCTION PROPERLYL



# def create_new_deck(win, decks_present, vbox2):
#     automatic
def add_manual_deck_to_database(deck_name, language):
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    name, type, language1 = deck_name.get(), "manual", language.get()
    cursor.execute("INSERT INTO decks (user_id, name, type, language) VALUES (?, ?, ?, ?)", (user_id, name, type, language1))
    connection.commit()
    connection.close()

def add_automatic_deck_to_database(deck_name, language):
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    name, type, language1 = deck_name.get(), "automatic", language.get()
    cursor.execute("INSERT INTO decks (user_id, name, type, language) VALUES (?, ?, ?, ?)", (user_id, name, type, language1))
    connection.commit()
    connection.close()


def new_manual_deck(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    language = ctk.CTkEntry(master=vbox2, placeholder_text="Language")
    deck_name = ctk.CTkEntry(master=vbox2, placeholder_text="Deck Name")
    deck_name.pack(pady=12, padx=10)
    language.pack(pady=12, padx=10)
    submit = ctk.CTkButton(master=vbox2, text="Create Deck", command=lambda:add_manual_deck_to_database(deck_name, language))
    submit.pack(pady=12, padx=10)

def new_automatic_deck(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    language = ctk.CTkEntry(master=vbox2, placeholder_text="Langauge")
    deck_name = ctk.CTkEntry(master=vbox2, placeholder_text="Deck Name")
    deck_name.pack(pady=12, padx=10)
    language.pack(pady=12, padx=10)
    submit = ctk.CTkButton(master=vbox2, text="Create Deck", command=lambda:add_automatic_deck_to_database(deck_name, language))
    submit.pack(pady=12, padx=10)

def create_new_decks(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    automatic_deck = ctk.CTkButton(master=vbox2, text="Automatic Deck", command=lambda:new_automatic_deck(win, vbox2))
    automatic_deck.pack(pady=12, padx=10)    
    manual_deck = ctk.CTkButton(master=vbox2, text="Manual Deck", command=lambda:new_manual_deck(win, vbox2))
    manual_deck.pack(pady=12, padx=10)

def add_automatic_card_to_database(win, vbox2, actual_lang, english_entry, deck_id): 
    #UNFINISHED AS I CANT DO THIS BIT WITHOUT INTERNET
    translator = Translator(from_lang="en", to_lang=actual_lang)
    english_text = english_entry.get()
    translated_text = translator.translate(english_text)
    ic(translated_text)
    ic(deck_id)
    text_on_screen = ctk.CTkLabel(master=vbox2, text=translated_text)
    text_on_screen.pack(pady=12, padx=10)
    text_on_screen2 = ctk.CTkLabel(master=vbox2, text="Adding")
    text_on_screen2.pack(pady=12, padx=10)
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("INSERT INTO flashcards (deck_id, english, translated) VALUES (?, ?, ?)", (deck_id, english_text, translated_text))
    connection.commit()
    connection.close()
    print("CHECKPOINT")
    time.sleep(1.2)
    text_on_screen.destroy()
    text_on_screen2.destroy()

def clear_manual_entry(entry1, entry2, label):
    entry1.delete(0, ctk.END)
    entry2.delete(0, ctk.END)
    entry2.configure(placeholder_text = "Translated text")
    label.destroy()

    

def add_manual_card_to_database(win, vbox2, english_text, translated_text, deck_id):
    english = english_text.get()
    translation = translated_text.get()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("INSERT INTO flashcards (deck_id, english, translated) VALUES (?, ?, ?)", (deck_id, english, translation))
    connection.commit()
    connection.close()
    label = ctk.CTkLabel(master=vbox2, text="Card Added!", font=("Roboto", 24))
    label.pack(pady=12, padx=10)    
    win.after(1350, lambda: clear_manual_entry(english_text, translated_text, label))

def get_info_for_add_card(win, vbox2, deckname):
    ic(deckname)
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT type FROM decks WHERE name = ?", (deckname,))
    decktype = cursor.fetchall()
    ic((decktype[0])[0])
    if ((decktype[0])[0]) == "automatic":
        cursor.execute("SELECT id FROM decks WHERE name = ?", (deckname,))
        deck_id = ((cursor.fetchone())[0])
        for widget in vbox2.winfo_children():
            widget.destroy()
        cursor.execute("SELECT language FROM decks WHERE name = ?", (deckname,))
        language = cursor.fetchall()
        ic(language)
        actual_lang = ((language[0])[0])
        ic(actual_lang)
        header = ctk.CTkLabel(master=vbox2, text="Editing {}, type {}".format(deckname, ((decktype[0])[0])), font=("Roboto", 24))
        header.pack(pady=12, padx=10)
        english_text = ctk.CTkEntry(master=vbox2, placeholder_text="English text")
        submit = ctk.CTkButton(master=vbox2, text="Submit", command=lambda:add_automatic_card_to_database(win, vbox2, actual_lang, english_text, deck_id))
        english_text.pack(pady=12, padx=10)
        submit.pack(pady=12, padx=10)
    if ((decktype[0])[0]) == "manual":
        cursor.execute("SELECT id FROM decks WHERE name = ?", (deckname,))
        deck_id = ((cursor.fetchone())[0])
        for widget in vbox2.winfo_children():
            widget.destroy()
        header = ctk.CTkLabel(master=vbox2, text="Editing {}, type {}".format(deckname, ((decktype[0])[0])), font=("Roboto", 24))
        header.pack(pady=12, padx=10)
        english_text = ctk.CTkEntry(master=vbox2, placeholder_text="English text")
        translated_text = ctk.CTkEntry(master=vbox2, placeholder_text="Translated text")
        english_text.pack(pady=12, padx=10)
        translated_text.pack(pady=12, padx=10)
        submit = ctk.CTkButton(master=vbox2, text="Submit", command=lambda:add_manual_card_to_database(win, vbox2, english_text, translated_text, deck_id))
        submit.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: edit_current_deck(win, vbox2, deckname), width=30)
    back_button.place(x=0, y=0)
def remove_card_from_database(win, vbox2, cc):
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("DELETE FROM flashcards WHERE english = ?", (cc,))
    connection.commit()
    connection.close()

def remove_cards_page(win, vbox2, deckname):
    for widget in vbox2.winfo_children():
        widget.destroy()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    print("REMOVE CARDS PAGE")
    cursor.execute("SELECT id FROM decks WHERE name = ?", (deckname,))
    deck_id = ((cursor.fetchone())[0])
    cursor.execute("SELECT english FROM flashcards WHERE deck_id = ?", (deck_id,))
    response = cursor.fetchall()
    ic(response)
    for each in response:
        current_card = each[0]
        ic(current_card)
        button = ctk.CTkButton(master=vbox2, text=current_card, command=lambda cc=current_card: remove_card_from_database(win, vbox2, cc))
        button.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: edit_current_deck(win, vbox2, deckname), width=30)
    back_button.place(x=0, y=0)

def edit_current_deck(win, vbox2, deckname):
    ic(deckname)
    for widget in vbox2.winfo_children():
        widget.destroy()
    title = ctk.CTkLabel(master=vbox2, text="Now editing: {}".format(deckname), font=("Roboto", 24))
    title.pack(pady=12, padx=10)
    add_card = ctk.CTkButton(master=vbox2, text="Add Cards", command=lambda:get_info_for_add_card(win, vbox2, deckname))
    remove_card = ctk.CTkButton(master=vbox2, text="Remove Cards", command=lambda:remove_cards_page(win, vbox2, deckname))
    add_card.pack(pady=12, padx=10)
    remove_card.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: edit_decks_page(win, vbox2, back_button_photo), width=30)
    back_button.place(x=0, y=0)



def edit_decks_page(win, vbox2, back_button_photo):
    for widget in vbox2.winfo_children():
        widget.destroy()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT name FROM decks WHERE user_id = ?", (user_id,))
    list_of_decks = cursor.fetchall()
    ic(list_of_decks)
    ic(list_of_decks[0])
    ic(list_of_decks[1])
    for each in list_of_decks:
        deckname = each[0]
        ic(deckname)
        button = ctk.CTkButton(master=vbox2, text=deckname, command=lambda dn=deckname: edit_current_deck(win, vbox2, dn))
        button.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: manage_decks(win, decks_present, vbox2, back_button_photo), width=30)
    back_button.place(x=0, y=0)  


def manage_decks(win, decks_present, vbox2, back_button_photo):
    for widget in vbox2.winfo_children():
        widget.destroy()  
    if decks_present:
        create_new_deck = ctk.CTkButton(master=vbox2, text="Create new deck", command=lambda:create_new_decks(win, vbox2))
        create_new_deck.pack(pady=12, padx=10)
    else:
        create_new_deck = ctk.CTkButton(master=vbox2, text="Create new deck", command=lambda:create_new_decks(win, vbox2))
        create_new_deck.pack(pady=12, padx=10)
        edit_decks = ctk.CTkButton(master=vbox2, text="Edit Decks", command=lambda:edit_decks_page(win, vbox2, back_button_photo))
        edit_decks.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: homepage(win, decks_present, back_button_photo), width=30)
    back_button.place(x=0, y=0)


def update_password(new_password_entry, vbox2, win):
    new_password = new_password_entry.get()
    new_password_entry.delete(0, ctk.END)
    hashed = hashlib.sha256((new_password).encode()).hexdigest()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("""
    UPDATE users
    SET password = ?
    WHERE id = ? 
    """, (hashed, user_id))
    connection.commit()
    connection.close()
    temp_label = ctk.CTkLabel(master=vbox2, text="Password Updated!", font=("Roboto", 24))
    temp_label.pack(pady=12, padx=10)
    win.after(1700, temp_label.destroy)
#Setting password to testing

def change_password_page(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    heading = ctk.CTkLabel(master=vbox2, text="Change Password", font=("Roboto", 24))
    heading.pack(pady=12, padx=10)
    new_password_entry = ctk.CTkEntry(master=vbox2, placeholder_text="Enter New Password", show="*")
    new_password_entry.pack(pady=12, padx=10)
    submit_button = ctk.CTkButton(master=vbox2, text="Submit", command=lambda: update_password(new_password_entry, vbox2, win))
    submit_button.pack(pady=12, padx=10)

def update_username(new_username_entry, vbox2, win):
    new_username = new_username_entry.get()
    new_username_entry.delete(0, ctk.END)
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("""
    UPDATE users
    SET username = ?
    WHERE id = ? 
    """, (new_username, user_id))
    connection.commit()
    connection.close()
    temp_label = ctk.CTkLabel(master=vbox2, text="Username Updated!", font=("Roboto", 24))
    temp_label.pack(pady=12, padx=10)
    win.after(1700, temp_label.destroy)

def change_username_page(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    heading = ctk.CTkLabel(master=vbox2, text="Change Username", font=("Roboto", 24))
    heading.pack(pady=12, padx=10)
    new_username_entry = ctk.CTkEntry(master=vbox2, placeholder_text="Enter New Username")
    new_username_entry.pack(pady=12, padx=10)
    submit_button = ctk.CTkButton(master=vbox2, text="Submit", command=lambda: update_username(new_username_entry, vbox2, win))
    submit_button.pack(pady=12, padx=10)

def account_settings_page(win, vbox2):
    for widget in vbox2.winfo_children():
        widget.destroy()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT * FROM users WHERE id = ?", (user_id,))
    response = cursor.fetchall()
    print(response[0])
    change_password_button = ctk.CTkButton(master=vbox2, text="Change Password", command=lambda: change_password_page(win, vbox2))
    change_password_button.pack(pady=12, padx=10)
    change_username_button = ctk.CTkButton(master=vbox2, text="Change Username", command=lambda: change_username_page(win, vbox2))
    change_username_button.pack(pady=12, padx=10)

    
# def text_entry_mode(win, vbox2, deck, list_of_english_terms):
#     for widget in vbox2.winfo_children():
#         widget.destroy()
#     loop = True
#     while loop == True:
#         integer = random.randint(1, len(list_of_english_terms))
#         current_english_text = list_of_english_terms[integer]
#         english = ctk.CTkLabel(master=vbox2, text=current_english_text , font=("Roboto", 24))
#         english.pack(pady=12, padx=10)
#         button = ctk.CTkButton(master=vbox2, text="Check")        




def study_deck(win, vbox2, deck):
    print("MADE ITTTT AGAINN")
    for widget in vbox2.winfo_children():
        widget.destroy()
    list_of_english_terms = []
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT id FROM decks WHERE name = ?", (deck,))
    response1 = cursor.fetchall()
    ic(response1)
    current_deck_id = ((response1[0])[0])
    ic(current_deck_id)
    cursor.execute("SELECT id FROM flashcards WHERE deck_id = ?", (current_deck_id,))
    response2 = cursor.fetchall()
    ic(response2)
    for each in response2:
        ic(each)
        current_card_id = each[0]
        ic(current_card_id)
        cursor.execute("SELECT english FROM flashcards WHERE id = ?", (current_card_id,))
        response3 = cursor.fetchall()
        ic(response3)
        english_to_add = ((response3[0])[0])
        ic(english_to_add)
        list_of_english_terms.append(english_to_add)
    ic(list_of_english_terms)
    text_entry = ctk.CTkButton(master=vbox2, text="Text Entry Mode", command=lambda: text_Entry.text_entry_mode(win, vbox2, deck, list_of_english_terms, user_id))
    text_entry.pack(pady=12, padx=10)
    standard = ctk.CTkButton(master=vbox2, text="Standard Mode", command=lambda: standard_learn.standard_mode(win, vbox2, deck, list_of_english_terms, user_id))
    standard.pack(pady=12, padx=10)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: study_deck_selection(win, vbox2, back_button_photo), width=30)
    back_button.place(x=0, y=0)



def study_deck_selection(win, vbox2, back_button_photo):
    for widget in vbox2.winfo_children():
        widget.destroy()
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT name FROM decks WHERE user_id = ?", (user_id,))
    response = cursor.fetchall()
    print(response)
    back_button = ctk.CTkButton(master=vbox2, fg_color="transparent", hover_color="#404040", image=back_button_photo, text="", command=lambda: homepage(win, decks_present, back_button_photo), width=30)
    back_button.place(x=0, y=0)
    for each in response:
        deck = each[0]
        ic(deck)
        button = ctk.CTkButton(master=vbox2, text=deck, command=lambda dn=deck: study_deck(win, vbox2, dn))
        button.pack(pady=12, padx=10)
    

def homepage(win, decks_present, back_button_photo):
    for widget in win.winfo_children():
        widget.destroy()
    mainbox = ctk.CTkFrame(master=win)
    mainbox.pack(fill="both", expand=True)
    vbox1 = ctk.CTkFrame(master=mainbox, width=300)
    vbox1.pack(side="left", fill="y", padx=10, pady=10)
    vbox2 = ctk.CTkFrame(master=mainbox)
    vbox2.pack(side="right", fill="both", expand=True, padx=10, pady=10)
    homepage_button = ctk.CTkButton(master=vbox1, text="Homepage", command=lambda:homepage(win, decks_present, back_button_photo))
    homepage_button.pack(pady=12, padx=10)
    software_sett_button = ctk.CTkButton(master=vbox1, text="Software Settings")
    software_sett_button.pack(pady=12, padx=10)
    account_sett_button = ctk.CTkButton(master=vbox1, text="Account Settings", command=lambda:account_settings_page(win, vbox2))
    account_sett_button.pack(pady=12, padx=10)
    sign_out_button = ctk.CTkButton(master=vbox1, text="Sign Out")
    sign_out_button.pack(pady=12, padx=10)
    manage_deck_button = ctk.CTkButton(master=vbox2, text="Manage Decks", command=lambda:manage_decks(win, decks_present, vbox2, back_button_photo))
    manage_deck_button.pack(pady=12, padx=10)
    study_button = ctk.CTkButton(master=vbox2, text="Study", command=lambda:study_deck_selection(win, vbox2, back_button_photo))
    study_button.pack(pady=12, padx=10)
    



connection = sqlite3.connect("userdata.db")
cursor = connection.cursor()
cursor.execute("SELECT COUNT(*) FROM decks WHERE user_id = ?", (loginpage.return_user_id(),))
decks_present = cursor.fetchone()[0] == 0

homepage(win, decks_present, back_button_photo)
win.mainloop()