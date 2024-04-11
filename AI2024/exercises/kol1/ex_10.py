import bisect

"""
Defining a class for the problem structure that we will solve with a search.
The Problem class is an abstract class from which we make inheritance to define the basic
characteristics of every problem we want to solve
"""


class Problem:
    def __init__(self, initial, goal=None):
        self.initial = initial
        self.goal = goal

    def successor(self, state):
        """Given a state, return a dictionary of {action : state} pairs reachable
        from this state. If there are many successors, consider an iterator
        that yields the successors one at a time, rather than building them
        all at once.

        :param state: given state
        :return:  dictionary of {action : state} pairs reachable
                  from this state
        :rtype: dict
        """
        raise NotImplementedError

    def actions(self, state):
        """Given a state, return a list of all actions possible
        from that state

        :param state: given state
        :return: list of actions
        :rtype: list
        """
        raise NotImplementedError

    def result(self, state, action):
        """Given a state and action, return the resulting state

        :param state: given state
        :param action: given action
        :return: resulting state
        """
        raise NotImplementedError

    def goal_test(self, state):
        """Return True if the state is a goal. The default method compares
        the state to self.goal, as specified in the constructor. Implement
        this method if checking against a single self.goal is not enough.

        :param state: given state
        :return: is the given state a goal state
        :rtype: bool
        """
        return state == self.goal

    def path_cost(self, c, state1, action, state2):
        """Return the cost of a solution path that arrives at state2 from state1
        via action, assuming cost c to get up to state1. If the problem is such
        that the path doesn't matter, this function will only look at state2.
        If the path does matter, it will consider c and maybe state1 and action.
        The default method costs 1 for every step in the path.

        :param c: cost of the path to get up to state1
        :param state1: given current state
        :param action: action that needs to be done
        :param state2: state to arrive to
        :return: cost of the path after executing the action
        :rtype: float
        """
        return c + 1

    def value(self):
        """For optimization problems, each state has a value.
        Hill-climbing and related algorithms try to maximize this value.

        :return: state value
        :rtype: float
        """
        raise NotImplementedError


"""
Definition of the class for node structure of the search.
The class Node is not inherited
"""


class Node:
    def __init__(self, state, parent=None, action=None, path_cost=0):
        """Create node from the search tree,  obtained from the parent by
        taking the action

        :param state: current state
        :param parent: parent state
        :param action: action
        :param path_cost: path cost
        """
        self.state = state
        self.parent = parent
        self.action = action
        self.path_cost = path_cost
        self.depth = 0  # search depth
        if parent:
            self.depth = parent.depth + 1

    def __repr__(self):
        return "<Node %s>" % (self.state,)

    def __lt__(self, node):
        return self.state < node.state

    def expand(self, problem):
        """List the nodes reachable in one step from this node.

        :param problem: given problem
        :return: list of available nodes in one step
        :rtype: list(Node)
        """
        return [self.child_node(problem, action)
                for action in problem.actions(self.state)]

    def child_node(self, problem, action):
        """Return a child node from this node

        :param problem: given problem
        :param action: given action
        :return: available node  according to the given action
        :rtype: Node
        """
        next_state = problem.result(self.state, action)
        return Node(next_state, self, action,
                    problem.path_cost(self.path_cost, self.state,
                                      action, next_state))

    def solution(self):
        """Return the sequence of actions to go from the root to this node.

        :return: sequence of actions
        :rtype: list
        """
        return [node.action for node in self.path()[1:]]

    def solve(self):
        """Return the sequence of states to go from the root to this node.

        :return: list of states
        :rtype: list
        """
        return [node.state for node in self.path()[0:]]

    def path(self):
        """Return a list of nodes forming the path from the root to this node.

        :return: list of states from the path
        :rtype: list(Node)
        """
        x, result = self, []
        while x:
            result.append(x)
            x = x.parent
        result.reverse()
        return result

    """We want the queue of nodes at breadth_first_search or
    astar_search to not contain states-duplicates, so the nodes that
    contain the same condition we treat as the same. [Problem: this can
    not be desirable in other situations.]"""

    def __eq__(self, other):
        return isinstance(other, Node) and self.state == other.state

    def __hash__(self):
        return hash(self.state)


"""
Definitions of helper structures for storing the list of generated, but not checked nodes
"""


class Queue:
    """Queue is an abstract class/interface. There are three types:
        Stack(): Last In First Out Queue (stack).
        FIFOQueue(): First In First Out Queue.
        PriorityQueue(order, f): Queue in sorted order (default min-first).
    """

    def __init__(self):
        raise NotImplementedError

    def append(self, item):
        """Adds the item into the queue

        :param item: given element
        :return: None
        """
        raise NotImplementedError

    def extend(self, items):
        """Adds the items into the queue

        :param items: given elements
        :return: None
        """
        raise NotImplementedError

    def pop(self):
        """Returns the first element of the queue

        :return: first element
        """
        raise NotImplementedError

    def __len__(self):
        """Returns the number of elements in the queue

        :return: number of elements in the queue
        :rtype: int
        """
        raise NotImplementedError

    def __contains__(self, item):
        """Check if the queue contains the element item

        :param item: given element
        :return: whether the queue contains the item
        :rtype: bool
        """
        raise NotImplementedError


class Stack(Queue):
    """Last-In-First-Out Queue."""

    def __init__(self):
        self.data = []

    def append(self, item):
        self.data.append(item)

    def extend(self, items):
        self.data.extend(items)

    def pop(self):
        return self.data.pop()

    def __len__(self):
        return len(self.data)

    def __contains__(self, item):
        return item in self.data


class FIFOQueue(Queue):
    """First-In-First-Out Queue."""

    def __init__(self):
        self.data = []

    def append(self, item):
        self.data.append(item)

    def extend(self, items):
        self.data.extend(items)

    def pop(self):
        return self.data.pop(0)

    def __len__(self):
        return len(self.data)

    def __contains__(self, item):
        return item in self.data


class PriorityQueue(Queue):
    """A queue in which the minimum (or maximum) element is returned first
     (as determined by f and order). This structure is used in
     informed search"""

    def __init__(self, order=min, f=lambda x: x):
        """
        :param order: sorting function, if order is min, returns the element
                      with minimal f (x); if the order is max, then returns the
                      element with maximum f (x).
        :param f: function f(x)
        """
        assert order in [min, max]
        self.data = []
        self.order = order
        self.f = f

    def append(self, item):
        bisect.insort_right(self.data, (self.f(item), item))

    def extend(self, items):
        for item in items:
            bisect.insort_right(self.data, (self.f(item), item))

    def pop(self):
        if self.order == min:
            return self.data.pop(0)[1]
        return self.data.pop()[1]

    def __len__(self):
        return len(self.data)

    def __contains__(self, item):
        return any(item == pair[1] for pair in self.data)

    def __getitem__(self, key):
        for _, item in self.data:
            if item == key:
                return item

    def __delitem__(self, key):
        for i, (value, item) in enumerate(self.data):
            if item == key:
                self.data.pop(i)


"""
Uninformed graph search
The main difference is that here we do not allow loops,
i.e. repetition of states
"""


def graph_search(problem, fringe):
    """Search through the successors of a problem to find a goal.
     If two paths reach a state, only use the best one.

    :param problem: given problem
    :param fringe: empty queue
    :return: Node
    """
    closed = {}
    fringe.append(Node(problem.initial))
    while fringe:
        node = fringe.pop()
        if problem.goal_test(node.state):
            return node
        if node.state not in closed:
            closed[node.state] = True
            fringe.extend(node.expand(problem))
    return None


def breadth_first_graph_search(problem):
    """Search the shallowest nodes in the search tree first.

    :param problem: given problem
    :return: Node
    """
    return graph_search(problem, FIFOQueue())


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

    if node is not None:
        print(node.solution())



