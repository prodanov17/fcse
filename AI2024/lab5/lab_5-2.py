import os
os.environ['OPENBLAS_NUM_THREADS'] = '1'
# from submission_script import *
from zad2_dataset import dataset
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier


# Ova e primerok od podatochnoto mnozestvo, za treniranje/evaluacija koristete ja
# importiranata promenliva dataset
dataset_sample = [[180.0, 23.6, 25.2, 27.9, 25.4, 14.0, 'Roach'],
                  [12.2, 11.5, 12.2, 13.4, 15.6, 10.4, 'Smelt'],
                  [135.0, 20.0, 22.0, 23.5, 25.0, 15.0, 'Perch'],
                  [1600.0, 56.0, 60.0, 64.0, 15.0, 9.6, 'Pike'],
                  [120.0, 20.0, 22.0, 23.5, 26.0, 14.5, 'Perch']]


if __name__ == '__main__':
    # Vashiot kod tuka
    col_index = int(input())
    num_trees = int(input())
    criterion = input()

    dataset_X = [[row[i] for i in range(len(row) - 1) if i != col_index] for row in dataset]
    dataset_Y = [row[-1] for row in dataset]

    train_X, test_X, train_Y, test_Y = train_test_split(dataset_X, dataset_Y, test_size=0.15, shuffle=False)

    classifier = RandomForestClassifier(n_estimators=num_trees, criterion=criterion, random_state=0)

    classifier.fit(train_X, train_Y)    

    accuracy = classifier.score(test_X, test_Y)

    print(f"Accuracy: {accuracy}")

    prompt = input().strip().split(" ")

    prompt = [x for i, x in enumerate(prompt) if i != col_index]

    print(classifier.predict([prompt]))
    print(classifier.predict_proba([prompt])[0])
    
    
    
    # Na kraj potrebno e da napravite submit na podatochnoto mnozestvo
    # i klasifikatorot so povik na slednite funkcii
    
    # submit na trenirachkoto mnozestvo
    # submit_train_data(train_X, train_Y)
    
    # submit na testirachkoto mnozestvo
    # submit_test_data(test_X, test_Y)
    
    # submit na klasifikatorot
    # submit_classifier(classifier)

