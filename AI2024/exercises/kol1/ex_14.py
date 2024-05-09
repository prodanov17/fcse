from constraint import *

def func(marija, simona, petar, vreme):
    simona_prisustvo = [13,14,16,19]
    marija_prisustvo = [14, 15, 18]
    petar_prisustvo = [12,13,16,17,18,19]
     
    if (marija == 1 and vreme in marija_prisustvo) and vreme in simona_prisustvo and (petar == 1 and vreme in petar_prisustvo):
        return True
    if (marija == 1 and vreme in marija_prisustvo) and vreme in simona_prisustvo and (petar == 0):
        return True
    if (petar == 1 and vreme in petar_prisustvo) and vreme in simona_prisustvo and (marija == 0):
        return True

    return False

if __name__ == '__main__':
    problem = Problem(BacktrackingSolver())
    
    # ---Dadeni se promenlivite, dodadete gi domenite-----
    problem.addVariable("Marija_prisustvo", [0,1])
    problem.addVariable("Simona_prisustvo", [1])
    problem.addVariable("Petar_prisustvo", [0,1])
    problem.addVariable("vreme_sostanok", Domain(set(range(12, 21))))
    # ----------------------------------------------------
    
    # ---Tuka dodadete gi ogranichuvanjata----------------
    problem.addConstraint(AllDifferentConstraint(), ["vreme_sostanok"])
    problem.addConstraint(func, ["Marija_prisustvo", "Simona_prisustvo", "Petar_prisustvo", "vreme_sostanok"])
    # ----------------------------------------------------

    [print(solution) for solution in problem.getSolutions()]
