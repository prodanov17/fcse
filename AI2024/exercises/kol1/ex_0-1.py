from constraint import *

if __name__ == '__main__':
    problem = Problem()
    variables = ["WA", "NT", "SA", "Q", "NSW", "V", "T"]
    domain = ["red", "green", "blue"]
    # Add variables
    problem.addVariables(variables, domain)

    problem.addConstraint(lambda a, b: a != b, ("WA", "NT"))
    problem.addConstraint(lambda a, b: a != b, ("WA", "SA"))
    problem.addConstraint(lambda a, b: a != b, ("NT", "SA"))
    problem.addConstraint(lambda a, b: a != b, ("Q", "NT"))
    problem.addConstraint(lambda a, b: a != b, ("NSW", "Q"))
    problem.addConstraint(lambda a, b: a != b, ("V", "NSW"))
    problem.addConstraint(lambda a, b: a != b, ("V", "SA"))
    problem.addConstraint(lambda a, b: a != b, ("NSW", "SA"))

    solutions = problem.getSolutions()
    print(solutions)
