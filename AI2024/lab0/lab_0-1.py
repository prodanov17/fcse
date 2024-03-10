import os
os.environ["OPENBLAS_NUM_THREADS"] = "1"

class Student:
    def __init__(self, firstName, lastName, index, subject, pointsTheory, pointsPractice, pointsLab) -> None:
        self.firstName = firstName
        self.lastName = lastName
        self.index = index
        self.subjects = [
            {
                "subject": subject,
                "theory" : pointsTheory,
                "practice" : pointsPractice,
                "lab" : pointsLab,
            }
        ]
        
    def getGrade(self, subject):
        for subj in self.subjects:
            if subj["subject"] == subject:
                theory = float(subj["theory"])
                practice = float(subj["practice"])
                lab = float(subj["lab"])
                return self.calculateGrade(theory + practice + lab)

    def calculateGrade(self, points):
        if points > 90:
            return 10
        elif points > 80:
            return 9
        elif points > 70:
            return 8
        elif points > 60:
            return 7
        elif points > 50:
            return 6
        else:
            return 5


if __name__ == "__main__":
    students = []
    while True:
        studentInput = input().strip()
        if studentInput == "end":
            break
        studentInfo = studentInput.split(",")

        studentExists = False

        for student in students:
            if student.index == studentInfo[2]:
                studentExists = True
                student.subjects.append({
                    "subject": studentInfo[3],
                    "theory" : studentInfo[4],
                    "practice" : studentInfo[5],
                    "lab" : studentInfo[6]
                })
                break
        if studentExists:
            continue
        student = Student(studentInfo[0], studentInfo[1], studentInfo[2], studentInfo[3], studentInfo[4], studentInfo[5], studentInfo[6])
        students.append(student)


    for student in students:
        print(f"Student: {student.firstName} {student.lastName}")
        for subject in student.subjects:
            print(f"----{subject['subject']}: {student.getGrade(subject['subject'])}")

        print("")
