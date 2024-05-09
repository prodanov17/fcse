from constraint import *
import random

def preklopuva(*args):
    seen = set()
    for arg in args:
        split_arg = arg.split("_")
        tupled_arg = (split_arg[0], int(split_arg[1]))
        if tupled_arg in seen or (tupled_arg[0], tupled_arg[1] - 1) in seen or (tupled_arg[0], tupled_arg[1] + 1) in seen:
            return False
        seen.add(tupled_arg)
    return True

def ml_preklopuva(*args):
    seen = set()
    for arg in args:
        split_arg = arg.split("_")
        hour = int(split_arg[1])
        if hour in seen:
            return False 
        seen.add(hour)

    return True




if __name__ == '__main__':
    problem = Problem(BacktrackingSolver())
    casovi_AI = int( input() )
    casovi_ML = int( input() )
    casovi_R = int( input() )
    casovi_BI = int( input() )

    AI_predavanja_domain = ["Mon_11", "Mon_12", "Wed_11", "Wed_12", "Fri_11", "Fri_12"]
    ML_predavanja_domain = ["Mon_12", "Mon_13", "Mon_15", "Wed_12", "Wed_13", "Wed_15", "Fri_11", "Fri_12", "Fri_15"]
    R_predavanja_domain = ["Mon_10", "Mon_11", "Mon_12", "Mon_13","Mon_14", "Mon_15", "Wed_10", "Wed_11", "Wed_12", "Wed_13","Wed_14", "Wed_15", "Fri_10", "Fri_11", "Fri_12", "Fri_13","Fri_14", "Fri_15"]
    BI_predavanja_domain = ["Mon_10", "Mon_11", "Wed_10", "Wed_11", "Fri_10", "Fri_11"]

    AI_vezbi_domain = ["Tue_10", "Tue_11", "Tue_12", "Tue_13", "Thu_10", "Thu_11", "Thu_12", "Thu_13"]
    ML_vezbi_domain = ["Tue_11", "Tue_13", "Tue_14", "Thu_11", "Thu_13", "Thu_14"]
    BI_vezbi_domain = ["Tue_10", "Tue_11", "Thu_10", "Thu_11"]

    domain = ["ML_vezbi", "AI_vezbi", "BI_vezbi"]
    ml_domain = ["ML_vezbi"]

    # ---Tuka dodadete gi promenlivite--------------------
    for i in range(1, casovi_AI + 1):
        problem.addVariable("AI_cas_" + str(i), AI_predavanja_domain)
        domain.append("AI_cas_" + str(i))
    for i in range(1, casovi_ML + 1):
        problem.addVariable("ML_cas_" + str(i), ML_predavanja_domain)
        domain.append("ML_cas_" + str(i))
        ml_domain.append("ML_cas_" + str(i))
    for i in range(1, casovi_BI + 1):
        problem.addVariable("BI_cas_" + str(i), BI_predavanja_domain)
        domain.append("BI_cas_" + str(i))
    for i in range(1, casovi_R + 1):
        problem.addVariable("R_cas_" + str(i), R_predavanja_domain)
        domain.append("R_cas_" + str(i))

    problem.addVariable("BI_vezbi", BI_vezbi_domain)
    problem.addVariable("ML_vezbi", ML_vezbi_domain)
    problem.addVariable("AI_vezbi", AI_vezbi_domain)

    # ---Tuka dodadete gi ogranichuvanjata----------------
    problem.addConstraint(ml_preklopuva, ml_domain)
    problem.addConstraint(preklopuva, domain)
    
    # ----------------------------------------------------
    solution = problem.getSolution()

    print(solution)
