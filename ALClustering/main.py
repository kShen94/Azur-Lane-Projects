import pandas as pd

import matplotlib.pyplot as plt
from sklearn.metrics import make_scorer
from sklearn.naive_bayes import GaussianNB

#import datasets
distance_eu = pd.read_csv("BBEU.csv",header=0,index_col=0)
data = pd.read_csv("BBNorm.csv",header=0,index_col=0)
raw = pd.read_csv("AL Ship Stats - BBStats.csv",header=0,index_col=0)
raw = raw.drop(['Reload2','Hit75/25','ReloadMult','FPMult','BuffRating','OffBuffs','OffBuffs2','Eq1Base','Eq1EffInitKai','BarrageRating'],axis=1)


from sklearn.cluster import AgglomerativeClustering, KMeans
X = distance_eu.to_numpy()
#cluster = AgglomerativeClustering(n_clusters=9, affinity='cosine', linkage='complete')


from sklearn.cluster import KMeans
cluster_range = range(3, 16)
meandist=[]
# loop through the cluster range
for k in cluster_range:
    # create a kmeans model with k clusters
    k_clf = KMeans(n_clusters=k,n_init=5)
    k_clf.fit(data)
    # append the inertia for the model to the meandist array
    meandist.append(k_clf.inertia_)

cluster_df = pd.DataFrame({"Clusters": cluster_range, "Average Distance": meandist})
print(cluster_df)

#plot clusters
plt.figure(figsize=(14, 7))
plt.plot(cluster_range, meandist, marker='o')
plt.xlabel('Clusters')
plt.ylabel('Average Distance')
plt.show()

#run kmeans
cluster = KMeans(n_clusters=11,random_state=5,n_init=10)
output = cluster.fit_predict(raw)
plt.scatter(X[:,0],X[:,1], c=cluster.labels_, cmap='rainbow')
plt.show()

#print ship + class label
data["Class"] = output
data= data.iloc[:,-1:]
print(data)
pd.DataFrame(data).to_csv("output.csv")
