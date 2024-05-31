import os

os.environ['OPENBLAS_NUM_THREADS'] = '1'
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OrdinalEncoder
from sklearn.naive_bayes import CategoricalNB
from submission_script import *
from dataset_script import dataset

# Ova e primerok od podatochnoto mnozestvo, za treniranje/evaluacija koristete ja
# importiranata promenliva dataset
dataset_sample = [['C', 'S', 'O', '1', '2', '1', '1', '2', '1', '2', '0'],
                  ['D', 'S', 'O', '1', '3', '1', '1', '2', '1', '2', '0'],
                  ['C', 'S', 'O', '1', '3', '1', '1', '2', '1', '1', '0'],
                  ['D', 'S', 'O', '1', '3', '1', '1', '2', '1', '2', '0'],
                  ['D', 'A', 'O', '1', '3', '1', '1', '2', '1', '2', '0']]

if __name__ == '__main__':
    # Vashiot kod tuka

    dataset_x = [row[:-1] for row in dataset]
    dataset_y = [row[-1] for row in dataset]

    encoder = OrdinalEncoder()
    encoded_dataset = encoder.fit_transform(dataset_x)

    # split_index = int(len(dataset_x) * 0.75)

    train_X, test_X, train_Y, test_Y = train_test_split(encoded_dataset, dataset_y, test_size=0.25, shuffle=False)
    # train_X = encoded_dataset[:split_index]
    # test_X = encoded_dataset[split_index:]

    # train_Y = dataset_y[:split_index]
    # test_Y = dataset_y[split_index:]
    # print(train_Y)
    # encoded_X_train = encoder.transform(train_X)
    # encoded_X_test = encoder.transform(test_X)

    classifier = CategoricalNB()

    classifier.fit(train_X, train_Y)

    accuracy = classifier.score(test_X, test_Y)
    print(accuracy)
    # accuracy = 0
    #
    # for x, y in zip(encoded_X_test, test_Y):
    #     y_pred = classifier.predict([x])[0]
    #     if y_pred == y:
    #         accuracy += 1
    #
    # print(accuracy / len(test_Y))

    new_sample = input()
    new_sample = new_sample.split(' ')
    new_sample_encoded = encoder.transform([new_sample])

    predicted_class = classifier.predict(new_sample_encoded)[0]
    probabilities = classifier.predict_proba(new_sample_encoded)
    print(predicted_class)
    print(probabilities)

    # Na kraj potrebno e da napravite submit na podatochnoto mnozestvo,
    # klasifikatorot i encoderot so povik na slednite funkcii

    # submit na trenirachkoto mnozestvo
    submit_train_data(train_X, train_Y)

    # submit na testirachkoto mnozestvo
    submit_test_data(test_X, test_Y)

    # submit na klasifikatorot
    submit_classifier(classifier)

    # submit na encoderot
    submit_encoder(encoder)
