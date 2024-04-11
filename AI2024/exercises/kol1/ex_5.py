from constraint import *

def notEqualConstraint(a, b):
    time = int(a.split("_")[1])
    return time != int(b.split("_")[1]) or time != int(b.split("_")[1] + 1)

if __name__ == '__main__':
    problem = Problem(BacktrackingSolver())
    casovi_AI = input()
    casovi_ML = input()
    casovi_R = input()
    casovi_BI = input()

    AI_predavanja_domain = ["Mon_11", "Mon_12", "Wed_11", "Wed_12", "Fri_11", "Fri_12"]
    ML_predavanja_domain = ["Mon_12", "Mon_13", "Mon_15", "Wed_12", "Wed_13", "Wed_15", "Fri_11", "Fri_12", "Fri_15"]
    R_predavanja_domain = ["Mon_10", "Mon_11", "Mon_12", "Mon_13","Mon_14", "Mon_15", "Wed_10", "Wed_11", "Wed_12", "Wed_13","Wed_14", "Wed_15", "Fri_10", "Fri_11", "Fri_12", "Fri_13","Fri_14", "Fri_15"]
    BI_predavanja_domain = ["Mon_10", "Mon_11", "Wed_10", "Wed_11", "Fri_10", "Fri_11"]

    AI_vezbi_domain = ["Tue_10", "Tue_11", "Tue_12", "Tue_13", "Thu_10", "Thu_11", "Thu_12", "Thu_13"]
    ML_vezbi_domain = ["Tue_11", "Tue_13", "Tue_14", "Thu_11", "Thu_13", "Thu_14"]
    BI_vezbi_domain = ["Tue_10", "Tue_11", "Thu_10", "Thu_11"]

    # ---Tuka dodadete gi promenlivite--------------------
    problem.addVariable("casovi_AI", AI_predavanja_domain)
    problem.addVariable("casovi_ML", ML_predavanja_domain)
    problem.addVariable("casovi_R", R_predavanja_domain)
    problem.addVariable("casovi_BI", BI_predavanja_domain)
    problem.addVariable("vezbi_AI", AI_vezbi_domain)
    problem.addVariable("vezbi_ML", ML_vezbi_domain)
    problem.addVariable("vezbi_BI", BI_vezbi_domain)

    # ---Tuka dodadete gi ogranichuvanjata----------------
    problem.addConstraint(notEqualConstraint, ["casovi_AI", "casovi_ML", "casovi_R", "casovi_BI", "vezbi_AI", "vezbi_ML", "vezbi_BI"])

    # ----------------------------------------------------
    solution = problem.getSolution()

    print(solution)