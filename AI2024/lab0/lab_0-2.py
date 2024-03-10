import os
import re

os.environ["OPENBLAS_NUM_THREADS"] = "1"

"""
input:
    5
    -   -   -   -   -
    -   -   -   -   -
    -   -   #   -   -
    -   -   -   -   -
    -   -   -   -   -
"""

def transformMatrix(matrix):
    matrix = [[0 if x == '-' else x for x in row] for row in matrix]
    for i in range(len(matrix)):
        for j in range(len(matrix[i])):
            if matrix[i][j] == '#':
                if i > 0 and matrix[i - 1][j] != '#':
                    matrix[i - 1][j] +=1
                if j > 0 and matrix[i][j - 1] != '#':
                    matrix[i][j - 1] +=1
                if i < len(matrix) - 1 and matrix[i + 1][j] != '#':
                    matrix[i + 1][j] +=1
                if j < len(matrix[i]) - 1 and matrix[i][j + 1] != '#':
                    matrix[i][j + 1] +=1
                if i > 0 and j > 0 and matrix[i - 1][j - 1] != '#':
                    matrix[i - 1][j - 1] +=1
                if i > 0 and j < len(matrix[i]) - 1 and matrix[i - 1][j + 1] != '#':
                    matrix[i - 1][j + 1] +=1
                if i < len(matrix) - 1 and j > 0 and matrix[i + 1][j - 1] != '#':
                    matrix[i + 1][j - 1] +=1
                if i < len(matrix) - 1 and j < len(matrix[i]) - 1 and matrix[i + 1][j + 1] != '#':
                    matrix[i + 1][j + 1] +=1

            else:
                # matrix[i][j] = 0
                pass
    return matrix

if __name__ == "__main__":
    n = int(input().strip())

    list = []

    while n > 0:
        n -=1
        line = input().strip()
        result = re.split(r'\s+', line)
        list.append(result)

    transformed = transformMatrix(list)

    for i in range(len(transformed)):
        for j in range(len(transformed[i])):
            print(transformed[i][j], end='   ')
        print()

