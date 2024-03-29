from searching_framework import *

class HouseProblem(Problem):
    def __init__(self, initial, goal, allowed):
        super().__init__(initial, goal)
        self.allowed = allowed

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

    def perform_action(self, state, action):
        """
        Returns the new state after performing the action.
        """
        player, house, house_direction = state
        new_player = player
        new_house = house
        new_direction = house_direction

        if action == 'Gore 1':
            new_player = (player[0], player[1] + 1)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Gore 2':
            new_player = (player[0], player[1] + 2)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Gore-desno 1':
            new_player = (player[0] + 1, player[1] + 1)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Gore-desno 2':
            new_player = (player[0] + 2, player[1] + 2)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Gore-levo 1':
            new_player = (player[0] - 1, player[1] + 1)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Gore-levo 2':
            new_player = (player[0] - 2, player[1] + 2)
            new_house, new_direction = self.move_house(house, house_direction)
        elif action == 'Stoj':
            new_player = player
            new_house, new_direction = self.move_house(house, house_direction)
    
        return (new_player, new_house, new_direction)


    def check_valid(self, state):
        """
        Check if the state is valid.
        """
        player, house, house_direction = state
        
        if(player[0] < 0 or player[0] >= 5 or player[1] < 0 or player[1] >= 9):
            return False

        if(house[0] < 0 or house[0] >= 5 or house[1] < 0 or house[1] >= 9):
            return False

        if player == house:
            return True

        if player not in self.allowed:
            return False

        return True

    def h(self, node):
        """
        Heuristic function estimating the cost to reach the goal state.
        """
        player, house, _ = node.state
        return abs(player[1] - house[1]) // 2

    
    def actions(self, state):
        return self.successor(state).keys()

    def result(self, state, action):
        return self.successor(state)[action]

    def goal_test(self, state):
        return state[0] == state[1]

    def get_actions(self):
        return ['Gore 1', 'Gore 2', 'Gore-desno 1', 'Gore-desno 2', 'Gore-levo 1', 'Gore-levo 2', 'Stoj']

    def move_house(self, house, house_direction):
        if house_direction == "desno":
            if house[0] == 4:
                return (house[0] - 1, house[1]), "levo"
            return (house[0] + 1, house[1]), "desno"
        else:
            if house[0] == 0:
                return (house[0] + 1, house[1]), "desno"
            return (house[0] - 1, house[1]), "levo"

if __name__ == '__main__':
    #added 0,0
    allowed = [(0,0), (1,0), (2,0), (3,0), (1,1), (2,1), (0,2), (2,2), (4,2), (1,3), (3,3), (4,3), (0,4), (2,4), (2,5), (3,5), (0,6), (2,6), (1,7), (3,7)]
    
    player = tuple( input().strip().split(",") )
    player = (int(player[0]), int(player[1]))
    house = tuple( input().strip().split(",") )
    house = (int(house[0]), int(house[1]))
    house_direction = input().strip()

    initial = (player, house, house_direction)

    # print(initial)

    house_problem = HouseProblem(initial, None, allowed=allowed)

    successor = house_problem.successor(initial)
    # print(successor)
    # for i in successor.values():
    #     print(house_problem.h(Node(i)))
    node = astar_search(house_problem)

    if node is not None:
        print(node.solution())
