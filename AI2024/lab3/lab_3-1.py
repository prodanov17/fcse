from constraint import *

"""
    Симона слободни термини: 13:00-15:00, 16:00-17:00, 19:00-20:00
    Марија слободни термини: 14:00-16:00, 18:00-19:00
    Петар слободни термини: 12:00-14:00, 16:00-20:00
"""

def someEqual(a, b, c, d):
    return a == d and (b == d or c == d)
    # return a == 1 and (b == 1 or c == 1)


if __name__ == '__main__':
    problem = Problem(BacktrackingSolver())
    
    marija = [13, 14, 16, 19]
    simona = [14, 15, 18]
    petar = [12, 13, 16, 17, 18, 19]
#[1 if i in marija else 0 for i in range(12,20)]

    # ---Dadeni se promenlivite, dodadete gi domenite-----
    problem.addVariable("Marija_prisustvo", marija)
    problem.addVariable("Simona_prisustvo", simona)
    problem.addVariable("Petar_prisustvo", petar)
    problem.addVariable("vreme_sostanok", [i for i in range(12, 20)])
    # ----------------------------------------------------
    
    # ---Tuka dodadete gi ogranichuvanjata----------------
    # problem.addConstraint(compareIndices, ("Simona_prisustvo", "vreme_sostanok"))
    problem.addConstraint(someEqual, ("Simona_prisustvo", "Marija_prisustvo", "Petar_prisustvo", "vreme_sostanok"))
    # 
    # ----------------------------------------------------
    
    [print(solution) for solution in problem.getSolutions()]
