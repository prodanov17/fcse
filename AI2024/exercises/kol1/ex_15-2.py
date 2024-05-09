from collections import defaultdict
from constraint import *

def allSame(*args):
    for i in range(len(args) - 1):
        if args[i] != args[i + 1]:
            return False
    return True

def max4(*args):
    my_list = [0 for _ in range(3)]

    for arg in args:
        index = int(arg[1])
        my_list[index-1] += 1
        if my_list[index-1] > 4:
            return False
    return True

def max4v2(*args):
    my_list = [0 for _ in range(4)]

    for arg in args:
        index = int(arg[1])
        my_list[index-1] += 1
        if my_list[index-1] > 4:
            return False
    return True

if __name__ == '__main__':
    num = int(input())

    papers = dict()
    string_map = defaultdict(list)
    paper_info = input()
    variables = list()

    while paper_info != 'end':
        title, topic = paper_info.split(' ')
        papers[title] = topic
        variables.append(f"{title} ({topic})")
        string_map[topic].append(f"{title} ({topic})")
        paper_info = input()

    domain = [f'T{i + 1}' for i in range(num)]

    problem = Problem(BacktrackingSolver())
    problem.addVariables(variables, domain)

    print(f"STRING_MAP={string_map}")
    for key in string_map.keys():
        if len(string_map[key]) <= 4:
            problem.addConstraint(AllEqualConstraint(), string_map[key])

    if num == 3:
        problem.addConstraint(max4, variables)
    else:
        problem.addConstraint(max4v2, variables)

    result = problem.getSolution()

    print_list = [f"{item}: {result.get(item)}\n" for item in variables]
    print(''.join(print_list))

