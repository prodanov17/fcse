from constraint import *

if __name__ == '__main__':
    problem = Problem(RecursiveBacktrackingSolver())
    variables = ["rook1", "rook2", "rook3", "rook4", "rook5", "rook6", "rook7"]
    domain = [(i,j) for i in range(len(variables)) for j in range(len(variables))]

    problem.addVariables(variables, domain)

    for rook1 in variables:
        for rook2 in variables:
            if rook1 < rook2:
                problem.addConstraint(lambda r1, r2: r1[0] != r2[0] and r1[1] != r2[1] and r1[0] != r1[1], (rook1, rook2))
    print(problem.getSolution())