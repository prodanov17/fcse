import os
from ex_1_dataset import dataset
from sklearn.preprocessing import OrdinalEncoder
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import CategoricalNB
from sklearn.metrics import accuracy_score

os.environ['OPENBLAS_NUM_THREADS'] = '1'

if __name__ == '__main__':
    new_sample = input().split(' ')

    dataset_X = [row[:-1] for row in dataset]
    dataset_Y = [row[-1] for row in dataset]

    encoder = OrdinalEncoder()
    encoded_dataset = encoder.fit_transform(dataset_X)

    train_X, test_X, train_Y, test_Y = train_test_split(
        encoded_dataset,
        dataset_Y,
        test_size=0.25,
        shuffle=False
    )

    model = CategoricalNB()

    model.fit(train_X, train_Y)

    predictions = model.predict(test_X)
    accuracy = accuracy_score(test_Y, predictions)

    print(accuracy)

    new_sample_encoded = encoder.transform([new_sample])

    predicted_class = model.predict(new_sample_encoded)[0]
    probabilities = model.predict_proba(new_sample_encoded)
    print(predicted_class)
    print(probabilities)
