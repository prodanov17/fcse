from abc import ABC, abstractmethod
import os
from time import sleep
import random

os.environ["OPENBLAS_NUM_THREADS"] = "1"
# random.seed(0)

class GameStrategy(ABC):
    @abstractmethod
    def calculate_next_position(self, player):
        pass

class RandomStrategy(GameStrategy):
    def __init__(self, game) -> None:
        self.game = game

    def calculate_next_position(self, player):
        desired_positions = self.game.check_neighbors(player.get_position()) 
        
        positions = self.game.get_player_available_moves(player.get_position())


        # a = input()

        if len(desired_positions) == 0 and len(positions) > 0:
            return random.choice(positions)
            
        elif len(desired_positions) == 1:
            return desired_positions[0]
        elif len(desired_positions) > 1:
            return random.choice(desired_positions)

        return None

class Player:

    def __init__(self, x, y, game) -> None:
        self.x = x
        self.y = y
        self.game = game

    def move(self, position) -> None:
        self.x, self.y = position
        if self.game.is_dot(position):
            self.game.eat_dot(position)
        self.game.set_player_position(position)

    def get_position(self):
        return (self.x, self.y)

class Game:
    def __init__(self, gamespace) -> None:
        self.gamespace = gamespace
        self.dots_count = sum(row.count('.') for row in gamespace)
        self.prev_position = (0, 0)
    
    def is_dot(self, position):
        x, y = position
        return self.gamespace[x][y] == '.'
    
    def set_player_position(self, position):
        x, y = position
        xprev, yprev = self.prev_position
        self.gamespace[x][y] = 'P'
        self.gamespace[xprev][yprev] = '#'

    def check_neighbors(self, position):
        x, y = position

        neighbors = []

        if x+1 < len(self.gamespace[0]) and self.is_dot((x + 1, y)): 
            neighbors.append((x + 1, y))

        if x-1 >= 0 and self.is_dot((x - 1, y)):
            neighbors.append((x - 1, y))

        if y + 1 < len(self.gamespace) and self.is_dot((x, y + 1)):
            neighbors.append((x, y + 1))

        if y-1 >= 0 and self.is_dot((x, y - 1)):
            neighbors.append((x, y - 1))

        return neighbors

    def is_prev_position(self, position):
        return position == self.prev_position

    def eat_dot(self, position):
        x, y = position

        if not self.is_dot(position):
            raise Exception("The field is not a dot")

        self.gamespace[x][y] = '#'
        self.dots_count -= 1

    def set_prev_position(self, position):
        self.prev_position = position

    def is_game_finished(self):
        return self.dots_count == 0

    def get_player_available_moves(self, position):
        x, y = position

            

        neighbors = []

        if x+1 < len(self.gamespace[0]):
            if (x+1, y) != self.prev_position:
                neighbors.append((x + 1, y))

        if x-1 >= 0:
            if (x-1, y) != self.prev_position:
                neighbors.append((x - 1, y))

        if y + 1 < len(self.gamespace):
            if (x, y+1) != self.prev_position:
                neighbors.append((x, y + 1))

        if y-1 >= 0:
            if (x, y-1) != self.prev_position:
                neighbors.append((x, y - 1))

        return neighbors

    def print_game_state(self):
        for row in self.gamespace:
            print(row, end='\n')

class Pacman:

    def __init__(self, player, game, strategy) -> None:
        self.player = player
        self.game = game
        self.strategy = strategy
        self.moves = []

    def add_move(self, position):
        x, y = position
        self.moves.append([x, y])

    def play_game(self):
        new_pos = self.strategy.calculate_next_position(self.player)
        game.set_prev_position(player.get_position())
        player.move(new_pos)
        self.add_move(new_pos)

    def is_game_over(self):
        return game.is_game_finished()

def clear_screen():
    os.system('cls' if os.name == 'nt' else 'clear')

if __name__ == "__main__":
    gamespace = []

    width = int(input().strip())
    height = int(input().strip())
    
    n = height
    #TODO: if no dots, return Nothing to do here
    while n > 0:
        n-=1
        line = input().strip()
        gamespace.append([x for x in line])

    gamespace[0][0] = 'P'
    game = Game(gamespace)
    player = Player(0,0, game)

    pacman = Pacman(player, game, RandomStrategy(game))

    clear_screen()

    game.print_game_state()

    sleep(1)
    while True:
        clear_screen()
        pacman.play_game()
        game.print_game_state()
        sleep(0.5)
        if pacman.is_game_over():
            break

    for i in pacman.moves:
        print(i)
        

