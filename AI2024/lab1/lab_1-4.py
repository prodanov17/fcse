from searching_framework import *

"""
Дадена е табла 8x6, каде што се поставени човече и топка. Потребно е човечето со туркање на топката да ја доведе до голот кој е обележан со сива боја. На таблата дополнително има противници кои се обележани со сина боја. Противниците се статични и не се движат.

Човечето може да се движи во пет насоки: горе, долу, десно, горе-десно и долу-десно за една позиција. При движењето, доколку пред него се наоѓа топката, може да ја турне топката во насоката во која се движи. Човечето не може да се наоѓа на истото поле како топката или некој од противниците. Топката исто така не може да се наоѓа на поле кое е соседно со некој од противниците (хоризнотално, вертикално или дијагонално) или на исто поле со некој од противниците.

На слика 1 е покажана една можна почетна состојба на таблата.

За сите тест примери големината на таблата е иста, а позицијата на човечето и топката се менуваат и се читаат од стандарден влез. Позицијата на противниците и голот е иста за сите тест примери. Ваша задача е да го имплементирате поместувањето на човечето (со тоа и туркањето на топката) во successor функцијата. Акциите се именуваат како „Pomesti coveche gore/dolu/desno/gore-desno/dolu-desno“ ако се поместува човечето, или како „Turni topka gore/dolu/desno/gore-desno/dolu-desno“ ако при поместувањето на чoвечето се турнува и топката. Дополнително, потребно е да проверите дали сте стигнале до целта, односно да ја имплементирате функцијата goal_test и да проверите дали состојбата е валидна, односно да ја дополните функцијата check_valid. Треба да примените неинформирано пребарување за да најдете решение со најмал број на чекори. 

"""
from searching_framework import *


class Soccer(Problem):
    def __init__(self, initial, goal, opponent):
        self.initial = initial
        self.goal = goal
        self.opponent = opponent

    def successor(self, state):
        successors = dict()
        actions = self.get_actions()

        for action in actions:
            # What will happen after this action?
            new_state = self.perform_action(state, action)

            if new_state is None:
                continue

            if self.check_valid(new_state):
                successors[action] = new_state

        return successors

    def actions(self, state):
        return self.successor(state).keys()

    def result(self, state, action):
        return self.successor(state)[action]

    def perform_action(self, state, action):
        """
        Returns the new state after the action has been performed.
        """
        player, ball = state
        player_x, player_y = player
        ball_x, ball_y = ball
        new_ball = ball
        new_player = player

        if action == 'Pomesti coveche gore':
            new_player = (player_x, player_y + 1)
            if new_player == ball:
                return None
        elif action == 'Pomesti coveche dolu':
            new_player = (player_x, player_y - 1)
            if new_player == ball:
                return None
        elif action == 'Pomesti coveche desno':
            new_player = (player_x + 1, player_y)
            if new_player == ball:
                return None
        elif action == 'Pomesti coveche gore-desno':
            new_player = (player_x + 1, player_y + 1)
            if new_player == ball:
                return None
        elif action == 'Pomesti coveche dolu-desno':
            new_player = (player_x + 1, player_y - 1)
            if new_player == ball:
                return None
        elif action == 'Turni topka gore':
            if player[1] == ball[1] - 1 and player[0] == ball[0]:
                new_player = ball
                new_ball = (ball_x, ball_y + 1)
            else:
                return None
        elif action == 'Turni topka dolu':
            if player[1] == ball[1] + 1 and player[0] == ball[0]:
                new_player = ball
                new_ball = (ball_x, ball_y - 1)
            else:
                return None
        elif action == 'Turni topka desno':
            if player[0] == ball[0] - 1 and player[1] == ball[1]:
                new_player = ball
                new_ball = (ball_x + 1, ball_y)
            else:
                return None
        elif action == 'Turni topka gore-desno':
            if player[0] == ball[0] - 1 and player[1] == ball[1] - 1:
                new_player = ball
                new_ball = (ball_x + 1, ball_y + 1)
            else:
                return None
        elif action == 'Turni topka dolu-desno':
            if player[0] == ball[0] - 1 and player[1] == ball[1] + 1:
                new_player = ball
                new_ball = (ball_x + 1, ball_y - 1)
            else:
                return None


        return (new_player, new_ball)

    def check_valid(self, state):
        player, ball = state

        if player[0] < 0 or player[0] > 7 or player[1] < 0 or player[1] > 5:
            return False
        if ball[0] < 0 or ball[0] > 7 or ball[1] < 0 or ball[1] > 5:
            return False

        if ball in self.get_near_positions(self.opponent[0]) or ball in self.get_near_positions(self.opponent[1]):
            return False

        if player == self.opponent[0] or player == self.opponent[1]:
            return False

        return True

    def get_near_positions(self, position):
        x, y = position
        return ((x, y), (x, y + 1), (x, y - 1), (x + 1, y), (x + 1, y + 1), (x + 1, y - 1), (x - 1, y), (x - 1, y + 1),
                (x - 1, y - 1))

    def get_actions(self):
        return ['Pomesti coveche gore', 'Pomesti coveche dolu', 'Pomesti coveche desno', 'Pomesti coveche gore-desno',
                'Pomesti coveche dolu-desno', 'Turni topka gore', 'Turni topka dolu', 'Turni topka desno',
                'Turni topka gore-desno', 'Turni topka dolu-desno']

    def goal_test(self, state):
        return state[1] in self.goal


if __name__ == "__main__":
    pos_opponent = ((3, 3), (5, 4))

    input_player = input().strip().split(',')
    input_ball = input().strip().split(',')

    pos_player = tuple(int(x) for x in input_player)
    pos_ball = tuple(int(x) for x in input_ball)

    pos_goal = ((7, 2), (7, 3))

    initial_state = (pos_player, pos_ball)
    goal_state = pos_goal

    soccer = Soccer(initial_state, goal_state, pos_opponent)

    # print(soccer.successor(initial_state))
    # exit()
    node = breadth_first_graph_search(soccer)

    if node is not None:
        print(node.solution())

