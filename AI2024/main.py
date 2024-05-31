from sklearn.datasets import make_classification
from sklearn.ensemble import RandomForestClassifier
import time

# Generate a random dataset
X, y = make_classification(n_samples=100000, n_features=20, random_state=42)

# Initialize the model
model = RandomForestClassifier(n_estimators=100, random_state=42)

# Measure training time
start_time = time.time()
model.fit(X, y)
end_time = time.time()

print(f"Training time: {end_time - start_time:.2f} seconds")

