from constraint import *
from collections import defaultdict

def max_count_constraint(num):
    def max_count(*args):
        my_list = [0 for _ in range(num)]
        count_exceed = False  # Flag to check if count exceeds num

        for arg in args:
            index = int(arg[1])
            my_list[index-1] += 1
            if my_list[index-1] > num:
                count_exceed = True  # If count exceeds num, set flag to True

        if count_exceed:
            return False
        return True
    return max_count

if __name__ == '__main__':
    num = int(input())
    
    papers = dict()
    
    string_map = defaultdict(list)
    variables = list()

    paper_info = input()
    while paper_info != 'end':
        title, topic = paper_info.split(' ')
        papers[title] = topic
        string_map[topic].append(f"{title} ({topic})")
        paper_info = input()
    
    # Tuka definirajte gi promenlivite
    variables = [f"{title} ({topic})" for title, topic in papers.items()]

    num_of_appearances = {topic: list(papers.values()).count(topic) for topic in set(papers.values())}
     
    domain = [f'T{i + 1}' for i in range(num)]
    
    problem = Problem(BacktrackingSolver())
    
    # Dokolku vi e potrebno moze da go promenite delot za dodavanje na promenlivite
    problem.addVariables(variables, domain)
    
    # Tuka dodadete gi ogranichuvanjata
    count = 0
    for key in string_map.keys():
        if len(string_map[key]) <= 4:
            problem.addConstraint(AllEqualConstraint(), string_map[key])

    problem.addConstraint(max_count_constraint(4), variables)


    result = problem.getSolution()
    
    print_list = [f"{item}: {result.get(item)}\n" for item in variables]
    print(''.join(print_list))

