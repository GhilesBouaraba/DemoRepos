import sqlite3
import pandas as pd
from icecream import ic

#P(Correct|Term, User) = P(Term|Correct, User) x P(Correct|User)
#                        ---------------------------------------
#                                        P(Term|User)

#P(Correct|Term) = C,term / N,term
#P(Correct|User) = C,user / N,user
#P(Term|Correct, User) = C, term correct / C, user correct
#P(Term|User) = C, term / C, user


def calculate(total_number_of_correct_attempts, total_number_of_attempts, number_of_term_attempts, number_of_correct_term_attempts):
    #P(Correct|User)
    p_correct_given_user = total_number_of_correct_attempts / total_number_of_attempts
    #P(Term|User)
    p_term_given_user = number_of_term_attempts / total_number_of_attempts
    #P(Term|Correct, User)
    p_term_given_correct_and_user = number_of_correct_term_attempts / total_number_of_correct_attempts

    #P(Correct|Term, User)
    p_correct_given_term_and_user = (p_term_given_correct_and_user * p_correct_given_user) / p_term_given_user
    ic(p_correct_given_term_and_user)
    return p_correct_given_term_and_user

def get_data(deck_id, term_id):
    connection = sqlite3.connect("userdata.db")
    cursor = connection.cursor()
    cursor.execute("SELECT COUNT(*) FROM user_interactions")
    number_of_total_entries = ((cursor.fetchone())[0])
    df = pd.read_sql_query("SELECT * FROM user_interactions", connection)
    print(df)
    for each in range(1, number_of_total_entries + 1):
        cursor.execute("SELECT term_id FROM user_interactions WHERE id = ?", (each,))
        current_term_id = ((cursor.fetchone())[0])
        cursor.execute("SELECT deck_id FROM flashcards WHERE id = ?", (current_term_id,))
        current_deck_id = ((cursor.fetchone())[0])
        if current_deck_id != deck_id:
            df = df[df['term_id'] != current_term_id]
    print(df)
    total_number_of_attempts = len(df)
    total_number_of_correct_attempts = df['is_correct'].sum()
    ic(total_number_of_attempts)
    ic(total_number_of_correct_attempts)
    number_of_term_attempts = (df['term_id'] == term_id).sum()
    ic(number_of_term_attempts)
    term_id_mask = df['term_id'] == term_id
    correct_mask = df['is_correct'] == 1
    combined_mask = term_id_mask & correct_mask
    number_of_correct_term_attempts = len(df[combined_mask])
    ic(number_of_correct_term_attempts)
    print(df)
    p_correct_given_term_and_user = calculate(total_number_of_correct_attempts, total_number_of_attempts, number_of_term_attempts, number_of_correct_term_attempts)
    ic(p_correct_given_term_and_user)
    return p_correct_given_term_and_user

# get_data(1, 8)