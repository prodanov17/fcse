import random

if __name__ == "__main__":
    wh = 20 

    initial = wh

    lines = []

# generate a matrix of dots and hashtags for pacman
    while initial > 0:
        initial-=1
        line = [random.choice(['.', '#']) for _ in range(wh)]
        lines.append(line)

    #print output

    print(wh)
    print(wh)
    for line in lines:
        print(''.join(line))

    # with open('game.txt', 'w') as f:
    #     f.write(f"{wh}\n")
    #     f.write(f"{wh}\n")
    #     for line in lines:
    #         f.write(''.join(line) + '\n')
