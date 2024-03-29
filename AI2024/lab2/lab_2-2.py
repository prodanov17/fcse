from searching_framework import *

class MazeProblem(Problem):
    def __init__(self, initial, goal, maze, walls):
        super().__init__(initial, goal)
        self.maze = maze
        self.walls = walls

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
        print(successors)
        for i in successors.values():
            print(self.h(Node(i)), end=' ')
        return successors

    def perform_action(self, state, action):
        """
        Returns the new state after performing the action.
        """
        player = state
        new_player = player

        if action == 'Gore':
            new_player = (player[0], player[1] + 1)
        elif action == 'Dolu':
            new_player = (player[0], player[1] - 1)
        elif action == 'Levo':
            new_player = (player[0] - 1, player[1])
        elif action == 'Desno 2':
            if (player[0] + 1, player[1]) in self.walls:
                return None
            new_player = (player[0] + 2, player[1])
        elif action == 'Desno 3':
            if (player[0] + 2, player[1]) in self.walls:
                return None
            elif (player[0] + 1, player[1]) in self.walls:
                return None
            new_player = (player[0] + 3, player[1])

        return new_player

    def check_valid(self, state):
        """
        Check if the state is valid.
        """
        x, y = state
        
        if (x < 0 or x > self.maze - 1):
            return False

        if (y < 0 or y > self.maze - 1):
            return False

        if (x,y) in self.walls:
            return False

        return True

    def h(self, node):
        """
        Heuristic based on the Manhattan distance divided by 3 to account for Desno 3.
        """
        x,y = node.state

        return (abs(x - self.goal[0]) // 3 ) + abs(y - self.goal[1])

    
    def actions(self, state):
        return self.successor(state).keys()

    def result(self, state, action):
        return self.successor(state)[action]

    def goal_test(self, state):
        #print(state, self.goal)
        return state == self.goal

    def get_actions(self):
        return ['Gore', 'Dolu', 'Levo', 'Desno 2', 'Desno 3']


if __name__ == '__main__':
    maze_size = int(input())
    num_of_walls = int(input())
    walls = []
    for i in range(num_of_walls):
        walls.append(tuple(map(int, input().split(','))))

    player = tuple(map(int, input().split(',')))
    house = tuple(map(int, input().split(','))) 
    # print(walls)

    initial = (player)
    maze_problem = MazeProblem(initial, house, maze_size, walls)

    # print(maze_problem.initial)
    # successor = maze_problem.successor(initial)
    # print(successor)
    # for i in successor.values():
    #     print(maze_problem.h(Node(i)))

    node = astar_search(maze_problem)

    if node is not None:
        print(node.solution())

