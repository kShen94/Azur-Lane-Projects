import numpy as np
import pandas as pd
from scipy.spatial.distance import pdist

df = pd.read_csv("AL Ship Stats - BBStats.csv",header=0,index_col=0)
df=df.drop(['Reload2','Hit75/25','ReloadMult','FPMult','BuffRating','OffBuffs','OffBuffs2','Eq1Base','Eq1EffInitKai','BarrageRating'],axis=1)

print(df)
df=df.fillna(0)
df2=(df-df.mean())/df.std()
pd.DataFrame(df2).to_csv("BBNorm.csv")
from sklearn.metrics.pairwise import euclidean_distances
df3 = euclidean_distances(df2)
pd.DataFrame(df3).to_csv("BBEU.csv")
#np.savetxt("BBEU.csv", df3, delimiter=",")
#np.savetxt("Headers.csv",header,delimiter="," )