import os
os.environ['OPENBLAS_NUM_THREADS'] = '1'
# from submission_script import *
from zad1_dataset import dataset
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OrdinalEncoder
from sklearn.tree import DecisionTreeClassifier

# Ova e primerok od podatochnoto mnozestvo, za treniranje/evaluacija koristete ja
# importiranata promenliva dataset
dataset_sample = [['C', 'S', 'O', '1', '2', '1', '1', '2', '1', '2', '0'],
                  ['D', 'S', 'O', '1', '3', '1', '1', '2', '1', '2', '0'],
                  ['C', 'S', 'O', '1', '3', '1', '1', '2', '1', '1', '0'],
                  ['D', 'S', 'O', '1', '3', '1', '1', '2', '1', '2', '0'],
                  ['D', 'A', 'O', '1', '3', '1', '1', '2', '1', '2', '0']]

if __name__ == '__main__':
    # Vashiot kod tuka

    testRange = int(input())
    testRange = (100 - testRange) / 100

    criterion = input()

    dataset_x = [row[:-1] for row in dataset]
    dataset_y = [row[-1] for row in dataset]

    encoder = OrdinalEncoder()
    encoded_dataset = encoder.fit_transform(dataset_x)

    print(testRange)

    test_X, train_X, test_Y, train_Y = train_test_split(encoded_dataset, dataset_y, test_size=(1- testRange ), shuffle=False)
    
    classifier = DecisionTreeClassifier(criterion=criterion, random_state=0)

    classifier.fit(train_X, train_Y)

    accuracy = classifier.score(test_X, test_Y)
    
    print(f"Depth: {classifier.get_depth()}")
    print(f"Number of leaves: {classifier.get_n_leaves()}")
    print(f"Accuracy: {accuracy}")
    feauters=list(classifier.feature_importances_)
    most_important_feature=feauters.index(max(feauters))
    print(f"Most important feature: {most_important_feature}")
    least_important_feature=feauters.index(min(feauters))
    print(f"Least important feature: {least_important_feature}")
    

    # Na kraj potrebno e da napravite submit na podatochnoto mnozestvo,
    # klasifikatorot i encoderot so povik na slednite funkcii
    
    # submit na trenirachkoto mnozestvo
    # submit_train_data(train_X, train_Y)
    
    # submit na testirachkoto mnozestvo
    # submit_test_data(test_X, test_Y)
    
    # submit na klasifikatorot
    # submit_classifier(classifier)
    
    # submit na encoderot
    # submit_encoder(encoder)

