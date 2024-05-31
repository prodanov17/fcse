import os
os.environ['OPENBLAS_NUM_THREADS'] = '1'

from sklearn.naive_bayes import GaussianNB
from sklearn.model_selection import train_test_split

from submission_script import *
from dataset_script import dataset

# Ova e primerok od podatochnoto mnozestvo, za treniranje/evaluacija koristete ja
# importiranata promenliva dataset
dataset_sample = [['1', '35', '12', '5', '1', '100', '0'],
                  ['1', '29', '7', '5', '1', '96', '1'],
                  ['1', '50', '8', '1', '3', '132', '0'],
                  ['1', '32', '11.75', '7', '3', '750', '0'],
                  ['1', '67', '9.25', '1', '1', '42', '0']]

if __name__ == '__main__':
    dataset_X = [[float(value) for value in row[:-1]] for row in dataset]
    dataset_Y = [[float(value) for value in row[-1]] for row in dataset]

    train_X, test_X, train_Y, test_Y = train_test_split(dataset_X, dataset_Y, test_size=0.15, shuffle=False)
    classifier = GaussianNB()

    classifier.fit(train_X, train_Y)

    accuracy = classifier.score(test_X, test_Y)

    print(accuracy)

    entry = [float(el) for el in input().split(" ")]
    entry_prediction = classifier.predict([entry])[0]
    print(int(entry_prediction))
    print(classifier.predict_proba([entry]))

    # Na kraj potrebno e da napravite submit na podatochnoto mnozestvo,
    # klasifikatorot i encoderot so povik na slednite funkcii

    # submit na trenirachkoto mnozestvo
    # submit_train_data(train_X, train_Y)

    # submit na testirachkoto mnozestvo
    # submit_test_data(test_X, test_Y)

    # submit na klasifikatorot
    # submit_classifier(classifier)

    # povtoren import na kraj / ne ja otstranuvajte ovaa linija
