from time import sleep
from searching_framework import *

"""
Во табла со димензии 10x10 се наоѓаат змија, зелени јаболки и црвени јаболки. Потребно е змијата да ги изеде зелените јаболки, а да ги одбегнува црвените јаболки кои се отровни. Змијата на почетокот зафаќа три полиња од таблата, едно поле за главата и две полиња за телото. При секое јадење на зелена јаболка телото на змијата се издолжува на крајот за едно поле (пример Слика 2). Во даден момент можни се три акции на движење на змијата: продолжи право, сврти лево и сврти десно. При движењето на змијата треба да се внимава змијата да не се изеде сама себе (колизија на главата на змијата со некој дел од телото) и да не излезе надвор од таблата. Потребно е проблемот да се реши во најмал број на потези.

За сите тест примери изгледот и големината на таблата се исти како на примерот даден на сликата. За сите тест примери почетната позиција на змијата е иста. За секој тест пример се менува бројот и почетната позиција на зелените и црвените јаболки.

Во рамки на почетниот код даден за задачата се вчитуваат влезните аргументи за секој тест пример. Во променливата crveni_jabolki се сочувани позициите на црвените јаболки (како листа од торки), а во променливата zeleni_jabolki се сочувани позициите на зелените јаболки. Табелата се претставува како координатен систем со координати x и y почнувајќи од нула, па соодветно, позициите се зададени како торка со прв елемент x и втор елемент y.

Движењата на змијата треба да ги именувате на следниот начин:

    ProdolzhiPravo - змијата се придвижува за едно поле нанапред
    SvrtiDesno - змијата се придвижува за едно поле на десно
    SvrtiLevo - змијата се придвижува за едно поле на лево

Вашиот код треба да има само еден повик на функција за приказ на стандарден излез (print) со кој ќе ја вратите секвенцата на движења која змијата треба да ја направи за да може да ги изеде сите зелени јаболки. Да се најде решението со најмал број на преземени акции употребувајќи некој алгоритам за неинформирано пребарување. Врз основа на тест примерите треба самите да определите кое пребарување ќе го користите.

"""

# static board size variable
BOARD_SIZE = 10


class SnakeBoard:
    def __init__(self, size, green_apple_positions, red_apple_positions) -> None:
        self.size = size
        self.green_apple_positions = green_apple_positions
        self.red_apple_positions = red_apple_positions
        self.board = [['0' for _ in range(size)] for _ in range(size)]
        self.draw_snake(Snake(((0, 2),), 'down'))

    def draw_snake(self, snake):
        for i in range(self.size):
            for j in range(self.size):
                if (i, j) in self.green_apple_positions:
                    self.board[i][j] = 'g'
                elif (i, j) in self.red_apple_positions:
                    self.board[i][j] = 'r'
                else:
                    self.board[i][j] = '0'

        for (x, y) in snake.body:
            self.board[x][y] = 'S'

    def add_apple(self, x, y, green):
        if green:
            self.green_apple_positions.add((x, y))
        else:
            self.red_apple_positions.add((x, y))
        self.board[x][y] = 'g' if green else 'r'

    def len_green_apples(self):
        return len(self.green_apple_positions)

    def pretty_print(self):
        for row in self.board:
            print('   ')
            for col in row:
                print(col, end='  ')


class Snake:
    def __init__(self, body, direction) -> None:
        self.body = body
        self.direction = direction
        self.tail = None

    def move(self):
        head = self.body[0]
        x, y = head
        if self.direction == 'up':
            new_head = (x, y + 1)
        elif self.direction == 'down':
            new_head = (x, y - 1)
        elif self.direction == 'left':
            new_head = (x - 1, y)
        elif self.direction == 'right':
            new_head = (x + 1, y)

        self.tail = self.body[-1]
        self.body = (new_head,) + self.body[:-1]
        return new_head

    def grow(self):
        self.body = self.body + (self.tail,)

    def actions(self, action):
        if action == 'ProdolzhiPravo':
            return self.move()
        elif action == 'SvrtiLevo':
            if self.direction == 'up':
                self.direction = 'left'
            elif self.direction == 'down':
                self.direction = 'right'
            elif self.direction == 'left':
                self.direction = 'down'
            elif self.direction == 'right':
                self.direction = 'up'
        elif action == 'SvrtiDesno':
            if self.direction == 'up':
                self.direction = 'right'
            elif self.direction == 'down':
                self.direction = 'left'
            elif self.direction == 'left':
                self.direction = 'up'
            elif self.direction == 'right':
                self.direction = 'down'

        return self.move()

    def get_all_actions(self):
        return ['ProdolzhiPravo', 'SvrtiLevo', 'SvrtiDesno']

    def get_valid_actions(self):
        valid_actions = []

        head = self.body[0]
        x, y = head
        if self.direction == 'up':
            if y < BOARD_SIZE - 1:
                valid_actions.append('ProdolzhiPravo')
            if x > 0:
                valid_actions.append('SvrtiLevo')
            if x < BOARD_SIZE - 1:
                valid_actions.append('SvrtiDesno')
        elif self.direction == 'down':
            if y > 0:
                valid_actions.append('ProdolzhiPravo')
            if x > 0:
                valid_actions.append('SvrtiDesno')
            if x < BOARD_SIZE - 1:
                valid_actions.append('SvrtiLevo')
        elif self.direction == 'left':
            if x > 0:
                valid_actions.append('ProdolzhiPravo')
            if y > 0:
                valid_actions.append('SvrtiLevo')
            if y < BOARD_SIZE - 1:
                valid_actions.append('SvrtiDesno')
        elif self.direction == 'right':
            if x < BOARD_SIZE - 1:
                valid_actions.append('ProdolzhiPravo')
            if y > 0:
                valid_actions.append('SvrtiDesno')
            if y < BOARD_SIZE - 1:
                valid_actions.append('SvrtiLevo')

        return valid_actions


class SnakeProblem(Problem):
    def __init__(self, initial, goal, board) -> None:
        super().__init__(initial, goal)
        self.initial = initial
        self.goal = goal
        self.board = board

    def successor(self, state):
        snake = Snake(state[0], state[1])
        actions = snake.get_valid_actions()

        neighbors = dict()
        for action in actions:
            new_state = self.result(state, action)
            if new_state == None:
                continue
            neighbors[action] = new_state

        return neighbors

    def actions(self, state):
        return self.successor(state).keys()

    def result(self, state, action):
        snake = Snake(state[0], state[1])
        pos = snake.actions(action)

        green_apples = list(state[2])
        red_apples = list(state[3])

        if pos in state[0][:-1]:
            return None

        if pos in state[3]:
            return None

        if pos in state[2]:
            snake.grow()
            green_apples.remove(pos)

        return (snake.body, snake.direction, tuple(green_apples), tuple(red_apples))

    def goal_test(self, state):
        return len(state[2]) == 0


if __name__ == "__main__":
    green_apples = int(input())
    green_apple_positions = set()
    for _ in range(green_apples):
        x, y = map(int, input().split(','))
        green_apple_positions.add((x, y))

    red_apples = int(input())
    red_apple_positions = set()
    for _ in range(red_apples):
        x, y = map(int, input().split(','))
        red_apple_positions.add((x, y))

    board = SnakeBoard(BOARD_SIZE, green_apple_positions, red_apple_positions)
    initial = (((0, 7), (0, 8), (0, 9)), 'down', tuple(green_apple_positions), tuple(red_apple_positions))

    problem = SnakeProblem(initial, None, board)
    node = breadth_first_graph_search(problem)
    
    


